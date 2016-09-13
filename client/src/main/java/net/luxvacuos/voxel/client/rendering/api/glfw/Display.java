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

import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_ANTIALIAS;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_STENCIL_STROKES;
import static org.lwjgl.nanovg.NanoVGGL3.nvgCreateGL3;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;

import de.matthiasmann.twl.utils.PNGDecoder;
import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.core.exception.LoadTextureException;
import net.luxvacuos.voxel.client.input.KeyboardHandler;
import net.luxvacuos.voxel.client.input.Mouse;

/**
 * 
 * A new complete re-write Display Manager.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * 
 */
@Deprecated
public class Display extends AbstractWindow {

	private GLFWVidMode vidmode;

	public Display() { }

	@Override
	public void create(int width, int height, String title, boolean vsync, boolean visible, boolean resizable,
			ContextFormat format, String[] icons) {
		Logger.log("Creating Window");
		super.width = width;
		super.height = height;
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		this.displayUtils = new DisplayUtils();

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, visible ? 1 : 0);
		//super.resizable = resizable;
		glfwWindowHint(GLFW_RESIZABLE, resizable ? 1 : 0);

		format.create();

		super.windowID = glfwCreateWindow(width, height, title, NULL, NULL);
		if (super.windowID == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		glfwMakeContextCurrent(super.windowID);
		super.capabilities = createCapabilities();
		
		super.kbHandle = new KeyboardHandler(super.windowID); //Sets up the Keyboard Handler
		//super.createCallbacks();
		super.setCallbacks();
		
		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(super.windowID, (vidmode.width() - super.width) / 2, (vidmode.height() - super.height) / 2);
		glfwSwapInterval(vsync ? 1 : 0);

		GLFWImage.Buffer iconsbuff = GLFWImage.malloc(2);
		try {
			for (int i = 0; i < icons.length; i++) {
				String path = icons[i];
				InputStream file = getClass().getClassLoader().getResourceAsStream(path);
				PNGDecoder decoder;
				decoder = new PNGDecoder(file);
				ByteBuffer bytebuf = ByteBuffer.allocateDirect(decoder.getWidth() * decoder.getHeight() * 4);
				decoder.decode(bytebuf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
				bytebuf.flip();
				iconsbuff.position(i).width(decoder.getWidth()).height(decoder.getHeight()).pixels(bytebuf);
			}
		} catch (IOException e) {
			throw new LoadTextureException(e);
		}

		iconsbuff.position(0);
		glfwSetWindowIcon(super.windowID, iconsbuff);
		iconsbuff.free();

		super.nvgID = nvgCreateGL3(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
		if (nvgID == NULL)
			throw new RuntimeException("Fail to create NanoVG");

		lastLoopTime = WindowManager.getTime();
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		glfwGetFramebufferSize(windowID, w, h);
		super.framebufferWidth = w.get(0);
		super.framebufferHeight = h.get(0);

		glfwGetWindowSize(super.windowID, w, h);
		super.width = w.get(0);
		super.height = h.get(0);
		super.pixelRatio = (float) framebufferWidth / (float) width;
		glViewport(0, 0, (int) (width * pixelRatio), (int) (height * pixelRatio));

		super.created = true;
	}

	@Override
	public void updateDisplay(int fps) {
		glfwSwapBuffers(super.windowID);
		glfwPollEvents();
		Mouse.poll();
		displayUtils.checkErrors();
		displayUtils.sync(fps);
	}

	public DisplayUtils getDisplayUtils() {
		return displayUtils;
	}

}
