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

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class Chunk implements IChunk {

	protected final ChunkNode node;
	protected final IDimension dim;
	protected volatile ChunkData data;
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Array<Entity> chunkLoaders = new Array<>();
	private BoundingBox aabb = new BoundingBox(new Vector3(0, 0, 0), new Vector3(16, 256, 16));

	protected Chunk(IDimension dim, ChunkNode node, ChunkData data) {
		this.node = node;
		this.data = data;
		this.dim = dim;
	}

	@Override
	public void beforeUpdate(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterUpdate(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

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
		this.markForRebuild();
	}

	@Override
	public int getX() {
		return this.node.getX();
	}

	@Override
	public int getY() {
		return this.node.getY();
	}

	@Override
	public int getZ() {
		return this.node.getZ();
	}

	@Override
	public void setBlockAt(int x, int y, int z, IBlock block) {
		block.setChunk(this);
		this.data.set(x, y, z, block);
	}

	@Override
	public IBlock getBlockAt(int x, int y, int z) {
		return data.get(x, y, z);
	}

	@Override
	public IDimension getDimension() {
		return null;
	}

	@Override
	public boolean hasCollisionData(int x, int y, int z) {
		return false;
	}

	@Override
	public void markForRebuild() {
		data.markForRebuild();
	}

	@Override
	public boolean needsRebuild() {
		return data.needsRebuild();
	}

	@Override
	public void registerChunkLoader(Entity entity) {
		if (!chunkLoaders.contains(entity, true))
			chunkLoaders.add(entity);
	}

	@Override
	public void removeChunkLoader(Entity entity) {
		chunkLoaders.removeValue(entity, true);
	}

	@Override
	public int chunkLoaders() {
		return chunkLoaders.size;
	}

	@Override
	public BoundingBox getBoundingBox(ChunkNode node) {
		return new BoundingBox(node.asVector3().add(this.aabb.min), node.asVector3().add(this.aabb.max));
	}

}
