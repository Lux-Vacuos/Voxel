package net.guerra24.voxel.client.engine.world;

import net.guerra24.voxel.client.engine.util.Logger;

public class World {

	public static final int WORLD_SIZE = 2;

	public static void init() {
		for (int x = 0; x < WORLD_SIZE; x++) {
			for (int z = 0; z < WORLD_SIZE; z++) {
				Chunk.create(x + 16, z + 16);
			}
		}
		Logger.log("World Generation completed with size: " + WORLD_SIZE);
	}
}
