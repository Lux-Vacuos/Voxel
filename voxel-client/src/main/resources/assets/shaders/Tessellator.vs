//
// This file is part of Voxel
// 
// Copyright (C) 2016-2018 Lux Vacuos
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

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCoords;

out vec2 pass_textureCoordsV;
out vec3 pass_positionV;

uniform float moveFactor;
uniform vec3 cameraPos;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform int useParallax;

void main() {
	vec3 pos = position - cameraPos;
	pass_positionV = position;
	vec4 worldPosition = vec4(pos, 1.0);
	pass_textureCoordsV = textureCoords;
	
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
}