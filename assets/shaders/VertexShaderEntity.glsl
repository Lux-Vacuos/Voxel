#version 330 core
//Shaders uses OpenGL 3.3


in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
//out vec3 toLightVector[1];
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
//uniform vec3 lightPosition[1];
uniform vec4 plane;

const float density = 0.007;
const float gradient = 10.0;


void main() {

	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	
	gl_ClipDistance[0] = dot(worldPosition, plane);
	
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	pass_textureCoords = textureCoords;
	
	surfaceNormal = (transformationMatrix * vec4(normal, 1)).xyz;
	//for(int i=0;i<1;i++) {
	//	toLightVector[i]= lightPosition[i] - worldPosition.xyz;
	//}
	
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance*density),gradient));
	visibility = clamp(visibility,0.0,1.1);
	
}