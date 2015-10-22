package io.github.guerra24.voxel.client.kernel.world.chunks;

import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.world.DimensionalWorld;

public class ChunkWorkerDestroy implements Runnable {
	private final DimensionalWorld world;
	private final int xx, zz;
	private final GameResources gm;

	public ChunkWorkerDestroy(final DimensionalWorld world, final GameResources gm, final int xx, final int zz) {
		this.world = world;
		this.gm = gm;
		this.xx = xx;
		this.zz = zz;
	}

	@Override
	public void run() {
		if (world.hasChunk(world.getChunkDimension(), xx, zz)) {
			world.saveChunk(world.getChunkDimension(), xx, zz, gm);
			world.removeChunk(world.getChunk(world.getChunkDimension(), xx, zz));
		}
	}
}
