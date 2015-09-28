/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.guerra24.voxel.client.kernel.graphics.opengl;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.Callbacks.glfwSetCallback;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowRefreshCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;

import de.matthiasmann.twl.utils.PNGDecoder;
import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.input.Keyboard;
import io.github.guerra24.voxel.client.kernel.input.Mouse;
import io.github.guerra24.voxel.client.kernel.resources.GameControllers;
import io.github.guerra24.voxel.client.kernel.util.Logger;

/**
 * Display Manager
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category OpenGL
 */
public class Display {

	/**
	 * LWJGL Window
	 */
	private static long window;

	/**
	 * Display VidMode
	 */
	private static ByteBuffer vidmode;
	/**
	 * Game loop variables
	 */
	private static double lastLoopTime;
	public static float timeCount;
	public static int fps;
	public static int fpsCount;
	public static int ups;
	public static int upsCount;
	private static int WIDTH = 1280;
	private static int HEIGHT = 720;

	/**
	 * LWJGL Callback
	 */
	private static GLFWErrorCallback errorCallback;
	public static GLFWKeyCallback keyCallback;
	public static GLFWCharCallback charCallback;
	public static GLFWCursorEnterCallback cursorEnterCallback;
	public static GLFWCursorPosCallback cursorPosCallback;
	public static GLFWMouseButtonCallback mouseButtonCallback;
	public static GLFWWindowFocusCallback windowFocusCallback;
	public static GLFWWindowIconifyCallback windowIconifyCallback;
	public static GLFWWindowSizeCallback windowSizeCallback;
	public static GLFWWindowPosCallback windowPosCallback;
	public static GLFWWindowRefreshCallback windowRefreshCallback;
	public static GLFWFramebufferSizeCallback framebufferSizeCallback;
	public static GLFWScrollCallback scrollCallback;

	/**
	 * Window variables
	 */
	public static boolean displayCreated = false;
	public static boolean displayFocused = false;
	public static boolean displayVisible = true;
	public static boolean displayDirty = false;
	public static boolean displayResizable = false;
	public static int latestEventKey = 0;
	public static int displayX = 0;
	public static int displayY = 0;
	public static boolean displayResized = false;
	public static int displayWidth = 0;
	public static int displayHeight = 0;
	public static int displayFramebufferWidth = 0;
	public static int displayFramebufferHeight = 0;
	public static boolean latestResized = false;
	public static int latestWidth = 0;
	public static int latestHeight = 0;

	private static long variableYieldTime, lastTime;

	public void initDsiplay() {
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		if (glfwInit() != GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GL_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		window = glfwCreateWindow(WIDTH, HEIGHT, KernelConstants.Title, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		createCallBacks();
		setCallbacks();
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - WIDTH) / 2, (GLFWvidmode.height(vidmode) - HEIGHT) / 2);
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		glfwSwapInterval(0);
	}

