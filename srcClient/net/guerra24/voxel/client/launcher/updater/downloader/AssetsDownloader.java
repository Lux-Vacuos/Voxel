package net.guerra24.voxel.client.launcher.updater.downloader;

import net.guerra24.voxel.client.kernel.util.Logger;
import net.guerra24.voxel.client.launcher.ConstantsLauncher;
import net.guerra24.voxel.client.launcher.login.Login;
import net.guerra24.voxel.client.launcher.properties.Reader;

public class AssetsDownloader {
	public static void Assets() {
		try {
			Downloader.download(ConstantsLauncher.download1, Reader.IconPath,
					false, false);
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
			Downloader.download(ConstantsLauncher.userInfo, Login.infoPath,
					false, false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
