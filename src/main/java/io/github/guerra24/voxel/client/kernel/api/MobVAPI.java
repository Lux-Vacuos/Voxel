package io.github.guerra24.voxel.client.kernel.api;

import io.github.guerra24.voxel.client.kernel.world.IMobManagerController;
import io.github.guerra24.voxel.client.kernel.world.entities.IEntity;

public class MobVAPI {

	private IMobManagerController mobController;

	public MobVAPI() {
	}

	public void registerEntity(IEntity entity) {
		mobController.registerMob(entity);
	}

	public void setMobController(IMobManagerController mobController) {
		this.mobController = mobController;
	}

	public IMobManagerController getMobController() {
		return mobController;
	}

}
