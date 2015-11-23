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
/*-----------DEFERRED SHADING IN-OUT-UNIFORMS-------------*/
/*--------------------------------------------------------*/

in vec2 textureCoords;
in vec4 posPos;

out vec4 out_Color;

uniform int camUnderWater;
uniform float camUnderWaterOffset;
uniform vec2 resolution;
uniform vec3 cameraPosition;
uniform vec3 previousCameraPosition;
uniform vec3 lightPosition;
uniform vec2 sunPositionInScreen;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 inverseProjectionMatrix;
uniform mat4 inverseViewMatrix;
uniform mat4 previousViewMatrix;
uniform sampler2D gDiffuse;
uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gData;
uniform sampler2D composite1;
uniform sampler2DShadow gDepth;

uniform int useFXAA;
uniform int useDOF;
uniform int useMotionBlur;
uniform int useBloom;
uniform int useVolumetricLight;

/*--------------------------------------------------------*/
/*----------------DEFERRED SHADING CONFIG-----------------*/
/*--------------------------------------------------------*/

#define FxaaInt2 ivec2
#define FxaaFloat2 vec2
#define FxaaTexLod0(t, p) textureLod(t, p, 0.0)
#define FxaaTexOff(t, p, o, r) textureLodOffset(t, p, 0.0, o)

#define FXAA_REDUCE_MIN   (1.0/128.0)
#define FXAA_REDUCE_MUL   (1.0/8.0)
#define FXAA_SPAN_MAX     8.0

const float bloom_amount = 2;
const int maxf = 4, maxt = 40;				
const float stp = 1.2, ref = 0.1, inc = 2.2;
const int NUM_SAMPLES = 50;		

#define rt_w resolution.x
#define rt_h resolution.y

/*--------------------------------------------------------*/
/*----------------DEFERRED SHADING CODE-------------------*/
/*--------------------------------------------------------*/

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
	vec4 image = texture(gDiffuse, texcoord);
	vec4 data = texture(gData, texcoord);
    vec4 position = texture(gPosition,texcoord);
    vec4 normal = texture(gNormal, texcoord);
    float depth = texture(gDepth, vec3(texcoord.xy, 0.0), 16);
	vec3 light = lightPosition;
    vec3 lightDir = light - position.xyz ;
    lightDir = normalize(lightDir);
    vec3 eyeDir = normalize(cameraPosition-position.xyz);
    float lightDirDOTviewDir = dot(-lightDir,eyeDir);
    if(data.b != 1) {
    	if(data.g == 1.0){
    		image.rgb -= vec3(data.a,data.a,data.a);
    	}
    	normal = normalize(normal);
    	vec3 vHalfVector = normalize(lightDir.xyz+eyeDir);
    	image = max(dot(normal.xyz,lightDir),0.2) * image;
    
   		if(data.r == 1.0){
    		vec3 worldStartingPos = position.xyz;
    		vec3 normal = normal.xyz;
    		vec3 cameraToWorld = worldStartingPos.xyz - cameraPosition.xyz;
    		float cameraToWorldDist = length(cameraToWorld);
    		vec3 cameraToWorldNorm = normalize(cameraToWorld);
    		vec3 refl = normalize(reflect(cameraToWorldNorm, normal));
    		float cosAngle = abs(dot(normal, cameraToWorldNorm));
    		float fact = 1 - cosAngle;
    		fact = min(1, 1.38 - fact*fact);
    		vec3 newPos;
    		vec4 newScreen;
    		float i = 0;
    		vec3 rayTrace = worldStartingPos;
    		float currentWorldDist, rayDist;
    		float incr = 0.4;
    		do {
        		i += 0.05;
        		rayTrace += refl*incr;
        		incr *= 1.3;
        		newScreen = projectionMatrix * viewMatrix * vec4(rayTrace, 1);
        		newScreen /= newScreen.w;
        		newPos = texture(gPosition, newScreen.xy/2.0+0.5).xyz;
        		currentWorldDist = length(newPos.xyz - cameraPosition.xyz);
        		rayDist = length(rayTrace.xyz - cameraPosition.xyz);
        		if (newScreen.x > 1 || newScreen.x < -1 || newScreen.y > 1 || newScreen.y < -1 || dot(refl, cameraToWorldNorm) < 0)
        			break;
    		} while(rayDist < currentWorldDist);
 			vec4 newColor = texture(gDiffuse, newScreen.xy/2.0 + 0.5);
    		if (dot(refl, cameraToWorldNorm) < 0)
        		fact = 1.0;
    		else if (newScreen.x > 1 || newScreen.x < -1 || newScreen.y > 1 || newScreen.y < -1)
        		fact = 1.0;
    		else if (cameraToWorldDist > currentWorldDist)
        		fact = 1.0;
        	image = image*fact + newColor*(1-fact) + pow(max(dot(normal.xyz,vHalfVector),0.0), 100) * 8;
    	}
    }
	vec4 raysColor = texture(composite1, texcoord);
    image.rgb = mix(image.rgb, raysColor.rgb, raysColor.a);
    if(useVolumetricLight == 1){
		if (lightDirDOTviewDir>0.0){
			float exposure	= 0.1/NUM_SAMPLES;
			float decay		= 1.0;
			float density	= 1;
			float weight	= 6.0;
			float illuminationDecay = 1.0;
			vec2 pos = vec2(0.0);
			pos.x = (sunPositionInScreen.x) / resolution.x;
			pos.y = (sunPositionInScreen.y) / resolution.y;
			vec2 deltaTextCoord = vec2( texcoord - pos);
			vec2 textCoo = texcoord;
			deltaTextCoord *= 1.0 / float(NUM_SAMPLES) * density;
			for(int i=0; i < NUM_SAMPLES ; i++) {
				textCoo -= deltaTextCoord;
				vec4 tsample = texture(composite1, textCoo );
				tsample *= illuminationDecay * weight;
				raysColor += tsample;
				illuminationDecay *= decay;
			}
			raysColor *= exposure * lightDirDOTviewDir;
			image +=  raysColor;
		}
	}
    
    if(camUnderWater == 1){
		out_Color = mix(vec4(0.0,0.0,0.3125,1.0),image,0.5);
	} else {
		out_Color = image;
	}
	
}