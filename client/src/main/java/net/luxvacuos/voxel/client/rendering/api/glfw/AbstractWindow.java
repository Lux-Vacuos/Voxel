/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwSetCursorEnterCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowRefreshCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;
import static org.lwjgl.nanovg.NanoVGGL3.nvgDelete;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GLCapabilities;

import net.luxvacuos.voxel.client.input.KeyboardHandler;
import net.luxvacuos.voxel.client.resources.AssimpResourceLoader;
import net.luxvacuos.voxel.client.resources.ResourceLoader;

public abstract class AbstractWindow implements IWindow {

	protected final long windowID;

	// The Keyboard Handler for this window
	protected KeyboardHandler kbHandle;

	protected DisplayUtils displayUtils;

	protected GLCapabilities capabilities;

	protected boolean created = false;
	protected boolean dirty = false;

	protected int posX = 0;
	protected int posY = 0;

	protected boolean resized = false;
	protected int width = 0;
	protected int height = 0;
	protected int framebufferWidth = 0;
	protected int framebufferHeight = 0;

	protected boolean latestResized = false;
	protected float pixelRatio;

	protected long nvgID;
	protected ResourceLoader resourceLoader;
	protected AssimpResourceLoader assimpResourceLoader;

	protected double lastLoopTime;
	protected float timeCount;

	protected AbstractWindow(long windowID, int width,
			int height) {
		this.windowID = windowID;
		this.displayUtils = new DisplayUtils();
		this.width = width;
		this.height = height;

		this.setCallbacks();
	}

	protected void setCallbacks() {
		this.kbHandle = new KeyboardHandler(this.windowID);
		glfwSetCursorEnterCallback(windowID, WindowManager.cursorEnterCallback);
		glfwSetCursorPosCallback(windowID, WindowManager.cursorPosCallback);
		glfwSetMouseButtonCallback(windowID, WindowManager.mouseButtonCallback);
		glfwSetWindowSizeCallback(windowID, WindowManager.windowSizeCallback);
		glfwSetWindowPosCallback(windowID, WindowManager.windowPosCallback);
		glfwSetWindowRefreshCallback(windowID, WindowManager.windowRefreshCallback);
		glfwSetFramebufferSizeCallback(windowID, WindowManager.framebufferSizeCallback);
		glfwSetScrollCallback(windowID, WindowManager.scrollCallback);
		glfwSetWindowRefreshCallback(windowID, WindowManager.refreshCallback);
	}

	@Override
	public void setVisible(boolean flag) {
		if (flag)
			glfwShowWindow(this.windowID);
		else
			glfwHideWindow(this.windowID);
	}

	public void resetViewport() {
		glViewport(0, 0, (int) (width * pixelRatio), (int) (height * pixelRatio));
	}

	public void setViewport(int x, int y, int width, int height) {
		glViewport(0, 0, width, height);
	}

	@Override
	public float getDelta() {
		double time = WindowManager.getTime();
		float delta = (float) (time - this.lastLoopTime);
		this.lastLoopTime = time;
		this.timeCount += delta;
		return delta;
	}

	public float getTimeCount() {
		return this.timeCount;
	}

	public void setTimeCount(float timeCount) {
		this.timeCount = timeCount;
	}

	public boolean isWindowCreated() {
		return this.created;
	}

	public boolean isWindowFocused() {
		return this.getWindowAttribute(GLFW.GLFW_FOCUSED);
	}

	public boolean visible() {
		return this.getWindowAttribute(GLFW.GLFW_VISIBLE);
	}

	public boolean dirty() {
		return this.dirty;
	}

	public boolean isResizable() {
		return this.getWindowAttribute(GLFW.GLFW_RESIZABLE);
	}

	public int getWindowX() {
		return this.posX;
	}

	public int getWindowY() {
		return this.posY;
	}

	public boolean wasResized() {
		return this.resized;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getFrameBufferWidth() {
		return this.framebufferWidth;
	}

	public int getFrameBufferHeight() {
		return this.framebufferHeight;
	}

	public float getPixelRatio() {
		return this.pixelRatio;
	}

	public long getID() {
		return this.windowID;
	}

	public long getNVGID() {
		return this.nvgID;
	}

	public ResourceLoader getResourceLoader() {
		if (this.resourceLoader == null)
			this.resourceLoader = new ResourceLoader(this.windowID, this.nvgID);
		return this.resourceLoader;
	}
	
	public AssimpResourceLoader getAssimpResourceLoader() {
		if(this.assimpResourceLoader == null)
			this.assimpResourceLoader = new AssimpResourceLoader(getResourceLoader());
		return this.assimpResourceLoader;
	}

	public KeyboardHandler getKeyboardHandler() {
		return this.kbHandle;
	}

	public boolean isCloseRequested() {
		return glfwWindowShouldClose(this.windowID);
	}

	private boolean getWindowAttribute(int attribute) {
		return (GLFW.glfwGetWindowAttrib(this.windowID, attribute) == GLFW.GLFW_TRUE ? true : false);
	}

	public GLCapabilities getCapabilities() {
		return this.capabilities;
	}

	@Override
	public void beingNVGFrame() {
		nvgBeginFrame(this.nvgID, this.width, this.height, this.pixelRatio);
	}

	@Override
	public void endNVGFrame() {
		nvgEndFrame(this.nvgID);
	}

	@Override
	public void closeDisplay() {
		nvgDelete(this.nvgID);
		Callbacks.glfwFreeCallbacks(this.windowID);
		glfwDestroyWindow(this.windowID);
		this.created = false;
	}

	@Override
	public void dispose() {
		resourceLoader.dispose();
	}

}
