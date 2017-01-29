/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

package net.luxvacuos.voxel.launcher.updater;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.luxvacuos.voxel.launcher.bootstrap.Bootstrap;
import net.luxvacuos.voxel.launcher.bootstrap.Bootstrap.Platform;
import net.luxvacuos.voxel.launcher.core.LauncherVariables;

public class VersionsManager {

	private static VersionsManager versionsManager;

	public static VersionsManager getVersionsManager() {
		if (versionsManager == null)
			versionsManager = new VersionsManager();
		return versionsManager;
	}

	private File local = new File(
			Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.CONFIG + "/local.json");
	private Gson gson;
	private RemoteVersions remoteVersions;
	private Version downloadingVersion = null;
	private boolean downloading = false;
	private boolean downloaded = false;
	private boolean launched = false;

	private VersionsManager() {
		gson = new Gson();
	}

	public void update() {
		new Thread(() -> {
			try {
				if (DownloadsHelper.download(
						Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.CONFIG
								+ "/remote.json",
						"/" + LauncherVariables.PROJECT + "/" + LauncherVariables.CONFIG + "/remote.json")) {
					File remote = new File(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/"
							+ LauncherVariables.CONFIG + "/remote.json");
					Files.copy(remote.toPath(), local.toPath(), REPLACE_EXISTING);
					remote.delete();
				}
				if (local.exists()) {
					remoteVersions = gson.fromJson(new FileReader(local), RemoteVersions.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void downloadAndRun(String version, VersionKey key)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		File fv = new File(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.CONFIG
				+ "/versions/" + version + "/");
		if (!fv.exists())
			fv.mkdirs();
		if (DownloadsHelper.download(
				Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.CONFIG + "/versions/"
						+ version + "/" + key.name + "-" + key.version + ".json",
				"/" + LauncherVariables.PROJECT + "/" + LauncherVariables.CONFIG + "/versions/" + version + "/"
						+ key.name + "-" + key.version + ".json")) {
			Version ver = gson
					.fromJson(
							new FileReader(
									Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.CONFIG
											+ "/versions/" + version + "/" + key.name + "-" + key.version + ".json"),
							Version.class);
			downloadingVersion = ver;
			downloading = true;
			ver.download();
			downloading = false;
			downloadingVersion = null;
			downloaded = true;

			ProcessBuilder pb;
			if (Bootstrap.getPlatform().equals(Platform.MACOSX)) {
				pb = new ProcessBuilder("java", "-XX:+UseG1GC", "-XstartOnFirstThread", "-Xmx1G", "-classpath",
						getClassPath(ver), "net.luxvacuos.voxel.client.bootstrap.Bootstrap", "-username",
						LauncherVariables.username, "-uuid", LauncherVariables.status.getUuid());
			} else {
				pb = new ProcessBuilder("java", "-XX:+UseG1GC", "-Xmx1G", "-classpath", getClassPath(ver),
						"net.luxvacuos.voxel.client.bootstrap.Bootstrap", "-username", LauncherVariables.username,
						"-uuid", LauncherVariables.status.getUuid());
			}
			pb.command().addAll(LauncherVariables.userArgs);
			try {
				launched = true;
				pb.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void downloadAndRun() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		String key = (String) remoteVersions.getVersions().keySet().toArray()[0];
		List<VersionKey> versions = remoteVersions.getVersions(key);
		downloadAndRun(key, versions.get(0));
	}

	private String getClassPath(Version ver) {
		StringBuilder builder = new StringBuilder();
		int size = ver.getLibs().size();
		int count = 0;
		builder.append(builder.append(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/"
				+ LauncherVariables.LIBRARIES + "/" + ver.getDomain() + "/" + ver.getName() + "/" + ver.getVersion()
				+ "/" + ver.getName() + "-" + ver.getVersion() + ".jar" + LauncherVariables.SEPARATOR));
		for (Library library : ver.getLibs()) {
			count++;
			builder.append(library.getClassPath());
			if (count == size)
				builder.append(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.LIBRARIES
						+ "/" + library.getDomain() + "/" + library.getName() + "/" + library.getVersion() + "/"
						+ library.getName() + "-" + library.getVersion() + ".jar");
			else
				builder.append(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.LIBRARIES
						+ "/" + library.getDomain() + "/" + library.getName() + "/" + library.getVersion() + "/"
						+ library.getName() + "-" + library.getVersion() + ".jar" + LauncherVariables.SEPARATOR);
		}
		return builder.toString();
	}

	public Version getDownloadingVersion() {
		return downloadingVersion;
	}

	public boolean isDownloaded() {
		return downloaded;
	}

	public boolean isDownloading() {
		return downloading;
	}

	public boolean isLaunched() {
		return launched;
	}

}
