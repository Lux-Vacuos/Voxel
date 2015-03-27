package voxel.client.core;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import voxel.client.core.engine.GameLoop;
import voxel.client.core.engine.Screen;
import voxel.client.core.engine.Window;
import voxel.client.core.util.Constants;
import voxel.client.core.util.GLCaps;
import voxel.client.core.util.Logger;
import voxel.server.core.MainServer;

public class MainClient extends Screen {

	private GameLoop gameLoop;
	

	public MainClient() {
		gameLoop = new GameLoop();
		gameLoop.setScreen(this);
		gameLoop.start(30);
	}

	@Override
	public void init() {
		Logger.log("Initializing engine");
		MainServer.init();
		Logger.log("Engine initialization completed");
	}

	@Override
	public void initGL() {
		Logger.log("Initializing OpenGL");
		Logger.log("Checking OpenGL GPU Capabilitites");
		GLCaps.checkGLCaps();
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		gluPerspective(Constants.FOV, Constants.ASPECT, 0.001f, 1000f);
		glMatrixMode(GL_MODELVIEW);

		glEnable(GL_DEPTH_TEST);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glEnable(GL_LINE_SMOOTH);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		Logger.log("OpenGL Initialization completed");
	}

	@Override
	public void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			dispose();
		}
		MainServer.update();
	}

	@Override
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0, 0.25f, 0.75f, 1);
		MainServer.render();
	}

	@Override
	public void dispose() {
		Logger.log("Disposing");
		MainServer.dispose();
	}

	public static void LaunchVoxel() {
		// System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir")
		// + "\\dlls");
		Window.createWindow(Constants.WIDTH, Constants.HEIGHT, "Voxels", true);
		new MainClient();
	}
}
