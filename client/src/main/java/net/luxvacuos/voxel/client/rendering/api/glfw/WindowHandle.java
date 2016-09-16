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

import org.lwjgl.glfw.GLFW;

import com.badlogic.gdx.utils.Array;

import net.luxvacuos.igl.Logger;

public final class WindowHandle {

	protected int width, height;
	protected String title;
	protected Array<String> icons = new Array<>();

	protected WindowHandle(int width, int height, String title) {
		Logger.log("Creating WindowHandle for '" + title + "'");
		this.width = width;
		this.height = height;
		this.title = title;

		// Reset the window hints
		GLFW.glfwDefaultWindowHints();

		// Set the window to use OpenGL 3.3 Core with forward compatibility
		this.setWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		this.setWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		this.setWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_API);
		this.setWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		this.setWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, true);
	}

	public WindowHandle canResize(boolean flag) {
		this.setWindowHint(GLFW.GLFW_RESIZABLE, flag);
		return this;
	}

	public WindowHandle isVisible(boolean flag) {
		this.setWindowHint(GLFW.GLFW_VISIBLE, flag);
		return this;
	}

	public WindowHandle isDecorated(boolean flag) {
		this.setWindowHint(GLFW.GLFW_DECORATED, flag);
		return this;
	}

	public WindowHandle setFocusOnCreation(boolean flag) {
		this.setWindowHint(GLFW.GLFW_FOCUSED, flag);
		return this;
	}

	public WindowHandle alwaysOnTop(boolean flag) {
		this.setWindowHint(GLFW.GLFW_FLOATING, flag);
		return this;
	}

	public WindowHandle isMaximizedOnCreation(boolean flag) {
		this.setWindowHint(GLFW.GLFW_MAXIMIZED, flag);
		return this;
	}

	public WindowHandle useDebugContext(boolean flag) {
		this.setWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, flag);
		return this;
	}

	public WindowHandle setPixelBuffer(PixelBufferHandle pbh) {
		Logger.log(pbh.toString());
		this.setWindowHint(GLFW.GLFW_RED_BITS, pbh.getRedBits());
		this.setWindowHint(GLFW.GLFW_ACCUM_RED_BITS, pbh.getRedBitsAccum());
		this.setWindowHint(GLFW.GLFW_GREEN_BITS, pbh.getGreenBits());
		this.setWindowHint(GLFW.GLFW_ACCUM_GREEN_BITS, pbh.getGreenBitsAccum());
		this.setWindowHint(GLFW.GLFW_BLUE_BITS, pbh.getBlueBits());
		this.setWindowHint(GLFW.GLFW_ACCUM_BLUE_BITS, pbh.getBlueBitsAccum());
		this.setWindowHint(GLFW.GLFW_ALPHA_BITS, pbh.getAlphaBits());
		this.setWindowHint(GLFW.GLFW_ACCUM_ALPHA_BITS, pbh.getAlphaBitsAccum());
		this.setWindowHint(GLFW.GLFW_DEPTH_BITS, pbh.getDepthBits());
		this.setWindowHint(GLFW.GLFW_STENCIL_BITS, pbh.getStencilBits());
		this.setWindowHint(GLFW.GLFW_AUX_BUFFERS, pbh.getAuxBuffers());
		this.setWindowHint(GLFW.GLFW_SAMPLES, pbh.getSamples());
		this.setWindowHint(GLFW.GLFW_REFRESH_RATE, pbh.getRefreshRate());
		this.setWindowHint(GLFW.GLFW_STEREO, pbh.getStereo());
		this.setWindowHint(GLFW.GLFW_SRGB_CAPABLE, pbh.getSRGBCapable());
		this.setWindowHint(GLFW.GLFW_DOUBLEBUFFER, pbh.getDoubleBuffer());
		return this;
	}

	public WindowHandle setWindowHint(int hint, boolean flag) {
		GLFW.glfwWindowHint(hint, (flag ? 1 : 0));
		return this;
	}

	public WindowHandle setIcon(String... icons) {
		this.icons.addAll(icons);
		return this;
	}

	public void setWindowHint(int hint, int value) {
		GLFW.glfwWindowHint(hint, value);
	}

}
