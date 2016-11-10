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
uniform vec3 lightPosition;
uniform sampler2D gDiffuse;
uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gPBR; // R = roughness, B = metallic
uniform sampler2D gMask;
uniform sampler2D composite0;
uniform int shadowDrawDistance;

const float transitionDistance = 5;

void main(void){
	vec2 texcoord = textureCoords;
	vec4 mask = texture(gMask, texcoord);
	vec4 image = texture(gDiffuse, texcoord);
	vec4 pbr = texture(gPBR, texcoord);
	if(mask.a != 1) {
    	vec4 position = texture(gPosition,texcoord);
    	vec4 normal = texture(gNormal, texcoord);
		image.rgb *= 1.0 - pbr.g;
		vec3 lightDir = lightPosition;
    	lightDir = normalize(lightDir);
    	vec3 eyeDir = normalize(cameraPosition-position.xyz);
		float distance = length(cameraPosition-position.xyz);
    	normal = normalize(normal);
    	float shadowDist = distance - (shadowDrawDistance - transitionDistance);
		shadowDist = shadowDist / transitionDistance;
		float fadeOut = clamp(1.0-shadowDist, 0.0, 1.0);
		float normalDotLight = max(dot(normal.xyz, lightDir), 0);
    	float finalLight = normalDotLight - (position.w * fadeOut);
    	finalLight = clamp(finalLight,0.015,1.0);
    	image = finalLight * image;
    	if(position.w <= 0.5 && normalDotLight > 0){
    		vec3 vHalfVector = normalize(lightDir.xyz+eyeDir);
	   		image += pow(max(dot(normal.xyz,vHalfVector),0.0), 200 / (1-pbr.r)) * (1-pbr.r);
	   	}
	}
    image += texture(composite0, texcoord);
	out_Color = image;
	
}