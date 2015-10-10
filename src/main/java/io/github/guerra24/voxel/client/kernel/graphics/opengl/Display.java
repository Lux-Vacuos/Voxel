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

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_DEBUG_CONTEXT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorEnterCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowFocusCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIconifyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowRefreshCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
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
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowRefreshCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import de.matthiasmann.twl.utils.PNGDecoder;
import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.input.Keyboard;
import io.github.guerra24.voxel.client.kernel.input.Mouse;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
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
	private GLFWVidMode vidmode;
	/**
	 * Game loop variables
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
		displayWidth = width;
		displayHeight = height;
		errorCallback = new GLFWErrorCallback() {
			@Override
			public void invoke(int arg0, long arg1) {
			}
		};
		glfwSetErrorCallback(errorCallback);
		if (glfwInit() != GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GL_TRUE);

		displayResizable = false;
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		window = glfwCreateWindow(displayWidth, displayHeight, KernelConstants.Title, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		createCallBacks();
		setCallbacks();
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.getWidth() - displayWidth) / 2, (vidmode.getHeight() - displayHeight) / 2);
		glfwMakeContextCurrent(window);
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
		VoxelGL33.glViewport(0, 0, displayWidth, displayHeight);
		lastLoopTimeUpdate = getTime();
		lastLoopTimeRender = getTime();
		displayCreated = true;
	}

	/**
	 * Updates the Display
	 * 
	 * @param fps
	 *            Game Max FPS
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void updateDisplay(int fps, GameResources gm) {
		if (KernelConstants.loaded) {
			ByteBuffer w = BufferUtils.createByteBuffer(4);
			ByteBuffer h = BufferUtils.createByteBuffer(4);
			glfwGetWindowSize(window, w, h);
			int width = w.getInt(0);
			int height = h.getInt(0);
			displayHeight = width;
			displayHeight = height;
			VoxelGL33.glViewport(0, 0, width, height);
			gm.getRenderer().setProjectionMatrix(gm.getRenderer().createProjectionMatrix(Display.getWidth(),
					Display.getHeight(), KernelConstants.FOV, KernelConstants.NEAR_PLANE, KernelConstants.FAR_PLANE));
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
		// errorCallback.release();//This not work
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
	 * @author Guerra24 <pablo230699@hotmail.com>
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
	 * @author Guerra24 <pablo230699@hotmail.com>
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
