package voxel.client.core.launcher.thread;

import voxel.client.core.MainClient;

public class CreateThread extends Thread {
	public void run() {
        MainClient.LaunchVoxel();
    }

    public static void StartThread() {
        (new CreateThread()).start();
    }
}
