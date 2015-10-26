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

package net.guerra24.voxel.resources.models;

import java.nio.ByteBuffer;

/**
 * Entity Texture
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class EntityTexture {
	/**
	 * Texture Width
	 */
	private int width;
	/**
	 * Texture Height
	 */
	private int height;
	/**
	 * ByteBuffer
	 */
	private ByteBuffer buffer;

	/**
	 * Constructor, Create an Entity Texutre
	 * 
	 * @param buffer
	 *            ByteBuffer
	 * @param width
	 *            Texture Width
	 * @param height
	 *            Texture Height
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public EntityTexture(ByteBuffer buffer, int width, int height) {
		this.buffer = buffer;
		this.width = width;
		this.height = height;
	}

	/**
	 * Get the Texture Width
	 * 
	 * @return Texture Width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the Texture Height
	 * 
	 * @return Texture Height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get Texture ByteBuffer
	 * 
	 * @return ByteBuffer
	 */
	public ByteBuffer getBuffer() {
		return buffer;
	}
}
