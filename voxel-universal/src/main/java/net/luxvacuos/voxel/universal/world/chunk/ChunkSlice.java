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

import net.luxvacuos.voxel.universal.world.block.Blocks;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.utils.BlockBooleanDataArray;
import net.luxvacuos.voxel.universal.world.utils.BlockLongDataArray;

public final class ChunkSlice {

	private final long BLOCK_METADATA_MASK = 0xFFFFFFFF00000000L;
	private final long BLOCK_ID_MASK = 0x00000000FFFFFFFFL;

	/**
	 * This block data array has both simple block metadata and the blockID
	 * packed in it:<br />
	 * 0xMMMMMMMMBBBBBBBB<br />
	 * The <i>M</i> bits is the simple block metadata<br />
	 * The <i>B</i> bits is the blockID<br />
	 * <br />
	 * The functions in ChunkData automatically pack and unpack the data, so it
	 * should be transparent to whatever is using it
	 */
	private BlockLongDataArray blockData;
	
	private BlockBooleanDataArray collisionMap;

	private final byte yOffset; // Used when saving the data to disk, so it can
								// be put back in the right place

	private short numBlocks = 0;

	private boolean blockRebuild, inHeightMap;

	protected ChunkSlice(byte offset) {
		this.yOffset = offset;
		this.blockRebuild = false;
		this.inHeightMap = false;
		this.blockData = new BlockLongDataArray();
		this.collisionMap = new BlockBooleanDataArray();
	}

	protected ChunkSlice(byte offset, BlockLongDataArray blockData) {
		this.yOffset = offset;
		this.blockRebuild = true;
		this.inHeightMap = false;
		this.blockData = blockData;
		this.collisionMap = new BlockBooleanDataArray();
	}

	public int getBlockIDAt(int x, int y, int z) {
		if (this.isEmpty())
			return 0;
		return (int) (this.blockData.get(x, y, z) & BLOCK_ID_MASK);
	}

	public int getBlockMetadataAt(int x, int y, int z) {
		if (this.isEmpty())
			return 0;
		return (int) ((this.blockData.get(x, y, z) & BLOCK_METADATA_MASK) >> 32);
	}

	public IBlock getBlockAt(int x, int y, int z) {
		if (this.isEmpty())
			return Blocks.getBlockByID(0);

		IBlock block = Blocks.getBlockByID(this.getBlockIDAt(x, y, z));
		block.setPackedMetadata(this.getBlockMetadataAt(x, y, z));
		return block;
	}

	public void setBlockAt(int x, int y, int z, IBlock block) {
		this.blockRebuild = true;
		long data = (long) (((block.getPackedMetadata()) << 32) + block.getID());
		this.blockData.set(x, y, z, data);
		this.collisionMap.set(x, y, z, block.hasCollision());
	}

	public boolean isBlockAir(int x, int y, int z) {
		if (this.isEmpty())
			return true;
		return this.blockData.get(x, y, z) == 0;
	}
	
	public boolean hasCollisionData(int x, int y, int z) {
		if(this.isEmpty())
			return false;
		return this.collisionMap.get(x, y, z);
	}

	public final BlockLongDataArray getBlockDataArray() {
		return this.blockData;
	}

	protected void setBlockDataArray(BlockLongDataArray array) {
		this.blockData = array;
		this.blockRebuild = true;
	}

	protected void markBlockRebuild() {
		this.blockRebuild = true;
	}
	
	public boolean needsBlockRebuild() {
		return this.blockRebuild;
	}

	protected void rebuildBlocks(boolean fullRebuild) {
		if (this.blockRebuild || fullRebuild) {
			this.blockRebuild = false;
			this.numBlocks = 0;
			
			IBlock block = null;
			long[] rawBlockData = this.blockData.getData();
			boolean[] rawCollisionData = this.collisionMap.getData();

			for (int i = 0; i < rawBlockData.length; i++) {
				if (rawBlockData[i] != 0) {
					this.numBlocks++;
					block = Blocks.getBlockByID(((int)(rawBlockData[i] & BLOCK_ID_MASK)));
					rawCollisionData[i] = block.hasCollision();
				}
			}
			
			this.collisionMap = new BlockBooleanDataArray(rawCollisionData);
		}
	}

	public final byte getOffset() {
		return this.yOffset;
	}

	public boolean isEmpty() {
		return this.numBlocks == 0;
	}

	protected void markInHeightMap() {
		this.inHeightMap = true;
	}

	protected boolean inHeightMap() {
		return this.inHeightMap;
	}

}
