package net.guerra24.client.launcher.thread;

import net.guerra24.voxel.client.StartClient;

public class CreateThread extends Thread {
	public void run() {
		StartClient.StartGame();
	}

	public static void StartThread() {
		(new CreateThread()).run();
	}
}
