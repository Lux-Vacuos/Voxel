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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.voxel.launcher.bootstrap.Bootstrap;
import net.luxvacuos.voxel.launcher.core.LauncherVariables;

/**
 * Object for handling versions in the launcher.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class Version {

	private String version;
	private String name;
	private String type;
	private String domain;
	private List<Library> libs;

	private transient float downloadProgress;

	/**
	 * 
	 * @param name
	 *            Project Name
	 * @param domain
	 *            Project Domain
	 * @param version
	 *            Project Version
	 * @param type
	 *            Type of Version
	 */
	public Version(String name, String domain, String version, String type) {
		this.name = name;
		this.domain = domain;
		this.version = version;
		this.type = type;
		libs = new ArrayList<>();
	}

	/**
	 * Downloads the jar from the server in {@link LauncherVariables#host} and
	 * their respective libraries and dependencies.
	 */
	public void download() {
		downloadProgress = 0;

		float add = 100f / getTotalLibs();
		add /= 100f;
		new File(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.LIBRARIES + "/" + domain
				+ "/" + name + "/" + version + "/").mkdirs();
		if (!new File(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.LIBRARIES + "/"
				+ domain + "/" + name + "/" + version + "/" + name + "-" + version + ".jar").exists())
			DownloadsHelper.download(
					Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.LIBRARIES + "/" + domain
							+ "/" + name + "/" + version + "/" + name + "-" + version + ".jar",
					"/" + LauncherVariables.PROJECT + "/" + LauncherVariables.LIBRARIES + "/" + domain + "/" + name
							+ "/" + version + "/" + name + "-" + version + ".jar");
		downloadProgress += add;
		for (Library library : libs) {
			library.download();
			downloadProgress += add * library.getTotalDeps();
		}
		downloadProgress = 1f;
	}

	public float getTotalLibs() {
		float total = 1;
		for (Library library : libs) {
			total += library.getTotalDeps();
		}
		return total;
	}

	public String getVersion() {
		return version;
	}

	public String getType() {
		return type;
	}

	public String getDomain() {
		return domain;
	}

	public String getName() {
		return name;
	}

	public List<Library> getLibs() {
		return libs;
	}

	public float getDownloadProgress() {
		return downloadProgress;
	}
}
