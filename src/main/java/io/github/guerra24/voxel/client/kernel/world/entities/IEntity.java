package io.github.guerra24.voxel.client.kernel.world.entities;

import io.github.guerra24.voxel.client.kernel.resources.GameControllers;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.world.World;

public interface IEntity {
	public void update(float delta, GameControllers gm, GuiResources gi, World world);
	public Entity getEntity();
}
