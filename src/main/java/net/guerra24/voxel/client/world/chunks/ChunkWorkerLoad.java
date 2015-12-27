package net.guerra24.voxel.client.world.chunks;

import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.world.IWorld;

public class ChunkWorkerLoad implements Runnable {
	private final IWorld world;
	private final int chunkDim,cx,cy,cz;
	private final GameResources gm;

	public ChunkWorkerLoad(IWorld world,int chunkDim, int cx, int cy, int cz, GameResources gm) {
		this.world = world;
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
		this.chunkDim = chunkDim;
		this.gm = gm;
	}

	@Override
	public void run() {
		world.loadChunk(chunkDim, cx, cy, cz, gm);
	}
}