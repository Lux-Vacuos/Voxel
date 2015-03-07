package net.voxel.core.util;

public class Font {
	
	private Texture fontTexture;
	
	public Texture loadFont(String location) {
		fontTexture = Texture.loadTexture(location);
		return fontTexture;
	}
	public void bind() {
		fontTexture.bind();
	}
}
