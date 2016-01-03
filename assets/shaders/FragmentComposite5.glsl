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
/*--------------COMPOSITE 5 IN-OUT-UNIFORMS---------------*/
/*--------------------------------------------------------*/

in vec2 textureCoords;
in vec4 posPos;

out vec4 out_Color;

uniform int camUnderWater;
uniform float camUnderWaterOffset;
uniform vec2 resolution;
uniform vec2 sunPositionInScreen;
uniform vec3 cameraPosition;
uniform vec3 previousCameraPosition;
uniform vec3 lightPosition;
uniform vec3 invertedLightPosition;
uniform vec3 skyColor;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 inverseProjectionMatrix;
uniform mat4 inverseViewMatrix;
uniform mat4 previousViewMatrix;
uniform sampler2D gDiffuse;
uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gData0;
uniform sampler2D gData1;
uniform sampler2D composite0;
uniform sampler2DShadow gDepth;

uniform int useFXAA;
uniform int useDOF;
uniform int useMotionBlur;
uniform int useBloom;
uniform int useVolumetricLight;

/*--------------------------------------------------------*/
/*------------------COMPOSITE 5 CONFIG--------------------*/
/*--------------------------------------------------------*/

const int NUM_SAMPLES = 50;
const float density = 0.005;
const float gradient = 2.0;

/*--------------------------------------------------------*/
/*------------------COMPOSITE 5 CODE----------------------*/
/*--------------------------------------------------------*/

void main(void){
	vec2 texcoord = textureCoords;
	if(camUnderWater == 1){
		texcoord.x += sin(texcoord.y * 4*2*3.14159 + camUnderWaterOffset) / 100;
	}
	vec4 image = texture(gDiffuse, texcoord);
	vec4 data = texture(gData0, texcoord);
	vec4 data1 = texture(gData1, texcoord);
    vec4 position = texture(gPosition,texcoord);
    vec4 normal = texture(gNormal, texcoord);
    float depth = texture(gDepth, vec3(texcoord.xy, 0.0), 16);
	vec3 light = lightPosition;
    vec3 lightDir = light - position.xyz ;
    lightDir = normalize(lightDir);
    vec3 eyeDir = normalize(cameraPosition-position.xyz);
	vec3 invertedLight = invertedLightPosition;
    vec3 invertedlightDir = invertedLight - position.xyz ;
    invertedlightDir = normalize(invertedlightDir);
    float lightDirDOTviewDir = dot(invertedlightDir,eyeDir);
    if(data.b != 1) {
    	normal = normalize(normal);
    	vec3 vHalfVector = normalize(lightDir.xyz+eyeDir);
    	float b = ((max(dot(normal.xyz,lightDir),0.0)) - data.a);
    	if(b <= data1.a)
    		b = data1.a;
    	b = clamp(b,0.2,1.0);
    	image = b * image;
    	if(data.r == 1)
    		if(data.a <= 0)
	   			image += pow(max(dot(normal.xyz,vHalfVector),0.0), 100) * 1.5;
    }
	vec4 raysColor = texture(composite0, texcoord);
	image.rgb = mix(image.rgb, raysColor.rgb, raysColor.a);
	if(useVolumetricLight == 1){
		if (lightDirDOTviewDir>0.0){
			float exposure	= 0.1/NUM_SAMPLES;
			float decay		= 1.02;
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
				vec4 tsample = texture(composite0, textCoo );
				tsample *= illuminationDecay * weight;
				raysColor += tsample;
				illuminationDecay *= decay;
			}
			raysColor *= exposure * lightDirDOTviewDir;
			image +=  raysColor;
		}
	}
	if(data.b != 1) {
		float distance = length(cameraPosition-position.xyz);
		float visibility = exp(-pow((distance*density),gradient));
		visibility = clamp(visibility,0.0,1.1);
    	image.rgb = mix(skyColor.rgb, image.rgb, visibility);
	}
    
    if(camUnderWater == 1){
		out_Color = mix(vec4(0.0,0.0,0.3125,1.0),image,0.5);
	} else {
		out_Color = image;
	}
	
}