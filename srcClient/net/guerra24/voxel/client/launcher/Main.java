package net.guerra24.voxel.client.launcher;

import net.guerra24.voxel.client.kernel.util.Logger;
import net.guerra24.voxel.client.launcher.init.Init;
import net.guerra24.voxel.client.launcher.properties.Reader;
import net.guerra24.voxel.client.launcher.properties.Writter;
import net.guerra24.voxel.client.launcher.updater.CreateDir;
import net.guerra24.voxel.client.launcher.updater.Update;

public class Main {
	public static void main(String[] args) {
		// Init.InitLog();
		Logger.log("Starting Launcher");
		Init.checkJava();
		Init.printSystemInfo();
		CreateDir.createDirectory();
		Writter.configWritter();
		Reader.configReader();
		//Update.getUpdate();
		//Update.getUpdateAssets();
		Launcher.LauncherStart();
	}
}