package voxel.client.engine;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import voxel.client.engine.util.Logger;

public class DisplayManager {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;

	private static PixelFormat pixelformat = new PixelFormat();

	public static final int FPS_CAP = 60;

	public static final String Title = "Game";

	public static void createDisplay() {
		Logger.log("Creating Display");
		Logger.log("LWJGL Version: " + Sys.getVersion());
		ContextAttribs attribs = new ContextAttribs(3, 2)
				.withForwardCompatible(true).withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(pixelformat, attribs);
			Display.setTitle(Title);
			Display.setResizable(false);
		} catch (LWJGLException e) {
			e.printStackTrace();
			Logger.error("Failed to create Display");
		}
		Logger.log("Running in OpenGL: " + glGetString(GL_VERSION));
		glViewport(0, 0, WIDTH, HEIGHT);

	}

	public static void updateDisplay() {

		Display.sync(FPS_CAP);
		Display.update();

	}

	public static void closeDisplay() {

		Display.destroy();
	}
}
