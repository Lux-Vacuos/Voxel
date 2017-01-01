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

package net.luxvacuos.voxel.universal.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * An Abstract version of GameSettings classes can extend
 * 
 * @author HACKhalo2 <hackhalotwo@gmail.com>
 *
 */
public abstract class AbstractGameSettings implements IGameSettings {

	/**
	 * The Properties file used to load and save data
	 */
	protected Properties prop = new Properties();

	/**
	 * The File path to the Properties File
	 */
	protected File settings;

	// Make sure the constructor isn't public for the Abstract class
	protected AbstractGameSettings() {
	}

	@Override
	public void registerValue(String key, String data) {
		this.prop.setProperty(key, data);
	}

	@Override
	public String getValue(String key) {
		return this.getValue(key, "");
	}

	@Override
	public String getValue(String key, String defaultData) {
		if (this.prop.containsKey(key))
			return this.prop.getProperty(key);

		return defaultData;
	}

	@Override
	public void save() {
		try {
			this.prop.store(new FileOutputStream(this.settings), "Voxel Settings");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void load(File settingsFile) {
		this.settings = settingsFile; // Save the settings file to the class
										// variable

		// Make sure the file actually exists on disk, and create it if needed
		if (this.settings.exists()) {
			InputStream in = null;
			try {
				in = new BufferedInputStream(new FileInputStream(this.settings));
				this.prop.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close(); // Always close an InputStream
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			// The file doesn't exist, make the directories that it needs
			String absolutePath = this.settings.getAbsolutePath();
			String filePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
			new File(filePath).mkdirs(); // XXX: Might need to have some
											// condition checking for this?
		}
	}

	protected int getVersion() {
		return Integer.parseInt(getValue("SettingsVersion", "1"));
	}

}
