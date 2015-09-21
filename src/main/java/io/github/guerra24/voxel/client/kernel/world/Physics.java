package io.github.guerra24.voxel.client.kernel.world;

import io.github.guerra24.voxel.client.kernel.resources.GameResources;

public class Physics {
	private DefaultMobManager mobManager;

	public Physics(GameResources gm) {
		mobManager = new VoxelMobManager(gm);
	}

	public DefaultMobManager getMobManager() {
		return mobManager;
	}

	public void setMobManager(DefaultMobManager mobManager) {
		this.mobManager = mobManager;
	}
}
