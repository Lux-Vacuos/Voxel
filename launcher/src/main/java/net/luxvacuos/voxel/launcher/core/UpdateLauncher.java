package net.luxvacuos.voxel.launcher.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javafx.stage.Stage;
import net.luxvacuos.voxel.launcher.bootstrap.Bootstrap;
import net.luxvacuos.voxel.launcher.updater.DownloadsHelper;
import net.luxvacuos.voxel.launcher.util.Logger;

public class UpdateLauncher {

	public UpdateLauncher() {
	}

	public boolean checkUpdate() {
		if (!LauncherVariables.apt) {
			URL url;
			String latest = LauncherVariables.VERSION;
			try {
				url = new URL(LauncherVariables.HOST + "/launcher/version");
				URLConnection conn = url.openConnection();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				latest = bufferedReader.readLine();
			} catch (IOException e) {
			}

			String[] vrs = latest.split("\\.");
			String[] vrsl = LauncherVariables.VERSION.split("\\.");
			if (Integer.parseInt(vrs[0]) > Integer.parseInt(vrsl[0])) {
				return true;
			}
			if (Integer.parseInt(vrs[0]) == Integer.parseInt(vrsl[0])
					&& Integer.parseInt(vrs[1]) > Integer.parseInt(vrsl[1])) {
				return true;
			}

			if (Integer.parseInt(vrs[0]) == Integer.parseInt(vrsl[0])
					&& Integer.parseInt(vrs[1]) == Integer.parseInt(vrsl[1])
					&& Integer.parseInt(vrs[2]) > Integer.parseInt(vrsl[2])) {
				return true;
			}
		}
		return false;
	}

	public void downloadAndRun(Stage stg) throws IOException {
		if (new File(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/launcher.jar").exists()) {
			ProcessBuilder pb = new ProcessBuilder("java", "-jar",
					Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/launcher.jar");
			pb.start();
			javafx.application.Platform.runLater(() -> stg.close());
			return;
		}
		Logger.log("Updating Launcher");
		new Thread(() -> {
			try {
				DownloadsHelper.download(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/launcher.jar",
						"/launcher/launcher.jar");
				ProcessBuilder pb = new ProcessBuilder("java", "-jar",
						Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/launcher.jar");
				pb.start();
				javafx.application.Platform.runLater(() -> stg.close());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

}
