#version 330 core
//Shaders uses OpenGL 3.3


in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[1];
in float visibility;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 skyColour;
uniform vec3 lightColour[1];
uniform vec3 attenuations[1];

void main(void) {

	vec3 unitNormal = normalize(surfaceNormal);
	
	vec3 totalDiffuse = vec3(0.0);
	
	for(int i=0;i<1;i++) {// DYNAMIC LIGHTS DISSABLED
		//float distance = length(toLightVector[i]);
		//float attFactor = attenuations[i].x + (attenuations[i].y * distance) + (attenuations[i].z * distance * distance);
		//vec3 unitLightVector = normalize(toLightVector[i]);
		//float nDotl = dot(unitNormal,unitLightVector);
		//float brightness = max(nDotl, 0.0);
		//totalDiffuse = totalDiffuse + (brightness * lightColour[i])/attFactor;
		
		float brightness = 1;
		totalDiffuse = totalDiffuse + (brightness * lightColour[i]);
	}
	totalDiffuse = max(totalDiffuse, 0.2);
	
	vec4 textureColour = texture(textureSampler, pass_textureCoords);
	if(textureColour.a<0.5) {
		discard;
	}
	out_Color = vec4(totalDiffuse, 1.0) * textureColour;
	out_Color = mix(vec4(skyColour,1.0),out_Color,visibility);
}