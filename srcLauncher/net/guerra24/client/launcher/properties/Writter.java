package net.guerra24.client.launcher.properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import net.guerra24.client.launcher.ConstantsLauncher;
import net.guerra24.voxel.client.engine.util.Logger;

public class Writter {
	public static void configWritter() {

		Properties prop = new Properties();
		OutputStream output = null;

		File f = new File(ConstantsLauncher.optionsPath);
		if (!f.exists()) {

			try {
				Logger.log("Creating config file");
				output = new FileOutputStream(ConstantsLauncher.optionsPath);
				prop.setProperty("IconPath", ConstantsLauncher.iconPath1);
				prop.setProperty("BackgroundPath", ConstantsLauncher.iconPath2);
				prop.store(output, null);

			} catch (IOException io) {
				io.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}
}