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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.lightengine.universal.util.Pair;
import net.luxvacuos.voxel.universal.world.block.Blocks;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class FutureChunk implements IChunk {

	private ChunkNode node;
	private IDimension dim;
	private ChunkData data;
	private final Future<ChunkData> futureData;
	private List<Pair<Vector3d, IBlock>> deferredBlocks;
	private BoundingBox aabb = new BoundingBox(new Vector3(0, 0, 0), new Vector3(16, 256, 16));

	public FutureChunk(IDimension dim, ChunkNode node, Future<ChunkData> future) {
		this.dim = dim;
		this.node = node;
		this.data = null;
		this.futureData = future;
		this.deferredBlocks = new ArrayList<Pair<Vector3d, IBlock>>();
	}

	@Override
	public ChunkData getChunkData() {
		if (this.isDone()) {
			return this.data;
		} else {
			try {
				this.data = this.futureData.get();
			} catch (Exception e) {
				Logger.error(e);
				throw new RuntimeException(e);
			}
			return this.data;
		}
	}

	@Override
	public IBlock getBlockAt(int x, int y, int z) {
		if (this.isDone()) {
			IBlock block = this.data.getBlockAt(x, y, z);
			if (this.data.hasComplexMetadataAt(x, y, z)) {
				block.setComplexMetadata(this.data.getComplexMetadataAt(x, y, z));
			}
			return block;
		} else // TODO: Maybe implement a FutureBlock class of some sort?
			return Blocks.getBlockByName("voxel:air");
	}

	@Override
	public void setBlockAt(int x, int y, int z, IBlock block) {
		if (this.isDone()) {
			this.data.setBlockAt(x, y, z, block);
		} else {
			Vector3d blockPos = new Vector3d(x, y, z);
			this.deferredBlocks.add(new Pair<Vector3d, IBlock>(blockPos, block));
		}
	}

	@Override
	public boolean hasCollisionData(int x, int y, int z) {
		if (this.isDone()) {
			return this.data.hasCollisionData(x, y, z);
		} else
			return false;
	}

	@Override
	public void markForRebuild() {
		if (this.isDone()) {
			this.data.markFullRebuild();
		}
	}

	@Override
	public boolean needsRebuild() {
		if (this.isDone()) {
			return this.data.needsRebuild();
		} else
			return false;
	}

	@Override
	public void registerChunkLoader(Entity entity) {
	}

	@Override
	public void removeChunkLoader(Entity entity) {
	}
	
	@Override
	public int chunkLoaders() {
		return 1; // TODO: Return 1 to prevent unloading (?)
	}

	@Override
	public void update(float delta) {
		if (this.data != null) {
			if (this.data.needsRebuild())
				this.data.rebuild();
			// TODO: update BlockEntities
		} else if (this.futureData.isDone()) {
			try {
				this.data = this.futureData.get();
				if (!this.deferredBlocks.isEmpty()) {
					Vector3d blockPos;
					IBlock block;
					for (Pair<Vector3d, IBlock> pair : this.deferredBlocks) {
						blockPos = pair.getFirst();
						block = pair.getSecond();

						if (block.hasComplexMetadata()) {
							// TODO: Implement this
						}

						this.data.setBlockAt((int) blockPos.x, (int) blockPos.y, (int) blockPos.z, block);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public ChunkNode getNode() {
		return this.node;
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
	public IDimension getDimension() {
		return this.dim;
	}

	public boolean isDone() {
		return (this.isFutureDone() && this.data != null);
	}

	public boolean isFutureDone() {
		return this.futureData.isDone();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public BoundingBox getBoundingBox(ChunkNode node) {
		return new BoundingBox(node.asVector3().add(this.aabb.min), node.asVector3().add(this.aabb.max));
	}

	@Override
	public IChunkHandle getHandle() {
		return new ChunkHandle(this);
	}

}
