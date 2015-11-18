//
// The MIT License (MIT)
//
// Copyright (c) 2015 Guerra24
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

#version 330 core

/*--------------------------------------------------------*/
/*----------------POST PROCESSING CONFIG------------------*/
/*--------------------------------------------------------*/

#define FxaaInt2 ivec2
#define FxaaFloat2 vec2
#define FxaaTexLod0(t, p) textureLod(t, p, 0.0)
#define FxaaTexOff(t, p, o, r) textureLodOffset(t, p, 0.0, o)

#define FXAA_REDUCE_MIN   (1.0/128.0)
#define FXAA_REDUCE_MUL   (1.0/8.0)
#define FXAA_SPAN_MAX     8.0

float bloom_amount = 1.1;

in vec2 textureCoords;
in vec4 posPos;

out vec4 out_Color;

uniform int camUnderWater;
uniform float camUnderWaterOffset;
uniform vec2 resolution;
uniform mat4 projectionMatrix;
uniform mat4 inverseProjectionMatrix;
uniform mat4 inverseViewMatrix;
uniform mat4 previousViewMatrix;
uniform vec3 cameraPosition;
uniform vec3 previousCameraPosition;
uniform sampler2D texture0;
uniform sampler2D texture1;
uniform sampler2D depth0;

uniform int useFXAA;
uniform int useDOF;
uniform int useMotionBlur;
uniform int useBloom;

#define rt_w resolution.x
#define rt_h resolution.y

vec3 FxaaPixelShader(vec4 posPos, sampler2D tex, vec2 rcpFrame) {
    vec3 rgbNW = FxaaTexLod0(tex, posPos.zw).xyz;
    vec3 rgbNE = FxaaTexOff(tex, posPos.zw, FxaaInt2(1,0), rcpFrame.xy).xyz;
    vec3 rgbSW = FxaaTexOff(tex, posPos.zw, FxaaInt2(0,1), rcpFrame.xy).xyz;
    vec3 rgbSE = FxaaTexOff(tex, posPos.zw, FxaaInt2(1,1), rcpFrame.xy).xyz;
    vec3 rgbM  = FxaaTexLod0(tex, posPos.xy).xyz;

    vec3 luma = vec3(0.299, 0.587, 0.114);
    float lumaNW = dot(rgbNW, luma);
    float lumaNE = dot(rgbNE, luma);
    float lumaSW = dot(rgbSW, luma);
    float lumaSE = dot(rgbSE, luma);
    float lumaM  = dot(rgbM,  luma);
    
    float lumaMin = min(lumaM, min(min(lumaNW, lumaNE), min(lumaSW, lumaSE)));
    float lumaMax = max(lumaM, max(max(lumaNW, lumaNE), max(lumaSW, lumaSE)));
    
    vec2 dir; 
    dir.x = -((lumaNW + lumaNE) - (lumaSW + lumaSE));
    dir.y =  ((lumaNW + lumaSW) - (lumaNE + lumaSE));

    float dirReduce = max(
        (lumaNW + lumaNE + lumaSW + lumaSE) * (0.25 * FXAA_REDUCE_MUL),
        FXAA_REDUCE_MIN);
    float rcpDirMin = 1.0/(min(abs(dir.x), abs(dir.y)) + dirReduce);
    dir = min(FxaaFloat2( FXAA_SPAN_MAX,  FXAA_SPAN_MAX), 
          max(FxaaFloat2(-FXAA_SPAN_MAX, -FXAA_SPAN_MAX), 
          dir * rcpDirMin)) * rcpFrame.xy;

    vec3 rgbA = (1.0/2.0) * (
        FxaaTexLod0(tex, posPos.xy + dir * (1.0/3.0 - 0.5)).xyz +
        FxaaTexLod0(tex, posPos.xy + dir * (2.0/3.0 - 0.5)).xyz);
    vec3 rgbB = rgbA * (1.0/2.0) + (1.0/4.0) * (
        FxaaTexLod0(tex, posPos.xy + dir * (0.0/3.0 - 0.5)).xyz +
        FxaaTexLod0(tex, posPos.xy + dir * (3.0/3.0 - 0.5)).xyz);
    float lumaB = dot(rgbB, luma);
    if((lumaB < lumaMin) || (lumaB > lumaMax)) return rgbA;
		return rgbB; }
 
