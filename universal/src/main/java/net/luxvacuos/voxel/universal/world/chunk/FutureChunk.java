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

import java.util.concurrent.Future;

import net.luxvacuos.voxel.universal.util.Pair;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class FutureChunk extends Chunk {
	
	private final Future<ChunkData> futureData;

	public FutureChunk(IDimension dim, ChunkNode node, Future<ChunkData> future) {
		super(dim, node, null);
		this.futureData = future;
	}
	
	@Override
	public ChunkData getChunkData() {
		if(!this.isDone())
			this.waitForFuture();
		return this.data;
	}
	
	@Override
	public IBlock getBlockAt(int x, int y, int z) {
		if(!this.isDone())
			this.waitForFuture();
		return super.getBlockAt(x, y, z);
	}
	
	@Override
	public void setBlockAt(int x, int y, int z, IBlock block) {
		if(!this.isDone())
			this.waitForFuture();
		super.setBlockAt(x, y, z, block);
	}
	
	@Override
	public boolean hasCollisionData(int x, int y, int z) {
		if(!this.isDone())
			this.waitForFuture();
		return super.hasCollisionData(x, y, z);
	}
	
	@Override
	public void markForRebuild() {
		if(!this.isDone())
			this.waitForFuture();
		super.markForRebuild();
	}

	@Override
	public boolean needsRebuild() {
		if(!this.isDone())
			this.waitForFuture();
		return super.needsRebuild();
	}
	
	@Override
	public boolean needsMeshRebuild() {
		if(!this.isDone())
			this.waitForFuture();
		return super.needsMeshRebuild();
	}
	
	@Override
	public void completedMeshRebuild() {
		if(!this.isDone())
			this.waitForFuture();
		super.completedMeshRebuild();
	}

	@Override
	public void update(float delta) {
		if(!this.isDone())
			this.waitForFuture();
		
		super.update(delta);
	}
	
	public boolean isDone() {
		if(this.data == null) {
			if(this.futureData.isDone())
				try {
					this.data = this.futureData.get();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			return this.futureData.isDone();
		} else return true;
	}
	
	private void waitForFuture() {
		if(!this.futureData.isDone()) {
			try {
				this.data = this.futureData.get();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
