package net.guerra24.client.launcher;

import net.guerra24.client.launcher.init.Init;
import net.guerra24.client.launcher.properties.Reader;
import net.guerra24.client.launcher.properties.Writter;
import net.guerra24.client.launcher.updater.CreateDir;
import net.guerra24.client.launcher.updater.Update;
import net.guerra24.voxel.client.engine.util.Logger;

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