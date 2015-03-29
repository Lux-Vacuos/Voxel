package voxel.client.core.launcher.thread;

import voxel.client.engine.MainClient;

public class CreateThread extends Thread {
	public void run() {
		MainClient.LaunchGame();
	}

	public static void StartThread() {
		(new CreateThread()).start();
	}
}
