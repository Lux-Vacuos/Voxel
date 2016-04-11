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

const float max_distort = 0.1;
const int num_iter = 12;
const float reci_num_iter_f = 1.0 / float(num_iter);

/*--------------------------------------------------------*/
/*---------------COMPOSITE FINAL 0 CODE-------------------*/
/*--------------------------------------------------------*/

vec2 barrelDistortion(vec2 coord, float amt) {
	vec2 cc = coord - 0.5;
	float dist = dot(cc, cc);
	return coord + cc * dist * amt;
}

float sat( float t )
{
	return clamp( t, 0.0, 1.0 );
}

float linterp( float t ) {
	return sat( 1.0 - abs( 2.0*t - 1.0 ) );
}

float remap( float t, float a, float b ) {
	return sat( (t - a) / (b - a) );
}

vec4 spectrum_offset( float t ) {
	vec4 ret;
	float lo = step(t,0.5);
	float hi = 1.0-lo;
	float w = linterp( remap( t, 1.0/6.0, 5.0/6.0 ) );
	ret = vec4(lo,1.0,hi, 1.) * vec4(1.0-w, w, 1.0-w, 1.);

	return pow( ret, vec4(1.0/2.2) );
}

void main(void){
	vec2 texcoord = textureCoords;
	vec4 textureColour = vec4(0.0);
	vec4 data = texture(gData0, texcoord);
	textureColour = texture(composite0, texcoord);
	// CHROMATIC ABERRATION - DISABLED - Needs own FBO/Shader
	/*
	vec2 uv=(gl_FragCoord.xy/resolution.xy);
	vec4 sumcol = vec4(0.0);
	vec4 sumw = vec4(0.0);	
	for ( int i=0; i<num_iter;++i )
	{
		float t = float(i) * reci_num_iter_f;
		vec4 w = spectrum_offset( t );
		sumw += w;
		sumcol += w * texture(composite0, barrelDistortion(uv, .6 * max_distort*t ) );
	}
		
	textureColour = sumcol / sumw;
	*/
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