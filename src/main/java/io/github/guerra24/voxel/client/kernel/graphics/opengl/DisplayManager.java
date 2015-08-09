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

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;
import io.github.guerra24.voxel.client.kernel.core.Kernel;
import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.util.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * Display Manager
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.1 Build-52
 * @since 0.0.1 Build-1
 * @category OpenGL
 */
public class DisplayManager {

	/**
	 * Last Frame Time Value
	 */
	private static long lastFrameTime;
	/**
	 * Game Delta Value
	 */
	private static float delta;
	/**
	 * Display Pixel Format
	 */
	private static PixelFormat pixelformat = new PixelFormat();

	/**
	 * Creates and Sets the Display
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void createDisplay() {
		Logger.log(Thread.currentThread(), "Creating Display");
		try {
			Display.setDisplayMode(new DisplayMode(KernelConstants.WIDTH,
					KernelConstants.HEIGHT));
			Display.setTitle(KernelConstants.Title);
			Display.setResizable(true);
			Display.setFullscreen(false);
			Display.setVSyncEnabled(KernelConstants.VSYNC);
			Display.create(pixelformat);
		} catch (LWJGLException e) {
			Logger.error(Thread.currentThread(), "Failed to create Display");
			e.printStackTrace();
		}
		Logger.log(Thread.currentThread(), "LWJGL Version: " + Sys.getVersion());
		Logger.log(Thread.currentThread(), "OpenGL Version: "
				+ glGetString(GL_VERSION));
		VoxelGL33.glViewport(0, 0, KernelConstants.WIDTH,
				KernelConstants.HEIGHT);
		lastFrameTime = getCurrentTime();
	}

	/**
	 * Updates the Display
	 * 
	 * @param fps
	 *            Game Max FPS
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void updateDisplay(int fps) {
		Display.sync(fps);
		Display.update();
		if (Display.wasResized()) {
			VoxelGL33.glViewport(0, 0, Display.getWidth(), Display.getHeight());
			Kernel.gameResources.renderer.createProjectionMatrix();
		}
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}

	/**
	 * Destroy the display
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void closeDisplay() {
		Display.destroy();
	}

	/**
	 * Get the delta value
	 * 
	 * @return Delta Value
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static float getFrameTimeSeconds() {
		return delta;
	}

	/**
	 * Get the Current Time
	 * 
	 * @return Current Time
	 */
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
}
