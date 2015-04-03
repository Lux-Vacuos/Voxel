package net.guerra24.client.launcher.updater.downloader;

import net.guerra24.client.launcher.ConstantsLauncher;
import net.guerra24.client.launcher.login.Login;
import net.guerra24.client.launcher.properties.Reader;
import net.guerra24.voxel.client.engine.util.Logger;

public class AssetsDownloader {
	public static void Assets() {
		try {
			Downloader.download(ConstantsLauncher.download1, Reader.IconPath, false,
					false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			Downloader.download(ConstantsLauncher.download2, Reader.BackPath,
					false, false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			Logger.log("Downloading user info");
			Downloader.download(
					ConstantsLauncher.userInfo,
					Login.infoPath, false, false);// Specific the web host of the
											// username and pass file
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
