package io.github.guerra24.voxel.client.kernel.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Logger {
	private static boolean printTimeStamps;
	private static SimpleDateFormat timeStampFormat;

	private Logger() {
	}

	static {
		setPrintTimeStamps(true);
		setTimeStampFormat(new SimpleDateFormat("MM/dd/yyyy h:mm:ss a"));
	}

	public static void log(Thread thread, Object... messages) {
		for (Object message : messages)
			System.out.println((printTimeStamps ? "[INFO " + getTimeStamp()
					+ "] " + "[" + thread.getName() + "]" : "")
					+ message);
	}

	public static void logS(Object... messages) {
		for (Object message : messages)
			System.out.println((printTimeStamps ? "[CLIENT " + getTimeStamp()
					+ "] " : "")
					+ message);
	}

	public static String getTimeStamp() {
		return timeStampFormat.format(new Date());
	}

	public static void warn(Thread thread, Object... messages) {
		for (Object message : messages)
			System.err.println((printTimeStamps ? "[WARNING " + getTimeStamp()
					+ "] " + "[" + thread.getName() + "]" : "")
					+ message);
	}

	public static void error(Thread thread, Object... messages) {
		for (Object message : messages)
			System.err
					.println((printTimeStamps ? "[FATAL ERROR "
							+ getTimeStamp() + "] " + "[" + thread.getName()
							+ "]" : "")
							+ message);
	}

	public static void setTimeStampFormat(SimpleDateFormat timeStampFormat) {
		Logger.timeStampFormat = timeStampFormat;
	}

	public static void setPrintTimeStamps(boolean printTimeStamps) {
		Logger.printTimeStamps = printTimeStamps;
	}
}