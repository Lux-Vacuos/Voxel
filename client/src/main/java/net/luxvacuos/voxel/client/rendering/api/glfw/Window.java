/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.rendering.api.glfw;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
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
import static org.lwjgl.glfw.GLFW.glfwSetWindowPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowRefreshCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;

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

import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.input.Mouse;

public abstract class Window implements IDisplay {

	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWCharCallback charCallback;
	private GLFWCursorEnterCallback cursorEnterCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWWindowFocusCallback windowFocusCallback;
	private GLFWWindowIconifyCallback windowIconifyCallback;
	private GLFWWindowSizeCallback windowSizeCallback;
	private GLFWWindowPosCallback windowPosCallback;
	private GLFWWindowRefreshCallback windowRefreshCallback;
	private GLFWFramebufferSizeCallback framebufferSizeCallback;
	private GLFWScrollCallback scrollCallback;

	protected boolean displayCreated = false;
	protected boolean displayFocused = false;
	protected boolean displayVisible = true;
	protected boolean displayDirty = false;
	protected boolean displayResizable = false;

	protected int latestEventKey = 0;

	protected int displayX = 0;
	protected int displayY = 0;

	protected boolean displayResized = false;
	protected int displayWidth = 0;
	protected int displayHeight = 0;
	protected int displayFramebufferWidth = 0;
	protected int displayFramebufferHeight = 0;

	protected boolean latestResized = false;
	protected int latestWidth = 0;
	protected int latestHeight = 0;
	protected float pixelRatio;

	protected long window;
	protected long vg;

	public void createCallbacks() {
		glfwSetErrorCallback(errorCallback = new GLFWErrorCallback() {
			GLFWErrorCallback delegate = GLFWErrorCallback.createPrint(System.err);

			@Override
			public void invoke(int error, long description) {
				delegate.invoke(error, description);
			}

			@Override
			public void free() {
				delegate.free();
				super.free();
			}
		});
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
				Mouse.addWheelEvent((int) yoffset);
			}
		};
		
	}

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

	public void releaseCallbacks() {
		errorCallback.free();
		keyCallback.free();
		charCallback.free();
		cursorEnterCallback.free();
		cursorPosCallback.free();
		mouseButtonCallback.free();
		windowFocusCallback.free();
		windowIconifyCallback.free();
		windowSizeCallback.free();
		windowPosCallback.free();
		windowRefreshCallback.free();
		framebufferSizeCallback.free();
		scrollCallback.free();
	}

	public GLFWErrorCallback getErrorCallback() {
		return errorCallback;
	}

	public GLFWKeyCallback getKeyCallback() {
		return keyCallback;
	}

	public GLFWCharCallback getCharCallback() {
		return charCallback;
	}

	public GLFWCursorEnterCallback getCursorEnterCallback() {
		return cursorEnterCallback;
	}

	public GLFWCursorPosCallback getCursorPosCallback() {
		return cursorPosCallback;
	}

	public GLFWMouseButtonCallback getMouseButtonCallback() {
		return mouseButtonCallback;
	}

	public GLFWWindowFocusCallback getWindowFocusCallback() {
		return windowFocusCallback;
	}

	public GLFWWindowIconifyCallback getWindowIconifyCallback() {
		return windowIconifyCallback;
	}

	public GLFWWindowSizeCallback getWindowSizeCallback() {
		return windowSizeCallback;
	}

	public GLFWWindowPosCallback getWindowPosCallback() {
		return windowPosCallback;
	}

	public GLFWWindowRefreshCallback getWindowRefreshCallback() {
		return windowRefreshCallback;
	}

	public GLFWFramebufferSizeCallback getFramebufferSizeCallback() {
		return framebufferSizeCallback;
	}

	public GLFWScrollCallback getScrollCallback() {
		return scrollCallback;
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

	public int getDisplayWidth() {
		return displayWidth;
	}

	public int getDisplayHeight() {
		return displayHeight;
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

	public float getPixelRatio() {
		return pixelRatio;
	}

	public long getWindow() {
		return window;
	}

	public long getVg() {
		return vg;
	}

	public boolean isCloseRequested() {
		return glfwWindowShouldClose(window) == GL_TRUE;
	}

}
