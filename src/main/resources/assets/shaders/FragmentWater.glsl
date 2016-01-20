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

in vec3 pass_position;
in vec3 normal;
in vec4 ShadowCoord;

out vec4 [5] out_Color;

uniform float moveFactor;
uniform sampler2DShadow depth;

uniform int useShadows;

const float xPixelOffset = 0.0005;
const float yPixelOffset = 0.0005;

float lookup( vec2 offSet){
	return texture(depth, ShadowCoord.xyz + vec3(offSet.x * xPixelOffset, offSet.y * yPixelOffset, 0.0) );
}

void main(void) {

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
	
	out_Color[0] = vec4(0.0, 0.266, 0.635, 0.0);  
	out_Color[1] = vec4(pass_position.xyz, 0.0);
	out_Color[2] = vec4(normal, 0.0);
	out_Color[3] = vec4(1.0,1.0,0.0,shadow);
	out_Color[4] = vec4(0.0,0.0,0.0,0.0);

}