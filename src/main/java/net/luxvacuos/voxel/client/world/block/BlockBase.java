/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Lux Vacuos
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

package net.luxvacuos.voxel.client.world.block;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.models.WaterTile;
import net.luxvacuos.voxel.client.world.items.ItemDropBase;
import net.luxvacuos.voxel.client.world.items.ItemDropMissing;
import net.luxvacuos.voxel.universal.util.vector.Vector3f;
import net.luxvacuos.voxel.universal.util.vector.Vector8f;

public abstract class BlockBase {
	@Deprecated
	protected boolean usesSingleModel = false;
	protected boolean transparent = false;
	protected boolean customModel = false;

	public abstract byte getId();

	public Vector8f texCoordsUp() {
		return null;
	}

	public Vector8f texCoordsDown() {
		return null;
	}

	public Vector8f texCoordsFront() {
		return null;
	}

	public Vector8f texCoordsBack() {
		return null;
	}

	public Vector8f texCoordsRight() {
		return null;
	}

	public Vector8f texCoordsLeft() {
		return null;
	}

	public BoundingBox getBoundingBox(Vector3f pos) {
		return new BoundingBox(new Vector3(pos.x, pos.y, pos.z), new Vector3(pos.x + 1, pos.y + 1, pos.z + 1));
	}

	public ItemDropBase getDrop(GameResources gm, Vector3f pos) {
		return new ItemDropMissing(BlocksResources.missingDrop, pos, this, 0, 0, 0, 0.2f);
	}

	/**
	 * Get the WaterTile of the Block
	 * 
	 * @param pos
	 *            Position
	 * @return WaterTile
	 */
	public WaterTile getWaterTitle(Vector3f pos) {
		return null;
	}

	/**
	 * Get a single model
	 * 
	 * @param pos
	 *            Position
	 * @return BlockEntity
	 * @deprecated
	 */
	public BlockEntity getSingleModel(Vector3f pos) {
		return null;
	}

	public void generateCustomModel(Tessellator tess, float x, float y, float z, boolean top, boolean bottom,
			boolean left, boolean right, boolean front, boolean back, float light) {
	}

	/**
	 * Check if uses single model
	 * 
	 * @return Uses single model
	 * @deprecated
	 */
	public boolean usesSingleModel() {
		return usesSingleModel;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public boolean isCustomModel() {
		return customModel;
	}
}
