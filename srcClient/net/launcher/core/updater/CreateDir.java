package net.launcher.core.updater;

import java.io.File;

import net.launcher.core.logger.Logger;

public class CreateDir {
	public static void createDirectory() {
		String appdata = System.getenv("APPDATA");
		Logger.log("Creating directory");
		new File(appdata + "\\Assets").mkdir();
	}
}
