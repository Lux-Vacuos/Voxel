package net.guerra24.client.launcher.thread;

import org.gnet.ServerStart;

public class CreateThread extends Thread {
	public void run() {
		try {
			ServerStart.main(null);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void StartThread() {
		(new CreateThread()).run();
	}
}