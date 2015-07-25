//
// The MIT License (MIT)
//
// Copyright (c) 2015 Guerra24 / ThinMatrix
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

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[1];
in vec3 lightIntensity;
in float visibility;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 skyColour;
uniform vec3 lightColour[1];
uniform vec3 attenuations[1];

void main(void) {

	vec3 unitNormal = normalize(surfaceNormal);
	
	vec4 totalDiffuse = vec4(0.0);
	
	// code for dynamic light
	for(int i=0;i<1;i++) {
		float distance = length(toLightVector[i]);
		float attFactor = attenuations[i].x + (attenuations[i].y * distance) + (attenuations[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDotl = dot(unitNormal,unitLightVector);
		float brightness = max(nDotl, 0.0);
		totalDiffuse.xyz = totalDiffuse.xyz + (brightness * lightColour[i])/attFactor;
		
	}
	vec4 light = clamp(vec4(lightIntensity, 1.0), 0.0, 1.0);
	totalDiffuse = totalDiffuse + light, 1.0;
	totalDiffuse = max(totalDiffuse, 0.1);
	totalDiffuse = min(totalDiffuse, 0.8);
	
	vec4 textureColour = texture(textureSampler, pass_textureCoords);
	if(textureColour.a<0.5) {
		discard;
	}
	out_Color = totalDiffuse * textureColour;
	out_Color = mix(vec4(skyColour,1.0),out_Color,visibility);
}