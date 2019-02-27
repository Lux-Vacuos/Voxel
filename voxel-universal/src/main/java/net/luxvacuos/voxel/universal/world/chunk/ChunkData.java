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

import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.voxel.universal.world.block.IBlock;

public class ChunkData {

	private boolean needsRebuild;

	private TagCompound blockEntityData;

	private IBlock[] blocks;

	private boolean isEmpty = true;

	protected ChunkData() {
		blocks = new IBlock[4096];
		blockEntityData = new TagCompound("BlockEntityData");
	}

	public void rebuild() {

	}

	public IBlock get(int x, int y, int z) {
		return blocks[(16 * 16 * z) + (16 * y) + x];
	}

	public void set(int x, int y, int z, IBlock block) {
		blocks[(16 * 16 * z) + (16 * y) + x] = block;
	}

	public void set(int i, IBlock block) {
		int z = i / (16 * 16);
		i -= (z * 16 * 16);
		int y = i / 16;
		int x = i % 16;
		set(x, y, z, block);
	}

	public IBlock[] getBlocks() {
		return blocks;
	}

	public void setBlockEntityData(TagCompound data) {
		blockEntityData = data;
	}

	public TagCompound getBlockEntityData() {
		return blockEntityData;
	}

	public void markForRebuild() {
		needsRebuild = true;
	}

	public boolean needsRebuild() {
		return needsRebuild;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

}
