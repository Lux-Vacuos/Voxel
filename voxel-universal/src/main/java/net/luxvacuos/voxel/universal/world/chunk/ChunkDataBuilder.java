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
import com.hackhalo2.nbt.exceptions.NBTTagNotFoundException;
import com.hackhalo2.nbt.exceptions.UnexpectedTagTypeException;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.voxel.universal.world.block.BlockBase;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.utils.BlockNode;

public class ChunkDataBuilder {

	private ChunkData data = new ChunkData();

	public ChunkDataBuilder() {
	}

	public ChunkDataBuilder setEmpty(boolean val) {
		data.setEmpty(val);
		return this;
	}

	public ChunkDataBuilder setBlockEntityData(TagCompound data) {
		this.data.setBlockEntityData(data);
		return this;
	}

	public ChunkDataBuilder setBlocks(TagCompound blocks)
			throws UnexpectedTagTypeException, NBTTagNotFoundException, NBTException {
		for (int i = 0; i < 4096; ++i) {
			TagCompound blockT = blocks.getCompound(Integer.toString(i));
			int z = i / (16 * 16);
			i -= (z * 16 * 16);
			int y = i / 16;
			int x = i % 16;
			IBlock block = new BlockBase(new BlockNode(x, y, z), blockT.getString("Name"));
			block.setMetadata(blockT.getInt("Metadata"));
			data.set(i, block);
		}
		return this;
	}

	public ChunkData build() {
		return data;
	}

}
