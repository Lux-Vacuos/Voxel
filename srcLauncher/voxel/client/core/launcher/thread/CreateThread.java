package voxel.client.core.launcher.thread;

import voxel.client.StartClient;

public class CreateThread extends Thread {
	public void run() {
		StartClient.StartGame();
	}

	public static void StartThread() {
		(new CreateThread()).run();
	}
}
