package io.github.guerra24.voxel.client.kernel.core;

public class UpdateThread extends Thread {
	@Override
	public void run() {
		while (Kernel.gameResources.gameStates.loop) {
			switch (Kernel.gameResources.gameStates.state) {
			case MAINMENU:
				break;
			case IN_PAUSE:
				break;
			case GAME:
				synchronized (Kernel.gameResources.waters) {
					synchronized (Kernel.gameResources.cubes) {
						Kernel.world.update(Kernel.gameResources.camera);
						Kernel.world.test();
					}
				}
				break;
			}
			Kernel.gameResources.gameStates.switchStates();
		}
	}
}
