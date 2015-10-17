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

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in float visibility;

out vec4 out_Color;

uniform sampler2D texture0;
uniform sampler2D depth0;
uniform vec3 skyColour;
uniform float time;
uniform float blendFactor;
uniform float blockBright;

void main(void) {

	vec4 totalDiffuse = vec4(blockBright);
	
	totalDiffuse = clamp(totalDiffuse, 0.0, 1.0);
	
	vec4 textureColour = texture(texture0, pass_textureCoords);
	if(textureColour.a<0.5) {
		discard;
	}
	out_Color = totalDiffuse * textureColour;
	out_Color = mix(vec4(skyColour,1.0),out_Color,visibility);
}