package voxel.client.engine.world;

import org.lwjgl.util.vector.Vector3f;

import voxel.client.StartClient;
import voxel.client.engine.entities.Entity;
import voxel.client.engine.world.blocks.Blocks;

public class Chunk {

	public static final int CHUNK_SIZE = 16;
	private static final int CHUNK_HEIGHT = 128;

	public static void create(int chunkX, int chunkZ) {
		float xOffset = CHUNK_SIZE * chunkX;
		float zOffset = CHUNK_SIZE * chunkZ;
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_HEIGHT; y++) {
				for (int z = 0; z < CHUNK_SIZE; z++) {
					if (y < 64) {
						if (StartClient.rand.nextInt(2) == 0) {
							if (StartClient.rand.nextBoolean()) {
								StartClient.allCubes.add(new Entity(
										Blocks.cubeStone, new Vector3f(x
												+ xOffset, y, z + zOffset), 0f,
										0f, 0f, 1f));
							}
							if (StartClient.rand.nextInt(2) == 0) {
								StartClient.allCubes.add(new Entity(
										Blocks.cubeSand, new Vector3f(x
												+ xOffset, y, z + zOffset), 0f,
										0f, 0f, 1f));
							}
						}
					}
				}
			}
		}
	}

	/*
	 * public static void createOres() { double sizeMena = (Math.random() * 2 +
	 * 2); int sizeHalfMena = (int) Math.round(sizeMena / 2);
	 * 
	 * if (y < 8 && Math.round(Math.random() * 100.0f) == 40) { for (int y1 =
	 * -sizeHalfMena; y1 < sizeHalfMena; y1++) { for (int z1 = -sizeHalfMena; z1
	 * < sizeHalfMena; z1++) { for (int x1 = -sizeHalfMena; x1 < sizeHalfMena;
	 * x1++) { if (Math.sqrt(Math.pow(y1, 2) + Math.pow(z1, 2) + Math.pow(x1,
	 * 2)) < sizeHalfMena) {
	 * 
	 * StartClient.allCubes.add(new Entity( Blocks.cubeSand, new Vector3f(x +
	 * x1, y + y1, z + z1), 0f, 0f, 0f, 1f)); } } } } } }
	 */
}
