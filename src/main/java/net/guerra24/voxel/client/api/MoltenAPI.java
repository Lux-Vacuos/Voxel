package net.guerra24.voxel.client.api;

import net.guerra24.voxel.client.core.GameSettings;
import net.guerra24.voxel.client.world.MobManager;
import net.guerra24.voxel.client.world.entities.IEntity;

public class MoltenAPI {

	private MobManager mobManager;
	private GameSettings gameSettings;

	public MoltenAPI(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}

	public void registetMob(IEntity mob) {
		mobManager.registerMob(mob);
	}

	public void registerSaveData(String key, String value) {
		gameSettings.registerValue(key, value);
	}
	
	public String getSaveData(String key){
		return gameSettings.getValue(key);
	}

	public void setMobManager(MobManager mobManager) {
		this.mobManager = mobManager;
	}

}
