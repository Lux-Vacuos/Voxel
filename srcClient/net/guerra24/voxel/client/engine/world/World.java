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

import net.guerra24.voxel.client.engine.DisplayManager;
import net.guerra24.voxel.client.engine.entities.Entity;
import net.guerra24.voxel.client.engine.resources.GameResources;
import net.guerra24.voxel.client.engine.resources.GuiResources;
import net.guerra24.voxel.client.engine.util.AbstractFilesPath;
import net.guerra24.voxel.client.engine.util.Logger;
import net.guerra24.voxel.client.engine.world.chunks.Chunk;

public class World {

	public static final int WORLD_SIZE = 2;

	private static boolean load = true;
	private static Gson gson = new Gson();

	public static void init() {
		GameResources.guis5.add(GuiResources.loadW);
		GameResources.guis5.remove(GuiResources.load);
		for (int x = 0; x < WORLD_SIZE; x++) {
			for (int z = 0; z < WORLD_SIZE; z++) {
				Chunk.create(x + 16, z + 16);
				GameResources.guiRenderer.render(GameResources.guis5);
				DisplayManager.updateDisplay();
			}
		}
		GameResources.allEntities.addAll(Chunk.cubes);
	}

	public static void loadGame() {
		GameResources.camera.loadCameraPos();
		SaveEntities.loadGame(AbstractFilesPath.entitiesPath);
		loadWorld(AbstractFilesPath.worldPath);
	}

	public static void saveGame() {
		saveWorld(AbstractFilesPath.worldPath);
		GameResources.camera.saveCameraPos();
		SaveEntities.saveGame(AbstractFilesPath.entitiesPath);
	}

	private static void saveWorld(String path) {
		String json = gson.toJson(Chunk.cubes);

		FileWriter writer;
		try {
			writer = new FileWriter(path);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			Logger.error("Failed to Save Game");
		}
	}

	private static void loadWorld(String path) {
		try {
			if (load) {
				Logger.log("Loading Game");
				BufferedReader world = new BufferedReader(new FileReader(path));
				JsonParser parser = new JsonParser();
				JsonArray jArray = parser.parse(world).getAsJsonArray();
				List<Entity> lcs = new ArrayList<Entity>();

				for (JsonElement obj : jArray) {
					Entity cse = gson.fromJson(obj, Entity.class);
					lcs.add(cse);
				}
				GameResources.allEntities.removeAll(Chunk.cubes);
				Chunk.cubes = lcs;
				GameResources.allEntities.addAll(Chunk.cubes);
				load = false;
				Logger.log("Load Completed");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Logger.error("Failed to load Save Game");
		}
	}
}
