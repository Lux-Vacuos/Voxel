package net.guerra24.voxel.client.launcher.updater;

import java.io.File;

import net.guerra24.voxel.client.kernel.util.Logger;

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
