package io.github.guerra24.voxel.client.kernel.world.entities;

import io.github.guerra24.voxel.client.kernel.api.VAPI;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.world.DimensionalWorld;

public interface IEntity {
	public void update(float delta, GameResources gm, GuiResources gi, DimensionalWorld world, VAPI api);

	public Entity getEntity();
}
