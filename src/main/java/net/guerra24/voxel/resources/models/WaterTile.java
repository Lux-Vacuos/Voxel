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

/**
 * Water Tile
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class WaterTile {

	/**
	 * Tile Size
	 */
	public static final float TILE_SIZE = 0.5f;
	/**
	 * Y Coord of the Tile
	 */
	private float height;
	/**
	 * X and Z Coords
	 */
	private float x, z;

	/**
	 * Constructor, Create a Water Tile
	 * 
	 * @param centerX
	 *            X Coord
	 * @param centerZ
	 *            Z Coord
	 * @param height
	 *            Y Coord
	 */
	public WaterTile(float centerX, float centerZ, float height) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
	}

	/**
	 * Get Y Coord
	 * 
	 * @return Y Coord
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Get X Coord
	 * 
	 * @return Z Coord
	 */
	public float getX() {
		return x;
	}

	/**
	 * Get Z Coord
	 * 
	 * @return Z Coord
	 */
	public float getZ() {
		return z;
	}

	public static float getTileSize() {
		return TILE_SIZE;
	}

}
