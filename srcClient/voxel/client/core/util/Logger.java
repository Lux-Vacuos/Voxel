package voxel.client.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Logger {
	private Logger() {
	}

	public static void log(String... messages) {
		for (String message : messages)
			System.out.println("[INFO " + getTimeStamp() + "] " + message);
	}

	public static String getTimeStamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");

		return sdf.format(date);
	}

	public static void warn(String... messages) {
		for (String message : messages)
			System.err.println("[WARNING " + getTimeStamp() + "] " + message);
	}

	public static void error(String... messages) {
		for (String message : messages)
			System.err.println("[FATAL ERROR " + getTimeStamp() + "] "
					+ message);
	}
}
