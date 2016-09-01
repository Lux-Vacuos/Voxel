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
in vec4 posPos;

out vec4 out_Color;

uniform vec2 resolution;
uniform vec2 sunPositionInScreen;
uniform vec3 cameraPosition;
uniform vec3 invertedLightPosition;
uniform sampler2D gPosition;
uniform sampler2D composite0;

uniform int useVolumetricLight;

const int NUM_SAMPLES = 50;

void main(void){
	vec2 texcoord = textureCoords;
	vec4 position = texture(gPosition,texcoord);
    vec3 eyeDir = normalize(cameraPosition-position.xyz);
    vec3 invertedlightDir = invertedLightPosition;
    invertedlightDir = normalize(invertedlightDir);
    float lightDirDOTviewDir = dot(invertedlightDir,eyeDir);
	vec4 raysColor = texture(composite0, texcoord);
	vec4 image = vec4(0.0);
	if(useVolumetricLight == 1){
		if (lightDirDOTviewDir>0.0){
			float exposure	= 0.1/NUM_SAMPLES;
			float decay		= 1;
			float density	= 1;
			float weight	= 6.0;
			float illuminationDecay = 2;
			vec2 pos = vec2(0.0);
			pos.x = (sunPositionInScreen.x) / resolution.x;
			pos.y = (sunPositionInScreen.y) / resolution.y;
			vec2 deltaTextCoord = vec2( texcoord - pos);
			vec2 textCoo = texcoord;
			deltaTextCoord *= 1.0 / float(NUM_SAMPLES) * density;
			for(int i=0; i < NUM_SAMPLES ; i++) {
				textCoo -= deltaTextCoord;
				vec4 tsample = texture(composite0, textCoo );
				tsample *= illuminationDecay * weight;
				raysColor += tsample;
				illuminationDecay *= decay;
			}
			raysColor *= exposure * lightDirDOTviewDir;
			image += raysColor;
		}
	}
	out_Color = image;
}