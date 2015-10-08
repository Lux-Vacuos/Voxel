package io.github.guerra24.voxel.client.kernel.core;

import io.github.guerra24.voxel.client.kernel.api.VAPI;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.world.WorldHandler;

public class WorldThread2 extends Thread {
	private GameResources gm;
	private WorldHandler world;
	private VAPI api;

	@Override
	public void run() {
		while (gm.getGameStates().loop) {
			switch (gm.getGameStates().state) {
			case MAINMENU:
				try {
					Thread.sleep((long) 33.3333);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case IN_PAUSE:
				try {
					Thread.sleep((long) 33.3333);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case GAME:
				world.getWorld(world.getActiveWorld()).updateChunkMesh(gm, api);
				break;
			case LOADING_WORLD:
				try {
					Thread.sleep((long) 33.3333);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	public void setGm(GameResources gm) {
		this.gm = gm;
	}

	public void setWorldHandler(WorldHandler dimensionHandler) {
		this.world = dimensionHandler;
	}

	public void setApi(VAPI api) {
		this.api = api;
	}
}
