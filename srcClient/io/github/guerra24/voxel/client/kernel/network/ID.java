package io.github.guerra24.voxel.client.kernel.network;

import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.kernel.util.AbstractFilesPath;
import io.github.guerra24.voxel.client.kernel.util.Logger;
import io.github.guerra24.voxel.client.launcher.login.Login;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class ID {

	private static String Username;
	private static UUID ID;

	public ID() {
		Username = Login.username1;
		ID = UUID.randomUUID();
		if (Username != null) {
			String id = "Username: " + Username + " UUID: " + ID;
			Logger.log(id);
			writeUUID(id);
		} else {
			throw new RuntimeException("Invalid Username");
		}
	}

	private void writeUUID(String id) {
		String json = Kernel.gameResources.gson.toJson(id);

		FileWriter writer;
		try {
			writer = new FileWriter(AbstractFilesPath.userPath);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
