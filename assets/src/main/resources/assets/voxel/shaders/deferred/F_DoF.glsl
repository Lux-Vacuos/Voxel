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

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D composite0;
uniform sampler2D gDepth;

uniform int useDOF;

void main(void){
	vec2 texcoord = textureCoords;
	vec4 textureColour = texture(composite0, texcoord);
	if(useDOF == 1){
		vec3 sum = textureColour.rgb;
		float bias = min(abs(texture(gDepth, texcoord).x - texture(gDepth, vec2(0.5)).x) * .02, .01);
		for (int i = -4; i < 4; i++) {
			for (int j = -4; j < 4; j++) {
				sum += texture(composite0, texcoord + vec2(j, i) * bias ).rgb;
			}
		}
		sum /= 65.0;
		textureColour = vec4(sum,1.0);
	}
	
    out_Color = textureColour;
}