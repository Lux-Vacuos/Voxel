package net.guerra24.voxel.client.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.client.kernel.DisplayManager;
import net.guerra24.voxel.client.kernel.Engine;
import net.guerra24.voxel.client.kernel.entities.Entity;
import net.guerra24.voxel.client.kernel.util.AbstractFilesPath;
import net.guerra24.voxel.client.kernel.util.Logger;
import net.guerra24.voxel.client.resources.GuiResources;
import net.guerra24.voxel.client.world.chunks.Chunk;

import org.lwjgl.util.vector.Vector2f;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class World {

	private static boolean load = true;

	private static float pos = -0.85f;
	private static double pos2 = 0.0d;
	public int WORLD_SIZE;

	public void init(int WORLD_SIZE) {
		this.WORLD_SIZE = WORLD_SIZE;
		initialize();
		Engine.gameResources.guis5.add(Engine.guiResources.loadW);
		Engine.gameResources.guis5.add(Engine.guiResources.loadBar);
		Engine.gameResources.guis5.remove(GuiResources.load);
		for (int x = 0; x < this.WORLD_SIZE; x++) {
			for (int z = 0; z < this.WORLD_SIZE; z++) {
				Chunk.create(x + 16, z + 16);
				pos = (float) (pos + pos2);
				Engine.guiResources.loadBar.setPosition(new Vector2f(pos, 0));
				Engine.gameResources.guiRenderer
						.render(Engine.gameResources.guis5);
				DisplayManager.updateDisplay();
			}
		}
		Engine.gameResources.guis5.remove(Engine.guiResources.loadW);
		Engine.gameResources.guis5.remove(Engine.guiResources.loadBar);
		Engine.gameResources.guis5.add(GuiResources.load);
		Engine.gameResources.allEntities.addAll(Chunk.cubes);
	}

	public void initialize() {
		if (WORLD_SIZE == 16) {
			pos2 = 16 / 6500.0;
		} else if (WORLD_SIZE == 8) {
			pos2 = 8 / 800.0;
		}
	}

	public void loadGame() {
		Engine.gameResources.camera.loadCameraPos();
		SaveEntities.loadGame(AbstractFilesPath.entitiesPath);
		loadWorld(AbstractFilesPath.worldPath);
	}

	public void saveGame() {
		saveWorld(AbstractFilesPath.worldPath);
		Engine.gameResources.camera.saveCameraPos();
		SaveEntities.saveGame(AbstractFilesPath.entitiesPath);
	}

	private void saveWorld(String path) {
		String json = Engine.gameResources.gson.toJson(Chunk.cubes);

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

	private void loadWorld(String path) {
		try {
			if (load) {
				Logger.log("Loading Game");
				BufferedReader world = new BufferedReader(new FileReader(path));
				JsonParser parser = new JsonParser();
				JsonArray jArray = parser.parse(world).getAsJsonArray();
				List<Entity> lcs = new ArrayList<Entity>();

				for (JsonElement obj : jArray) {
					Entity cse = Engine.gameResources.gson.fromJson(obj,
							Entity.class);
					lcs.add(cse);
				}
				Engine.gameResources.allEntities.removeAll(Chunk.cubes);
				Chunk.cubes = lcs;
				Engine.gameResources.allEntities.addAll(Chunk.cubes);
				load = false;
				Logger.log("Load Completed");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Logger.error("Failed to load Save Game");
		}
	}
}
