package io.github.guerra24.voxel.client.kernel.world.chunks;

import io.github.guerra24.voxel.client.kernel.world.DimensionalWorld;

public class ChunkWorkerMesh implements Runnable {
	private final DimensionalWorld world;
	private Chunk chunk;

	public ChunkWorkerMesh(final DimensionalWorld world, Chunk chunk) {
		this.world = world;
		this.chunk = chunk;
	}

	@Override
	public void run() {
		synchronized (chunk) {
			chunk.update1(world);
			chunk.update2(world);
			chunk.update3(world);
			chunk.update4(world);
			chunk.rebuilding = false;
			chunk.needsRebuild = false;
		}
	}
}
