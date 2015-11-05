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

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in float visibility;
in vec4 ShadowCoord;

out vec4 out_Color;

uniform sampler2D texture0;
uniform sampler2DShadow depth0;
uniform vec3 skyColour;
uniform vec3 lightPosition;
uniform float blockBright;

float CalcDirectionalLightFactor(vec3 lightDirection, vec3 normal) {
    float DiffuseFactor = dot(normalize(normal), -lightDirection);

    if (DiffuseFactor > 0) {
        return DiffuseFactor;
    } else {
        return 0.0;
    }
}

float CalcPointLightFactor(vec3 lightPosition, vec3 normal, vec3 position) {
    vec3 LightDirection = position - lightPosition;
	float Distance = length(LightDirection);
    LightDirection = normalize(LightDirection);

    float Attenuation = 0.1 * Distance + //linear
                        0.001 * Distance * Distance; //exponential

    return CalcDirectionalLightFactor(LightDirection, normal) / Attenuation;
}

void main(void) {

	struct DirectionalLight {
        vec3 Color;
        vec3 DiffuseIntensity;
        vec3 Direction;
    } Light0;
 	vec3 unitNormal = normalize(surfaceNormal);
    Light0.Direction = lightPosition;
    Light0.Color = vec3(1.0, 1.0, 1.0);
    Light0.DiffuseIntensity = vec3(0.8, 0.8, 0.8);

    vec3 DiffuseColor = Light0.Color * Light0.DiffuseIntensity * CalcDirectionalLightFactor(Light0.Direction, unitNormal);
	vec4 totalDiffuse = vec4(DiffuseColor,1.0);
	vec4 textureColour = texture(texture0, pass_textureCoords);
	
	if(textureColour.a<0.5) {
		discard;
	}
	totalDiffuse = clamp(totalDiffuse, 0.2, 1.0);

	float bias = 0.001;
    	if (texture(depth0, vec3(ShadowCoord.xy, 0.0))  <  ShadowCoord.z-bias ){
   	 		totalDiffuse.xyz -= 0.8;
    }
    
    totalDiffuse.xyz  = totalDiffuse.xyz + blockBright;
	totalDiffuse = clamp(totalDiffuse, 0.2, 1.0);
	out_Color = totalDiffuse * textureColour;
	out_Color = mix(vec4(skyColour,1.0),out_Color,visibility);
}