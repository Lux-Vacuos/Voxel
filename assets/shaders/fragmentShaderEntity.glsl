#version 330 core
//Shaders uses OpenGL 3.3


in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 skyColour;
uniform vec3 lightColour;

void main(void) {

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDotl = dot(unitNormal,unitLightVector);
	float brightness = max(nDotl, 0.0);
	vec3 diffuse = brightness * lightColour;
		
	vec4 textureColour = texture(textureSampler, pass_textureCoords);
	if(textureColour.a<0.5) {
		discard;
	}
	out_Color = vec4(diffuse, 1.0) * textureColour;
	out_Color = mix(vec4(skyColour,1.0),out_Color,visibility);
}