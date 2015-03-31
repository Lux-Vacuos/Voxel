package voxel.client.engine.world;

import org.lwjgl.util.vector.Vector3f;

import voxel.client.StartClient;
import voxel.client.engine.entities.Entity;

public class CreateChunk {

	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_HEIGHT = 32;

	public static void createChunks() {
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_HEIGHT; y++) {
				for (int z = 0; z < CHUNK_SIZE; z++) {
					StartClient.allCubes.add(new Entity(StartClient.cubeModel,
							new Vector3f(x, y, z), 0f, 0f, 0f, 1f));
				}
			}
		}
	}
}
