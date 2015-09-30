package io.github.guerra24.voxel.client.kernel.particle;

import io.github.guerra24.voxel.client.kernel.resources.GameControllers;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.resources.Loader;
import io.github.guerra24.voxel.client.kernel.world.DimensionalWorld;

public interface IParticleController {
	public void init(Loader loader);

	public void render(GameControllers gm);
	
	public void update(float delta, GameControllers gm, GuiResources gi, DimensionalWorld world);

	public void dispose();
}
