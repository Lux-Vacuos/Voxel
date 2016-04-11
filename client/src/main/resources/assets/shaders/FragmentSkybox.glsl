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

in vec3 textureCoords;
in vec3 pass_position;

out vec4 [5] out_Color;

uniform float time;
uniform vec3 fogColour;
uniform vec3 lightPosition;

const float lowerLimit = -0.0;
const float upperLimit =  0.6;

#define CLOUD_COVER		0.55
#define CLOUD_SHARPNESS		0.005

float hash( float n )
{
	return fract(sin(n)*43758.5453);
}

float noise( in vec2 x )
{
	vec2 p = floor(x);
	vec2 f = fract(x);
    	f = f*f*(3.0-2.0*f);
    	float n = p.x + p.y*57.0;
    	float res = mix(mix( hash(n+  0.0), hash(n+  1.0),f.x), mix( hash(n+ 57.0), hash(n+ 58.0),f.x),f.y);
    	return res;
}

float fbm( vec2 p )
{
    	float f = 0.0;
    	f += 0.50000*noise( p ); p = p*2.02;
    	f += 0.25000*noise( p ); p = p*2.03;
    	f += 0.12500*noise( p ); p = p*2.01;
    	f += 0.06250*noise( p ); p = p*2.04;
    	f += 0.03125*noise( p );
    	return f/0.984375;
}

void main(void){

	float tt = time / 10;
	vec2 wind_vec = vec2(0.001 + tt*0.01, 0.003 + tt * 0.01);
	
	// Set up domain
	vec2 q = (textureCoords.xz);
	vec2 p = -1.0 + 3.0 * q + wind_vec;
	
	float f = fbm( 2.0*p );

	float cover = CLOUD_COVER;
	float sharpness = CLOUD_SHARPNESS;
	
	float c = f - (1.0 - cover);
	if ( c < 0.0 )
		c = 0.0;
	f = 1.0 - (pow(sharpness, c));
	
	float fac = max(dot(vec3(0,1,0),normalize(lightPosition)),-1.0);
	fac = clamp(fac,0.02,1.0);
	f *= 3;
	f *= fac;
	
	float facHaze = max(dot(textureCoords,normalize(lightPosition)),-1.0);
	vec3 col = vec3(1,0.2,0);

    vec4 finalColour = vec4(fogColour,1.0);
    finalColour.rgb *= fac;
    float factor = (textureCoords.y - lowerLimit) / (upperLimit - lowerLimit);
    factor = clamp(factor, 0.0, 1.0);
    finalColour = mix(finalColour, vec4(f,f,f,1.0), factor);
    
    out_Color[0] = finalColour;
    out_Color[1] = vec4(pass_position.xyz,0);
    out_Color[2] = vec4(0.0);
    out_Color[3] = vec4(0,0,1,0);
    out_Color[4] = vec4(1.0 - f,0,0,0);
    
    if(factor < 0.01)
    	out_Color[4].r = 0;
}