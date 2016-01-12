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

/*--------------------------------------------------------*/
/*-----------COMPOSITE FINAL 0 IN-OUT-UNIFORMS------------*/
/*--------------------------------------------------------*/

in vec2 textureCoords;
in vec4 posPos;

out vec4 out_Color;

uniform vec2 sunPositionInScreen;
uniform vec2 resolution;
uniform vec3 cameraPosition;
uniform vec3 previousCameraPosition;
uniform mat4 projectionMatrix;
uniform mat4 inverseProjectionMatrix;
uniform mat4 inverseViewMatrix;
uniform mat4 previousViewMatrix;
uniform sampler2D composite0;
uniform sampler2D gDepth;
uniform sampler2D gData0;

uniform int useDOF;
uniform int useMotionBlur;

/*--------------------------------------------------------*/
/*---------------COMPOSITE FINAL 0 CONFIG-----------------*/
/*--------------------------------------------------------*/

/*--------------------------------------------------------*/
/*------------------COMPOSITE 0 CODE----------------------*/
/*--------------------------------------------------------*/

void main(void){
	vec2 texcoord = textureCoords;
	vec4 textureColour = texture(composite0, texcoord);
	vec4 data = texture(gData0, texcoord);
	if(useDOF == 1){
		vec3 sum = textureColour.rgb;
		float bias = min(abs(texture(gDepth, texcoord).x - texture(gDepth, vec2(0.5)).x) * .02, .01);
		for (int i = -3; i < 3; i++) {
			for (int j = -3; j < 3; j++) {
				sum += texture(composite0, texcoord + vec2(j, i) * bias ).rgb;
			}
		}
		sum /= 36.0;
		textureColour = vec4(sum,1.0);
	}
	
	if(useMotionBlur == 1){
		vec3 sum = textureColour.rgb;
		vec4 tex = vec4(texcoord, 0.0,0.0);
		float depth = texture(gDepth, texcoord).x;
		vec4 currentPosition = vec4(tex.x * 2.0 - 1.0, tex.y * 2.0 - 1.0, 2.0 * depth - 1.0, 1.0);
		vec4 fragposition = inverseProjectionMatrix * currentPosition;
		fragposition = inverseViewMatrix * fragposition;
		fragposition /= fragposition.w;
		fragposition.xyz += cameraPosition;
	
		vec4 previousPosition = fragposition;
		previousPosition.xyz -= previousCameraPosition;
		previousPosition = previousViewMatrix * previousPosition;
		previousPosition = projectionMatrix * previousPosition;
		previousPosition /= previousPosition.w;
		vec2 velocity = (currentPosition - previousPosition).st * 0.015;
		int samples = 1;
		vec2 coord = tex.st + velocity;
		for (int i = 0; i < 12; ++i, coord += velocity) {
				sum += texture(composite0, coord).rgb;
				++samples;
		}	
		sum = sum/samples;
		textureColour = vec4(sum, 1.0);
	}
	
    out_Color = textureColour;
}