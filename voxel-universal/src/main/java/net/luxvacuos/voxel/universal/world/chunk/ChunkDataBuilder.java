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

package net.luxvacuos.voxel.universal.world.chunk;

import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.voxel.universal.world.chunk.ChunkData;
import net.luxvacuos.voxel.universal.world.utils.BlockLongDataArray;

public final class ChunkDataBuilder {
	private ChunkData data = new ChunkData();

	public ChunkDataBuilder() { }

	public ChunkDataBuilder setSlice(int index, TagCompound data) throws NBTException {
		if(index >= 0 && index < this.data.slices.length) {
			ChunkSlice slice = new ChunkSlice(data.getByte("Offset"));
			if(data.getByte("Empty") == 0) {
				slice.setBlockDataArray(new BlockLongDataArray(data.getLongArray("BlockData")));
			}
			
			this.data.slices[index] = slice;
		}
		
		return this;
	}
	
	public ChunkDataBuilder newSlice(int index) {
		this.data.slices[index] = new ChunkSlice((byte)index);
		return this;
	}

	public ChunkDataBuilder setBlockMetadata(TagCompound data) {
		this.data.setComplexBlockMetadata(data);
		return this;
	}

	public ChunkData build() {
		this.data.markFullRebuild();
		return this.data;
	}
}
