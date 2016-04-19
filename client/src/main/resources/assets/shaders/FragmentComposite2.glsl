//
// The MIT License (MIT)
//
// Copyright (c) 2015-2016 Lux Vacuos
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
/*--------------COMPOSITE 2 IN-OUT-UNIFORMS---------------*/
/*--------------------------------------------------------*/

in vec2 textureCoords;

out vec4 out_Color;

uniform vec2 resolution;
uniform sampler2D composite0;
uniform sampler2D composite1;
uniform float exposure;

/*--------------------------------------------------------*/
/*------------------COMPOSITE 2 CONFIG--------------------*/
/*--------------------------------------------------------*/

const float weight[5] = float[] (0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);

const float gamma = 2.2;

/*--------------------------------------------------------*/
/*------------------COMPOSITE 2 CODE----------------------*/
/*--------------------------------------------------------*/


void main(void){
	vec2 texcoord = textureCoords;
	vec4 image0 = vec4(0.0);
	vec2 tex_offset = 1.0 / resolution;
    vec3 result = texture(composite0, texcoord).rgb * weight[0];
    
    for(int i = 1; i < 5; ++i) {
        result += texture(composite0, texcoord + vec2(0.0, tex_offset.y * i)).rgb * weight[i];
        result += texture(composite0, texcoord - vec2(0.0, tex_offset.y * i)).rgb * weight[i];
    }
    vec3 hdrColor = texture(composite1, texcoord).rgb;      
    vec3 bloomColor = result.rgb;
    hdrColor += bloomColor;
    vec3 final = vec3(1.0) - exp(-hdrColor * exposure);
    final = pow(final, vec3(1.0 / gamma));
    out_Color.xyz = final.xyz;
}