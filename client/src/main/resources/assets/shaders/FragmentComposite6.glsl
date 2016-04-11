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
/*--------------COMPOSITE 6 IN-OUT-UNIFORMS---------------*/
/*--------------------------------------------------------*/

in vec2 textureCoords;
in vec4 posPos;

out vec4 out_Color;

uniform vec2 sunPositionInScreen;
uniform vec2 resolution;
uniform sampler2D gData0;
uniform sampler2D gData1;

/*--------------------------------------------------------*/
/*------------------COMPOSITE 6 CONFIG--------------------*/
/*--------------------------------------------------------*/

/*--------------------------------------------------------*/
/*------------------COMPOSITE 6 CODE----------------------*/
/*--------------------------------------------------------*/

vec2 size = vec2(40, 45);

void main(void){

	vec2 scale = vec2(resolution.x / 1280, resolution.y / 720);
	size *= scale;

	vec2 texcoord = textureCoords;
	vec4 image = vec4(0.0);
	vec4 data0 = texture(gData0, texcoord);
	vec4 data1 = texture(gData1, texcoord);
    if(data0.b == 1 && data1.r > 0){
    	vec2 midpoint = sunPositionInScreen.xy;
		float radius = min(resolution.x, resolution.y) * 0.1;
		float dist = length(gl_FragCoord.xy - midpoint);
		float circle = 1 - smoothstep(size.x-1.0, size.y+1.0, dist);
		image.rgb = vec3(circle,circle,circle);
		image.a = circle;
		image *= data1.r;
		
	// OLD CUBE SUN GENERATION
    /* 
    	if(gl_FragCoord.x <= sunPositionInScreen.x + 60 && gl_FragCoord.x >= sunPositionInScreen.x - 60 && gl_FragCoord.y <= sunPositionInScreen.y + 60 && gl_FragCoord.y >= sunPositionInScreen.y - 60){
    		image.rgb = mix(image.rgb,vec3(1, 0.870588, 0.678431),0.5);
	    	image.a = 0.2;
    	}
    	if(gl_FragCoord.x <= sunPositionInScreen.x + 40 && gl_FragCoord.x >= sunPositionInScreen.x - 40 && gl_FragCoord.y <= sunPositionInScreen.y + 40 && gl_FragCoord.y >= sunPositionInScreen.y - 40){
    		image.rgb = mix(image.rgb,vec3(1, 0.870588, 0.678431),0.8);
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
    	*/
    }
    out_Color = image;

}