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

package net.luxvacuos.voxel.server.world.chunks;

import java.util.Random;

import net.luxvacuos.voxel.server.world.Dimension;
import net.luxvacuos.voxel.server.world.block.Block;

public class ChunkGenerator {

	public void addTree(Dimension world, int xo, int yo, int zo, int treeHeight, Random rand) {
		for (int y = 0; y < treeHeight; y++) {
			world.setGlobalBlock(xo, yo + y, zo, Block.Wood);
		}
		for (int x = 0; x < treeHeight; x++) {
			for (int z = 0; z < treeHeight; z++) {
				for (int y = 0; y < treeHeight; y++) {
					int xx = x - (treeHeight - 1) / 2;
					int yy = y - (treeHeight - 1) / 2;
					int zz = z - (treeHeight - 1) / 2;
					if (xx == 0 && zz == 0 && yy <= 0)
						continue;
					double test = Math.sqrt((double) xx * xx + yy * yy + zz * zz);
					if (test < (treeHeight - 1) / 2) {
						if (rand.nextDouble() < 0.8) {
							world.setGlobalBlock(xo + xx, yo + yy + treeHeight - 1, zo + zz, Block.Leaves);
						}
					}
				}
			}
		}

	}

	public void generateCaves(Chunk chunk, Dimension dim) {
	}

}
