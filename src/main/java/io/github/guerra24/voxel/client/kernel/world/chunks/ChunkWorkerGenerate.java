package io.github.guerra24.voxel.client.kernel.world.chunks;

import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.world.DimensionalWorld;

public class ChunkWorkerGenerate implements Runnable {
	private final DimensionalWorld world;
	private final int xx, zz;
	private final GameResources gm;

	public ChunkWorkerGenerate(final DimensionalWorld world, final GameResources gm, final int xx, final int zz) {
		this.world = world;
		this.gm = gm;
		this.xx = xx;
		this.zz = zz;
	}

	@Override
	public void run() {
		synchronized (world.getChunks()) {
			if (!world.hasChunk(world.getChunkDimension(), xx, zz)) {
				if (world.existChunkFile(world.getChunkDimension(), xx, zz)) {
					world.loadChunk(world.getChunkDimension(), xx, zz, gm);
				} else {
					world.addChunk(new Chunk(world.getChunkDimension(), xx, zz, world));
					world.saveChunk(world.getChunkDimension(), xx, zz, gm);
				}
			}
		}
	}
}
