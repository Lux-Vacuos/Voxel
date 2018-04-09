/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.launcher.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javafx.stage.Stage;
import net.luxvacuos.adus.core.DownloadsHelper;
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
		Logger.log("Updating Launcher");
		new Thread(() -> {
			try {
				File p = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
				DownloadsHelper.download(p.toString(), "/launcher/launcher.jar");
				ProcessBuilder pb = new ProcessBuilder("java", "-jar", p.toString());
				pb.start();
				javafx.application.Platform.runLater(() -> stg.close());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

}
