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

package net.luxvacuos.voxel.universal.tasks;

import java.util.concurrent.Callable;

import net.luxvacuos.voxel.universal.world.chunk.IChunk;
import net.luxvacuos.voxel.universal.world.chunk.generator.IChunkGenerator;

public class ChunkGenerateTask implements Callable<IChunk> {
	private IChunk chunk;
	private IChunkGenerator gen;

	public ChunkGenerateTask(IChunk chunk, IChunkGenerator gen) {
		this.chunk = chunk;
		this.gen = gen;
	}

	@Override
	public IChunk call() throws Exception {
		this.gen.generateChunk(this.chunk, (this.chunk.getX() << 4) , (this.chunk.getZ() << 4));
		this.chunk.markForRebuild();
		return this.chunk;
	}

}
