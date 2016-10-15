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

in vec2 pass_textureCoords;
in vec3 pass_normal;
in vec3 pass_position;
in vec4 pass_Data;
in vec4 ShadowCoord;
in vec3 posInTangent;
in mat3 TBN;

out vec4 [5] out_Color;

uniform vec3 cameraPos;
uniform int transparent;
uniform sampler2D texture0;
uniform sampler2DShadow depth;
uniform sampler2D normalMap;
uniform sampler2D heightMap;
uniform sampler2D specularMap;
uniform sampler2D shadowTex;

uniform int useShadows;
uniform int useParallax;
uniform float rainFactor;

const float xPixelOffset = 0.0002;
const float yPixelOffset = 0.0002;

const float heightScale = 0.02f;

vec2 ParallaxMapping(vec2 texCoords, vec3 viewDir) { 

	const float minLayers = 16;
	const float maxLayers = 32;
	float numLayers = mix(maxLayers, minLayers, abs(dot(vec3(0.0, 0.0, 1.0), viewDir)));  
    float layerDepth = 1.0 / numLayers;
    float currentLayerDepth = 0.0;
    vec2 P = viewDir.xy * heightScale; 
    vec2 deltaTexCoords = P / numLayers;
    vec2 currentTexCoords = texCoords;
	float currentDepthMapValue = texture(heightMap, currentTexCoords).r;
  
	while(currentLayerDepth < currentDepthMapValue) {
    	currentTexCoords -= deltaTexCoords;
    	currentDepthMapValue = texture(heightMap, currentTexCoords).r;  
   		currentLayerDepth += layerDepth;  
	}
	vec2 prevTexCoords = currentTexCoords + deltaTexCoords;
	float afterDepth  = currentDepthMapValue - currentLayerDepth;
	float beforeDepth = texture(heightMap, prevTexCoords).r - currentLayerDepth + layerDepth;
	float weight = afterDepth / (afterDepth - beforeDepth);
	vec2 finalTexCoords = prevTexCoords * weight + currentTexCoords * (1.0 - weight);
	return finalTexCoords;
} 

float lookup( vec2 offSet){
	return texture(depth, ShadowCoord.xyz + vec3(offSet.x * xPixelOffset, offSet.y * yPixelOffset, 0.0) );
}

void main(void) {
	vec4 data = pass_Data;
	float bright = data.y;
	int id = int(data.x + 0.5f);

	vec3 eyeDir = normalize(TBN * (cameraPos - pass_position));
	vec2 texcoords = pass_textureCoords;
	if(useParallax == 1)
		texcoords = ParallaxMapping(texcoords, eyeDir);
	
	float shadow = 0;
	if(useShadows == 1){
		float x,y;
		for (y = -1.5 ; y <=1.5 ; y+=1.0)
			for (x = -1.5 ; x <=1.5 ; x+=1.0)
				shadow += -lookup(vec2(x,y)) + 1;
		shadow /= 16.0 ;
		shadow *= texture(shadowTex, ShadowCoord.xy).r;
	}

    vec4 textureColour = texture(texture0, texcoords);
    if(textureColour.a == 0)
    	discard;
    
    if(transparent == 1 && textureColour.a == 1)
    	discard;
    else if(transparent == 0 && textureColour.a < 1)
    	discard;
    	
	vec3 normal = texture(normalMap, texcoords).rgb;
	normal = normalize(normal * 2.0 - 1.0);   
	normal = normalize(TBN * normal);
	
	float reflectionFactor = rainFactor;
	reflectionFactor += texture(specularMap, texcoords).r;
	reflectionFactor = clamp(reflectionFactor,0,1);
	
	if(transparent == 1){
		shadow = clamp(shadow,0.015,1.0);
    	textureColour.rgb *= (1 - shadow);
	}
    	
	out_Color[0] = textureColour;
	out_Color[1] = vec4(pass_position.xyz,0);
	out_Color[2] = vec4(normal,0.0);
	out_Color[3] = vec4(reflectionFactor,1.0,0.0,shadow);
	out_Color[4] = vec4(0.0, 0.0, 0.0, bright);
		
		
}