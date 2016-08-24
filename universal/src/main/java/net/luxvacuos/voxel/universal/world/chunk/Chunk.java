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

import net.luxvacuos.voxel.universal.world.block.Blocks;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class Chunk implements IChunk {
	private final ChunkNode node;
	protected ChunkData data;
	
	protected Chunk(ChunkNode node) {
		this.node = node;
	}

	@Override
	public ChunkNode getNode() {
		return this.node;
	}

	@Override
	public ChunkData getChunkData() {
		return this.data;
	}
	
	void setChunkData(ChunkData data) {
		this.data = data;
		this.data.markFullRebuild();
	}

	@Override
	public int getX() {
		return this.node.getX();
	}

	@Override
	public int getZ() {
		return this.node.getZ();
	}

	@Override
	public IBlock getBlockAt(int x, int y, int z) {
		IBlock block = Blocks.getBlockByID(this.data.getBlockAt(x, y, z));
		if(block.hasMetadata()) {
			//TODO: Implement this
		}
		return block;
	}
	
	public void setSunlight(int value) {
		this.data.setSkyLight(value);
	}

	public void setBlockAt(int x, int y, int z, IBlock block) {
		//TODO: Update neighboring blocks if block == air
		this.data.setBlockAt(x, y, z, block.getID());
		if(block.hasMetadata()) {
			//TODO: Implement this
		}
	}

	@Override
	public ChunkSnapshot takeSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void markForRebuild() {
		this.data.markFullRebuild();
	}
	
	@Override
	public boolean needsRebuild() {
		return this.data.needsRebuild();
	}
	
	public void update(float delta) {
		if(this.data.needsRebuild()) this.data.rebuild();
		
		//TODO: update BlockEntities
	}

}
