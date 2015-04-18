package net.guerra24.voxel.client.launcher.thread;

public class CreateThread extends Thread {
	public void run() {
	}

	public static void StartThread() {
		(new CreateThread()).run();
	}
}