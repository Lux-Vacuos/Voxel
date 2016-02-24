//
// The MIT License (MIT)
//
// Copyright (c) 2015-2016 Guerra24
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

in vec3 position;
in vec2 textureCoords;
in vec3 normal;
in vec4 data;
in vec3 tangent;
in vec3 bitangent;

out float fadeOut;
out vec2 pass_textureCoords;
out vec3 pass_normal;
out vec3 pass_position;
out vec3 posInTangent;
out vec4 pass_Data;
out vec4 ShadowCoord;
out mat3 TBN;

uniform float moveFactor;
uniform vec3 cameraPos;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionLightMatrix;
uniform mat4 viewLightMatrix;
uniform mat4 biasMatrix;

uniform int useShadows;
uniform int useParallax;

void main() {
	vec3 pos = position - cameraPos;
	vec4 worldPosition = vec4(pos, 1.0);
	pass_textureCoords = textureCoords;
	int id = int(data.x + 0.5f);
	if(id == 7 || id == 10){
		float sina = sin(position.x + moveFactor) * 0.02;
		float sinb = cos(position.z - moveFactor) * 0.008;
		worldPosition += vec4(0.0, sina + sinb, 0.0, 0.0);
		vec3 addPos = vec3(0.0, sina + sinb, 0.0);
		vec3 addNormal = vec3(sina, 1.0, sinb);
		pass_position = position + addPos;
		pass_normal = normal + addNormal;
	} else {
		pass_position = position;
		pass_normal = normal;
	}
	
	pass_Data = data;
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	
	vec3 T = normalize(tangent);
	vec3 B = normalize(bitangent);
	vec3 N = normalize(normal);
	TBN = mat3(T, B, N);
	
	if(useShadows == 1){
		vec4 posLight = viewLightMatrix * vec4(pass_position, 1.0);
		vec4 a = projectionLightMatrix * posLight;
		ShadowCoord = biasMatrix * a;
	}
}