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

package io.github.guerra24.voxel.client.kernel.core;

/**
 * Interface for Kernel
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.1 Build-52
 * @since 0.0.1 Build-52
 * @category Kernel
 */
public interface IKernel {

	/**
	 * Initialize the Kernel
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void init();

	/**
	 * The Main Loop, contains the render, update and error loops
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void mainLoop();

	/**
	 * Render Loop, contains all the render functions
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void render();

	/**
	 * Update Loop, contains all the update functions
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void update();

	/**
	 * Error Detection Loop, check the game errors
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void error();

	/**
	 * Dispose Kernel, disposes all resources loaded
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void dispose();
}
