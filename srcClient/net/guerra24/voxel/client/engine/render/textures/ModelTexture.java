package net.guerra24.voxel.client.engine.render.textures;

public class ModelTexture {

	private int textureID;

	private float shineDamper = 1;
	private float reflectivity = 0;

	public ModelTexture(int id) {
		this.textureID = id;
	}

	public int getID() {
		return this.textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

}
