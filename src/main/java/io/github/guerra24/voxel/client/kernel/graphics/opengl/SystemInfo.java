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

import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.glGetString;
import io.github.guerra24.voxel.client.kernel.util.Logger;

import org.lwjgl.opengl.Display;

/**
 * System Info
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.1 Build-52
 * @since 0.0.1 Build-52
 * @category OpenGL
 */
public class SystemInfo {
	/**
	 * Prints the GPU Info
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void printSystemInfo() {

		Logger.log(Thread.currentThread(), "Vendor: " + glGetString(GL_VENDOR));

		Logger.log(Thread.currentThread(), "Renderer: "
				+ glGetString(GL_RENDERER));

		if (Display.getVersion() != null) {
			Logger.log(Thread.currentThread(),
					"Driver Version: " + Display.getVersion());
		} else {
			Logger.warn(Thread.currentThread(),
					"Could not detect driver version");
		}

	}

}
