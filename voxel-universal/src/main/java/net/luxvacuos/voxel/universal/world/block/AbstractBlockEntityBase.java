/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.voxel.universal.material.BlockMaterial;
import net.luxvacuos.voxel.universal.tools.ToolTier;
import net.luxvacuos.voxel.universal.world.chunk.IChunk;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.BlockNode;

public abstract class AbstractBlockEntityBase extends Entity implements IBlockEntity {
	private int x, y, z;
	protected final BlockMaterial material;
	private BoundingBox aabb = new BoundingBox(new Vector3(0, 0, 0), new Vector3(1, 1, 1));
	private int id;
	private int metadata;
	protected TagCompound complexMetadata = null;
	private IChunk chunk = null;
	
	protected AbstractBlockEntityBase(BlockMaterial material) {
		this.material = material;
	}
	
	protected AbstractBlockEntityBase(BlockMaterial material, BoundingBox aabb) {
		this.material = material;
		this.aabb = aabb;
	}

	@Override
	public int getID() {
		return this.id;
	}
	
	AbstractBlockEntityBase setID(int id) {
		this.id = id;
		return this;
	}

	@Override
	public String getName() {
		return this.material.getName();
	}

	@Override
	public BoundingBox getBoundingBox(BlockNode node) {
		return new BoundingBox(node.asVector3().add(this.aabb.min), node.asVector3().add(this.aabb.max));
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
		return false;
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
	public ToolTier getToolTier() {
		return this.material.getTierNeeded();
	}
	
	@Override
	public TagCompound getComplexMetaData() {
		if(this.complexMetadata == null)
			this.complexMetadata = new CompoundBuilder().start().build();
		
		return this.complexMetadata;
	}
	
	@Override
	public void setComplexMetadata(TagCompound metadata) {
		this.complexMetadata = metadata;
	}
	
	@Override
	public void onBlockUpdate(BlockNode node, IBlock replaced) {
		//Event Handler for block updates
	}

	@Override
	public void setChunk(IChunk chunk) {
		if(this.chunk == null)
			this.chunk = chunk;
	}

	@Override
	public IChunk getChunk() {
		return this.chunk;
	}

	@Override
	public IDimension getDimension() {
		if(this.chunk != null)
			return this.chunk.getDimension();
		
		return null;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public int getZ() {
		return this.z;
	}

	@Override
	public void setPosition(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void setPosition(BlockNode node) {
		this.x = node.getX();
		this.y = node.getY();
		this.z = node.getZ();
	}

	@Override
	public IBlockHandle getHandle() {
		return new BlockHandle(this);
	}

}
