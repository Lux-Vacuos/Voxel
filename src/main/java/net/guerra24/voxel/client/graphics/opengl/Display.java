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

package net.guerra24.voxel.client.graphics.opengl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowRefreshCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import de.matthiasmann.twl.utils.PNGDecoder;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.input.Keyboard;
import net.guerra24.voxel.client.input.Mouse;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.util.Logger;

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
	private GLFWVidMode vidmode;
	/**
	 * Display Data
	 */
	private static double lastLoopTimeUpdate;
	public static float timeCountUpdate;
	private static double lastLoopTimeRender;
	public static float timeCountRender;
	public static int fps;
	public static int fpsCount;
	public static int ups;
	public static int upsCount;

	/**
	 * LWJGL Callback
	 */
	private static GLFWErrorCallback errorCallback;
	public GLFWKeyCallback keyCallback;
	public GLFWCharCallback charCallback;
	public GLFWCursorEnterCallback cursorEnterCallback;
	public GLFWCursorPosCallback cursorPosCallback;
	public GLFWMouseButtonCallback mouseButtonCallback;
	public GLFWWindowFocusCallback windowFocusCallback;
	public GLFWWindowIconifyCallback windowIconifyCallback;
	public GLFWWindowSizeCallback windowSizeCallback;
	public GLFWWindowPosCallback windowPosCallback;
	public GLFWWindowRefreshCallback windowRefreshCallback;
	public GLFWFramebufferSizeCallback framebufferSizeCallback;
	public GLFWScrollCallback scrollCallback;

	/**
	 * Window variables
	 */
	private boolean displayCreated = false;
	private boolean displayFocused = false;
	private boolean displayVisible = true;
	private boolean displayDirty = false;
	private boolean displayResizable = false;
	private int latestEventKey = 0;
	private int displayX = 0;
	private int displayY = 0;
	private boolean displayResized = false;
	private static int displayWidth = 0;
	private static int displayHeight = 0;
	private int displayFramebufferWidth = 0;
	private int displayFramebufferHeight = 0;
	private boolean latestResized = false;
	private int latestWidth = 0;
	private int latestHeight = 0;

	private static long variableYieldTime, lastTime;

	public void initDsiplay(int width, int height) {
		glfwSetErrorCallback(errorCallback = new GLFWErrorCallback() {
			GLFWErrorCallback delegate = GLFWErrorCallback.createPrint(System.err);

			@Override
			public void invoke(int error, long description) {
				if (error == GLFW_VERSION_UNAVAILABLE)
					Logger.error("Voxel requires OpenGL 3.3 or higher");
				delegate.invoke(error, description);
			}

			@Override
			public void release() {
				delegate.release();
				super.release();
			}
		});
		displayWidth = width;
		displayHeight = height;
		if (glfwInit() != GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_TRUE);

		displayResizable = false;
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

		window = glfwCreateWindow(displayWidth, displayHeight, VoxelVariables.Title, NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		createCallBacks();
		setCallbacks();
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - displayWidth) / 2, (vidmode.height() - displayHeight) / 2);
		glfwMakeContextCurrent(window);
		glfwSwapInterval(VoxelVariables.VSYNC ? 1 : 0);
	}

	/**
	 * Create the LWJGL CallBacks
	 * 
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
	 */
	public void setCallbacks() {
		glfwSetKeyCallback(window, keyCallback);
		glfwSetCharCallback(window, charCallback);
		glfwSetCursorEnterCallback(window, cursorEnterCallback);
		glfwSetCursorPosCallback(window, cursorPosCallback);
		glfwSetMouseButtonCallback(window, mouseButtonCallback);
		glfwSetWindowFocusCallback(window, windowFocusCallback);
		glfwSetWindowIconifyCallback(window, windowIconifyCallback);
		glfwSetWindowSizeCallback(window, windowSizeCallback);
		glfwSetWindowPosCallback(window, windowPosCallback);
		glfwSetWindowRefreshCallback(window, windowRefreshCallback);
		glfwSetFramebufferSizeCallback(window, framebufferSizeCallback);
		glfwSetScrollCallback(window, scrollCallback);
	}

	/**
	 * Creates and Sets the Display
	 * 
	 */
	public void startUp() {
		Logger.log("Creating Display");
		try {
			String[] IconPath = new String[2];
			IconPath[0] = "assets/icon/icon32.png";
			IconPath[1] = "assets/icon/icon64.png";
			ByteBuffer[] icon_array = new ByteBuffer[IconPath.length];
			for (int i = 0; i < IconPath.length; i++) {
				icon_array[i] = ByteBuffer.allocateDirect(1);
				String path = IconPath[i];
				icon_array[i] = loadIcon(path);
			}
		} catch (IOException e) {
			Logger.error("Failed to load icon");
			e.printStackTrace();
		}
		createCapabilities();
		glViewport(0, 0, displayWidth, displayHeight);
		lastLoopTimeUpdate = getTime();
		lastLoopTimeRender = getTime();
		ByteBuffer w = BufferUtils.createByteBuffer(4);
		ByteBuffer h = BufferUtils.createByteBuffer(4);
		glfwGetWindowSize(window, w, h);
		int width = w.getInt(0);
		int height = h.getInt(0);
		displayHeight = width;
		displayHeight = height;
		displayCreated = true;
	}

	/**
	 * Updates the Display
	 * 
	 * @param fps
	 *            Game Max FPS
	 */
	public void updateDisplay(int fps, GameResources gm) {
		glfwSwapBuffers(window);
		glfwPollEvents();
		Mouse.poll();
		sync(fps);
		checkVRAM();
	}

	/**
	 * Destroy the display
	 * 
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
	 */
	public static double getTime() {
		return glfwGetTime();
	}

	public static long getNanoTime() {
		return (long) (glfwGetTime() * (1000L * 1000L * 1000L));
	}

	private void checkVRAM() {
	}

	/**
	 * Calculates the Delta
	 * 
	 * @return Delta
	 */
	public static float getDeltaUpdate() {
		double time = getTime();
		float delta = (float) (time - lastLoopTimeUpdate);
		lastLoopTimeUpdate = time;
		timeCountUpdate += delta;
		return delta;
	}

	/**
	 * Calculates the Delta
	 * 
	 * @return Delta
	 */
	public static float getDeltaRender() {
		double time = getTime();
		float delta = (float) (time - lastLoopTimeRender);
		lastLoopTimeRender = time;
		timeCountRender += delta;
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
	 */
	public static boolean isCloseRequested() {
		return glfwWindowShouldClose(window) == GL_TRUE;
	}

	public static int getWidth() {
		return displayWidth;
	}

	public static void setWidth(int width) {
		displayWidth = width;
	}

	public boolean isDisplayCreated() {
		return displayCreated;
	}

	public boolean isDisplayFocused() {
		return displayFocused;
	}

	public boolean isDisplayVisible() {
		return displayVisible;
	}

	public boolean isDisplayDirty() {
		return displayDirty;
	}

	public boolean isDisplayResizable() {
		return displayResizable;
	}

	public int getLatestEventKey() {
		return latestEventKey;
	}

	public int getDisplayX() {
		return displayX;
	}

	public int getDisplayY() {
		return displayY;
	}

	public boolean isDisplayResized() {
		return displayResized;
	}

	public int getDisplayFramebufferWidth() {
		return displayFramebufferWidth;
	}

	public int getDisplayFramebufferHeight() {
		return displayFramebufferHeight;
	}

	public boolean isLatestResized() {
		return latestResized;
	}

	public int getLatestWidth() {
		return latestWidth;
	}

	public int getLatestHeight() {
		return latestHeight;
	}

	public static int getHeight() {
		return displayHeight;
	}

	public static void setHeight(int height) {
		displayHeight = height;
	}

	/**
	 * Limits the fps to a fixed value
	 * 
	 * @param fps
	 *            FPS Limit
	 */
	private void sync(int fps) {
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
