package io.github.guerra24.voxel.client.world;

import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.world.entities.Entity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class SaveEntities {
	public static void saveGame(String path) {
		String json = Kernel.gameResources.gson
				.toJson(Kernel.gameResources.allObjects);

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

		try {
			BufferedReader world = new BufferedReader(new FileReader(path));
			JsonParser parser = new JsonParser();
			JsonArray jArray = parser.parse(world).getAsJsonArray();
			List<Entity> lcs = new ArrayList<Entity>();

			for (JsonElement obj : jArray) {
				Entity cse = Kernel.gameResources.gson.fromJson(obj,
						Entity.class);
				lcs.add(cse);
			}
			Kernel.gameResources.allEntities
					.removeAll(Kernel.gameResources.allObjects);
			Kernel.gameResources.allObjects = lcs;
			Kernel.gameResources.allEntities
					.addAll(Kernel.gameResources.allObjects);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
