package net.guerra24.voxel.client.particle;

public class ParticleTexture {

	private int textureID;
	private int numbreOfRows;

	public ParticleTexture(int textureID, int numbreOfRows) {
		this.textureID = textureID;
		this.numbreOfRows = numbreOfRows;
	}

	public int getTextureID() {
		return textureID;
	}

	public int getNumbreOfRows() {
		return numbreOfRows;
	}

}
