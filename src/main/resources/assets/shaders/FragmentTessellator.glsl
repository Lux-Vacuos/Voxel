//
// The MIT License (MIT)
//
// Copyright (c) 2015-2016 Guerra24
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
in mat3 TBN;
in vec3 pass_normal;
in vec3 pass_position;
in vec4 pass_Data;
in vec4 ShadowCoord;
in vec3 posInTangent;

out vec4 [5] out_Color;

uniform sampler2D texture0;
uniform sampler2DShadow depth;
uniform sampler2D normalMap;
uniform sampler2D heightMap;

uniform int useShadows;
uniform int useParallax;

vec2 poissonDisk[16] = vec2[]( 
   vec2( -0.94201624, -0.39906216 ), 
   vec2( 0.94558609, -0.76890725 ), 
   vec2( -0.094184101, -0.92938870 ), 
   vec2( 0.34495938, 0.29387760 ), 
   vec2( -0.91588581, 0.45771432 ), 
   vec2( -0.81544232, -0.87912464 ), 
   vec2( -0.38277543, 0.27676845 ), 
   vec2( 0.97484398, 0.75648379 ), 
   vec2( 0.44323325, -0.97511554 ), 
   vec2( 0.53742981, -0.47373420 ), 
   vec2( -0.26496911, -0.41893023 ), 
   vec2( 0.79197514, 0.19090188 ), 
   vec2( -0.24188840, 0.99706507 ), 
   vec2( -0.81409955, 0.91437590 ), 
   vec2( 0.19984126, 0.78641367 ), 
   vec2( 0.14383161, -0.14100790 ) 
);


#ifdef NVIDIA
const float heightScale = 0.01f;

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

void main(void) {
	vec4 data = pass_Data;
	float bright = data.y;
	int id = int(data.x + 0.5f);

	vec3 eyeDir = normalize(posInTangent);
	vec2 texcoords = pass_textureCoords;
	#ifdef NVIDIA
	if(useParallax == 1)
		texcoords = ParallaxMapping(texcoords, eyeDir);
	#endif
	
	float shadow = 0;
	if(useShadows == 1){
		for (int i=0;i<16;i++){
	    	if (texture(depth, vec3(ShadowCoord.xy + poissonDisk[i]/1600.0 , 0.0))  < ShadowCoord.z){
   		 		shadow += 0.08;
   	 		}
   	 		if(ShadowCoord.z > 1.0){
       			shadow = 0.0;
       			break;
       		}
		}
	}

    vec4 textureColour = texture(texture0, texcoords);
    if(textureColour.a < 0.5)
    	discard;
    	
	vec3 normal = texture(normalMap, texcoords).rgb;
	normal = normalize(normal * 2.0 - 1.0);   
	normal = normalize(TBN * normal);
    	
	out_Color[0] = textureColour;
	out_Color[1] = vec4(pass_position.xyz,0);
	out_Color[2] = vec4(normal,0.0);
	out_Color[3] = vec4(0.0,1.0,0.0,shadow);
	out_Color[4] = vec4(0.0, 0.0, 0.0, bright);
	if(id == 8 || id == 13)
		out_Color[3].r = 1.0;
}