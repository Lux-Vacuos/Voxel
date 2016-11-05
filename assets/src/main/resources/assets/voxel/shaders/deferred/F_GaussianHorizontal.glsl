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
in vec2 blurTexCoords[11];

out vec4 out_Color;

uniform sampler2D composite0;

void main(void){
	vec2 texcoord = textureCoords;
    vec4 result = vec4(0.0);
    result += texture(composite0, blurTexCoords[0]) * 0.0093;
    result += texture(composite0, blurTexCoords[1]) * 0.028002;
    result += texture(composite0, blurTexCoords[2]) * 0.065984;
    result += texture(composite0, blurTexCoords[3]) * 0.121703;
    result += texture(composite0, blurTexCoords[4]) * 0.175713;
    result += texture(composite0, blurTexCoords[5]) * 0.198596;
    result += texture(composite0, blurTexCoords[6]) * 0.175713;
    result += texture(composite0, blurTexCoords[7]) * 0.121703;
    result += texture(composite0, blurTexCoords[8]) * 0.065984;
    result += texture(composite0, blurTexCoords[9]) * 0.028002;
	result += texture(composite0, blurTexCoords[10]) * 0.0093;
    out_Color = result;
}