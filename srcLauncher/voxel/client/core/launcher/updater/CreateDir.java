package voxel.client.core.launcher.updater;

import java.io.File;

import voxel.client.core.util.Logger;

public class CreateDir {
	public static void createDirectory() {
		String appdata = System.getenv("APPDATA");
		Logger.log("Creating directory");
		new File(appdata + "\\Assets").mkdir();
	}
}
