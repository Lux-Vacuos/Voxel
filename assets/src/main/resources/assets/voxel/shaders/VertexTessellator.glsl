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

in vec3 position;
in vec2 textureCoords;
in vec3 normal;
in vec4 data;
in vec3 tangent;
in vec3 bitangent;

out float fadeOut;
out vec2 pass_textureCoords;
out vec3 pass_normal;
out vec3 pass_position;
out vec3 posInTangent;
out vec4 pass_Data;
out vec4 ShadowCoord;
out mat3 TBN;

uniform float moveFactor;
uniform vec3 cameraPos;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionLightMatrix;
uniform mat4 viewLightMatrix;
uniform mat4 biasMatrix;

uniform int useShadows;
uniform int useParallax;

void main() {
	vec3 pos = position - cameraPos;
	vec4 worldPosition = vec4(pos, 1.0);
	pass_textureCoords = textureCoords;
	int id = int(data.x + 0.5f);
	if(id == 7 || id == 10){
		float sina = sin(position.x + moveFactor) * 0.02;
		float sinb = cos(position.z - moveFactor) * 0.008;
		worldPosition += vec4(0.0, sina + sinb, 0.0, 0.0);
		vec3 addPos = vec3(0.0, sina + sinb, 0.0);
		vec3 addNormal = vec3(sina, 1.0, sinb);
		pass_position = position + addPos;
		pass_normal = normal + addNormal;
	} else {
		pass_position = position;
		pass_normal = normal;
	}
	
	pass_Data = data;
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	
	vec3 T = normalize(tangent);
	vec3 B = normalize(bitangent);
	vec3 N = normalize(normal);
	TBN = mat3(T, B, N);
	
	if(useShadows == 1){
		vec4 posLight = viewLightMatrix * vec4(pass_position, 1.0);
		vec4 a = projectionLightMatrix * posLight;
		ShadowCoord = biasMatrix * a;
	}
}