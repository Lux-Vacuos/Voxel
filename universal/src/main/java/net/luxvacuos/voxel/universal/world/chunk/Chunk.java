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

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class Chunk implements IChunk {
	protected final ChunkNode node;
	protected final IDimension dim;
	protected volatile ChunkData data;
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();

	protected Chunk(IDimension dim, ChunkNode node, ChunkData data) {
		this.node = node;
		this.data = data;
		this.dim = dim;
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
		this.lock.readLock().lock();
		try {
			IBlock block = this.data.getBlockAt(x, y, z);
			if (block.hasComplexMetadata()) {
				// TODO: Implement this
			}
			return block;
		} catch (Exception e) {
			return null;
		} finally {
			this.lock.readLock().unlock();
		}
	}

	public void setSunlight(int value) {
		this.data.setSkyLight(value);
	}
	
	@Override
	public void setBlockAt(int x, int y, int z, IBlock block) {
		// TODO: Update neighboring blocks if block == air
		this.data.setBlockAt(x, y, z, block);
		if (block.hasComplexMetadata()) {
			// TODO: Implement this
		}
	}
	
	@Override
	public boolean hasCollisionData(int x, int y, int z) {
		return this.data.hasCollisionData(x, y, z);
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
	
	@Override
	public boolean needsMeshRebuild() {
		return this.data.needsMeshRebuild();
	}
	
	@Override
	public void completedMeshRebuild(){
		this.data.completedMeshRebuild();
	}

	@Override
	public void update(float delta) {
		if (this.data.needsRebuild())
			this.data.rebuild();
		// TODO: update BlockEntities
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public IDimension getDimension() {
		return this.dim;
	}

}
