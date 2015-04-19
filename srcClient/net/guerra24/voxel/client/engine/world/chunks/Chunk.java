package net.guerra24.voxel.client.engine.world.chunks;

import org.lwjgl.util.vector.Vector3f;

import net.guerra24.voxel.client.engine.Engine;
import net.guerra24.voxel.client.engine.entities.Entity;
import net.guerra24.voxel.client.engine.world.chunks.blocks.Blocks;
import net.guerra24.voxel.client.engine.world.chunks.blocks.BlocksType;
import net.guerra24.voxel.client.engine.world.generation.SimplexNoise;

public class Chunk {

	public static final int CHUNK_SIZE = 16;
	private static final int CHUNK_HEIGHT = 128;

	private static int[][][] blocks;

	public static void create(int chunkX, int chunkZ) {
		float xOffset = CHUNK_SIZE * chunkX - 303;
		float zOffset = CHUNK_SIZE * chunkZ - 303;

		blocks = new int[CHUNK_SIZE][CHUNK_HEIGHT][CHUNK_SIZE];
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_HEIGHT; y++) {
				for (int z = 0; z < CHUNK_SIZE; z++) {

					int x2 = (int) (SimplexNoise.noise(x, y) * CHUNK_SIZE);
					int z2 = (int) (SimplexNoise.noise(z, y) * CHUNK_SIZE);
					if (Engine.rand.nextInt(9) == 0) {
						blocks[x][y][z] = BlocksType.Stone.getId();
					} else if (Engine.rand.nextInt(9) == 0) {
						blocks[x][y][z] = BlocksType.Grass.getId();
					}
					if (blocks[x][y][z] == 1) {
						Engine.allCubes.add(new Entity(Blocks.cubeStone,
								new Vector3f(x2 + xOffset, y, z2 + zOffset),
								0f, 0f, 0f, 1f));
					} else if (blocks[x][y][z] == 2) {
						Engine.allCubes.add(new Entity(Blocks.cubeGrass,
								new Vector3f(x2 + xOffset, y, z2 + zOffset),
								0f, 0f, 0f, 1f));
					}

					if (y == 0) {
						if (Engine.rand.nextInt(1) == 0) {
							Engine.allCubes.add(new Entity(Blocks.cubeIndes,
									new Vector3f(x + xOffset, y, z + zOffset),
									0, 0, 0, 1));
						}
					}

				}
			}
		}
	}
}
