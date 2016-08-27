/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
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

package net.luxvacuos.voxel.universal.api;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.luxvacuos.voxel.universal.api.mod.IMod;

public class ModLoader {

	private File modsFolder;

	public void loadMods(ModsHandler modsHandler, String prefix) {
		modsFolder = new File(prefix + "/mods");
		if (!modsFolder.exists())
			modsFolder.mkdirs();
		try {
			Files.walk(Paths.get(modsFolder.toURI())).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					if (filePath.toFile().getAbsolutePath().endsWith(".jar")) {
						try {
							URLClassLoader child = new URLClassLoader(new URL[] { filePath.toFile().toURI().toURL() },
									this.getClass().getClassLoader());
							String name = filePath.getFileName().toString();
							name = name.substring(0, name.lastIndexOf('.'));
							Class<?> classToLoad;
							classToLoad = Class.forName("mod_" + name, true, child);
							modsHandler.addMod((IMod) classToLoad.newInstance());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
