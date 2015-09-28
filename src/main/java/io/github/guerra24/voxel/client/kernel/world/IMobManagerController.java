package io.github.guerra24.voxel.client.kernel.world;

import java.util.List;

import io.github.guerra24.voxel.client.kernel.resources.GameControllers;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.world.entities.IEntity;
import io.github.guerra24.voxel.client.kernel.world.entities.Player;

public interface IMobManagerController {
	public void init(GameControllers gm);

	public void update(float delta, GameControllers gm, GuiResources gi, World world);

	public void dispose();

	public void registerMob(IEntity mob);

	public List<IEntity> getMobs();

	public Player getPlayer();
}
