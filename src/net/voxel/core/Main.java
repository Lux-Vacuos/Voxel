package net.voxel.core;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import net.logger.Logger;
import net.voxel.core.util.GLCaps;
import net.voxel.core.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.nishu.utils.GameLoop;
import com.nishu.utils.Screen;
import com.nishu.utils.Window;

public class Main extends Screen {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int fps = 30;
	public static final String title = "Voxel game";
	public static float fov = 90.0f;
	public static float nearClip = 0.001f;
	public static float farClip = 100.0f;
	public static float aspect = 1.777777777777778f;
	private static boolean mouseLock = false;

	private GameLoop gameLoop;
	private World world;

	public Main() {
		Logger.log("Starting game loop");
		gameLoop = new GameLoop();
		gameLoop.setScreen(this);
		Logger.log("Max fps: " + fps);
		gameLoop.start(fps);
	}

	public static void main(String[] args) {
		Logger.log("Creating Window"); 
		Window.createWindow(WIDTH, HEIGHT, title, mouseLock);
		new Main();
	}

	@Override
	public void init() {
		world = new World();
	}

	@Override
	public void initGL() {
		Logger.log("Initializing OpenGL");
		GLCaps.checkGLCaps();
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov, aspect, nearClip, farClip);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
		Logger.log("Initializing Render");
	}

	@Override
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.22f, 0.34f, 0.525f, 1);

		world.render();
	}

	@Override
	public void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Logger.log("Disposing");
			dispose();
		}
		world.update();
	}

	@Override
	public void dispose() {
		world.dispose();
	}
}