package io.github.guerra24.voxel.client.kernel.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.github.guerra24.voxel.client.kernel.api.VAPI;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;

public class WorldHandler {

	private Map<Integer, DimensionalWorld> worlds;
	private int activeWorld;

	public WorldHandler() {
		worlds = new HashMap<Integer, DimensionalWorld>();
	}

	public void addWorld(int id, Random seed, VAPI api, GameResources gm) {
		if (!worldExist(id)) {
			DimensionalWorld world = new DimensionalWorld();
			world.startWorld("World-" + id, seed, id, api, gm);
			worlds.put(world.getWorldID(), world);
		}
		activeWorld = id;
	}

	public boolean worldExist(int id) {
		if (worlds.get(id) != null)
			return true;
		else
			return false;
	}

	public void removeWorld(int world, GameResources gm) {
		worlds.get(world).clearChunkDimension(gm);
	}

	public DimensionalWorld getWorld(int world) {
		return worlds.get(world);
	}

	public int getActiveWorld() {
		return activeWorld;
	}
}
