package io.github.guerra24.voxel.client.kernel.world.chunks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorldService {
	public final ExecutorService es;

	public WorldService() {
		this.es = Executors.newFixedThreadPool(4);
	}

	public void add_worker(ChunkWorkerGenerate worker) {
		es.execute(worker);
	}
	
	public void add_worker(ChunkWorkerMesh worker) {
		es.execute(worker);
	}
	
	public void add_worker(ChunkWorkerDestroy worker) {
		es.execute(worker);
	}
}
