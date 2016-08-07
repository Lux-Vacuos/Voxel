//
// This file is part of Voxel
// 
// Copyright (C) 2016 Lux Vacuos
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
// 
//

#version 330 core

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
uniform sampler2D gData0;
uniform sampler2D gData1;
uniform sampler2D composite0;
uniform sampler2DShadow gDepth;

uniform int useFXAA;
uniform int useDOF;
uniform int useMotionBlur;
uniform int useVolumetricLight;
uniform int useReflections;

void main(void){
	vec2 texcoord = textureCoords;
	vec4 image = texture(composite0, texcoord);
	vec4 data = texture(gData0, texcoord);
    vec4 position = texture(gPosition,texcoord);
    vec4 normal = texture(gNormal, texcoord);
    float depth = texture(gDepth, vec3(texcoord.xy, 0.0), 0);
    if(data.b != 1) {
   		if(data.r > 0.0){
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
    			fact = min(1, 1.3333 - fact*fact);
    			//factt = min(1, 1.38 - factt*factt);
    			vec3 newPos;
    			vec4 newScreen;
    			vec3 rayTrace = worldStartingPos;
    			float currentWorldDist, rayDist;
    			float incr = 0.2;
    			do {
        			rayTrace += refl*incr;
        			incr *= 1.2;
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
        		image = image*fact + newColor *(1-fact) * data.r;
        	}
    	}
    }
    
    if(camUnderWater == 1){
		out_Color = mix(vec4(0.0,0.0,0.3125,1.0),image,0.5);
	} else {
		out_Color = image;
	}
	
}