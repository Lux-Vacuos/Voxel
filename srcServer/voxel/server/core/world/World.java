package voxel.server.core.world;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_MODULATE;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTexEnvi;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import voxel.client.core.util.Constants;
import voxel.client.core.util.Logger;
import voxel.server.core.glutil.Color4f;
import voxel.server.core.glutil.Font;
import voxel.server.core.glutil.GameLoop;
import voxel.server.core.glutil.Screen;
import voxel.server.core.glutil.Text;
import voxel.server.core.world.block.Tile;

public class World extends Screen {

	public static final int AIRCHUNK = 0, MIXEDCHUNK = 1;

	private Font font;
	private WorldManager worldManager;

	private boolean renderText = false;

	private Color4f color4f = Color4f.WHITE;

	public static int textureID;

	public World() {
		initGL();
		init();
	}

	@Override
	public void init() {
		Tile.createTileMap();
		font = new Font();
		font.loadFont("Default", "fonts/comic.png");

		Logger.log("Creating World with " + Constants.viewDistance + " Chunks");
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
		input();
		worldManager.update();
	}

	private void input() {
		if (Mouse.isButtonDown(0)) {
			Mouse.setGrabbed(true);
		}
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_F3)) {
					renderText = !renderText;
				}
			}
		}
	}

	@Override
	public void render() {
		render3D();

		worldManager.render();
		glLoadIdentity();
		Text.renderString(font, Constants.title + Constants.version, 0f, 1.45f,
				Constants.textSize, color4f);

		if (renderText) {
			render2D();
			renderText();
		}
	}

	private void renderText() {
		Text.renderString(font, Constants.title + Constants.version, 0f, 1.45f,
				Constants.textSize, color4f);
		Text.renderString(font, "Debug Info", 0f, 1.35f, Constants.textSize,
				color4f);
		Text.renderString(font, "FPS: " + GameLoop.getFPS(), 0f, 1.30f,
				Constants.textSize, Color4f.WHITE);
		Text.renderString(font, "X:"
				+ (int) worldManager.getMobManager().getPlayer().getX() + " Y:"
				+ (int) worldManager.getMobManager().getPlayer().getY() + " Z:"
				+ (int) worldManager.getMobManager().getPlayer().getZ(), 0f,
				1.25f, Constants.textSize, Color4f.WHITE);
		Text.renderString(font, "Rotx:"
				+ (int) worldManager.getMobManager().getPlayer().getPitch()
				+ " RotY:"
				+ (int) worldManager.getMobManager().getPlayer().getYaw()
				+ " RotZ:"
				+ (int) worldManager.getMobManager().getPlayer().getRoll(), 0f,
				1.20f, Constants.textSize, Color4f.WHITE);
		Text.renderString(font, "Chunks: " + Constants.chunksLoaded + " ("
				+ Constants.chunksFrustum + ")", 0f, 1.20f, Constants.textSize,
				Color4f.WHITE);
	}

	public void render2D() {
		glCullFace(GL_BACK);
		glClearDepth(1);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		glOrtho(0, 1, 0, 1, -1, 1);
		glViewport(0, 0, Constants.WIDTH, Constants.HEIGHT);
		glMatrixMode(GL_MODELVIEW);
	}

	public void render3D() {
		glCullFace(GL_FRONT);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		gluPerspective(Constants.FOV, Constants.ASPECT, 0.001f, 1000f);
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
