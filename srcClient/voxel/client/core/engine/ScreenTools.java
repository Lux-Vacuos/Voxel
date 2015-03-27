package voxel.client.core.engine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.opengl.Display;

import voxel.client.core.engine.color.Color4f;

public class ScreenTools {

	public static void clearScreen(boolean clearDepth, Color4f color) {
		clearScreenBits(clearDepth);
		clearScreenColor(color);
	}

	public static void clearScreen(boolean clearDepth, float r, float g,
			float b, float a) {
		clearScreenColor(r, g, b, a);
		clearScreenBits(clearDepth);
	}

	public static void clearScreenBits(boolean clearDepth) {
		if (clearDepth)
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		else if (!clearDepth)
			glClear(GL_COLOR_BUFFER_BIT);
	}

	public static void clearScreenColor(Color4f color) {
		ScreenTools.clearScreenColor(color.r, color.g, color.b, color.a);
	}

	public static void clearScreenColor(float r, float g, float b, float a) {
		glClearColor(r, g, b, a);
	}

	public static void renderOrtho(double left, double right, double bottom,
			double top) {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		glMatrixMode(GL_MODELVIEW);
		glOrtho(left, right, bottom, top, 1, -1);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public static void render2D() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		glMatrixMode(GL_MODELVIEW);
		glOrtho(0, 1, 0, 1, -1, 1);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glLoadIdentity();
	}

	public static void render3D() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		gluPerspective(67.0f, Display.getWidth() / Display.getHeight(), 0.001f,
				1000f);
		glMatrixMode(GL_MODELVIEW);

		glEnable(GL_DEPTH_TEST);
	}
}
