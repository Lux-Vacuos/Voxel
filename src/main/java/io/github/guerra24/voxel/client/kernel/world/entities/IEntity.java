package io.github.guerra24.voxel.client.kernel.world.entities;

import io.github.guerra24.voxel.client.kernel.resources.GameControllers;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.world.DimensionalWorld;

public interface IEntity {
	public void update(float delta, GameControllers gm, GuiResources gi, DimensionalWorld world);
	public Entity getEntity();
}
