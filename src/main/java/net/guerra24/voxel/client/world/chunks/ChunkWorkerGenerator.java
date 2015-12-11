package net.guerra24.voxel.client.world.chunks;

import net.guerra24.voxel.client.world.IWorld;

public class ChunkWorkerGenerator implements Runnable {
	private final IWorld world;
	private Chunk chunk;

	public ChunkWorkerGenerator(final IWorld world, Chunk chunk) {
		this.world = world;
		this.chunk = chunk;
	}

	@Override
	public void run() {
		chunk.createBasicTerrain(world);
		chunk.created = true;
		chunk.creating = false;
	}
}