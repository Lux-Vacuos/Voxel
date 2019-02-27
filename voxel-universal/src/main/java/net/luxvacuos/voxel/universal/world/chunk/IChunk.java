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

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.lightengine.universal.resources.IDisposable;
import net.luxvacuos.lightengine.universal.util.IUpdatable;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public interface IChunk extends IUpdatable, IDisposable {

	public ChunkNode getNode();

	public int getX();
	
	public int getY();

	public int getZ();

	public void setBlockAt(int x, int y, int z, IBlock block);

	public IBlock getBlockAt(int x, int y, int z);

	public IDimension getDimension();

	public ChunkData getChunkData();

	public boolean hasCollisionData(int x, int y, int z);

	public void markForRebuild();

	public boolean needsRebuild();

	public void registerChunkLoader(Entity entity);

	public void removeChunkLoader(Entity entity);

	public int chunkLoaders();

	public BoundingBox getBoundingBox(ChunkNode node);

}
