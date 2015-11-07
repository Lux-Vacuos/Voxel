package net.guerra24.voxel.client.world.chunks;

import net.guerra24.voxel.client.world.IWorld;

public class ChunkWorkerMesh implements Runnable {
	private final IWorld world;	
	private Chunk chunk;

	public ChunkWorkerMesh(final IWorld world, Chunk chunk) {
		this.world = world;
		this.chunk = chunk;
	}

	@Override
	public void run() {
		chunk.update(world);
		chunk.needsRebuild = false;
		chunk.updated = true;
		chunk.updating = false;
	}
}
