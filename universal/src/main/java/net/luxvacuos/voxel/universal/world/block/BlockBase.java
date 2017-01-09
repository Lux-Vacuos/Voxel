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

package net.luxvacuos.voxel.universal.world.block;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.voxel.universal.material.BlockMaterial;
import net.luxvacuos.voxel.universal.world.utils.BlockCoords;

public class BlockBase implements IBlock {
	protected final BlockMaterial material;
	private BoundingBox aabb = new BoundingBox(new Vector3(0, 0, 0), new Vector3(1, 1, 1));
	private int id;
	private int metadata = 0;
	protected TagCompound complexMetadata = null;

	public BlockBase(BlockMaterial material) {
		this.material = material;
	}

	public BlockBase(BlockMaterial material, BoundingBox aabb) {
		this.material = material;
		this.aabb = aabb;
	}

	@Override
	public int getID() {
		return this.id;
	}

	public BlockBase setID(int id) {
		this.id = id;
		return this;
	}

	@Override
	public String getName() {
		return this.material.getName();
	}

	@Override
	public BoundingBox getBoundingBox(BlockCoords pos) {
		return new BoundingBox(new Vector3(pos.getX() + aabb.min.x, pos.getY() + aabb.min.y, pos.getZ() + aabb.min.z),
				new Vector3(pos.getX() + aabb.max.x, pos.getY() + aabb.max.y, pos.getZ() + aabb.max.z));
	}

	@Override
	public boolean isAffectedByGravity() {
		return this.material.affectedByGravity();
	}

	@Override
	public boolean hasCollision() {
		return this.material.blocksMovement();
	}

	@Override
	public boolean isFluid() {
		return this.material.isLiquid();
	}

	@Override
	public boolean hasComplexMetadata() {
		return (this.complexMetadata != null);
	}

	@Override
	public int getPackedMetadata() {
		return this.metadata;
	}

	@Override
	public void setPackedMetadata(int packedMetadata) {
		this.metadata = packedMetadata;

	}

	@Override
	public void setComplexMetadata(TagCompound metadata) {
		this.complexMetadata = metadata;
	}

}
