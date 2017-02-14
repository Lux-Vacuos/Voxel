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
        result += textureLod(image, blurTexCoords[0], 0) * 0.024418;
        result += textureLod(image, blurTexCoords[1], 0) * 0.032928;
        result += textureLod(image, blurTexCoords[2], 0) * 0.042669;
        result += textureLod(image, blurTexCoords[3], 0) * 0.05313;
        result += textureLod(image, blurTexCoords[4], 0) * 0.06357;
        result += textureLod(image, blurTexCoords[5], 0) * 0.073088;
        result += textureLod(image, blurTexCoords[6], 0) * 0.080748;
        result += textureLod(image, blurTexCoords[7], 0) * 0.085724;
        result += textureLod(image, blurTexCoords[8], 0) * 0.08745;
        result += textureLod(image, blurTexCoords[9], 0) * 0.085724;
        result += textureLod(image, blurTexCoords[10], 0) * 0.080748;
        result += textureLod(image, blurTexCoords[11], 0) * 0.073088;
        result += textureLod(image, blurTexCoords[12], 0) * 0.06357;
        result += textureLod(image, blurTexCoords[13], 0) * 0.05313;
        result += textureLod(image, blurTexCoords[14], 0) * 0.042669;
        result += textureLod(image, blurTexCoords[15], 0) * 0.032928;
        result += textureLod(image, blurTexCoords[16], 0) * 0.024418;
        out_Color = result;
    } else {
        out_Color = texture(image, textureCoords);
    }
}