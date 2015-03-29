package voxel.client.core.launcher.thread;

import voxel.client.engine.StartEngine;

public class CreateThread extends Thread {
	public void run() {
		StartEngine.LaunchGame();
	}

	public static void StartThread() {
		(new CreateThread()).start();
	}
}
