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
/*--------------COMPOSITE 4 IN-OUT-UNIFORMS---------------*/
/*--------------------------------------------------------*/

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D composite0;
uniform sampler2D gData0;

/*--------------------------------------------------------*/
/*------------------COMPOSITE 4 CONFIG--------------------*/
/*--------------------------------------------------------*/

/*--------------------------------------------------------*/
/*------------------COMPOSITE 4 CODE----------------------*/
/*--------------------------------------------------------*/


void main(void){
	vec2 texcoord = textureCoords;
	vec4 image = vec4(0.0);
	vec4 result = vec4(0.0);
	vec4 data0 = texture(gData0, texcoord);
	if(data0.b != 1){
	image = texture(composite0, texcoord);
	float brightness = dot(image.rgb, vec3(1, 1, 1));
    	if(brightness > 1)
    	    result = vec4(image.rgb, 1.0);
		out_Color = result;
	}
}