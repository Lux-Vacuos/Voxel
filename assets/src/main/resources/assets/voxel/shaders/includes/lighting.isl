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

##function DistributionGGX
float DistributionGGX(vec3 N, vec3 H, float roughness) {
    float a = roughness*roughness;
    float a2 = a*a;
    float NdotH = max(dot(N, H), 0.0);
    float NdotH2 = NdotH*NdotH;
	
    float nom = a2;
    float denom = (NdotH2 * (a2 - 1.0) + 1.0);
    denom = PI * denom * denom;
	
    return nom / denom;
}
##end

##function GeometrySchlickGGX
float GeometrySchlickGGX(float NdotV, float roughness) {
    float r = (roughness + 1.0);
    float k = (r*r) / 8.0;

    float nom = NdotV;
    float denom = NdotV * (1.0 - k) + k;
	
    return nom / denom;
}
##end

##function GeometrySmith
float GeometrySmith(vec3 N, vec3 V, vec3 L, float roughness) {
    float NdotV = max(dot(N, V), 0.0);
    float NdotL = max(dot(N, L), 0.0);
    float ggx2 = GeometrySchlickGGX(NdotV, roughness);
    float ggx1 = GeometrySchlickGGX(NdotL, roughness);
	
    return ggx1 * ggx2;
}
##end

##function fresnelSchlickRoughness
vec3 fresnelSchlickRoughness(float cosTheta, vec3 F0, float roughness) {
    return F0 + (max(vec3(1.0 - roughness), F0) - F0) * pow(1.0 - cosTheta, 5.0);
}
##end

##function computeAmbientOcclusion
float computeAmbientOcclusion(vec3 position, vec3 normal) {
	if(useAmbientOcclusion == 1) {
    	float ambientOcclusion = 0;
		vec2 filterRadius = vec2(10 / resolution.x, 10 / resolution.y);
    	for (int i = 0; i < sample_count; ++i) {
	    	vec2 sampleTexCoord = textureCoords + (poisson16[i] * (filterRadius));
       		float sampleDepth = texture(gDepth, sampleTexCoord).r;
       		vec3 samplePos = texture(gPosition, sampleTexCoord).rgb;
       		vec3 sampleDir = normalize(samplePos - position);
       		float NdotS = max(dot(normal, sampleDir), 0);
       		float VPdistSP = distance(position, samplePos);
       		float a = 1.0 - smoothstep(distanceThreshold, distanceThreshold * 2, VPdistSP);
       		float b = NdotS;
	       	ambientOcclusion += (a * b) * 1.3;
    	}
	return -(ambientOcclusion / sample_count) + 1;
	} else
		return 1;
}
##end