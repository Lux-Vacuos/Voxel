package net.guerra24.voxel.client.engine.resources.models;

import net.guerra24.voxel.client.engine.render.textures.ModelTexture;

public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture texture;

	public TexturedModel(RawModel model, ModelTexture texture) {
		this.rawModel = model;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}
