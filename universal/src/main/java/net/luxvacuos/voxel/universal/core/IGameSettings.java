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

package net.luxvacuos.voxel.universal.core;

import java.io.File;

/**
 * Interface for the various GameSettings classes Voxel can use
 * 
 * @author HACKhalo2 <hackhalotwo@gmail.com>
 *
 */
public interface IGameSettings {
	
	/**
	 * The current version of the settings file
	 */
	static final int VERSION = 7;
	
	/**
	 * Registers a data value with the associated key
	 * @param key Key
	 * @param data The data to store
	 */
	public void registerValue(String key, String data);
	
	/**
	 * Gets the associated value from the supplied key
	 * @param key Key
	 * @return Any data associated with the given key
	 */
	public String getValue(String key);
	
	/**
	 * Gets the associated value from the supplied key, or the default data back
	 * if no value is returned
	 * @param key Key
	 * @param defaultData The default data to use if no value was set with the supplied key
	 * @return Any data associated with the given key
	 */
	public String getValue(String key, String defaultData);
	
	/**
	 * Loads the settings from the disk
	 * @param settingsFile The settings File
	 */
	public void load(File settingsFile);
	
	/**
	 * Read the settings
	 */
	public void read();
	
	/**
	 * Saves the Settings to the disk
	 */
	public void save();
	
	/**
	 * Updates the setting values with the ones in the Runtime Data
	 */
	public void update();

}
