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

uniform vec3 cameraPosition;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 pointLightsPos[256];

uniform sampler2D gDiffuse;
uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gMask;
uniform sampler2D composite0;

void main(void){
	vec2 texcoord = textureCoords;
    vec4 composite = texture(composite0, texcoord);
	vec4 mask = texture(gMask, texcoord);
	if(mask.a != 1) {
    	vec4 position = texture(gPosition,texcoord);
    	vec4 normal = texture(gNormal, texcoord);
		vec3 eyeDir = normalize(cameraPosition-position.xyz);
	    normal = normalize(normal);
    	for(int x = 0; x < 256; x++){
	    	if(pointLightsPos[x].y < 0)
	    		break;
	    	vec3 lightDir = pointLightsPos[x] - position.xyz;
	    	float distance = length(lightDir);
	    	float attFactor = 1 + (0.01 * distance) + (0.002 * distance * distance);
	    	lightDir = normalize(lightDir);
	    	float finalLight = max(dot(normal.xyz, lightDir), 0) * 1;
	    	finalLight /= attFactor;
	    	composite += finalLight * composite;
	    	vec3 vHalfVector = normalize(lightDir.xyz+eyeDir);
	    	float spec = pow(max(dot(normal.xyz,vHalfVector),0.0), 100) * 1.5;
	    	spec /= attFactor;
			composite += spec;
		}
	}
	out_Color = composite;
}