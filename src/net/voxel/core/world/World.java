package net.voxel.core.world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.io.IOException;

import net.logger.Logger;
import net.voxel.core.Main;
import net.voxel.core.world.blocks.Blocks;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.nishu.utils.Camera;
import com.nishu.utils.Camera3D;
import com.nishu.utils.Color4f;
import com.nishu.utils.Font;
import com.nishu.utils.GameLoop;
import com.nishu.utils.Screen;
import com.nishu.utils.Text;

public class World extends Screen {
	
	//chunk types
	public static final int AIRCHUNK = 0, MIXEDCHUNK = 1; 
	public static int viewDistance = 4;
	
	private Camera camera;
	private Font font;
	private WorldManager worldManager;
	public static Texture texture;
	
	public String title = "Voxel ";
	public String version = "0.0.4a";

	public static boolean debug = false;

	public World() {
		initGL();
		init();
	}

	@Override
	public void init() {
		Blocks.createBlockMap();
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("spritesheets/blocks/BlocksText.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.log("Initializing Camera");
		camera = new Camera3D.CameraBuilder().setAspectRatio(Main.aspect)
				.setRotation(0, 0, 0).setPosition(0, 0, 0)
				.setFieldOfView(Main.fov).build();
		font = new Font();
		font.loadFont("Default", "fonts/comic.png");
		Logger.log("Generating Chunks");
		worldManager = new WorldManager();
		Logger.log("Generation completed");
	}

	@Override
	public void initGL() { 
		glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D); 
		glEnable(GL_BLEND);
    	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void update() {
		input();
		worldManager.update();
	}

	private void input() {
		camera.updateKeys(32, 2);
		camera.updateMouse(1, 90, -90);
		if (Mouse.isButtonDown(0)) {
			Mouse.setGrabbed(true);
		} else if (Mouse.isButtonDown(1)) {
			Mouse.setGrabbed(false);
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_F3) && !debug) {
					Logger.log("Debug is enable");
					debug = true;
				} else if (Keyboard.isKeyDown(Keyboard.KEY_F3) && debug) {
					Logger.log("Debug is disable");
					debug = false;
				}
			}
		}
	}

	@Override
	public void render() {
		texture.bind();
		render3D();
		camera.applyTranslations();
		worldManager.render();
		glLoadIdentity();
		render2D();
		}

	public void render2D() {
		glCullFace(GL_BACK);
		glColor3f(1, 1, 1);
		glClearDepth(1);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		glViewport(0, 0, Main.WIDTH, Main.HEIGHT);
		glOrtho(0, 1, 0, 1, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		Color4f color4f = Color4f.WHITE;
		if (debug) {
			Text.renderString(font, title + version, 0f, 1.21f, 0.4f, color4f);
			Text.renderString(font, "Debug Info", 0f, 1.15f, 0.4f, color4f);
			Text.renderString(font, "X: " + camera.getX(), 0f, 1.10f, 0.4f,
					color4f);
			Text.renderString(font, "Y: " + camera.getY(), 0f, 1.05f, 0.4f,
					color4f);
			Text.renderString(font, "Z: " + camera.getZ(), 0f, 1.00f, 0.4f,
					color4f);
			Text.renderString(font, "Xrot: " + camera.getPitch(), 0f, 0.95f,
					0.4f, color4f);
			Text.renderString(font, "Yrot: " + camera.getYaw(), 0f, 0.90f,
					0.4f, color4f);
			Text.renderString(font, "FPS: " + GameLoop.getFPS(), 0f, 0.85f,
					0.4f, color4f);
		}
	}

	public void render3D() {
		glCullFace(GL_FRONT);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(Main.fov, Main.aspect, Main.nearClip, Main.farClip);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
	}

	@Override
	public void dispose() {
		Logger.log("System closed");
		Display.destroy();
		System.exit(0);
	}
}