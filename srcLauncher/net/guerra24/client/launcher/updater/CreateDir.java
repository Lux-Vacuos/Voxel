package net.guerra24.client.launcher.updater;

import java.io.File;

import net.guerra24.voxel.client.engine.util.Logger;

public class CreateDir {
	public static void createDirectory() {
		String appdata = System.getenv("APPDATA");
		Logger.log("Creating directory");
		new File(appdata + "\\Assets").mkdir();
	}
}
