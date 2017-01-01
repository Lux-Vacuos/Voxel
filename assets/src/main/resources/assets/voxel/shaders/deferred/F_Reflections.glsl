//
// This file is part of Voxel
// 
// Copyright (C) 2016-2017 Lux Vacuos
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

out vec4 out_Color;

uniform vec3 cameraPosition;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gMask;
uniform sampler2D gPBR;
uniform sampler2D gDiffuse;
uniform samplerCube gEnvironment;
uniform sampler2D composite0;
uniform sampler2DShadow gDepth;

uniform int useReflections;

vec3 fresnelSchlickRoughness(float cosTheta, vec3 F0, float roughness) {
    return F0 + (max(vec3(1.0 - roughness), F0) - F0) * pow(1.0 - cosTheta, 5.0);
}

void main(void){
	vec2 texcoord = textureCoords;
	vec4 image = texture(composite0, texcoord);
	vec4 mask = texture(gMask, texcoord);
	if(mask.a != 1) {
   		if(useReflections == 1) {
			vec4 pbr = texture(gPBR, texcoord);
			if(pbr.b > 0.0) {
	    		vec4 position = texture(gPosition,texcoord);
    			vec3 normal = texture(gNormal, texcoord).rgb;
    			float depth = texture(gDepth, vec3(texcoord.xy, 0.0), 0);
    			vec3 worldStartingPos = position.xyz;
    			vec3 cameraToWorld = worldStartingPos.xyz - cameraPosition.xyz;
    			float cameraToWorldDist = length(cameraToWorld);
    			vec3 cameraToWorldNorm = normalize(cameraToWorld);
    			vec3 refl = normalize(reflect(cameraToWorldNorm, normal));
				
				vec3 V = normalize(cameraPosition - position.xyz);
				vec3 F0 = vec3(0.04); 
				F0 = mix(F0, texture(gDiffuse, texcoord).rgb, pbr.g);
				vec3 F = fresnelSchlickRoughness(max(dot(normal, V), 0.0), F0, pbr.r);
				vec3 kS = F;
				vec3 kD = vec3(1.0) - kS;
				kD *= 1.0 - pbr.g;	
				
    			vec3 newPos;
    			vec4 newScreen;
    			vec3 rayTrace = worldStartingPos;
    			float currentWorldDist, rayDist;
				float i = 0;
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
        			if (newScreen.x > 1 || newScreen.x < -1 || newScreen.y > 1 || newScreen.y < -1 || newScreen.z > 1 || newScreen.z < -1 || i >= 1.0 || cameraToWorldDist > currentWorldDist)
		       			break;
    			} while(rayDist < currentWorldDist);

				vec4 newColor = texture(composite0, newScreen.xy/2.0 + 0.5);
				vec4 enviromentMap = texture(gEnvironment, refl);

 				float fact = 1.0;
    			if (dot(refl, cameraToWorldNorm) < 0)
	    			fact = 0.0;
    			else if (newScreen.x > 1 || newScreen.x < -1 || newScreen.y > 1 || newScreen.y < -1)
		        	fact = 0.0;
    			else if (cameraToWorldDist > currentWorldDist)
		        	fact = 0.0;
				else if(newScreen.z > 1 && newScreen.z < -1)
					fact = 0.0;
				vec4 finalReflection = mix(enviromentMap, newColor, fact);
				image.rgb = image.rgb * kD + max(finalReflection.rgb, 0.0) * (1-kD);
    		}
		}
	}
	out_Color = image;
	
}