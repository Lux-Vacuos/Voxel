package voxel.server.core.world;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_MODULATE;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glTexEnvi;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.opengl.Display;

import voxel.client.core.engine.Screen;
import voxel.client.core.util.ConstantsClient;
import voxel.client.core.util.Logger;

public class World extends Screen {

	public static final int AIRCHUNK = 0, MIXEDCHUNK = 1;

	private WorldManager worldManager;

	public static int textureID;

	public World() {
		initGL();
		init();
	}

	@Override
	public void init() {
		Logger.log("Creating World with " + ConstantsClient.viewDistance
				+ " Chunks");
		worldManager = new WorldManager();
		Logger.log("World initialization completed");
	}

	@Override
	public void initGL() {
		Logger.log("Initializing World");
		glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
		glEnable(GL_CULL_FACE);
	}

	@Override
	public void update() {
		worldManager.update();
	}

	@Override
	public void render() {
		render3D();

		worldManager.render();
		glLoadIdentity();
	}

	public void render3D() {
		glCullFace(GL_FRONT);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		gluPerspective(ConstantsClient.FOV, ConstantsClient.ASPECT, 0.001f,
				1000f);
		glMatrixMode(GL_MODELVIEW);

		glEnable(GL_DEPTH_TEST);
	}

	@Override
	public void dispose() {
		Display.destroy();
		Logger.log("System exit");
		System.exit(0);
	}
}
