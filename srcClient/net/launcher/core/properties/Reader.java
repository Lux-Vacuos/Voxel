package net.launcher.core.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.launcher.core.Constants;

public class Reader {
	
	public static String IconPath;
	public static String BackPath;
	public static String Jar;
	
	public static void configReader() {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(Constants.optionsPath);

			prop.load(input);
			
			IconPath = prop.getProperty("IconPath");
			BackPath = prop.getProperty("BackgroundPath");
			Jar = prop.getProperty("JarName");
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}