#version 330 core
//Shaders uses OpenGL 3.3


in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;

void main(void){

	out_Color = texture(guiTexture,textureCoords);

}