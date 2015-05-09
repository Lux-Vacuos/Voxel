package net.guerra24.voxel.client.engine.network;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import com.google.gson.Gson;

import net.guerra24.voxel.client.engine.util.AbstractFilesPath;
import net.guerra24.voxel.client.engine.util.Logger;
import net.guerra24.voxel.client.launcher.login.Login;

public class ID {

	private static Gson gson = new Gson();
	public static String id;

	public static void generateUUID() {
		id = "Username: " + Login.username1 + " UUID: "
				+ UUID.randomUUID();
		Logger.log(id);
	}

	public static void writeUUID() {
		String json = gson.toJson(id);

		FileWriter writer;
		try {
			writer = new FileWriter(AbstractFilesPath.userPath);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			Logger.error("Failed to Save Game");
		}
	}
}
