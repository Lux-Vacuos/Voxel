package voxel.client.engine.core;

import org.lwjgl.Sys;

import voxel.client.engine.StartEngine;
import voxel.client.engine.util.Logger;

public class Time {

	public static int getDelta() {
		long time = getTime();
		int delta = (int) (time - StartEngine.lastFrame);
		StartEngine.lastFrame = time;

		return delta;
	}

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static void updateFPS() {
		if (getTime() - StartEngine.lastFPS > 1000) {
			Logger.log("FPS: " + StartEngine.fps);
			StartEngine.fps = 0;
			StartEngine.lastFPS += 1000;
		}
		StartEngine.fps++;
	}
}
