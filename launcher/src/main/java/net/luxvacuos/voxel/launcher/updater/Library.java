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
 * Library, virtual representation of a library.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class Library {

	private String name;
	private String version;
	private String domain;
	private List<Library> dependencies;

	/**
	 * 
	 * @param name
	 *            Library Name
	 * @param domain
	 *            Library Domain
	 * @param version
	 *            Library Version
	 */
	public Library(String name, String domain, String version) {
		this.name = name;
		this.version = version;
		this.domain = domain;
		dependencies = new ArrayList<>();
	}

	/**
	 * Download the library and the dependencies
	 */
	public void download() {
		new File(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.LIBRARIES + "/" + domain
				+ "/" + name + "/" + version + "/").mkdirs();
		if (!new File(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.LIBRARIES + "/"
				+ domain + "/" + name + "/" + version + "/" + name + "-" + version + ".jar").exists())
			DownloadsHelper.download(
					Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.LIBRARIES + "/" + domain
							+ "/" + name + "/" + version + "/" + name + "-" + version + ".jar",
					"/" + LauncherVariables.PROJECT + "/" + LauncherVariables.LIBRARIES + "/" + domain + "/" + name
							+ "/" + version + "/" + name + "-" + version + ".jar");
		for (Library library : dependencies) {
			library.download();
		}
	}

	public float getTotalDeps() {
		float total = 1f;
		for (Library library : dependencies) {
			total += library.getTotalDeps();
		}
		return total;
	}

	public String getName() {
		return name;
	}

	public String getDomain() {
		return domain;
	}

	public String getVersion() {
		return version;
	}

	public List<Library> getDependencies() {
		return dependencies;
	}

	public String getClassPath() {
		StringBuilder builder = new StringBuilder();
		for (Library library : getDependencies()) {
			builder.append(Bootstrap.getPrefix() + LauncherVariables.PROJECT + "/" + LauncherVariables.LIBRARIES + "/" + library.getDomain() + "/"
					+ library.getName() + "/" + library.getVersion() + "/" + library.getName() + "-"
					+ library.getVersion() + ".jar" + LauncherVariables.SEPARATOR);
			builder.append(library.getClassPath());
		}
		return builder.toString();
	}

}
