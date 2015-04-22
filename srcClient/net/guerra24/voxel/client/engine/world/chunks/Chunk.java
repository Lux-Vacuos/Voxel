package net.guerra24.voxel.client.engine.world.chunks;

import org.lwjgl.util.vector.Vector3f;

import net.guerra24.voxel.client.engine.Engine;
import net.guerra24.voxel.client.engine.entities.Entity;
import net.guerra24.voxel.client.engine.world.chunks.blocks.Blocks;
import net.guerra24.voxel.client.engine.world.generation.SimplexNoise;

public class Chunk {

	public static final int CHUNK_SIZE = 16;
	private static final int CHUNK_HEIGHT = 128;

	public static void create(int chunkX, int chunkZ) {
		int xOffset = CHUNK_SIZE * chunkX - 303;
		int zOffset = CHUNK_SIZE * chunkZ - 303;

		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_HEIGHT; y++) {
				for (int z = 0; z < CHUNK_SIZE; z++) {
					int x2 = (int) (SimplexNoise.noise(x, z) * CHUNK_SIZE);
					int z2 = (int) (SimplexNoise.noise(z, x) * CHUNK_SIZE);
					create(x, y, z, xOffset, zOffset);
				}
			}
		}
	}

	private static void create(int x, int y, int z, int xOffset, int zOffset) {
		if (y < 60 && y > 0) {
			if (Engine.rand.nextInt(2) == 0) {
				if (Engine.rand.nextBoolean()) {
					Engine.allCubes.add(new Entity(Blocks.cubeStone,
							new Vector3f(x + xOffset, y, z + zOffset), 0f, 0f,
							0f, 1f));
				}
			}
		}
		if (y < 64 && y > 62) {
			if (Engine.rand.nextInt(2) == 0) {
				Engine.allCubes.add(new Entity(Blocks.cubeGrass, new Vector3f(x
						+ xOffset, y, z + zOffset), 0f, 0f, 0f, 1f));

			}
		}
		if (y < 63 && y > 57) {
			if (Engine.rand.nextInt(2) == 1) {
				Engine.allCubes.add(new Entity(Blocks.cubeDirt, new Vector3f(x
						+ xOffset, y, z + zOffset), 0, 0, 0, 1));
			}
		}
		double sizeMena = (Math.random() * 2 + 2);
		int sizeHalfMena = (int) Math.round(sizeMena / 2);

		if (y < 12 && y > 1 && Math.round(Math.random() * 2000.0f) == 1) {
			for (int y1 = -sizeHalfMena; y1 < sizeHalfMena; y1++) {
				for (int z1 = -sizeHalfMena; z1 < sizeHalfMena; z1++) {
					for (int x1 = -sizeHalfMena; x1 < sizeHalfMena; x1++) {
						if (Math.sqrt(Math.pow(y1, 2) + Math.pow(z1, 2)
								+ Math.pow(x1, 2)) < sizeHalfMena) {

							Engine.allCubes.add(new Entity(
									Blocks.cubeDiamondOre,
									new Vector3f(x + x1 + xOffset, y + y1, z
											+ z1 + zOffset), 0f, 0f, 0f, 1f));
						}
					}
				}
			}
		}
		double sizeMena1 = (Math.random() * 2 + 2);
		int sizeHalfMena1 = (int) Math.round(sizeMena1 / 2);

		if (y < 32 && y > 1 && Math.round(Math.random() * 2000.0f) == 1) {
			for (int y2 = -sizeHalfMena1; y2 < sizeHalfMena1; y2++) {
				for (int z2 = -sizeHalfMena1; z2 < sizeHalfMena1; z2++) {
					for (int x2 = -sizeHalfMena1; x2 < sizeHalfMena1; x2++) {
						if (Math.sqrt(Math.pow(y2, 2) + Math.pow(z2, 2)
								+ Math.pow(x2, 2)) < sizeHalfMena1) {

							Engine.allCubes.add(new Entity(Blocks.cubeGoldOre,
									new Vector3f(x + x2 + xOffset, y + y2, z
											+ z2 + zOffset), 0f, 0f, 0f, 1f));
						}
					}
				}
			}
		}
		if (y == 0) {
			if (Engine.rand.nextInt(1) == 0) {
				Engine.allCubes.add(new Entity(Blocks.cubeIndes, new Vector3f(x
						+ xOffset, y, z + zOffset), 0, 0, 0, 1));
			}
		}
	}
}
