package voxel.client.core.launcher.init;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import voxel.client.core.launcher.Constants;
import voxel.client.core.util.Logger;

public class Init {
	public static void InitLog() {
		try {
			System.setOut(new PrintStream(new FileOutputStream(
					Constants.logpath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void checkJava() {
		String javaVersion = System.getProperty("java.version");
		if (!javaVersion.startsWith("1.8"))
			throw new RuntimeException("JRE 1.8.0 "
					+ "is required to run the launcher.");
		Logger.log("JRE " + javaVersion + " found");
	}
	
	public static void printSystemInfo() {
		Logger.log("System Info");
		Logger.log(System.getProperty("os.name"));
		Logger.log("OS Version " + System.getProperty("os.version"));
		Logger.log("OS Arch " + System.getProperty("os.arch"));
	}
}
