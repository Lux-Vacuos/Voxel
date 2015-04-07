package net.guerra24.voxel.client.engine.render.textures;

public class ModelTexture {

	private int textureID;

	private boolean hasTransparency = false;

	public ModelTexture(int id) {
		this.textureID = id;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public int getID() {
		return this.textureID;
	}

}
