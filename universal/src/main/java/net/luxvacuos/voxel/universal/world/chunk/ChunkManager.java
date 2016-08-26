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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.luxvacuos.voxel.universal.util.Pair;
import net.luxvacuos.voxel.universal.world.chunk.generator.FlatChunkGenerator;
import net.luxvacuos.voxel.universal.world.chunk.generator.IChunkGenerator;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public final class ChunkManager {
	protected final IDimension dimension;
	protected final ExecutorService executor = Executors.newCachedThreadPool();
	protected IChunkGenerator chunkGenerator = new FlatChunkGenerator();
	
	protected List<Future<Pair<ChunkNode, ChunkData>>> chunkLoadList;
	
	public ChunkManager(IDimension dimension) {
		this.dimension = dimension;
	}
	
	public void setGenerator(IChunkGenerator generator) {
		this.chunkGenerator = generator;
	}
	
	public void update() {
		
	}

}
