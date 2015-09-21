package io.github.guerra24.voxel.client.kernel.world.entities;

import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;

public interface IEntity {
	public void update(float delta, GameResources gm, GuiResources gi);
	public Entity getEntity();
}
