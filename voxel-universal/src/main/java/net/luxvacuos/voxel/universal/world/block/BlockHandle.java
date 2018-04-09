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

import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.voxel.universal.tools.ToolTier;
import net.luxvacuos.voxel.universal.world.chunk.IChunkHandle;
import net.luxvacuos.voxel.universal.world.dimension.IDimensionHandle;

public class BlockHandle implements IBlockHandle {
	
	private IBlock parentBlock;

	BlockHandle(IBlock block) {
		this.parentBlock = block;
	}

	@Override
	public int getID() {
		return this.parentBlock.getID();
	}

	@Override
	public String getName() {
		return this.parentBlock.getName();
	}

	@Override
	public int getX() {
		return this.parentBlock.getX();
	}

	@Override
	public int getY() {
		return this.parentBlock.getY();
	}

	@Override
	public int getZ() {
		return this.parentBlock.getZ();
	}

	@Override
	public boolean isAffectedByGravity() {
		return this.parentBlock.isAffectedByGravity();
	}

	@Override
	public boolean isFluid() {
		return this.parentBlock.isFluid();
	}

	@Override
	public boolean hasComplexMetadata() {
		return this.parentBlock.hasComplexMetadata();
	}

	@Override
	public TagCompound getComplexMetaData() {
		return this.parentBlock.getComplexMetaData();
	}

	@Override
	public ToolTier getToolTier() {
		return this.parentBlock.getToolTier();
	}

	@Override
	public IChunkHandle getChunk() {
		return this.parentBlock.getChunk().getHandle();
	}

	@Override
	public IDimensionHandle getDimension() {
		return this.parentBlock.getDimension().getHandle();
	}

}
