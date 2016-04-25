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

in vec2 pass_textureCoords;
in vec3 pass_normal;
in vec3 pass_position;
in vec4 pass_Data;
in vec4 ShadowCoord;
in vec3 posInTangent;
in mat3 TBN;

out vec4 [5] out_Color;

uniform vec3 cameraPos;
uniform sampler2D texture0;
uniform sampler2DShadow depth;
uniform sampler2D normalMap;
uniform sampler2D heightMap;
uniform sampler2D specularMap;

uniform int useShadows;
uniform int useParallax;
uniform float rainFactor;

const float xPixelOffset = 0.0002;
const float yPixelOffset = 0.0002;

#ifdef NVIDIA
const float heightScale = 0.02f;

vec2 ParallaxMapping(vec2 texCoords, vec3 viewDir) { 

	const float minLayers = 8;
	const float maxLayers = 32;
	float numLayers = mix(maxLayers, minLayers, abs(dot(vec3(0.0, 0.0, 1.0), viewDir)));  
    float layerDepth = 1.0 / numLayers;
    float currentLayerDepth = 0.0;
    vec2 P = viewDir.xy * heightScale; 
    float deltaTexCoords = P / numLayers;
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
#endif

float lookup( vec2 offSet){
	return texture(depth, ShadowCoord.xyz + vec3(offSet.x * xPixelOffset, offSet.y * yPixelOffset, 0.0) );
}

void main(void) {
	vec4 data = pass_Data;
	float bright = data.y;
	int id = int(data.x + 0.5f);

	vec3 eyeDir = normalize(TBN * (cameraPos - pass_position));
	vec2 texcoords = pass_textureCoords;
	#ifdef NVIDIA
	if(useParallax == 1)
		texcoords = ParallaxMapping(texcoords, eyeDir);
	#endif
	
	float shadow = 0;
	if(useShadows == 1){
		float x,y;
		for (y = -1.5 ; y <=1.5 ; y+=1.0)
			for (x = -1.5 ; x <=1.5 ; x+=1.0)
				shadow += -lookup(vec2(x,y)) + 1;
		shadow /= 16.0 ;
	}

    vec4 textureColour = texture(texture0, texcoords);
    if(textureColour.a < 0.5)
    	discard;
    	
	vec3 normal = texture(normalMap, texcoords).rgb;
	normal = normalize(normal * 2.0 - 1.0);   
	normal = normalize(TBN * normal);
	
	float reflectionFactor = rainFactor;
	reflectionFactor += texture(specularMap, texcoords).r;
	reflectionFactor = clamp(reflectionFactor,0,1);
    	
	out_Color[0] = textureColour;
	out_Color[1] = vec4(pass_position.xyz,0);
	out_Color[2] = vec4(normal,0.0);
	out_Color[3] = vec4(reflectionFactor,1.0,0.0,shadow);
	out_Color[4] = vec4(0.0, 0.0, 0.0, bright);
		
		
}