package net.launcher.core.updater.downloader;

import net.launcher.core.Constants;
import net.launcher.core.logger.Logger;
import net.launcher.core.login.Login;
import net.launcher.core.properties.Reader;

public class AssetsDownloader {
	public static void Assets() {
		try {
			Downloader.download(Constants.download1, Reader.IconPath, false,
					false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			Downloader.download(Constants.download2, Reader.BackPath,
					false, false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			Logger.log("Downloading user info");
			Downloader.download(
					Constants.userInfo,
					Login.infoPath, false, false);// Specific the web host of the
											// username and pass file
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
