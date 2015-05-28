package io.github.guerra24.voxel.client.launcher.updater;

import io.github.guerra24.voxel.client.kernel.util.Logger;

import java.io.File;

public class CreateDir {
	public static void createDirectory() {
		String appdata = System.getenv("APPDATA");
		File f = new File(appdata + "\\Assets");
		if (!f.exists()) {
			Logger.log("Creating directory");
			new File(appdata + "\\Assets").mkdir();
		}

	}
}
