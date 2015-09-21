package io.github.guerra24.voxel.client.kernel.world;

import java.util.ArrayList;
import java.util.List;

import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.block.BlocksResources;
import io.github.guerra24.voxel.client.kernel.world.entities.IEntity;
import io.github.guerra24.voxel.client.kernel.world.entities.Player;

public class DefaultMobManager implements IMobManagerController {
	private List<IEntity> mobs = new ArrayList<IEntity>();
	private Player player;

	public DefaultMobManager(GameResources gm) {
		init(gm);
	}

	@Override
	public void init(GameResources gm) {
		player = new Player(BlocksResources.cubeGlassUP, new Vector3f(0, 80, -4), 0, 0, 0, 1);
		mobs.add(player);
		mobs.add(gm.getCamera());
	}

	@Override
	public void update(float delta, GameResources gm, GuiResources gi) {
		for (int x = 0; x < mobs.size(); x++) {
			mobs.get(x).update(delta, gm, gi);
		}
	}

	@Override
	public void dispose() {
		mobs.clear();
	}

	@Override
	public List<IEntity> getMobs() {
		return mobs;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

}
