//
// This file is part of Voxel
// 
// Copyright (C) 2016 Lux Vacuos
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
// 
//

#version 330 core

in vec2 textureCoords;
in vec4 posPos;

out vec4 out_Color;

uniform int camUnderWater;
uniform float camUnderWaterOffset;
uniform float time;
uniform vec2 resolution;
uniform vec3 cameraPosition;
uniform vec3 lightPosition;
uniform vec3 skyColor;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform sampler2D gDiffuse;
uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gData0;
uniform sampler2D gData1;
uniform sampler2D composite0;
uniform sampler2D composite1;
uniform sampler2DShadow gDepth;

uniform int useFXAA;
uniform int useDOF;
uniform int useMotionBlur;
uniform int useVolumetricLight;

const int NUM_SAMPLES = 120;
const float density = 0.01;
const float gradient = 2.0;
const float transitionDistance = 5;
const float shadowDistance = 60;

void main(void){
	vec2 texcoord = textureCoords;
	vec4 image = texture(gDiffuse, texcoord);
	vec4 data = texture(gData0, texcoord);
	vec4 data1 = texture(gData1, texcoord);
    vec4 position = texture(gPosition,texcoord);
    vec4 normal = texture(gNormal, texcoord);
    float depth = texture(gDepth, vec3(texcoord.xy, 0.0), 0);
    vec3 lightDir = lightPosition;
    lightDir = normalize(lightDir);
    vec3 eyeDir = normalize(cameraPosition-position.xyz);
	float distance = length(cameraPosition-position.xyz);
    if(data.b != 1) {
    	normal = normalize(normal);
    	float shadowDist = distance - (shadowDistance - transitionDistance);
		shadowDist = shadowDist / transitionDistance;
		float fadeOut = clamp(1.0-shadowDist, 0.0, 1.0);
    	float b = max(dot(normal.xyz, lightDir), 0) - (data.a * fadeOut);
    	if(b <= data1.a)
    		b = data1.a;
    	b = clamp(b,0.02,1.0);
    	image = b * image;
    	if(data.r> 0.0)
    		if(data.a <= 0.5){
    			vec3 vHalfVector = normalize(lightDir.xyz+eyeDir);
	   			image += pow(max(dot(normal.xyz,vHalfVector),0.0), 100) * data.r * 1.5;
	   		}
    }
    image += texture(composite0, texcoord);
	out_Color = image;
	
}