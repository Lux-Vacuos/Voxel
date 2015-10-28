package net.guerra24.voxel.world;

import java.util.HashMap;
import java.util.Map;

import net.guerra24.voxel.util.Logger;

public class WorldsHandler {

	private Map<String, IWorld> worlds;
	private IWorld activeWorld;

	public WorldsHandler() {
		init();
	}

	public void init() {
		worlds = new HashMap<String, IWorld>();
	}

	public void registerWorld(String name, IWorld world) {
		worlds.put(name, world);
		Logger.log("The World type " + name + " has been successfully registered");
	}

	public void setActiveWorld(String name) {
		activeWorld = worlds.get(name);
	}

	public IWorld getActiveWorld() {
		return activeWorld;
	}

	public void dispose() {
		worlds.clear();
	}

}
