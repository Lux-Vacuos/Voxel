package net.guerra24.voxel.client.engine;

import static org.lwjgl.opengl.GL11.*;
import net.guerra24.voxel.client.engine.menu.Loading;
import net.guerra24.voxel.client.engine.util.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 60;
	private static final String Title = "Game";

	private static long lastFrameTime;
	private static float delta;

	private static PixelFormat pixelformat = new PixelFormat();

	public static Loading splash;

	public static void createDisplay() {
		Logger.log("Creating Display");
		Logger.log("LWJGL Version: " + Sys.getVersion());
		//ContextAttribs attribs = new ContextAttribs(3, 3)
		//		.withForwardCompatible(true).withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			//Display.create(pixelformat, attribs);
			Display.create(pixelformat);
			splash = new Loading();
			Display.setTitle(Title);
			Display.setResizable(false);
			Display.setFullscreen(false);
		} catch (LWJGLException e) {
			e.printStackTrace();
			Logger.error("Failed to create Display");
		}
		Logger.log("OpenGL Version: " + glGetString(GL_VERSION));
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
