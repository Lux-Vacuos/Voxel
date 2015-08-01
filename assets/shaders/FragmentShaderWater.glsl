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

in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVector;
in vec3 fromLightVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform float moveFactor;
uniform vec3 lightColour;
uniform vec3 skyColour;

const float waveStrength = 0.02;
const float shineDamper = 20.0;
const float reflectivity = 0.6;

void main(void) {

	vec2 ndc = (clipSpace.xy/clipSpace.w)/2.0 + 0.5;
	
	vec2 distortedTexCoords = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg*0.1;
	distortedTexCoords = textureCoords + vec2(distortedTexCoords.x, distortedTexCoords.y+moveFactor);
	vec2 totalDistortion = (texture(dudvMap, distortedTexCoords).rg * 2.0 - 1.0) * waveStrength;
	
	vec3 viewVector = normalize(toCameraVector);
	float refractiveFactor = dot(viewVector, vec3( 0.0, 1.0, 0.0));
	refractiveFactor = pow(refractiveFactor, 0.8);
	
	vec4 normalMapColour = texture(normalMap, distortedTexCoords);
	vec3 normal = vec3(normalMapColour.r * 2.0 - 1.0,normalMapColour.b,normalMapColour.g * 2.0 - 1.0);
	normal = normalize(normal);
	
	vec3 reflectedLight = reflect(normalize(fromLightVector), normal);
	float specular = max(dot(reflectedLight, viewVector), 0.0);
	specular = pow(specular, shineDamper);
	vec3 specularHighlights = vec3(1.0,1.0,1.0) * specular * reflectivity;
	
	out_Color = vec4(0.0, 0.0, 0.8, 0.8) + vec4(specularHighlights, 0.0);
	out_Color = mix(vec4(skyColour,1.0),out_Color,visibility);

}