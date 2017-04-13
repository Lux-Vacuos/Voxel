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
in vec2 blurTexCoords[17];

out vec4 out_Color;

uniform sampler2D image;
uniform sampler2D window;

void main(void){
    vec4 result = vec4(0.0);
    vec4 mask = texture(image, textureCoords);
    if(mask.a == 0) {
        result += texture(image, blurTexCoords[0]) * 0.024418;
        result += texture(image, blurTexCoords[1]) * 0.032928;
        result += texture(image, blurTexCoords[2]) * 0.042669;
        result += texture(image, blurTexCoords[3]) * 0.05313;
        result += texture(image, blurTexCoords[4]) * 0.06357;
        result += texture(image, blurTexCoords[5]) * 0.073088;
        result += texture(image, blurTexCoords[6]) * 0.080748;
        result += texture(image, blurTexCoords[7]) * 0.085724;
        result += texture(image, blurTexCoords[8]) * 0.08745;
        result += texture(image, blurTexCoords[9]) * 0.085724;
        result += texture(image, blurTexCoords[10]) * 0.080748;
        result += texture(image, blurTexCoords[11]) * 0.073088;
        result += texture(image, blurTexCoords[12]) * 0.06357;
        result += texture(image, blurTexCoords[13]) * 0.05313;
        result += texture(image, blurTexCoords[14]) * 0.042669;
        result += texture(image, blurTexCoords[15]) * 0.032928;
        result += texture(image, blurTexCoords[16]) * 0.024418;
        out_Color = result;
        out_Color.a = 0;
    } else {
        out_Color = texture(image, textureCoords);
    }
}