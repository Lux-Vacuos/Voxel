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

struct Material {
	vec3 color;
	float roughness;
	float metallic;
};

in float visibility;
in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec4 pass_position;
in vec4 ShadowCoord;

out vec4 [5] out_Color;

uniform sampler2D texture0;
uniform sampler2DShadow depth;
uniform vec3 skyColour;
uniform vec3 lightPosition;
uniform Material material;

uniform int useShadows;

const float xPixelOffset = 0.0002;
const float yPixelOffset = 0.0002;

float lookup( vec2 offSet){
	return texture(depth, ShadowCoord.xyz + vec3(offSet.x * xPixelOffset, offSet.y * yPixelOffset, 0.0) );
}
void main(void) {
    vec4 textureColour = texture(texture0, pass_textureCoords);
	float shadow = 0;
	if(useShadows == 1){
		float x,y;
		for (y = -1.5 ; y <=1.5 ; y+=1.0)
			for (x = -1.5 ; x <=1.5 ; x+=1.0)
				shadow += -lookup(vec2(x,y)) + 1;
		shadow /= 16.0 ;
		if(ShadowCoord.z > 1.0){
       		shadow = 0.0;
       	}
	}
    
    if(textureColour.a<0.5) {
		discard;
	}
	
	out_Color[0] = textureColour;
	out_Color[1] = vec4(pass_position.xyz,shadow);
	out_Color[2] = vec4(surfaceNormal.xyz,0);
	out_Color[3] = vec4(material.roughness, material.metallic, 0.0, 0.0);
	out_Color[4] = vec4(0.0);
}