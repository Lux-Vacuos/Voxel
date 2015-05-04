#version 330 core
//Shaders uses OpenGL 3.3


in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;

void main(void){

	vec4 textureColour = texture(guiTexture, textureCoords);
	if(textureColour.a<0.5) {
		discard;
	}
	
	out_Color = texture(guiTexture,textureCoords);
}