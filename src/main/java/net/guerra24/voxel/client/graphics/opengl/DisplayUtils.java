/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
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

import static org.lwjgl.opengl.GL11.GL_INVALID_ENUM;
import static org.lwjgl.opengl.GL11.GL_INVALID_OPERATION;
import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_OUT_OF_MEMORY;
import static org.lwjgl.opengl.GL11.GL_STACK_OVERFLOW;
import static org.lwjgl.opengl.GL11.GL_STACK_UNDERFLOW;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;

public class DisplayUtils {

	private long variableYieldTime, lastTime;

	public ByteBuffer loadIcon(String path) throws IOException {
		InputStream file = getClass().getClassLoader().getResourceAsStream(path);
		PNGDecoder decoder = new PNGDecoder(file);
		ByteBuffer bytebuf = ByteBuffer.allocateDirect(decoder.getWidth() * decoder.getHeight() * 4);
		decoder.decode(bytebuf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
		bytebuf.flip();
		return bytebuf;
	}

	public void checkErrors() {
		switch (glGetError()) {
		case GL_NO_ERROR:
			break;
		case GL_INVALID_ENUM:
			throw new RuntimeException("OpenGL: Invalid Enum");
		case GL_INVALID_VALUE:
			throw new RuntimeException("OpenGL: Invalid Value");
		case GL_INVALID_OPERATION:
			throw new RuntimeException("OpenGL: Invalid Operation");
		case GL_INVALID_FRAMEBUFFER_OPERATION:
			throw new RuntimeException("OpenGL: Invalid FrameBuffer Operation");
		case GL_OUT_OF_MEMORY:
			throw new RuntimeException("OpenGL: Out of Memory");
		case GL_STACK_UNDERFLOW:
			throw new RuntimeException("OpenGL: Underflow");
		case GL_STACK_OVERFLOW:
			throw new RuntimeException("OpenGL: Overflow");
		}
	}

	public void sync(int fps) {
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
