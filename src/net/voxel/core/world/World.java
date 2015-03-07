package net.voxel.core.world;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import net.logger.Logger;
import net.voxel.core.Main;
import net.voxel.core.world.chunks.Chunk;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.nishu.utils.Camera;
import com.nishu.utils.Camera3D;
import com.nishu.utils.Color4f;
import com.nishu.utils.Font;
import com.nishu.utils.GameLoop;
import com.nishu.utils.Screen;
import com.nishu.utils.Text;

public class World extends Screen {
	private Camera camera;
	private Font font;
	
	private Chunk c;
	
	public String title = "Voxel ";
	public String version = "0.0.2a";

	public static boolean debug = false;

	public World() {
		initGL();
		init();
	}

	@Override
	public void init() {
		Logger.log("Initializing Camera");
		camera = new Camera3D.CameraBuilder().setAspectRatio(Main.aspect)
				.setRotation(0, 0, 0).setPosition(0, 0, 0)
				.setFieldOfView(Main.fov).build();
		font = new Font();
		font.loadFont("Default", "comic.png");
		
		c = new Chunk(0, 0, 0);
	}

	@Override
	public void initGL() {
	}

	@Override
	public void update() {
		input();
	}

	private void input() {
		camera.updateKeys(32, 1);
		camera.updateMouse(1, 90, -90);
		if (Mouse.isButtonDown(0)) {
			Mouse.setGrabbed(true);
		} else if (Mouse.isButtonDown(1)) {
			Mouse.setGrabbed(false);
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
					Logger.log("Disposing");
					dispose();
				}
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
		render2D();
		render3D();
		camera.applyTranslations();
		c.render();
		glLoadIdentity();
	}

	public void render2D() {
		glColor3f(1, 1, 1);
		glClearDepth(1);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		glViewport(0, 0, Main.WIDTH, Main.HEIGHT);
		glOrtho(0, 1, 0, 1, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		Color4f color4f = new Color4f(1, 1, 1, 1);
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
		;
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
		c.dispose();
		Display.destroy();
		System.exit(0);
	}
}