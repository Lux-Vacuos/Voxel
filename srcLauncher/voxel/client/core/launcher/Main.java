package voxel.client.core.launcher;

import voxel.client.core.launcher.init.Init;
import voxel.client.core.launcher.properties.Reader;
import voxel.client.core.launcher.properties.Writter;
import voxel.client.core.launcher.updater.CreateDir;
import voxel.client.core.launcher.updater.Update;
import voxel.client.engine.util.Logger;

public class Main {
	public static void main(String[] args) {
		//Init.InitLog();
		Logger.log("Starting Launcher");
		Init.checkJava();
		Init.printSystemInfo();
		CreateDir.createDirectory();
		Writter.configWritter();
		Reader.configReader();
		Update.getUpdate();
		Update.getUpdateAssets();
		Launcher.LauncherStart();
	}
}