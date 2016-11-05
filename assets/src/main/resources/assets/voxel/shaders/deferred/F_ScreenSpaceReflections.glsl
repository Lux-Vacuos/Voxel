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

out vec4 out_Color;

uniform vec3 cameraPosition;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gMask;
uniform sampler2D gPBR;
uniform sampler2D composite0;
uniform sampler2DShadow gDepth;

uniform int useReflections;

void main(void){
	vec2 texcoord = textureCoords;
	vec4 image = texture(composite0, texcoord);
	vec4 mask = texture(gMask, texcoord);
	if(mask.a != 1) {
   		if(useReflections == 1){
    		vec4 position = texture(gPosition,texcoord);
			vec4 pbr = texture(gPBR, texcoord);
    		vec3 normal = texture(gNormal, texcoord).rgb;
    		float depth = texture(gDepth, vec3(texcoord.xy, 0.0), 0);
    		vec3 worldStartingPos = position.xyz;
    		vec3 cameraToWorld = worldStartingPos.xyz - cameraPosition.xyz;
    		float cameraToWorldDist = length(cameraToWorld);
    		vec3 cameraToWorldNorm = normalize(cameraToWorld);
    		vec3 refl = normalize(reflect(cameraToWorldNorm, normal));
    		float fact = pbr.g;
			fact += (1-pbr.r) * 0.2;
			fact = clamp(fact,0,1);
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
        		if (newScreen.x > 1 || newScreen.x < -1 || newScreen.y > 1 || newScreen.y < -1)
				//|| dot(refl, cameraToWorldNorm) < 0 || cameraToWorldDist > currentWorldDist)
	       			break;
    		} while(rayDist < currentWorldDist);
 			vec4 newColor = texture(composite0, newScreen.xy/2.0 + 0.5);
    		//if (dot(refl, cameraToWorldNorm) < 0)
	    	//	fact = 1.0;
    		//else 
			if (newScreen.x > 1 || newScreen.x < -1 || newScreen.y > 1 || newScreen.y < -1)
	        	fact = 0.0;
    		//else if (cameraToWorldDist > currentWorldDist)
	        //	fact = 1.0;
			image = mix(image, newColor, fact);
    	}
	}
	out_Color = image;
	
}