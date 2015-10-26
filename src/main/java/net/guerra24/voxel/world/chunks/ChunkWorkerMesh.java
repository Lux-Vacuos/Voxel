package net.guerra24.voxel.world.chunks;

import net.guerra24.voxel.world.DimensionalWorld;

public class ChunkWorkerMesh implements Runnable {
	private final DimensionalWorld world;
	private Chunk chunk;

	public ChunkWorkerMesh(final DimensionalWorld world, Chunk chunk) {
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
