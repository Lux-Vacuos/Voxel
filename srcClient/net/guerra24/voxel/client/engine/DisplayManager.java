package net.guerra24.voxel.client.engine;

import java.nio.ByteBuffer;

import net.guerra24.voxel.client.StartClient;
import net.guerra24.voxel.client.engine.util.Logger;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class DisplayManager {

	private static GLFWErrorCallback errorCallback;
	private static GLFWKeyCallback keyCallback;

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	private static final int FPS_CAP = 60;
	private static final String Title = "Game";

	private static long lastFrameTime;
	private static float delta;

	public static long window;

	public static void createDisplay() {
		Logger.log("Creating Display");
		Logger.log("LWJGL Version: " + Sys.getVersion());
		try {
			init();
			loop();
			glfwDestroyWindow(window);
			keyCallback.release();
		} finally {
			glfwTerminate();
			errorCallback.release();
			Logger.error("Failed to create Display");
		}
		lastFrameTime = getCurrentTime();
	}

	private static void init() {
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

		if (glfwInit() != GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_REFRESH_RATE, FPS_CAP);

		window = glfwCreateWindow(WIDTH, HEIGHT, Title, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action,
					int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
					glfwSetWindowShouldClose(window, GL_TRUE); // We will detect
																// this in our
																// rendering
																// loop
			}
		});

		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - WIDTH) / 2,
				(GLFWvidmode.height(vidmode) - HEIGHT) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwShowWindow(window);
	}

	private static void loop() {
		GLContext.createFromCurrent();

		// glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		StartClient.StartGame();

	}

	public static void updateDisplay() {
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}

	public static float getFrameTimeSeconds() {
		return delta;
	}

	public static void closeDisplay() {
		glfwTerminate();
	}

	private static long getCurrentTime() {
		return (long) (GLFW.glfwGetTime() * 1000 / GLFW.glfwGetTime());
	}
}
