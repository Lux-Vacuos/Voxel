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

uniform sampler2D image;
uniform sampler2D window;

void main(void){
    vec4 source = texture(image,textureCoords);
    vec4 window = texture(window,textureCoords);
    if(window.a < 1 && window.r != 0 && window.g != 0 && window.b != 0) {
        vec3 sum = source.rgb;
        for (int i = -4; i < 4; i++) {
		    for (int j = -4; j < 4; j++) {
			    sum += texture(image, textureCoords + vec2(j, i) * 0.002 ).rgb;
    		}
	    }
	    sum /= 65.0;
        source.rgb = sum;
    }
    out_Color = mix(source, window, window.a);
}