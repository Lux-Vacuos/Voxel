package net.launcher.core;

import net.launcher.core.init.Init;
import net.launcher.core.logger.Logger;
import net.launcher.core.properties.Reader;
import net.launcher.core.properties.Writter;
import net.launcher.core.updater.CreateDir;
import net.launcher.core.updater.Update;

public class Main {
	public static void main(String[] args) {
		Init.InitLog();
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