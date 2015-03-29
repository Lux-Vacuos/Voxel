package voxel.client.core.launcher.updater.downloader;

import voxel.client.core.launcher.ConstantsLauncher;
import voxel.client.core.launcher.login.Login;
import voxel.client.core.launcher.properties.Reader;
import voxel.client.engine.util.Logger;

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
