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

##include struct Material

in vec2 pass_textureCoords;
in vec3 pass_position;
in mat3 TBN;

out vec4 [5] out_Color;

uniform Material material;

void main(void) {

	vec4 diffuseF = texture(material.diffuseTex, pass_textureCoords);
	float roughnessF = texture(material.roughnessTex, pass_textureCoords).r;
	float metallicF = texture(material.metallicTex, pass_textureCoords).r;

   	diffuseF *= material.diffuse;
	roughnessF *= material.roughness;
	metallicF *= material.metallic;
    if(diffuseF.a < 1)
		discard;
	
	vec3 normal = texture(material.normalTex, pass_textureCoords).rgb;
	normal = normalize(normal * 2.0 - 1.0);
	normal = normalize(TBN * normal);
	
	out_Color[0] = diffuseF;
	out_Color[1] = vec4(pass_position.xyz, 1);
	out_Color[2] = vec4(normal.xyz,0);
	out_Color[3] = vec4(roughnessF, metallicF, 0.0, 0.0);
	out_Color[4] = vec4(0.0);
}
