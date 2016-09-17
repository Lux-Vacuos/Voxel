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

out vec4 out_Color;

uniform vec2 resolution;
uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D composite0;
uniform sampler2D gDepth;

const float distanceThreshold = 5;
const int sample_count = 16;
const vec2 poisson16[] = vec2[](
                                vec2( -0.94201624,  -0.39906216 ),
                                vec2(  0.94558609,  -0.76890725 ),
                                vec2( -0.094184101, -0.92938870 ),
                                vec2(  0.34495938,   0.29387760 ),
                                vec2( -0.91588581,   0.45771432 ),
                                vec2( -0.81544232,  -0.87912464 ),
                                vec2( -0.38277543,   0.27676845 ),
                                vec2(  0.97484398,   0.75648379 ),
                                vec2(  0.44323325,  -0.97511554 ),
                                vec2(  0.53742981,  -0.47373420 ),
                                vec2( -0.26496911,  -0.41893023 ),
                                vec2(  0.79197514,   0.19090188 ),
                                vec2( -0.24188840,   0.99706507 ),
                                vec2( -0.81409955,   0.91437590 ),
                                vec2(  0.19984126,   0.78641367 ),
                                vec2(  0.14383161,  -0.14100790 )
                               );

void main(void){
	vec2 texcoord = textureCoords;
    vec4 position = texture(gPosition,texcoord);
    vec4 normal = texture(gNormal, texcoord);
    vec4 depth = texture(gDepth, texcoord);
    vec4 image = texture(composite0, texcoord);
    vec2 filterRadius = vec2(10 / resolution.x, 10 / resolution.y);
    
    float ambientOcclusion = 0;
    for (int i = 0; i < sample_count; ++i) {
        vec2 sampleTexCoord = texcoord + (poisson16[i] * (filterRadius));
        float sampleDepth = texture(gDepth, sampleTexCoord).r;
        vec3 samplePos = texture(gPosition, sampleTexCoord).rgb;
        vec3 sampleDir = normalize(samplePos - position.rgb);
        float NdotS = max(dot(normal.rgb, sampleDir), 0);
        float VPdistSP = distance(position.rgb, samplePos);
        float a = 1.0 - smoothstep(distanceThreshold, distanceThreshold * 2, VPdistSP);
        float b = NdotS;
        ambientOcclusion += (a * b) * 1.3;
    }
	out_Color = mix(image, vec4(0.0), ambientOcclusion / sample_count);
	
}