package net.guerra24.voxel.client.world.chunks;

import java.util.Random;

import net.guerra24.voxel.client.world.IWorld;
import net.guerra24.voxel.client.world.InfinityWorld;
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

	public void generateCaves(byte[][][] blocks, InfinityWorld world, int sizeX, int sizeZ, int sizeY, int cx, int cz) {
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					double tempHeight = world.getNoise().getNoise(x + cx * 16, y, z + cz * 16);
					tempHeight++;
					if (tempHeight > 0.9) {
					} else
						blocks[x][y][z] = 0;
					if (y == 0)
						blocks[x][y][z] = Block.Indes.getId();
				}
			}
		}

	}
}
