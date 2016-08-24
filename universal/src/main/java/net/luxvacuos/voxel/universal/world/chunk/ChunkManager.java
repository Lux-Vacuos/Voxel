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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.luxvacuos.voxel.universal.world.chunk.generator.IChunkGenerator;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public final class ChunkManager {
	protected final IDimension dimension;
	
	protected final ExecutorService executor = Executors.newCachedThreadPool();
	
	protected IChunkGenerator chunkGenerator;
	
	public ChunkManager(IDimension dimension) {
		this.dimension = dimension;
	}

}
