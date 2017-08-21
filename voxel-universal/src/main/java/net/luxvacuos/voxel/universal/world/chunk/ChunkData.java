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

import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.utils.BlockLongDataArray;

public final class ChunkData {

	private short[] heightmap; // [(Z * WIDTH) + X]
	ChunkSlice[] slices;
	private boolean needsRebuild, fullRebuild;
	private TagCompound blockMetadata;

	protected ChunkData() {
		this.needsRebuild = false;
		this.fullRebuild = false;
		this.heightmap = new short[256]; // 16 * 16
		this.slices = new ChunkSlice[16];
		this.blockMetadata = new TagCompound("BlockMetadata");

		for (int i = 0; i < this.slices.length; i++)
			this.slices[i] = new ChunkSlice((byte) i);
	}

	public int getBlockIDAt(int x, int y, int z) {
		return this.slices[this.getSlice(y)].getBlockIDAt(x, this.modY(y), z);
	}

	public int getBlockMetadataAt(int x, int y, int z) {
		return this.slices[this.getSlice(y)].getBlockMetadataAt(x, this.modY(y), z);
	}

	public boolean hasComplexMetadataAt(int x, int y, int z) {
		return this.blockMetadata.hasTagByName("Metadata_" + x + "_" + y + "_" + z);
	}

	public CompoundBuilder generateMetadataBuilderFor(int x, int y, int z) {
		return new CompoundBuilder().start("Metadata_" + x + "_" + y + "_" + z);
	}

	public TagCompound getComplexMetadataAt(int x, int y, int z) {
		if (this.hasComplexMetadataAt(x, y, z)) {
			try {
				return this.blockMetadata.getTag("Metadata_" + x + "_" + y + "_" + z, TagCompound.class);
			} catch (NBTException e) {
				Logger.error(e);
				return this.generateMetadataBuilderFor(x, y, z).build();
			}
		} else {
			return this.generateMetadataBuilderFor(x, y, z).build();
		}
	}

	public void removeComplexMetadataAt(int x, int y, int z) {
		if (this.blockMetadata.hasTagByName("Metadata_" + x + "_" + y + "_" + z)) {
			try {
				this.blockMetadata.removeTag("Metadata_" + x + "_" + y + "_" + z);
			} catch (NBTException e) {
				Logger.error(e);
			}
		}
	}

	public void saveComplexMetadata(TagCompound compound) {
		try {
			if (this.blockMetadata.hasTagByName(compound.getName()))
				this.blockMetadata.removeTag(compound);

			this.blockMetadata.addTag(compound);
		} catch (NBTException e) {
			Logger.error(e);
		}
	}

	public TagCompound getComplexBlockMetadata() {
		return this.blockMetadata;
	}

	protected void setComplexBlockMetadata(TagCompound data) {
		this.blockMetadata = data;
	}

	public IBlock getBlockAt(int x, int y, int z) {
		return this.slices[this.getSlice(y)].getBlockAt(x, this.modY(y), z);
	}

	protected void setBlockAt(int x, int y, int z, IBlock block) {
		this.needsRebuild = true;
		this.slices[this.getSlice(y)].setBlockAt(x, this.modY(y), z, block);
	}

	public boolean isBlockAir(int x, int y, int z) {
		if (this.heightmap[x * z] < y)
			return true;
		return this.slices[this.getSlice(y)].isBlockAir(x, this.modY(y), z);
	}

	public boolean hasCollisionData(int x, int y, int z) {
		return this.slices[this.getSlice(y)].hasCollisionData(x, this.modY(y), z);
	}

	public final BlockLongDataArray[] getBlockDataArrays() {
		BlockLongDataArray[] array = new BlockLongDataArray[this.slices.length];

		for (int i = 0; i < this.slices.length; i++)
			array[i] = this.slices[i].getBlockDataArray();

		return array;
	}

	protected void setBlockDataArrays(BlockLongDataArray[] arrays) {
		if (this.slices.length != arrays.length)
			return;

		for (int i = 0; i < this.slices.length; i++)
			this.slices[i].setBlockDataArray(arrays[i]);
	}

	public final ImmutableArray<ChunkSlice> getChunkSlices() {
		Array<ChunkSlice> array = new Array<ChunkSlice>();

		for (ChunkSlice slice : this.slices)
			array.add(slice);

		return new ImmutableArray<ChunkSlice>(array);
	}

	public boolean needsRebuild() {
		return this.needsRebuild || this.fullRebuild;
	}

	protected void markFullRebuild() {
		this.fullRebuild = true;
		for (ChunkSlice slice : this.slices)
			slice.markBlockRebuild();
	}

	protected void rebuild() {
		boolean rebuildHeightMap = false;
		for (ChunkSlice slice : this.slices) {
			if (this.fullRebuild || slice.needsBlockRebuild()) {
				if (slice.inHeightMap() && !rebuildHeightMap)
					rebuildHeightMap = true;
				slice.rebuildBlocks(this.fullRebuild);
			}
		}
		if (rebuildHeightMap)
			this.buildHeightMap();

		if (this.fullRebuild)
			this.fullRebuild = false;
	}

	private void buildHeightMap() {
		short maxY = ((short) ((this.slices.length << 4) - 1));
		short currentY;
		short lowestY = maxY;

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				short test = this.heightmap[(z * 16) + x];

				if (test != 0) {
					currentY = ((short) (test + 10));
					while (!this.slices[this.getSlice(currentY)].isBlockAir(x, this.modY(currentY), z)) {
						currentY += 10;
						if (currentY >= maxY) {
							currentY = maxY;
							break;
						}
					}
				} else
					currentY = maxY;

				while (this.slices[this.getSlice(currentY)].isBlockAir(x, this.modY(currentY), z))
					currentY--;

				this.heightmap[(z * 16) + x] = currentY;

				if (currentY < lowestY)
					lowestY = currentY;
			}
		}

		int lastSliceIndex = 0, sliceIndex = 0;
		for (short y = maxY; y > lowestY; y--) {
			sliceIndex = this.getSlice(y);
			if (lastSliceIndex == sliceIndex)
				continue;

			lastSliceIndex = sliceIndex;
			this.slices[sliceIndex].markInHeightMap();
		}
	}

	protected boolean shouldGenerate() {
		for (ChunkSlice slice : this.slices) {
			if (slice.needsBlockRebuild())
				slice.rebuildBlocks(this.fullRebuild);
			if (!slice.isEmpty())
				return false;
		}

		return true;
	}

	private int modY(int y) {
		return (y & 0x0F);
	}

	private int getSlice(int y) {
		return y >> 4;
	}
}
