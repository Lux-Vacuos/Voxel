/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.guerra24.voxel.client.world.chunks;

import java.util.Random;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.world.IWorld;
import net.guerra24.voxel.client.world.SimplexNoise;
import net.guerra24.voxel.client.world.block.Block;

public class ChunkGenerator {
	public void addTree(IWorld world, int xo, int yo, int zo, int treeHeight, Random rand) {
		for (int y = 0; y < treeHeight; y++) {
			world.setGlobalBlock(xo, yo + y, zo, Block.Wood.getId());
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
							world.setGlobalBlock(xo + xx, yo + yy + treeHeight - 1, zo + zz, Block.Leaves.getId());
						}
					}
				}
			}
		}

	}

	public void generateCaves(Chunk chunk, SimplexNoise noise) {
		for (int x = 0; x < VoxelVariables.CHUNK_SIZE; x++) {
			for (int z = 0; z < VoxelVariables.CHUNK_SIZE; z++) {
				for (int y = 0; y < VoxelVariables.CHUNK_HEIGHT; y++) {
					if (noise.getNoise((int) (x + chunk.posX), (int) (y + chunk.posY), (int) (z + chunk.posZ)) > 0.18
							&& chunk.getLocalBlock(x, y, z) != Block.Water.getId())
						chunk.setLocalBlock(x, y, z, Block.Air.getId());
				}
			}
		}
	}
}
