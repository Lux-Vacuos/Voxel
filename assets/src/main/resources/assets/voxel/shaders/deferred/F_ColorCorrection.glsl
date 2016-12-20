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

uniform sampler2D composite0;
uniform sampler2D composite1;
uniform float exposure;

#define GAMMA  2.2

void main(void){
	vec2 texcoord = textureCoords;
    vec4 bloomColor = texture(composite0, texcoord);
    vec4 hdrColor = texture(composite1, texcoord);      
    
    vec4 final = vec4(1.0) - exp(-hdrColor * exposure);
    final = pow(final, vec4(1.0 / GAMMA));
    final += bloomColor;
    out_Color = final;
}