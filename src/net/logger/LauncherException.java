package net.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class LauncherException extends RuntimeException {
	private static final long serialVersionUID = 8516388250664228299L;

	public LauncherException(String message) {
		super(message);
	}

	public static void reThrow(Throwable t) {
		Writer result = new StringWriter();
		PrintWriter printWriter = new PrintWriter(result);
		t.printStackTrace(printWriter);

		throw new LauncherException(result.toString());
	}
}
