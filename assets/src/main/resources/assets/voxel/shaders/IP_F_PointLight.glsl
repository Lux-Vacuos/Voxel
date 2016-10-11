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
uniform sampler2D gData0;
uniform sampler2D gData1;
uniform sampler2D composite0;
uniform sampler2DShadow gDepth;

void main(void){
	vec2 texcoord = textureCoords;
	vec4 image = texture(gDiffuse, texcoord);
	vec4 data = texture(gData0, texcoord);
	vec4 data1 = texture(gData1, texcoord);
    vec4 position = texture(gPosition,texcoord);
    vec4 normal = texture(gNormal, texcoord);
    float depth = texture(gDepth, vec3(texcoord.xy, 0.0), 0);
    vec4 composite = texture(composite0, texcoord);
    vec3 eyeDir = normalize(cameraPosition-position.xyz);
    normal = normalize(normal);
    for(int x = 0; x < 256; x++){
    	if(pointLightsPos[x].y < 0)
    		break;
    	vec3 lightDir = pointLightsPos[x] - position.xyz;
    	float distance = length(lightDir);
    	float attFactor = 1 + (0.01 * distance) + (0.002 * distance * distance);
    	lightDir = normalize(lightDir);
    	if(data.b != 1) {
    		float b = max(dot(normal.xyz, lightDir), 0) * 1;
    		b /= attFactor;
    		composite += b * composite;
    		if(data.r> 0.0) {
    			vec3 vHalfVector = normalize(lightDir.xyz+eyeDir);
    			float spec = pow(max(dot(normal.xyz,vHalfVector),0.0), 100) * data.r * 1.5;
    			spec /= attFactor;
		   		composite += spec;
		   		
		   	}
		}
	}
    
	out_Color = composite;
}