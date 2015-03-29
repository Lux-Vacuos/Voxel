package voxel.client.engine;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import voxel.client.engine.core.Time;
import voxel.client.engine.util.Logger;

public class MainClient {

	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	public static long lastFrame;
	public static int fps;
	public static long lastFPS;

	public static void Start() {
		try {
			Logger.log("Creating Display");
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			PixelFormat pixelFormat = new PixelFormat();
			/*
			 * ContextAttribs contextAtrributes = new ContextAttribs(3, 3);
			 * contextAtrributes.withForwardCompatible(true);
			 * contextAtrributes.withProfileCore(true);This allows to run only
			 * in OpenGL 3.3+, functions that are deprecated and removed are not
			 * available.
			 */
			Display.create(pixelFormat);
		} catch (LWJGLException e) {
			e.printStackTrace();
			Logger.error("Unable to create display");
			System.exit(0);
		}

		InitGL();
		Time.getDelta(); // call once before loop to initialise lastFrame
		lastFPS = Time.getTime();

		while (!Display.isCloseRequested()) {
			@SuppressWarnings("unused")
			int delta = Time.getDelta();

			Render();
		}

		Display.destroy();
	}

	public static void InitGL() {
		Logger.log("LWJGL Version: " + Sys.getVersion());
		Logger.log("Running in OpenGL: " + glGetString(GL_VERSION));
		glViewport(0, 0, Display.getDisplayMode().getWidth(), Display
				.getDisplayMode().getHeight());
		glClearColor(0f, 0f, 0f, 0f);
	}

	public static void Render() {
		Time.updateFPS();
		Display.update();
		Display.sync(60);
	}

	public static void main(String[] args) {
		Start();
	}

	public static void LaunchGame() {
		Start();
	}
}
