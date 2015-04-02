package voxel.client.engine.world;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import voxel.client.engine.entities.Entity;
import voxel.client.engine.resources.models.TexturedModel;

public class World extends Entity {

	public World(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		init();
	}

	private static ArrayList<Chunk> loadedChunks;
	public static final int WORLD_SIZE = 1;

	public static void init() {
		loadedChunks = new ArrayList<Chunk>();
		createWorld();
	}

	public static void createWorld() {
		for (int x = 0; x < WORLD_SIZE; x++) {
			for (int z = 0; z < WORLD_SIZE; z++) {
				loadedChunks.add(new Chunk(null, new Vector3f(x
						* Chunk.CHUNK_SIZE, 0, z * Chunk.CHUNK_SIZE), 0, 0, 0,
						0));
			}
		}
	}
}
