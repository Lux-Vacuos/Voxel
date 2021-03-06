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

package net.luxvacuos.voxel.universal.world.chunk.generator;

import java.util.Random;

import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.chunk.ChunkData;
import net.luxvacuos.voxel.universal.world.chunk.IChunk;

public abstract class AbstractChunkGenerator implements IChunkGenerator {
	protected Random rng = null;
	protected INoiseGenerator noise = null;

	protected AbstractChunkGenerator() {
		this(System.currentTimeMillis(), new FlatNoiseGenerator(64));
	}

	protected AbstractChunkGenerator(INoiseGenerator noise) {
		this(System.currentTimeMillis(), noise);
	}

	protected AbstractChunkGenerator(long rngseed) {
		this(rngseed, new FlatNoiseGenerator(64));
	}

	protected AbstractChunkGenerator(long rgnseed, INoiseGenerator noise) {
		this.rng = new Random(rgnseed);
		this.noise = noise;
	}

	public void setNoiseGenerator(INoiseGenerator noise) {
		this.noise = noise;
	}

	public void setSeed(long seed) {
		this.rng = new Random(seed);
	}

	@Override
	public void generateChunk(IChunk chunk, int worldX, int worldY, int worldZ) {
		ChunkData data = chunk.getChunkData();
		int adjWorldX, adjWorldY, adjWorldZ;
		double noise = 0, noise3D = 0;

		for (int y = 0; y < 16; y++) {
			adjWorldY = worldY + y;
			for (int x = 0; x < 16; x++) {
				adjWorldX = worldX + x;
				for (int z = 0; z < 16; z++) {
					adjWorldZ = worldZ + z;
					noise = this.noise.eval(adjWorldX, adjWorldZ);
					// noise3D = this.noise.eval(adjWorldX, y, adjWorldZ);
					data.set(x, y, z, this.generateBlock(adjWorldX, adjWorldY, adjWorldZ, noise, noise3D));
				}
			}
		}

		chunk.markForRebuild();
	}

	protected abstract IBlock generateBlock(int chunkX, int chunkY, int chunkZ, double noise, double noise3D);
}
