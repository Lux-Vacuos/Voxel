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

package net.luxvacuos.voxel.universal.world.chunk;

import net.luxvacuos.voxel.universal.world.utils.BlockDataArray;

public final class ChunkSlice {

	private static final int BLOCK_LIGHT_MASK = 0x000000FF;
	private static final int SKY_LIGHT_MASK = 0x0000FF00;

	private BlockDataArray blockData;

	/**
	 * This light data array has both the sky light data and block light data packed in it:<br />
	 * 0x0000SSBB<br />
	 * The <i>S</i> bits is the skylight data<br />
	 * The <i>B</i> bits is blocklight data<br />
	 * <br />
	 * The functions in ChunkData automatically pack and unpack the data, so it should be
	 * transparent to whatever is using it
	 */
	private BlockDataArray lightData;

	@SuppressWarnings("unused")
	private byte yOffset; //Used when saving the data to disk, so it can be put back in the right place

	private short numBlocks, numSkyLight, numBlockLight;
	
	private boolean blockRebuild, lightRebuild;

	protected ChunkSlice() {
		this.numBlocks = 0;
		this.numSkyLight = 0;
		this.numBlockLight = 0;
		this.blockData = new BlockDataArray();
		this.lightData = new BlockDataArray();
	}

	public int getBlockAt(int x, int y, int z) {
		if(this.isEmpty()) return 0;
		return this.blockData.get(x, y, z);
	}

	public void setBlockAt(int x, int y, int z, int data) {
		this.numBlocks++;
		this.blockData.set(x, y, z, data);
	}

	public boolean isBlockAir(int x, int y, int z) {
		if(this.isEmpty()) return true;
		return this.blockData.get(x, y, z) == 0;
	}

	public int getSkyLightAt(int x, int y, int z) {
		if(!this.hasSkyLight()) return 0;
		return ((this.lightData.get(x, y, z) & SKY_LIGHT_MASK) >> 8);
	}

	public void setSkyLightAt(int x, int y, int z, int value) {
		if(!this.hasSkyLight()) this.numSkyLight++;

		int i = ((value & BLOCK_LIGHT_MASK) << 8);
		int j = (this.lightData.get(x, y, z) & ~SKY_LIGHT_MASK); //0xFFFF00FF
		this.lightData.set(x, y, z, (i + j));
	}

	public int getBlockLightAt(int x, int y, int z) {
		if(!this.hasBlockLight()) return 0;
		return (this.lightData.get(x, y, z) & BLOCK_LIGHT_MASK);
	}

	public void setBlockLightAt(int x, int y, int z, int value) {
		if(!this.hasBlockLight()) this.numBlockLight++;
		
		int i = (value & BLOCK_LIGHT_MASK);
		int j = (this.lightData.get(x, y, z) & ~BLOCK_LIGHT_MASK); //0xFFFFFF00
		this.lightData.set(x, y, z, (i + j));
	}
	
	/**
	 * Get's the <b>raw, unpacked</b> value from the lightmap
	 * @param x
	 * @param y
	 * @param z
	 * @return the raw lightmap value at the supplied x, y, and z coordinates
	 */
	public int getRawLightDataAt(int x, int y, int z) {
		return this.lightData.get(x, y, z);
	}

	public void setRawLightDataAt(int x, int y, int z, int rawValue) {
		this.lightData.set(x, y, z, rawValue);
	}

	/**
	 * Get's the ChunkSlice Light Data Array<br />
	 * <br />
	 * <b>WARNING:<br />
	 * The data in this array is packed, modifying it without knowing what you're doing <i>will</i><br />
	 * translate your documents into Swahilli, make your TV record "Gigli", neuter your pets, and give your laundry static cling.<br />
	 * YOU HAVE BEEN WARNED!</b>
	 * 
	 * @return The light data array
	 */
	public final BlockDataArray getLightDataArray() {
		return this.lightData;
	}

	public void setLightDataArray(BlockDataArray array) {
		this.lightData = array;
	}

	public final BlockDataArray getBlockDataArray() {
		return this.blockData;
	}

	public void setBlockDataArray(BlockDataArray array) {
		this.blockData = array;
	}

	public boolean isEmpty() {
		return this.numBlocks == 0;
	}

	protected boolean hasSkyLight() {
		return this.numSkyLight != 0;
	}
	
	protected boolean hasBlockLight() {
		return this.numBlockLight != 0;
	}

	protected ChunkSlice setOffset(byte offset) {
		this.yOffset = offset;
		return this;
	}

}
