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

package net.luxvacuos.voxel.universal.world.chunk;

import net.luxvacuos.voxel.universal.world.block.IBlockHandle;
import net.luxvacuos.voxel.universal.world.dimension.IDimensionHandle;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public final class ChunkHandle implements IChunkHandle {
	
	private IChunk parentChunk;

	ChunkHandle(IChunk chunk) {
		this.parentChunk = chunk;
	}

	@Override
	public ChunkNode getNode() {
		return this.parentChunk.getNode();
	}

	@Override
	public int getX() {
		return this.parentChunk.getX();
	}

	@Override
	public int getZ() {
		return this.parentChunk.getZ();
	}

	@Override
	public IBlockHandle getBlockAt(int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDimensionHandle getDimension() {
		return this.parentChunk.getDimension().getHandle();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
