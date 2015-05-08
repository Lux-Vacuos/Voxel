package net.guerra24.voxel.client.engine.network;

import java.util.UUID;

import net.guerra24.voxel.client.engine.util.Logger;
import net.guerra24.voxel.client.launcher.login.Login;

public class ID {

	public static void generateUUID() {
		String id = "Username: " + Login.username1 + ", With UUID: "
				+ UUID.randomUUID();
		Logger.log(id);
	}
}