vec4 PostFX(sampler2D tex, vec2 uv, float time) {
	vec4 c = vec4(0.0);
	vec2 rcpFrame = vec2(1.0/rt_w, 1.0/rt_h);
	c.rgb = FxaaPixelShader(posPos, tex, rcpFrame);
	c.a = 1.0;
	return c;
}

void main(void){
	vec2 texcoord = textureCoords;
	if(camUnderWater == 1){
		texcoord.x += sin(texcoord.y * 4*2*3.14159 + camUnderWaterOffset) / 100;
	}
	
	vec4 textureColour;
	
	if(useFXAA == 1){
		textureColour = PostFX(texture0, texcoord, 0.0);
	}else{
		textureColour = texture(texture0, texcoord);
	}
	
	if(useDOF == 1){
		vec3 sum = textureColour.rgb;
		float bias = min(abs(texture(depth0, texcoord).x - texture(depth0, vec2(0.5)).x) * .02, .01);
		for (int i = -3; i < 3; i++) {
			for (int j = -3; j < 3; j++) {
				sum += texture(texture0, texcoord + vec2(j, i) * bias ).rgb;
			}
		}
		sum /= 36.0;
		textureColour = vec4(sum,1.0);
	}
	
	if(useMotionBlur == 1){
		vec3 sum = textureColour.rgb;
		vec4 tex = vec4(texcoord, 0.0,0.0);
		float depth = texture(depth0, texcoord).x;
		vec4 currentPosition = vec4(tex.x * 2.0 - 1.0, tex.y * 2.0 - 1.0, 2.0 * depth - 1.0, 1.0);
		vec4 fragposition = inverseProjectionMatrix * currentPosition;
		fragposition = inverseViewMatrix * fragposition;
		fragposition /= fragposition.w;
		fragposition.xyz += cameraPosition;
	
		vec4 previousPosition = fragposition;
		previousPosition.xyz -= previousCameraPosition;
		previousPosition = previousViewMatrix * previousPosition;
		previousPosition = projectionMatrix * previousPosition;
		previousPosition /= previousPosition.w;
		vec2 velocity = (currentPosition - previousPosition).st * 0.015;
		int samples = 1;
		vec2 coord = tex.st + velocity;
		for (int i = 0; i < 12; ++i, coord += velocity) {
				sum += texture(texture0, coord).rgb;
				++samples;
		}	
		sum = sum/samples;
		textureColour = vec4(sum, 1.0);
	}
	
	if(useBloom == 1){
		float rad = 0.001, sc = 20.0, blm_amount = 0.02*bloom_amount;
		int i = 0, samples = 1; vec4 clr = vec4(0.0);
	
		for (i = -8; i < 8; i++) {
			vec2 d = vec2(-i, i), e = vec2(0, i), f = texcoord;
	
			clr += texture(texture0, f+( d.yy )*rad)*sc;
			clr += texture(texture0, f+( d.yx )*rad)*sc;
			clr += texture(texture0, f+( d.xy )*rad)*sc;
			clr += texture(texture0, f+( d.xx )*rad)*sc;

			clr += texture(texture0, f+( e.xy )*rad)*sc;
			clr += texture(texture0, f+(-e.yx )*rad)*sc;
			clr += texture(texture0, f+(-e.xy )*rad)*sc;
			clr += texture(texture0, f+( e.yx )*rad)*sc;

			++samples;
			sc = sc - 1.0;
		}
		clr = (clr/8.0)/samples; 
		textureColour.rgb += clr.rgb*blm_amount;
	}
	
	if(camUnderWater == 1){
		out_Color = mix(vec4(0.0,0.0,0.3125,1.0),textureColour,0.5);
	} else {
		out_Color = textureColour;
	}
	
}