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

in vec2 position;

out vec4 clipSpace;
out vec2 textureCoords;
out vec3 toCameraVector;
out vec3 pass_position;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 cameraPosition;
uniform vec3 directLightDirection;
uniform float fogDensity;
uniform float moveFactor;

const float tiling = 0.5;

void main(void) {
	vec4 worldPosition = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);
	float sina = sin(worldPosition.x + moveFactor) * 0.05;
	float sinb = cos(worldPosition.z + moveFactor) * 0.02;
	worldPosition += vec4(0.0, sina + sinb, 0.0, 0.0);
	clipSpace = projectionMatrix * viewMatrix * worldPosition;
	gl_Position = clipSpace;
	pass_position = worldPosition.xyz;
	textureCoords = vec2(position.x/2.0 + 0.5, position.y/2.0 + 0.5) * tiling;
}