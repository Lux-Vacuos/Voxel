//
// The MIT License (MIT)
//
// Copyright (c) 2015-2016 Guerra24
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
/*--------------COMPOSITE 1 IN-OUT-UNIFORMS---------------*/
/*--------------------------------------------------------*/

in vec2 textureCoords;
in vec4 posPos;

out vec4 out_Color;

uniform int camUnderWater;
uniform float camUnderWaterOffset;
uniform float skyboxBlendFactor;
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
uniform sampler2D gData0;
uniform sampler2D gData1;
uniform sampler2D composite0;
uniform sampler2DShadow gDepth;
uniform samplerCube skyboxDay;
uniform samplerCube skyboxNight;

uniform int useFXAA;
uniform int useDOF;
uniform int useMotionBlur;
uniform int useVolumetricLight;
uniform int useReflections;

/*--------------------------------------------------------*/
/*------------------COMPOSITE 1 CONFIG--------------------*/
/*--------------------------------------------------------*/

const int NUM_SAMPLES = 50;		

/*--------------------------------------------------------*/
/*------------------COMPOSITE 1 CODE----------------------*/
/*--------------------------------------------------------*/

void main(void){
	vec2 texcoord = textureCoords;
	if(camUnderWater == 1){
		texcoord.x += sin(texcoord.y * 4*2*3.14159 + camUnderWaterOffset) / 100;
	}
	vec4 image = texture(composite0, texcoord);
	vec4 data = texture(gData0, texcoord);
    vec4 position = texture(gPosition,texcoord);
    vec4 normal = texture(gNormal, texcoord);
    float depth = texture(gDepth, vec3(texcoord.xy, 0.0), 0);
    if(data.b != 1) {
   		if(data.r == 1.0){
   			if(useReflections == 1){
    			vec3 worldStartingPos = position.xyz;
    			vec3 normal = normal.xyz;
    			vec3 cameraToWorld = worldStartingPos.xyz - cameraPosition.xyz;
    			float cameraToWorldDist = length(cameraToWorld);
    			vec3 cameraToWorldNorm = normalize(cameraToWorld);
    			vec3 refl = normalize(reflect(cameraToWorldNorm, normal));
    			float cosAngle = abs(dot(normal, cameraToWorldNorm));
    			float fact = 1 - cosAngle;
    			//float factt = 1 - cosAngle;
    			fact = min(1, 1.38 - fact*fact);
    			//factt = min(1, 1.38 - factt*factt);
    			vec3 newPos;
    			vec4 newScreen;
    			float i = 0;
    			vec3 rayTrace = worldStartingPos;
    			float currentWorldDist, rayDist;
    			float incr = 0.4;
    			do {
	        		i += 0.05;
        			rayTrace += refl*incr;
        			incr *= 1.4;
        			newScreen = projectionMatrix * viewMatrix * vec4(rayTrace, 1);
        			newScreen /= newScreen.w;
        			newPos = texture(gPosition, newScreen.xy/2.0+0.5).xyz;
        			currentWorldDist = length(newPos.xyz - cameraPosition.xyz);
        			rayDist = length(rayTrace.xyz - cameraPosition.xyz);
        			if (newScreen.x > 1 || newScreen.x < -1 || newScreen.y > 1 || newScreen.y < -1 || dot(refl, cameraToWorldNorm) < 0 || cameraToWorldDist > currentWorldDist)
	        			break;
    			} while(rayDist < currentWorldDist);
 				vec4 newColor = texture(composite0, newScreen.xy/2.0 + 0.5);
    			if (dot(refl, cameraToWorldNorm) < 0)
	        		fact = 1.0;
    			else if (newScreen.x > 1 || newScreen.x < -1 || newScreen.y > 1 || newScreen.y < -1)
	        		fact = 1.0;
    			else if (cameraToWorldDist > currentWorldDist)
	        		fact = 1.0;
	        	// FAKE CUBE REFLECTION
        		//vec3 I = normalize(position.xyz - cameraPosition);
    			//vec3 R = reflect(I, normalize(normal.xyz));
   				//vec4 imageDay = texture(skyboxDay, R);
   				//vec4 imageNight = texture(skyboxNight, R);
    			//vec4 fakeRelf = mix(imageDay, imageNight, skyboxBlendFactor);
    			//image = mix(image,fakeRelf, 1-factt);
        		image = image*fact + newColor *(1-fact);
        	}
    	}
    }
    
    if(camUnderWater == 1){
		out_Color = mix(vec4(0.0,0.0,0.3125,1.0),image,0.5);
	} else {
		out_Color = image;
	}
	
}