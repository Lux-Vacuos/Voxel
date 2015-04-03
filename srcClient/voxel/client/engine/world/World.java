package voxel.client.engine.world;

import org.lwjgl.util.vector.Vector3f;

import voxel.client.engine.entities.Entity;
import voxel.client.engine.resources.models.TexturedModel;

public class World extends Entity {

	public World(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		init();
	}

	public static final int WORLD_SIZE = 1;

	public static void init() {
		Chunk.create();
	}

}
