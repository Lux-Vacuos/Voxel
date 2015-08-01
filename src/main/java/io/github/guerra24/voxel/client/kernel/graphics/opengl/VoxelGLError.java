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

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public final class VoxelGLError {
	private VoxelGLError() {
	}

	public static void check() {
		check(false);
	}

	public static void check(boolean force) {

		switch (glGetError()) {
		case GL_NO_ERROR:
			break;
		case GL_INVALID_ENUM:
			new VoxelGLException.InvalidEnum();
		case GL_INVALID_VALUE:
			new VoxelGLException.InvalidValue();
		case GL_INVALID_OPERATION:
			new VoxelGLException.InvalidOperation();
		case GL_INVALID_FRAMEBUFFER_OPERATION:
			new VoxelGLException.InvalidFramebufferOperation();
		case GL_OUT_OF_MEMORY:
			new VoxelGLException.OutOfMemory();
		case GL_STACK_UNDERFLOW:
			new VoxelGLException.StackUnderflow();
		case GL_STACK_OVERFLOW:
			new VoxelGLException.StackOverflow();
		}
	}

	public static Value get() {
		switch (glGetError()) {
		case GL_INVALID_ENUM:
			return Value.INVALID_ENUM;
		case GL_INVALID_VALUE:
			return Value.INVALID_VALUE;
		case GL_INVALID_OPERATION:
			return Value.INVALID_OPERATION;
		case GL_INVALID_FRAMEBUFFER_OPERATION:
			return Value.INVALID_FRAMEBUFFER_OPERATION;
		case GL_OUT_OF_MEMORY:
			return Value.OUT_OF_MEMORY;
		case GL_STACK_UNDERFLOW:
			return Value.STACK_UNDERFLOW;
		case GL_STACK_OVERFLOW:
			return Value.STACK_OVERFLOW;
		}

		return Value.NO_ERROR;
	}

	public static enum Value {
		NO_ERROR, INVALID_ENUM, INVALID_VALUE, INVALID_OPERATION, INVALID_FRAMEBUFFER_OPERATION, OUT_OF_MEMORY, STACK_UNDERFLOW, STACK_OVERFLOW
	}
}