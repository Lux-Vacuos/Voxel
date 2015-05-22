package net.guerra24.voxel.client.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.client.kernel.Engine;
import net.guerra24.voxel.client.kernel.entities.Entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class SaveEntities {
	public static void saveGame(String path) {
		String json = Engine.gameResources.gson
				.toJson(Engine.gameResources.allObjects);

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
				Entity cse = Engine.gameResources.gson.fromJson(obj,
						Entity.class);
				lcs.add(cse);
			}
			Engine.gameResources.allEntities
					.removeAll(Engine.gameResources.allObjects);
			Engine.gameResources.allObjects = lcs;
			Engine.gameResources.allEntities
					.addAll(Engine.gameResources.allObjects);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
