package voxel.client.core;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import voxel.client.core.engine.util.GLCaps;
import voxel.client.core.engine.util.Logger;

public class MainClient {

	public static int WIDTH = 1280;
	public static int HEIGHT = 720;

	public static void Start() {
		try {
			Logger.log("Creating Display");
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Logger.error("Unable to create display");
			System.exit(0);
		}

		InitGL();

		while (!Display.isCloseRequested()) {
			Render();
			Display.update();
		}

		Display.destroy();
	}

	public static void InitGL() {
		GLCaps.checkGLCaps();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH, 0, HEIGHT, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	public static void Render() {
		// Clear the screen and depth buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// set the color of the quad (R,G,B,A)
		glColor3f(0.5f, 0.5f, 1.0f);

		// draw quad
		glBegin(GL_QUADS);
		glVertex2f(100, 100);
		glVertex2f(100 + 200, 100);
		glVertex2f(100 + 200, 100 + 200);
		glVertex2f(100, 100 + 200);
		glEnd();

	}

	public static void main(String[] args) {
		Logger.log("LWJGL Version: " + Sys.getVersion());
		Start();
	}

	public static void LaunchGame() {
		Start();
	}

}
