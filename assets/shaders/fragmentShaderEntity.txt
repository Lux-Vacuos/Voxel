#version 330 core
//Shaders uses OpenGL 3.3


in vec2 pass_textureCoords;
in float visibility;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 skyColour;

void main(void) {

	vec4 textureColour = texture(textureSampler, pass_textureCoords);
	if(textureColour.a<0.5) {
		discard;
	}
	out_Color = textureColour;
	out_Color = mix(vec4(skyColour,1.0),out_Color,visibility);
}