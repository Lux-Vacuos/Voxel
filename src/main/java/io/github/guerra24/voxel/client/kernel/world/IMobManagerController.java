package io.github.guerra24.voxel.client.kernel.world;

import java.util.List;

import io.github.guerra24.voxel.client.kernel.api.VAPI;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.world.entities.IEntity;
import io.github.guerra24.voxel.client.kernel.world.entities.Player;

public interface IMobManagerController {
	public void init(GameResources gm);

	public void update(float delta, GameResources gm, GuiResources gi, DimensionalWorld world, VAPI api);

	public void dispose();

	public void registerMob(IEntity mob);

	public List<IEntity> getMobs();

	public Player getPlayer();
}
