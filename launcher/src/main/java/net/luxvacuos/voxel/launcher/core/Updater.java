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

package net.luxvacuos.voxel.launcher.core;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.luxvacuos.voxel.launcher.bootstrap.Bootstrap;
import net.luxvacuos.voxel.launcher.bootstrap.Bootstrap.Platform;

public class Updater {

	private static Updater updater;

	public static Updater getUpdater() {
		if (updater == null)
			updater = new Updater();
		return updater;
	}

	private Gson gson;
	private VersionsHandler versionsHandler;
	private File local = new File(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/CONFIG/local.json");
	private Version downloadingVersion = null;
	private boolean downloading = false;
	private boolean downloaded = false;
	private boolean launched = false;

	private Updater() {
		gson = new Gson();
		new File(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.CONFIG + "/").mkdirs();
		new File(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.CONFIG + "/versions/")
				.mkdirs();
	}

	public void downloadAndRun() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		VersionKey key = versionsHandler.getVersions().get(0);
		if (DownloadsHelper.download(
				Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.CONFIG + "/versions/"
						+ key.name + "-" + key.version + ".json",
				"/" + LauncherVariables.PROJECT + "/" + LauncherVariables.CONFIG + "/versions/" + key.name + "-"
						+ key.version + ".json")) {
			Version ver = gson
					.fromJson(
							new FileReader(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/"
									+ LauncherVariables.CONFIG + "/versions/" + key.name + "-" + key.version + ".json"),
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
						LauncherVariables.username);
			} else {
				pb = new ProcessBuilder("java", "-XX:+UseG1GC", "-Xmx1G", "-classpath", getClassPath(ver),
						"net.luxvacuos.voxel.client.bootstrap.Bootstrap", "-username", LauncherVariables.username);
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

	public void getRemoteVersions() {
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
					versionsHandler = gson.fromJson(new FileReader(local), VersionsHandler.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
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

	public boolean isDownloading() {
		return downloading;
	}

	public Version getDownloadingVersion() {
		return downloadingVersion;
	}

	public boolean isDownloaded() {
		return downloaded;
	}

	public boolean isLaunched() {
		return launched;
	}

	public VersionsHandler getVersionsHandler() {
		return versionsHandler;
	}

}
