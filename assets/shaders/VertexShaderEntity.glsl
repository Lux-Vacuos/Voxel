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

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector[1];
out vec3 lightIntensity;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[1];
uniform vec4 plane;
uniform vec3 directLightDirection;

const float density = 0.0023;
const float gradient = 10.0;

float CalcDirectionalLightFactor(vec3 lightDirection, vec3 normal) {
    float DiffuseFactor = dot(normalize(normal), -lightDirection);

    if (DiffuseFactor > 0) {
        return DiffuseFactor;
    }
    else {
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

void main() {

	struct DirectionalLight {
        vec3 Color;
        vec3 AmbientIntensity;
        vec3 DiffuseIntensity;
        vec3 Direction;
    } Light0;
    
    Light0.Color = vec3(1.0, 1.0,1.0);
    Light0.AmbientIntensity = vec3(0.5, 0.5, 0.5);
    Light0.DiffuseIntensity = vec3(0.8, 0.8, 0.8);
    Light0.Direction = directLightDirection;

	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	
	gl_ClipDistance[0] = dot(worldPosition, plane);
	
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	pass_textureCoords = textureCoords;
	
	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	
	//Dynamic Light
	for(int i=0;i<1;i++) {
		toLightVector[i]= lightPosition[i] - worldPosition.xyz;
	}
	
	vec3 AmbientColor = Light0.AmbientIntensity * Light0.Color;
    vec3 DiffuseColor = Light0.Color * Light0.DiffuseIntensity * CalcDirectionalLightFactor(Light0.Direction, normal);

    lightIntensity = DiffuseColor + AmbientColor;
	
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance*density),gradient));
	visibility = clamp(visibility,0.0,1.1);
	
}