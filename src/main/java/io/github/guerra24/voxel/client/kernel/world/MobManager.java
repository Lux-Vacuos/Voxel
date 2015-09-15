package io.github.guerra24.voxel.client.kernel.world;

import java.util.ArrayList;
import java.util.List;

import io.github.guerra24.voxel.client.kernel.core.Kernel;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.block.BlocksResources;
import io.github.guerra24.voxel.client.kernel.world.entities.IEntity;
import io.github.guerra24.voxel.client.kernel.world.entities.Player;

public class MobManager {
	private List<IEntity> mobs = new ArrayList<IEntity>();
	private Player player;

	public MobManager() {
		init();
	}

	public void init() {
		player = new Player(BlocksResources.cubeGlassUP, new Vector3f(0, 80, -4), 0, 0, 0, 1);
		mobs.add(player);
		mobs.add(Kernel.gameResources.camera);
	}

	public void update(float delta) {
		for (int x = 0; x < mobs.size(); x++) {
			mobs.get(x).update(delta);
		}
	}

	public void dispose() {
		mobs.clear();
	}

	public List<IEntity> getMobs() {
		return mobs;
	}

	public Player getPlayer() {
		return player;
	}
}
