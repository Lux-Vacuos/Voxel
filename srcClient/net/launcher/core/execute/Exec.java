package net.launcher.core.execute;

import net.launcher.core.logger.Logger;
import net.launcher.core.properties.Reader;

public class Exec {
	public static void Main() throws Exception {
		Process ps = Runtime.getRuntime().exec(
				new String[] { "java", "-jar", Reader.Jar});
		ps.waitFor();
		java.io.InputStream is = ps.getInputStream();
		byte b[] = new byte[is.available()];
		is.read(b, 0, b.length);
		Logger.log(new String(b));
	}
}
