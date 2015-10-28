package net.guerra24.voxel.world;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.guerra24.voxel.world.chunks.ChunkWorkerMesh;

public class WorldService {
	public final ExecutorService es;

	public WorldService() {
		this.es = Executors.newWorkStealingPool();
	}

	public void add_worker(ChunkWorkerMesh worker) {
		es.execute(worker);
	}
	
}
