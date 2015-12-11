package net.guerra24.voxel.client.world;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorldService {
	public final ExecutorService es;

	public WorldService() {
		this.es = Executors.newWorkStealingPool();
	}

	public void add_worker(Runnable worker) {
		es.execute(worker);
	}
	
}