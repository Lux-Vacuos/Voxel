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

/*--------------------------------------------------------*/
/*----------------SUN PASS IN-OUT-UNIFORMS----------------*/
/*--------------------------------------------------------*/

in vec2 textureCoords;
in vec4 posPos;

out vec4 out_Color;

uniform vec2 sunPositionInScreen;
uniform vec2 resolution;
uniform sampler2D gData0;
uniform sampler2D gData1;

/*--------------------------------------------------------*/
/*-------------------SUN PASS CONFIG----------------------*/
/*--------------------------------------------------------*/

/*--------------------------------------------------------*/
/*--------------------SUN PASS CODE-----------------------*/
/*--------------------------------------------------------*/

vec2 size = vec2(40, 85);

void main(void){
	vec2 scale = vec2(resolution.x / 1280, resolution.y / 720);
	size *= scale;

	vec2 texcoord = textureCoords;
	vec4 image = vec4(0.0);
	vec4 data0 = texture(gData0, texcoord);
	vec4 data1 = texture(gData1, texcoord);
    if(data0.b == 1 && data1.r > 0){
    	vec2 midpoint = sunPositionInScreen.xy;
		float dist = length(gl_FragCoord.xy - midpoint);
		float circle = 1 - smoothstep(size.x-1.0, size.y+1.0, dist);
		image.rgb = vec3(circle,circle,circle);
		image.a = circle;
		image *= data1.r;
    }
    out_Color = image;

}