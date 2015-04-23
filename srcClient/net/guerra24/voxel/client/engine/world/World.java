package net.guerra24.voxel.client.engine.world;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import net.guerra24.voxel.client.engine.Engine;
import net.guerra24.voxel.client.engine.world.chunks.Chunk;

public class World {

	public static final int WORLD_SIZE = 2;
	public static String worldPath = "assets/world/World.json";

	public static void init() {
		for (int x = 0; x < WORLD_SIZE; x++) {
			for (int z = 0; z < WORLD_SIZE; z++) {
				Chunk.create(x + 16, z + 16);
			}
		}
		Engine.allCubes.addAll(Chunk.cubes);
		saveGame(worldPath);
	}

	public static void saveGame(String path) {
		Gson gson = new Gson();
		String json = gson.toJson(Chunk.cubes);

		FileWriter writer;
		try {
			writer = new FileWriter(path);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
