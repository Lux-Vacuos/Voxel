//
// The MIT License (MIT)
//
// Copyright (c) 2015 Guerra24
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

#version 330 core

in vec2 textureCoords;
in vec4 pass_position;

out vec4 [4] out_Color;

uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform float moveFactor;
uniform vec3 skyColour;

uniform int useHQWater;

const float waveStrength = 0.02;

void main(void) {
	
	out_Color[0] = vec4(0.0, 0.266, 0.635, 0);  
	out_Color[1] = vec4(pass_position.xyz,0);
	if(useHQWater == 1){
		vec2 distortedTexCoords = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg*0.1;
		distortedTexCoords = textureCoords + vec2(distortedTexCoords.x, distortedTexCoords.y+moveFactor);
		vec2 totalDistortion = (texture(dudvMap, distortedTexCoords).rg * 2.0 - 1.0) * waveStrength;
		vec4 normalMapColour = texture(normalMap, totalDistortion);
		vec3 normal = vec3(normalMapColour.r * 2.0 - 1.0,normalMapColour.b,normalMapColour.g * 2.0 - 1.0);
		out_Color[2] = vec4(normal.xyz,0);
	} else {
		out_Color[2] = vec4(0,1,0,0);
	}
	out_Color[3] = vec4(1.0,0.0,0.0,0.0);

}