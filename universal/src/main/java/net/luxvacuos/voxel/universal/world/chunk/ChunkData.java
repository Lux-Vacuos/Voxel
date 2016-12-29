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

import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;

import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.utils.BlockIntDataArray;
import net.luxvacuos.voxel.universal.world.utils.BlockLongDataArray;

public final class ChunkData {

	private short[] heightmap; //[(Z * WIDTH) + X]
	ChunkSlice[] slices;
	private boolean needsRebuild, fullRebuild;
	private int skyLight;
	//TODO: Implement a way to store block metadata

	protected ChunkData() {
		this.needsRebuild = false;
		this.fullRebuild = false;
		this.skyLight = 0;
		this.heightmap = new short[256]; //16 * 16
		this.slices = new ChunkSlice[16];

		for(int i = 0; i < this.slices.length; i++) this.slices[i] = new ChunkSlice((byte)i);
	}

	public int getBlockIDAt(int x, int y, int z) {
		return this.slices[this.getSlice(y)].getBlockIDAt(x, this.modY(y), z);
	}
	
	public int getBlockMetadataAt(int x, int y, int z) {
		return this.slices[this.getSlice(y)].getBlockMetadataAt(x, this.modY(y), z);
	}
	
	public IBlock getBlockAt(int x, int y, int z) {
		return this.slices[this.getSlice(y)].getBlockAt(x, this.modY(y), z);
	}

	protected void setBlockAt(int x, int y, int z, IBlock block) {
		this.slices[this.getSlice(y)].setBlockAt(x, this.modY(y), z, block);
	}

	public boolean isBlockAir(int x, int y, int z) {
		if(this.heightmap[x * z] < y) return true;
		return this.slices[this.getSlice(y)].isBlockAir(x, this.modY(y), z);
	}
	
	public void setSkyLight(int value) {
		this.skyLight = value;
	}

	public int getSkyLightAt(int x, int y, int z) {
		ChunkSlice slice = this.slices[this.getSlice(y)];
		
		if(!slice.hasSkyLight()) return 0;
		return slice.getSkyLightAt(x, this.modY(y), z);
	}

	protected void setSkyLightAt(int x, int y, int z) {
		this.slices[this.getSlice(y)].setSkyLightAt(x, this.modY(y), z, this.skyLight);
	}

	public int getBlockLightAt(int x, int y, int z) {
		return this.slices[this.getSlice(y)].getBlockLightAt(x, this.modY(y), z);
	}

	protected void setBlockLightAt(int x, int y, int z, int value) {
		this.slices[this.getSlice(y)].setBlockLightAt(x, this.modY(y), z, value);
	}

	/**
	 * Get's the <b>raw, unpacked</b> value from the lightmap
	 * @param x
	 * @param y
	 * @param z
	 * @return the raw lightmap value at the supplied x, y, and z coordinates
	 */
	public int getRawLightDataAt(int x, int y, int z) {
		return this.slices[this.getSlice(y)].getRawLightDataAt(x, this.modY(y), z);
	}

	protected void setRawSkyLightAt(int x, int y, int z, int rawValue) {
		this.slices[this.getSlice(y)].setRawLightDataAt(x, this.modY(y), z, rawValue);
	}

	/**
	 * Get's the Chunks Light Data Array<br />
	 * <br />
	 * <b>WARNING:<br />
	 * The data in this array is packed, modifying it without knowing what you're doing <i>will</i><br />
	 * translate your documents into Swahilli, make your TV record "Gigli", neuter your pets, and give your laundry static cling.<br />
	 * YOU HAVE BEEN WARNED!</b>
	 * 
	 * @return The light data array
	 */
	public final BlockIntDataArray[] getLightDataArrays() {
		BlockIntDataArray[] array = new BlockIntDataArray[this.slices.length];

		for(int i = 0; i < this.slices.length; i++) array[i] = this.slices[i].getLightDataArray();

		return array;
	}

	protected void setLightDataArrays(BlockIntDataArray[] arrays) {
		if(this.slices.length != arrays.length) return;

		for(int i = 0; i < this.slices.length; i++) this.slices[i].setLightDataArray(arrays[i]);
	}

	public final BlockLongDataArray[] getBlockDataArrays() {
		BlockLongDataArray[] array = new BlockLongDataArray[this.slices.length];

		for(int i = 0; i < this.slices.length; i++) array[i] = this.slices[i].getBlockDataArray();

		return array;
	}

	protected void setBlockDataArrays(BlockLongDataArray[] arrays) {
		if(this.slices.length != arrays.length) return;

		for(int i = 0; i < this.slices.length; i++) this.slices[i].setBlockDataArray(arrays[i]);
	}
	
	public final ImmutableArray<ChunkSlice> getChunkSlices() {
		Array<ChunkSlice> array = new Array<ChunkSlice>();
		
		for(ChunkSlice slice : this.slices) array.add(slice);
		
		return new ImmutableArray<ChunkSlice>(array);
	}
	
	protected boolean needsRebuild() {
		return this.needsRebuild || this.fullRebuild;
	}
	
	protected void markFullRebuild() {
		this.fullRebuild = true;
	}
	
	protected void rebuild() {
		boolean rebuildHeightMap = false;
		for(ChunkSlice slice : this.slices) {
			if(this.fullRebuild || slice.needsBlockRebuild()) {
				if(slice.inHeightMap() && !rebuildHeightMap) rebuildHeightMap = true;
				slice.rebuildBlocks();
			}
		}
		if(rebuildHeightMap) this.buildHeightMap();
		
		for(ChunkSlice slice : this.slices) {
			if(this.fullRebuild || slice.needsLightRebuild()) {
				slice.wipeLightData(true, true);
				slice.rebuildSkyLight(this.heightmap, this.skyLight);
			}
		}
	}
	
	protected void rebuildBlockLight() {
		for(ChunkSlice slice : this.slices) {
			if(this.fullRebuild || slice.needsLightRebuild()) slice.rebuildBlockLight();
		}
		
		if(this.fullRebuild) this.fullRebuild = false;
	}
	
	private void buildHeightMap() {
		short maxY = ((short)((this.slices.length << 4) - 1));
		short currentY;
		short lowestY = maxY;
		
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				short test = this.heightmap[(z * 16) + x];
				
				if(test != 0) {
					currentY = ((short)(test + 10));
					while(!this.slices[this.getSlice(currentY)].isBlockAir(x, this.modY(currentY), z)) {
						currentY += 10;
						if(currentY >= maxY) {
							currentY = maxY;
							break;
						}
					}
				} else currentY = maxY;
				
				while(this.slices[this.getSlice(currentY)].isBlockAir(x, this.modY(currentY), z)) currentY--;
				
				this.heightmap[(z * 16) + x] = currentY;
				
				if(currentY < lowestY) lowestY = currentY;
			}
		}
		
		int lastSliceIndex = 0, sliceIndex = 0;
		for(short y = maxY; y > lowestY; y--) {
			sliceIndex = this.getSlice(y);
			if(lastSliceIndex == sliceIndex) continue;
			
			lastSliceIndex = sliceIndex;
			this.slices[sliceIndex].markInHeightMap();
		}
	}
	
	protected boolean shouldGenerate() {
		for(ChunkSlice slice : this.slices) {
			if(slice.needsBlockRebuild()) slice.rebuildBlocks();
			if(!slice.isEmpty()) return false;
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
