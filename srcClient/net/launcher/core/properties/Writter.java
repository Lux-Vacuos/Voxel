package net.launcher.core.properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import net.launcher.core.Constants;
import net.launcher.core.logger.Logger;

public class Writter {
	public static void configWritter() {

		Properties prop = new Properties();
		OutputStream output = null;

		File f = new File(Constants.optionsPath);
		if (!f.exists()) {

			try {
				Logger.log("Creating config file");
				output = new FileOutputStream(Constants.optionsPath);
				prop.setProperty("IconPath", Constants.iconPath1);
				prop.setProperty("BackgroundPath", Constants.iconPath2);
				prop.setProperty("JarName", Constants.jarName);
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