	/**
	 * Create the LWJGL CallBacks
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void createCallBacks() {
		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				latestEventKey = key;
				if (action == GLFW_RELEASE || action == GLFW.GLFW_PRESS) {
					Keyboard.addKeyEvent(key, action == GLFW.GLFW_PRESS ? true : false);
				}
			}
		};

		charCallback = new GLFWCharCallback() {
			@Override
			public void invoke(long window, int codepoint) {
				Keyboard.addCharEvent(latestEventKey, (char) codepoint);
			}
		};

		cursorEnterCallback = new GLFWCursorEnterCallback() {
			@Override
			public void invoke(long window, int entered) {
				Mouse.setMouseInsideWindow(entered == GL_TRUE);
			}
		};

		cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				Mouse.addMoveEvent(xpos, ypos);
			}
		};

		mouseButtonCallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				Mouse.addButtonEvent(button, action == GLFW.GLFW_PRESS ? true : false);
			}
		};

		windowFocusCallback = new GLFWWindowFocusCallback() {
			@Override
			public void invoke(long window, int focused) {
				displayFocused = focused == GL_TRUE;
			}
		};

		windowIconifyCallback = new GLFWWindowIconifyCallback() {
			@Override
			public void invoke(long window, int iconified) {
				displayVisible = iconified == GL_FALSE;
			}
		};

		windowSizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				latestResized = true;
				latestWidth = width;
				latestHeight = height;
			}
		};

		windowPosCallback = new GLFWWindowPosCallback() {
			@Override
			public void invoke(long window, int xpos, int ypos) {
				displayX = xpos;
				displayY = ypos;
			}
		};

		windowRefreshCallback = new GLFWWindowRefreshCallback() {
			@Override
			public void invoke(long window) {
				displayDirty = true;
			}
		};

		framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				displayFramebufferWidth = width;
				displayFramebufferHeight = height;
			}
		};

		scrollCallback = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				Mouse.addWheelEvent((int) (yoffset * 120));
			}
		};
	}

	/**
	 * Set the LWJGL CallBacks
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void setCallbacks() {
		glfwSetCallback(window, keyCallback);
		glfwSetCallback(window, charCallback);
		glfwSetCallback(window, cursorEnterCallback);
		glfwSetCallback(window, cursorPosCallback);
		glfwSetCallback(window, mouseButtonCallback);
		glfwSetCallback(window, windowFocusCallback);
		glfwSetCallback(window, windowIconifyCallback);
		glfwSetCallback(window, windowSizeCallback);
		glfwSetCallback(window, windowPosCallback);
		glfwSetCallback(window, windowRefreshCallback);
		glfwSetCallback(window, framebufferSizeCallback);
		glfwSetCallback(window, scrollCallback);
	}

	/**
	 * Creates and Sets the Display
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void startUp() {
		Logger.log(Thread.currentThread(), "Creating Display");
		try {
			URL icon1 = getClass().getClassLoader().getResource("assets/icon/icon32.png");
			URL icon2 = getClass().getClassLoader().getResource("assets/icon/icon64.png");

			String[] IconPath = new String[2];
			IconPath[0] = icon1.getFile();
			IconPath[1] = icon2.getFile();
			ByteBuffer[] icon_array = new ByteBuffer[IconPath.length];
			for (int i = 0; i < IconPath.length; i++) {
				icon_array[i] = ByteBuffer.allocateDirect(1);
				String path = IconPath[i];
				icon_array[i] = loadIcon(path);
			}
		} catch (IOException e) {
			Logger.error(Thread.currentThread(), "Failed to create Display");
			e.printStackTrace();
		}
		createCapabilities();
		Logger.log(Thread.currentThread(), "LWJGL Version: " + Sys.getVersion());
		Logger.log(Thread.currentThread(), "OpenGL Version: " + glGetString(GL_VERSION));
		VoxelGL33.glViewport(0, 0, WIDTH, HEIGHT);
		lastLoopTime = getTime();
	}

	/**
	 * Updates the Display
	 * 
	 * @param fps
	 *            Game Max FPS
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void updateDisplay(int fps, GameControllers gm) {
		if (KernelConstants.loaded) {
			ByteBuffer w = BufferUtils.createByteBuffer(4);
			ByteBuffer h = BufferUtils.createByteBuffer(4);
			glfwGetWindowSize(window, w, h);
			int width = w.getInt(0);
			int height = h.getInt(0);
			WIDTH = width;
			HEIGHT = height;
			VoxelGL33.glViewport(0, 0, width, height);
			gm.getRenderer().createProjectionMatrix();
		}
		glfwSwapBuffers(window);
		glfwPollEvents();
		Mouse.poll();
		sync(fps);
	}

	/**
	 * Destroy the display
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void closeDisplay() {
		glfwDestroyWindow(window);
		glfwTerminate();
		errorCallback.release();
		keyCallback.release();
		charCallback.release();
		cursorEnterCallback.release();
		cursorPosCallback.release();
		mouseButtonCallback.release();
		windowFocusCallback.release();
		windowIconifyCallback.release();
		windowSizeCallback.release();
		windowPosCallback.release();
		windowRefreshCallback.release();
		framebufferSizeCallback.release();
		scrollCallback.release();
	}

	/**
	 * Loads the Icon
	 * 
	 * @param path
	 *            Icon Path
	 * @return ByteBuffer
	 * @throws IOException
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private static ByteBuffer loadIcon(String path) throws IOException {
		InputStream inputStream = new FileInputStream(path);
		try {
			PNGDecoder decoder = new PNGDecoder(inputStream);
			ByteBuffer bytebuf = ByteBuffer.allocateDirect(decoder.getWidth() * decoder.getHeight() * 4);
			decoder.decode(bytebuf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
			bytebuf.flip();
			return bytebuf;
		} finally {
			inputStream.close();
		}
	}

	/**
	 * Get the time
	 * 
	 * @return time
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static double getTime() {
		return glfwGetTime();
	}

	/**
	 * Calculates the Delta
	 * 
	 * @return Delta
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static float getDelta() {
		double time = getTime();
		float delta = (float) (time - lastLoopTime);
		lastLoopTime = time;
		timeCount += delta;
		return delta;
	}

	/**
	 * Get the Window
	 * 
	 * @return window
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static long getWindow() {
		return window;
	}

	/**
	 * If a close is requested
	 * 
	 * @return Boolean
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static boolean isCloseRequested() {
		return glfwWindowShouldClose(window) == GL_TRUE;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static void setWidth(int wIDTH) {
		WIDTH = wIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}

	public static void setHeight(int hEIGHT) {
		HEIGHT = hEIGHT;
	}

	/**
	 * Limits the fps to a fixed value
	 * 
	 * @param fps
	 *            FPS Limit
	 */
	public static void sync(int fps) {
		if (fps <= 0)
			return;
		long sleepTime = 1000000000 / fps;
		long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000 * 1000));
		long overSleep = 0;

		try {
			while (true) {
				long t = System.nanoTime() - lastTime;

				if (t < sleepTime - yieldTime) {
					Thread.sleep(1);
				} else if (t < sleepTime) {
					Thread.yield();
				} else {
					overSleep = t - sleepTime;
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lastTime = System.nanoTime() - Math.min(overSleep, sleepTime);
			if (overSleep > variableYieldTime) {
				variableYieldTime = Math.min(variableYieldTime + 200 * 1000, sleepTime);
			} else if (overSleep < variableYieldTime - 200 * 1000) {
				variableYieldTime = Math.max(variableYieldTime - 2 * 1000, 0);
			}
		}
	}

}
