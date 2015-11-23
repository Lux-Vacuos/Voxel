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

/*--------------------------------------------------------*/
/*--------------COMPOSITE 1 IN-OUT-UNIFORMS---------------*/
/*--------------------------------------------------------*/

in vec2 textureCoords;
in vec4 posPos;

out vec4 out_Color;

uniform vec2 sunPositionInScreen;
uniform sampler2D gData;

/*--------------------------------------------------------*/
/*------------------COMPOSITE 1 CONFIG--------------------*/
/*--------------------------------------------------------*/

/*--------------------------------------------------------*/
/*------------------COMPOSITE 1 CODE----------------------*/
/*--------------------------------------------------------*/


void main(void){
	vec2 texcoord = textureCoords;
	vec4 image = vec4(0.0);
	vec4 data = texture(gData, texcoord);

    if(data.b == 1){
    	if(gl_FragCoord.x <= sunPositionInScreen.x + 60 && gl_FragCoord.x >= sunPositionInScreen.x - 60 && gl_FragCoord.y <= sunPositionInScreen.y + 60 && gl_FragCoord.y >= sunPositionInScreen.y - 60){
    		image.rgb = mix(image.rgb,vec3(1, 0.870588, 0.678431),0.2);
    		image.a = 0.2;
    	}
    	if(gl_FragCoord.x <= sunPositionInScreen.x + 40 && gl_FragCoord.x >= sunPositionInScreen.x - 40 && gl_FragCoord.y <= sunPositionInScreen.y + 40 && gl_FragCoord.y >= sunPositionInScreen.y - 40){
    		image.rgb = mix(image.rgb,vec3(1, 0.870588, 0.678431),0.5);
    		image.a = 0.5;
    	}
    	if(gl_FragCoord.x <= sunPositionInScreen.x + 30 && gl_FragCoord.x >= sunPositionInScreen.x - 30 && gl_FragCoord.y <= sunPositionInScreen.y + 30 && gl_FragCoord.y >= sunPositionInScreen.y - 30){
    		image.rgb = vec3(1, 0.870588, 0.678431);
    		image.a = 1;
    	}
    	if(gl_FragCoord.x <= sunPositionInScreen.x + 20 && gl_FragCoord.x >= sunPositionInScreen.x - 20 && gl_FragCoord.y <= sunPositionInScreen.y + 20 && gl_FragCoord.y >= sunPositionInScreen.y - 20){
    		image.rgb = vec3(1,1,1);
    		image.a = 1;
    	}
    }
    out_Color = image;

}