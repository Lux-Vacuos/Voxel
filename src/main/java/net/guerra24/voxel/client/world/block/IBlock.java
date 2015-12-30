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

package net.guerra24.voxel.client.world.block;

import com.badlogic.gdx.math.collision.BoundingBox;

import net.guerra24.voxel.client.resources.models.WaterTile;
import net.guerra24.voxel.universal.util.vector.Vector3f;
import net.guerra24.voxel.universal.util.vector.Vector8f;

public abstract class IBlock {

	public abstract byte getId();

	public abstract Vector8f texCoordsUp();

	public abstract Vector8f texCoordsDown();

	public abstract Vector8f texCoordsFront();

	public abstract Vector8f texCoordsBack();

	public abstract Vector8f texCoordsRight();

	public abstract Vector8f texCoordsLeft();
	
	public abstract BoundingBox getBoundingBox(Vector3f pos);
	
	/**
	 * Get the WaterTile of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return WaterTile
	 */
	public abstract WaterTile getWaterTitle(Vector3f pos);

	/**
	 * Get a single model
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 */
	public abstract BlockEntity getSingleModel(Vector3f pos);

	/**
	 * Check if uses single model
	 * 
	 * @return Uses single model
	 */
	public abstract boolean usesSingleModel();
}
