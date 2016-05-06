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
/*--------------COMPOSITE 2 IN-OUT-UNIFORMS---------------*/
/*--------------------------------------------------------*/

in vec2 textureCoords;

out vec4 out_Color;

uniform vec2 resolution;
uniform sampler2D composite0;
uniform sampler2D composite1;
uniform float exposure;

/*--------------------------------------------------------*/
/*------------------COMPOSITE 2 CONFIG--------------------*/
/*--------------------------------------------------------*/

const float weight[5] = float[] (0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);

const float gamma = 2.2;

/*--------------------------------------------------------*/
/*------------------COMPOSITE 2 CODE----------------------*/
/*--------------------------------------------------------*/


void main(void){
	vec2 texcoord = textureCoords;
	vec4 image0 = vec4(0.0);
	vec2 tex_offset = 1.0 / resolution;
    vec3 result = texture(composite0, texcoord).rgb * weight[0];
    
    for(int i = 1; i < 5; ++i) {
        result += texture(composite0, texcoord + vec2(0.0, tex_offset.y * i)).rgb * weight[i];
        result += texture(composite0, texcoord - vec2(0.0, tex_offset.y * i)).rgb * weight[i];
    }
    vec3 hdrColor = texture(composite1, texcoord).rgb;      
    vec3 bloomColor = result.rgb;
    hdrColor += bloomColor;
    vec3 final = vec3(1.0) - exp(-hdrColor * exposure);
    final = pow(final, vec3(1.0 / gamma));
    out_Color.xyz = final.xyz;
}