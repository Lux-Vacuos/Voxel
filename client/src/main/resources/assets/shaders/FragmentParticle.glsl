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

in vec2 textureCoords0;
in vec2 textureCoords1;
in float blend;

out vec4 out_Color;

uniform sampler2D particleTexture;

void main(void){

	vec4 colour0 = texture(particleTexture, textureCoords0);
	vec4 colour1 = texture(particleTexture, textureCoords1);
	out_Color = mix(colour0, colour1, blend);

}