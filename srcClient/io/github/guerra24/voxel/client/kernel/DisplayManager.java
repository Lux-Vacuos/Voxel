package io.github.guerra24.voxel.client.kernel;

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glViewport;
import io.github.guerra24.voxel.client.kernel.util.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	private static final int FPS_CAP = 30;
	private static final String Title = "Voxel Game";

	private static long lastFrameTime;
	private static float delta;

	private static PixelFormat pixelformat = new PixelFormat();

	public static void createDisplay() {
		Logger.log(Thread.currentThread(), "Creating Display");
		Logger.log(Thread.currentThread(), "LWJGL Version: " + Sys.getVersion());
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(pixelformat);
			Display.setTitle(Title);
			Display.setResizable(false);
			Display.setFullscreen(false);
		} catch (LWJGLException e) {
			Logger.error(Thread.currentThread(), "Failed to create Display");
			e.printStackTrace();
		}
		Logger.log(Thread.currentThread(), "OpenGL Version: "
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
