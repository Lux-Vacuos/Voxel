package net.guerra24.voxel.client.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorCode;

public class PerRunLog extends FileAppender {

	private static SimpleDateFormat timeStampFormat;

	static {
		timeStampFormat = new SimpleDateFormat("yyyy-MM-dd-h-mm-ss-a");
	}

	public PerRunLog() {
	}

	public PerRunLog(Layout layout, String filename, boolean append, boolean bufferedIO, int bufferSize)
			throws IOException {
		super(layout, filename, append, bufferedIO, bufferSize);
	}

	public PerRunLog(Layout layout, String filename, boolean append) throws IOException {
		super(layout, filename, append);
	}

	public PerRunLog(Layout layout, String filename) throws IOException {
		super(layout, filename);
	}

	@Override
	public void activateOptions() {
		if (fileName != null) {
			try {
				fileName = getNewLogFileName();
				setFile(fileName, fileAppend, bufferedIO, bufferSize);
			} catch (Exception e) {
				errorHandler.error("Error while activating log options", e, ErrorCode.FILE_OPEN_FAILURE);
			}
		}
	}

	private String getNewLogFileName() {
		if (fileName != null) {
			final String DOT = ".";
			final String HIPHEN = "-";
			final File logFile = new File(fileName);
			final String fileName = logFile.getName();
			String newFileName = "";

			final int dotIndex = fileName.indexOf(DOT);
			if (dotIndex != -1) {
				newFileName = fileName.substring(0, dotIndex) + HIPHEN + timeStampFormat.format(new Date()) + DOT
						+ fileName.substring(dotIndex + 1, dotIndex + 4);
			} else {
				newFileName = fileName + HIPHEN + timeStampFormat.format(new Date());
			}
			return logFile.getParent() + File.separator + newFileName;
		}
		return null;
	}
}
