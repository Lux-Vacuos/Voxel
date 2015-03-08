package net.voxel.core.util;

import net.logger.Logger;

public class SpriteSheets {
	private Texture texture;
	private String path;
	private int size;
	
	public static SpriteSheets blocks = new SpriteSheets("spritesheets/blocks/BlocksText.png", 16);
	
	public SpriteSheets(String path, int size) {
		Logger.log("Loading Texture");
		this.path = path;
		this.size = 1 / size;
		load();
	}

	private void load() {
		texture = Texture.loadTexture(path);
	}

	public void bind() {
		texture.bind();
	}

	public void unbind() {
		texture.unbind();
	}

	public void delete() {
		texture.delete();
	}

	public int uniformSize() {
		return size;
	}
}
