package net.guerra24.voxel.client.engine.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.guerra24.voxel.client.engine.Engine;
import net.guerra24.voxel.client.engine.entities.Entity;
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

	public static void loadGame(String path) {
		Gson gson = new Gson();

		try {
			BufferedReader world = new BufferedReader(new FileReader(path));
			JsonParser parser = new JsonParser();
			JsonArray jArray = parser.parse(world).getAsJsonArray();
			List<Entity> lcs = new ArrayList<Entity>();

			for (JsonElement obj : jArray) {
				Entity cse = gson.fromJson(obj, Entity.class);
				lcs.add(cse);
			}
			Engine.allCubes.removeAll(Chunk.cubes);
			Chunk.cubes = lcs;
			Engine.allCubes.addAll(Chunk.cubes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
