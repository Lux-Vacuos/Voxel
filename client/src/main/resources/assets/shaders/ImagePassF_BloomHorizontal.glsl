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

/*--------------------------------------------------------*/
/*--------------COMPOSITE 3 IN-OUT-UNIFORMS---------------*/
/*--------------------------------------------------------*/

in vec2 textureCoords;

out vec4 out_Color;

uniform vec2 resolution;
uniform sampler2D composite0;

/*--------------------------------------------------------*/
/*---------------BLOOM HORIZONTAL CONFIG------------------*/
/*--------------------------------------------------------*/

const float weight[5] = float[] (0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);

/*--------------------------------------------------------*/
/*----------------BLOOM HORIZONTAL CODE-------------------*/
/*--------------------------------------------------------*/


void main(void){
	vec2 texcoord = textureCoords;
	vec2 tex_offset = 1.0 / resolution;
    vec3 result = texture(composite0, texcoord).rgb * weight[0];
    for(int i = 1; i < 5; ++i) {
        result += texture(composite0, texcoord + vec2(tex_offset.x * i, 0.0)).rgb * weight[i];
        result += texture(composite0, texcoord - vec2(tex_offset.x * i, 0.0)).rgb * weight[i];
    }
    out_Color.rgb = result;
}