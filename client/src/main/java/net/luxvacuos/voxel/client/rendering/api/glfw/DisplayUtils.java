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

import static org.lwjgl.opengl.GL11.GL_INVALID_ENUM;
import static org.lwjgl.opengl.GL11.GL_INVALID_OPERATION;
import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL11.GL_OUT_OF_MEMORY;
import static org.lwjgl.opengl.GL11.GL_STACK_OVERFLOW;
import static org.lwjgl.opengl.GL11.GL_STACK_UNDERFLOW;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION;
import static org.lwjgl.opengl.GL45.GL_CONTEXT_LOST;

import net.luxvacuos.voxel.client.core.exception.OpenGLException;

public class DisplayUtils {

	private Sync sync;

	public DisplayUtils() {
		String osName = System.getProperty("os.name");

		if (osName.startsWith("Win")) {
			Thread timerAccuracyThread = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(Long.MAX_VALUE);
					} catch (Exception e) {
					}
				}
			});

			timerAccuracyThread.setName("LWJGL Timer");
			timerAccuracyThread.setDaemon(true);
			timerAccuracyThread.start();
		}
		sync = new Sync();
	}

	public void checkErrors() {
		/*
		switch (glGetError()) {
		case GL_INVALID_ENUM:
			throw new OpenGLException("GL_INVALID_ENUM");
		case GL_INVALID_VALUE:
			throw new OpenGLException("GL_INVALID_VALUE");
		case GL_INVALID_OPERATION:
			throw new OpenGLException("GL_INVALID_OPERATION");
		case GL_STACK_OVERFLOW:
			throw new OpenGLException("GL_STACK_OVERFLOW");
		case GL_STACK_UNDERFLOW:
			throw new OpenGLException("GL_STACK_UNDERFLOW");
		case GL_OUT_OF_MEMORY:
			throw new OpenGLException("GL_OUT_OF_MEMORY");
		case GL_INVALID_FRAMEBUFFER_OPERATION:
			throw new OpenGLException("GL_INVALID_FRAMEBUFFER_OPERATION");
		case GL_CONTEXT_LOST:
			throw new OpenGLException("GL_CONTEXT_LOST");
		}
*/
	}

	public void sync(int fps) {
		sync.sync(fps);
	}

}
