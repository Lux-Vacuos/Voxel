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

uniform vec3 cameraPosition;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 pointLightsPos[256];

uniform sampler2D gDiffuse;
uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gMask;
uniform sampler2D gPBR; // R = roughness, B = metallic
uniform sampler2D composite0;


float beckmannDistribution(float x, float roughness) {
	float NdotH = max(x, 0.0001);
  	float cos2Alpha = NdotH * NdotH;
	float tan2Alpha = (cos2Alpha - 1.0) / cos2Alpha;
	float roughness2 = roughness * roughness;
	float denom = 3.141592653589793 * roughness2 * cos2Alpha * cos2Alpha;
	return exp(tan2Alpha / roughness2) / denom;
}

float beckmannSpecular(vec3 lightDirection, vec3 viewDirection, vec3 surfaceNormal, float roughness) {
	return beckmannDistribution(dot(surfaceNormal, normalize(lightDirection + viewDirection)), roughness);
}

void main(void){
	vec2 texcoord = textureCoords;
    vec4 composite = texture(composite0, texcoord);
	vec4 mask = texture(gMask, texcoord);
	if(mask.a != 1) {
		vec4 pbr = texture(gPBR, texcoord);
    	vec4 position = texture(gPosition,texcoord);
    	vec4 normal = texture(gNormal, texcoord);
		vec3 eyeDir = normalize(cameraPosition-position.xyz);
	    normal = normalize(normal);
    	for(int x = 0; x < 256; x++){
	    	if(pointLightsPos[x].y < 0)
	    		break;
	    	vec3 lightDir = pointLightsPos[x] - position.xyz;
    		lightDir = normalize(lightDir);
	    	float distance = length(lightDir);
	    	float attFactor = (distance*distance);
			composite.rgb *= 1.0 - pbr.g;
    		vec3 eyeDir = normalize(cameraPosition-position.xyz);
			float normalDotLight = max(dot(normal.xyz, lightDir), 0) * 1.5;
    		normalDotLight = max(normalDotLight, 0.0);
			normalDotLight /= attFactor;
    		composite += normalDotLight * composite;
    		if(normalDotLight > 0){
	    		vec3 vHalfVector = normalize(lightDir.xyz+eyeDir);
				float specular = beckmannSpecular(lightDir.xyz, eyeDir, normal.xyz, pbr.r);
				specular /= attFactor;
		   		composite += specular;
		   	}

		}
	}
	out_Color = composite;
}