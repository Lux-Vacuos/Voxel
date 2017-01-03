//
// This file is part of Voxel
// 
// Copyright (C) 2016-2017 Lux Vacuos
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

out vec4 out_Color;

uniform vec3 cameraPosition;
uniform vec3 lightPosition;
uniform sampler2D gDiffuse;
uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gPBR; // R = roughness, G = metallic, B = specular
uniform sampler2D gMask;
uniform sampler2D composite0;
uniform int shadowDrawDistance;

#define TRANSITION_DISTANCE 2.5

##include variable pi

##include function DistributionGGX

##include function GeometrySchlickGGX

##include function GeometrySmith

##include function fresnelSchlickRoughness

void main(void) {
	vec2 texcoord = textureCoords;
	vec4 mask = texture(gMask, texcoord);
	vec4 image = texture(gDiffuse, texcoord);
	if (mask.a != 1) {
		vec4 pbr = texture(gPBR, texcoord);
    	vec4 position = texture(gPosition, texcoord);
    	vec4 normal = texture(gNormal, texcoord);

		vec3 N = normalize(normal.rgb);
	    vec3 V = normalize(cameraPosition - position.rgb);

		float roughness = pbr.r;
		float metallic = pbr.g;

	    vec3 F0 = vec3(0.04);
	    F0 = mix(F0, image.rgb, metallic);
	    vec3 F = fresnelSchlickRoughness(max(dot(N, V), 0.0), F0, roughness);

	    vec3 kS = F;
	    vec3 kD = vec3(1.0) - kS;
	    kD *= 1.0 - metallic;	  
	
    	vec3 Lo = vec3(0.0);
       	vec3 L = normalize(lightPosition);
       	vec3 H = normalize(V + L);
        vec3 radiance = vec3(1.0);        
	
       	float NDF = DistributionGGX(N, H, roughness);        
       	float G = GeometrySmith(N, V, L, roughness);      
        
    	vec3 nominator = NDF * G * F;
       	float denominator = max(dot(V, N), 0.0) * max(dot(L, N), 0.0) + 0.001; 
        vec3 brdf = nominator / denominator;
	
       	float NdotL = max(dot(N, L) - position.w, 0.0) ;      
       	Lo += (kD * image.rgb / PI + brdf) * radiance * NdotL;

    	vec3 ambient = vec3(0.03) * image.rgb;
    	vec3 color = ambient + Lo;
		image.rgb = color;

	}
    image += texture(composite0, texcoord);
	out_Color = image;
	
}
