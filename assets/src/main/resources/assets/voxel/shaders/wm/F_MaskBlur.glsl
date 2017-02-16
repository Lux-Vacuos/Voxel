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
uniform int blurBehind;
uniform vec2 resolution;
uniform vec4 frame;

void main(void){
    vec4 window = texture(window, textureCoords);
    out_Color = texture(image, textureCoords);
    if(blurBehind == 1)
        if(gl_FragCoord.x > frame.x && gl_FragCoord.y > frame.y - frame.w && gl_FragCoord.x < frame.x + frame.z && gl_FragCoord.y < frame.y)
            out_Color.a = 0;
}