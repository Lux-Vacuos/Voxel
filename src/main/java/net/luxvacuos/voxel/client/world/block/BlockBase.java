/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
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
