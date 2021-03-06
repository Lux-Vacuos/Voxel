//
// This file is part of Voxel
// 
// Copyright (C) 2016-2018 Lux Vacuos
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

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

in vec2 pass_textureCoordsV[];
in vec3 pass_positionV[];

out vec2 pass_textureCoords;
out vec3 pass_position;
out mat3 TBN;

vec3 calculateTriangleNormal(){
	vec3 tangent = pass_positionV[1].xyz - pass_positionV[0].xyz;
	vec3 bitangent = pass_positionV[2].xyz - pass_positionV[0].xyz;
	vec3 normal = cross(tangent, bitangent);	
	return normalize(normal);
}

void main() {
    
    vec3 normal = calculateTriangleNormal();
    vec3 tangent;

	vec3 c1 = cross(normal, vec3(0.0, 0.0, 1.0));
	vec3 c2 = cross(normal, vec3(0.0, 1.0, 0.0));

	if(length(c1) > length(c2) ) {
		tangent = c1;
	} else {
		tangent = c2;
	}

	vec3 T = normalize(tangent);
	vec3 N = normalize(normal);
	T = normalize(T - dot(T, N) * N);
	vec3 B = cross(N, T);
	TBN = mat3(T, B, N);

    pass_position = pass_positionV[0];
    pass_textureCoords = pass_textureCoordsV[0];
    gl_Position = gl_in[0].gl_Position;
    EmitVertex();

    pass_position = pass_positionV[1];
    pass_textureCoords = pass_textureCoordsV[1];
    gl_Position =  gl_in[1].gl_Position;
    EmitVertex();

    pass_position = pass_positionV[2];
    pass_textureCoords = pass_textureCoordsV[2];
    gl_Position =  gl_in[2].gl_Position;
    EmitVertex();

    EndPrimitive();
}