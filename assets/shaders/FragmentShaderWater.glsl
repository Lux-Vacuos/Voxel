#version 330 core

out vec4 out_Color;

in vec4 clipSpace;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;

void main(void) {

	vec2 ndc = (clipSpace.xy/clipSpace.w)/2.0 + 0.5;
	vec2 refractTexCoords = vec2(ndc.x, ndc.y);
	vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);
	vec4 refractionColour = texture(refractionTexture, refractTexCoords);
	vec4 reflectionColour = texture(reflectionTexture, reflectTexCoords);
	
	out_Color = mix(reflectionColour, refractionColour, 0.4);

}