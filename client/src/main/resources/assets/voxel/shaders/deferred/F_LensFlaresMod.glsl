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
uniform sampler2D composite1;
uniform sampler2D composite2;
uniform sampler2D composite3;

uniform int useLensFlares;

const float mult = 4.0;

void main(){
    vec2 texcoords = textureCoords;
    vec4 textureColor = texture(composite3, texcoords);
    if(useLensFlares == 1) {
        vec4 lensMod = texture(composite1, texcoords);
        lensMod += texture(composite2, texcoords);
        vec4 lensFlare = texture(composite0, texcoords) * (lensMod * mult);
        textureColor += lensFlare;
    }
    out_Color = textureColor;
}