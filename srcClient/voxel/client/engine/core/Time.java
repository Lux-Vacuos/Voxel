package voxel.client.engine.core;

import org.lwjgl.Sys;

import voxel.client.engine.MainClient;
import voxel.client.engine.util.Logger;

public class Time {

	public static int getDelta() {
		long time = getTime();
		int delta = (int) (time - MainClient.lastFrame);
		MainClient.lastFrame = time;

		return delta;
	}

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static void updateFPS() {
		if (getTime() - MainClient.lastFPS > 1000) {
			Logger.log("FPS: " + MainClient.fps);
			MainClient.fps = 0;
			MainClient.lastFPS += 1000;
		}
		MainClient.fps++;
	}
}
