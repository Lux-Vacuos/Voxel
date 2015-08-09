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

import io.github.guerra24.voxel.client.kernel.core.Kernel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.Rectangle;

/**
 * Voxel OpenGL Calls Manager
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.1 Build-52
 * @since 0.0.1 Build-52
 * @category OpenGL
 */
public final class VoxelGL33 {
	/**
	 * glEnable
	 * 
	 * @param capability
	 *            Value
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void glEnable(int capability) {
		GL11.glEnable(capability);
		VoxelGLError.check();
	}

	/**
	 * glDisable
	 * 
	 * @param capability
	 *            Value
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void glDisable(int capability) {
		GL11.glDisable(capability);
		VoxelGLError.check();
	}

	/**
	 * glBlendFunc
	 * 
	 * @param src
	 *            OpenGL Blend src
	 * @param dst
	 *            OpenGL Blend dst
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void glBlendFunc(int src, int dst) {
		GL11.glBlendFunc(src, dst);
		VoxelGLError.check();
	}

	/**
	 * glClearColor
	 * 
	 * @param color
	 *            Color
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void glClearColor(Color color) {
		GL11.glClearColor(color.getRed(), color.getGreen(), color.getBlue(),
				color.getAlpha());
	}

	/**
	 * glClearColor
	 * 
	 * @param r
	 *            Red Value
	 * @param g
	 *            Green Value
	 * @param b
	 *            Blue Value
	 * @param a
	 *            Alpha Value
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void glClearColor(float r, float g, float b, float a) {
		GL11.glClearColor(r, g, b, a);
		VoxelGLError.check();
	}

	/**
	 * glClear
	 * 
	 * @param buffers
	 *            OpenGL Buffers
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void glClear(int buffers) {
		GL11.glClear(buffers);
		VoxelGLError.check();
	}

	/**
	 * glDrawArrays
	 * 
	 * @param mode
	 *            Mode of Rendering
	 * @param offset
	 *            Offset
	 * @param vertexCount
	 *            Vertex
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void glDrawArrays(int mode, int offset, int vertexCount) {
		GL11.glDrawArrays(mode, offset, vertexCount);
		VoxelGLError.check();

		Kernel.renderCalls++;
	}

	/**
	 * glDrawElements
	 * 
	 * @param mode
	 *            Mode of Rendering
	 * @param vertexCount
	 *            Vertex
	 * @param type
	 *            Type of Rendering
	 * @param offset
	 *            Offset
	 */
	public static void glDrawElements(int mode, int vertexCount, int type,
			int offset) {
		GL11.glDrawElements(mode, vertexCount, type, offset);
		VoxelGLError.check();

		Kernel.renderCalls++;
	}

	/**
	 * glViewport
	 * 
	 * @param rect
	 *            Rectangle
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void glViewport(Rectangle rect) {
		glViewport(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	/**
	 * glViewPort
	 * 
	 * @param x
	 *            X Position
	 * @param y
	 *            Y Position
	 * @param width
	 *            Width
	 * @param height
	 *            Height
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void glViewport(float x, float y, float width, float height) {
		GL11.glViewport((int) x, (int) y, (int) width, (int) height);
		VoxelGLError.check();
	}

	/**
	 * glDepthMask
	 * 
	 * @param value
	 *            Boolean Flag
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void glDepthMask(boolean value) {
		GL11.glDepthMask(value);
		VoxelGLError.check();
	}

	/**
	 * glDepthFunc
	 * 
	 * @param func
	 *            Type
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void glDepthFunc(int func) {
		GL11.glDepthFunc(func);
		VoxelGLError.check();
	}

	/**
	 * glCullFace
	 * 
	 * @param mode
	 *            Type
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void glCullFace(int mode) {
		GL11.glCullFace(mode);
		VoxelGLError.check();
	}

	/**
	 * glColorMask
	 * 
	 * @param red
	 *            Red Color
	 * @param green
	 *            Green Color
	 * @param blue
	 *            Blue Color
	 * @param alpha
	 *            Alpha Color
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void glColorMask(boolean red, boolean green, boolean blue,
			boolean alpha) {
		GL11.glColorMask(red, green, blue, alpha);
		VoxelGLError.check();
	}
}