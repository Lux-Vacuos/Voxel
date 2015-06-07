package io.github.guerra24.voxel.client.kernel;

import static org.lwjgl.opengl.GL11.*;
import io.github.guerra24.voxel.client.kernel.util.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	private static final int FPS_CAP = 60;
	private static final String Title = "Voxel Game";

	private static long lastFrameTime;
	private static float delta;

	private static PixelFormat pixelformat = new PixelFormat();

	public static void createDisplay() {
		Logger.log(Kernel.currentThread(), "Creating Display");
		Logger.log(Kernel.currentThread(), "LWJGL Version: " + Sys.getVersion());
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(pixelformat);
			Display.setTitle(Title);
			Display.setResizable(false);
			Display.setFullscreen(false);
		} catch (LWJGLException e) {
			e.printStackTrace();
			Logger.error(Kernel.currentThread(), "Failed to create Display");
		}
		Logger.log(Kernel.currentThread(), "OpenGL Version: "
				+ glGetString(GL_VERSION));
		glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}

	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}

	public static float getFrameTimeSeconds() {
		return delta;
	}

	public static void closeDisplay() {
		Display.destroy();
	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
}
