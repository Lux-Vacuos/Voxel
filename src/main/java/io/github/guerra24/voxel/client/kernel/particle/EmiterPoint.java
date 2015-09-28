package io.github.guerra24.voxel.client.kernel.particle;

import io.github.guerra24.voxel.client.kernel.resources.Loader;
import io.github.guerra24.voxel.client.kernel.resources.models.ModelTexture;
import io.github.guerra24.voxel.client.kernel.resources.models.RawModel;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;

public class EmiterPoint {

	private Vector3f origin;
	private TexturedModel model;
	private float spawnRationPerTick = 0;
	private int max;

	private float x, y, z;

	private RawModel rModel;
	private ModelTexture texture;

	public EmiterPoint(Vector3f origin, Loader loader, float x, float y, float z) {
		this.origin = origin;
		this.x = x;
		this.y = y;
		this.z = z;
		rModel = loader.getObjLoader().loadObjModel("particle", loader);
		texture = new ModelTexture(loader.loadTextureBlocks("particleTest"));
		model = new TexturedModel(rModel, texture);
		max = 40;
	}

	public void spawnParticles(ParticleController controller) {
		origin = new Vector3f(x, y, z);
		spawnRationPerTick++;
		max = 200;
		if (spawnRationPerTick >= max) {
			controller.getParticles().add(new Particle(model, origin, 0, 0, 0, 600, 0.4f));
			spawnRationPerTick = 0;
		}
	}
}
