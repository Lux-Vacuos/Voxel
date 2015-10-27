package net.guerra24.voxel.world.chunks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorldService {
	public final ExecutorService es;

	public WorldService() {
		this.es = Executors.newFixedThreadPool(1);
	}

	public void add_worker(ChunkWorkerMesh worker) {
		es.execute(worker);
	}
	
}
