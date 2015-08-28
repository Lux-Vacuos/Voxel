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

package io.github.guerra24.voxel.client.kernel.resources.models;

import org.lwjglx.util.vector.Vector2f;

/**
 * Gui Texture
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.3 Build-59
 * @since 0.0.1 Build-52
 * @category Assets
 */
public class GuiTexture {
	/**
	 * Texture ID
	 */
	private int texture;
	/**
	 * Position
	 */
	private Vector2f position;
	/**
	 * Scale
	 */
	private Vector2f scale;

	/**
	 * Constructor, Create a Gui Texture
	 * 
	 * @param texture
	 *            Texture ID
	 * @param position
	 *            Position
	 * @param scale
	 *            Scale
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}

	/**
	 * Get Texture ID
	 * 
	 * @return Texture ID
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public int getTexture() {
		return texture;
	}

	/**
	 * Get Position
	 * 
	 * @return Position
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public Vector2f getPosition() {
		return position;
	}

	/**
	 * Get Scale
	 * 
	 * @return Scale
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public Vector2f getScale() {
		return scale;
	}

	/**
	 * Set Position
	 * 
	 * @param position
	 *            Position
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void setPosition(Vector2f position) {
		this.position = position;
	}

	/**
	 * Set Scale
	 * 
	 * @param position
	 *            Position
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

}
