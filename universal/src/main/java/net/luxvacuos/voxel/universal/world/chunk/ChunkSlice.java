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

	public final byte yOffset; //Used when saving the data to disk, so it can be put back in the right place

	private short numBlocks = 0, numSkyLight = 0, numBlockLight = 0;

	private boolean blockRebuild, skyLightRebuild, blockLightRebuild, inHeightMap;

	public ChunkSlice(byte offset) {
		this.yOffset = offset;
		this.blockRebuild = false;
		this.skyLightRebuild = false;
		this.blockLightRebuild = false;
		this.inHeightMap = false;
		this.blockData = new BlockDataArray();
		this.lightData = new BlockDataArray();
	}

	public ChunkSlice(byte offset, BlockDataArray blockData, BlockDataArray lightData) {
		this.yOffset = offset;
		this.blockRebuild = true;
		this.skyLightRebuild = true;
		this.blockLightRebuild = true;
		this.blockData = blockData;
		this.lightData = lightData;
	}

	public int getBlockAt(int x, int y, int z) {
		if(this.isEmpty()) return 0;
		return this.blockData.get(x, y, z);
	}

	public void setBlockAt(int x, int y, int z, int blockID) {
		this.blockRebuild = true;
		this.blockData.set(x, y, z, blockID);
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
		this.setSkyLightAt(x, y, z, value, true);
	}

	private void setSkyLightAt(int x, int y, int z, int value, boolean requireRebuild) {
		if(!this.skyLightRebuild && requireRebuild) this.skyLightRebuild = requireRebuild;

		int shift = ((value & BLOCK_LIGHT_MASK) << 8);
		int mask = (this.lightData.get(x, y, z) & ~SKY_LIGHT_MASK); //0xFFFF00FF
		this.lightData.set(x, y, z, (shift + mask));
	}

	public int getBlockLightAt(int x, int y, int z) {
		if(!this.hasBlockLight()) return 0;
		return (this.lightData.get(x, y, z) & BLOCK_LIGHT_MASK);
	}

	public void setBlockLightAt(int x, int y, int z, int value) {
		this.setBlockLightAt(x, y, z, value, true);
	}

	private void setBlockLightAt(int x, int y, int z, int value, boolean requireRebuild) {
		if(!this.blockLightRebuild && requireRebuild) this.blockLightRebuild = requireRebuild;

		int shift = (value & BLOCK_LIGHT_MASK);
		int mask = (this.lightData.get(x, y, z) & ~BLOCK_LIGHT_MASK); //0xFFFFFF00
		this.lightData.set(x, y, z, (shift + mask));
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
		this.skyLightRebuild = true;
		this.blockLightRebuild = true;
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
		this.skyLightRebuild = true;
		this.blockLightRebuild = true;
	}

	public final BlockDataArray getBlockDataArray() {
		return this.blockData;
	}

	public void setBlockDataArray(BlockDataArray array) {
		this.blockData = array;
		this.blockRebuild = true;
	}

	protected boolean needsBlockRebuild() {
		return this.blockRebuild;
	}

	protected boolean needsLightRebuild() {
		return this.skyLightRebuild || this.blockLightRebuild;
	}

	protected void wipeLightData(boolean blockLight, boolean skyLight) {
		int[] rawLightData = this.lightData.getData();
		int[] newLightData = new int[rawLightData.length];
		int value;

		for(int i = 0; i < rawLightData.length; i++) {
			value = rawLightData[i];
			if(blockLight) value = (value & ~BLOCK_LIGHT_MASK); //0xFFFFFF00
			if(skyLight) value = (value & ~SKY_LIGHT_MASK);     //0xFFFF00FF
			newLightData[i] = value;
		}

		this.lightData = new BlockDataArray(newLightData);
	}

	protected void rebuildBlocks() {
		if(this.blockRebuild) {
			this.blockRebuild = false;
			this.numBlocks = 0;
			int[] rawData = this.blockData.getData();

			for(int i = 0; i < rawData.length; i++) {
				if(rawData[i] != 0) this.numBlocks++;
			}
		}
	}

	protected void rebuildSkyLight(short[] heightmap, int skyLight) {
		if(this.skyLightRebuild) { //Only rebuild if needed
			this.numSkyLight = 0;

			for(int z = 0; z < 16; z++) {
				for(int x = 0; x < 16; x++) {
					int y = heightmap[(z * 16) + x];
					int offset = (y >> 4);

					if(this.yOffset > offset) y = 0;
					else if(this.yOffset == offset) y = (y & 0x0F);
					else continue;

					for(int height = 15; height >= y; height--) {
						this.setSkyLightAt(x, y, z, skyLight, false);
						this.numSkyLight++;
					}
				}
			}

			if(!this.hasSkyLight()) this.inHeightMap = false;
			this.skyLightRebuild = false;
		}
	}

	protected void rebuildBlockLight() {
		if(this.blockLightRebuild) {
			this.numBlockLight = 0;
			int[] rawLightData = this.lightData.getData();
			int value;

			for(int i = 0; i < rawLightData.length; i++) {
				value = (rawLightData[i] & BLOCK_LIGHT_MASK);
				if(value > 0) this.numBlockLight++;
			}

			this.blockLightRebuild = false;
		}
	}

	public boolean isEmpty() {
		return this.numBlocks == 0;
	}
	
	public boolean hasLightData() {
		return (this.numSkyLight > 0 || this.numBlockLight > 0);
	}

	protected void markInHeightMap() {
		this.inHeightMap = true;
	}

	protected boolean inHeightMap() {
		return this.inHeightMap;
	}

	protected boolean hasSkyLight() {
		return (this.numSkyLight != 0 && this.inHeightMap);
	}

	protected boolean hasBlockLight() {
		return this.numBlockLight != 0;
	}

}
