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
in vec4 pass_position;

out vec4 [5] out_Color;

uniform sampler2D texture0;

void main(void) {
    vec4 textureColour = texture(texture0, pass_textureCoords);
	out_Color[0] = textureColour;
	out_Color[1] = vec4(pass_position.xyz,0);
	out_Color[2] = vec4(surfaceNormal.xyz,0);
	out_Color[3] = vec4(1.0, 0.0, 0.0, 0.0);
	out_Color[4] = vec4(0.0,0.0,0.0,0.0);
}