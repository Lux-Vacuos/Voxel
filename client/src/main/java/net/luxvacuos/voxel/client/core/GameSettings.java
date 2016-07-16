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

package net.luxvacuos.voxel.client.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.luxvacuos.voxel.client.bootstrap.Bootstrap;

/**
 * 
 * Here all Voxel's settings are stored to a file.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class GameSettings {

	/**
	 * Properties object used to store and load settings.
	 */
	private Properties prop;
	/**
	 * File path to store
	 */
	private File settings;
	/**
	 * Settings version
	 */
	private static int version = 6;

	/**
	 * Create a GameSettings instance that will set the path, create the
	 * Properties object and check for existing file.
	 */
	public GameSettings() {
		// Set settings path
		settings = new File(VoxelVariables.settings);
		// Create Properties
		prop = new Properties();
		// Check for existing file
		if (settings.exists()) {
			// Try load the file from the previus path
			try {
				prop.load(new FileInputStream(settings));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// otherwise create folders
		} else {
			new File(Bootstrap.getPrefix() + "voxel/config").mkdirs();
		}
		// Check for version 1
		if (getVersion() >= 1) {
			// Parse Shadow, Volumetric Light and FXAA
			VoxelVariables.useShadows = Boolean.parseBoolean(getValue("useShadows"));
			VoxelVariables.useVolumetricLight = Boolean.parseBoolean(getValue("useVolumetricLight"));
			VoxelVariables.useFXAA = Boolean.parseBoolean(getValue("useFXAA"));
		}
		// Check for version 2
		if (getVersion() >= 2) {
			// Parse VSync, FPS, UPS and radius
			VoxelVariables.VSYNC = Boolean.parseBoolean(getValue("VSYNC"));
			VoxelVariables.FPS = Integer.parseInt(getValue("FPS"));
			VoxelVariables.UPS = Integer.parseInt(getValue("UPS"));
			VoxelVariables.radius = Integer.parseInt(getValue("DrawDistance"));
		}
		// Check for version 3
		if (getVersion() >= 3) {
			// Parse Motion Blur and DoF
			VoxelVariables.useMotionBlur = Boolean.parseBoolean(getValue("useMotionBlur"));
			VoxelVariables.useDOF = Boolean.parseBoolean(getValue("useDOF"));
		}
		// Check for version 4
		if (getVersion() >= 4) {
			// Parse Reflections and Parallax
			VoxelVariables.useReflections = Boolean.parseBoolean(getValue("useReflections"));
			VoxelVariables.useParallax = Boolean.parseBoolean(getValue("useParallax"));
		}
		// Check for version 5
		if (getVersion() >= 5) {
			// Parse FoV
			VoxelVariables.FOV = Integer.parseInt(getValue("FOV"));
		}
		// Check for version 6
		if (getVersion() >= 6) {
			// Parse rendering pipeline
			VoxelVariables.renderingPipeline = getValue("RenderingPipeline");
		}
		// Update Settings
		updateSetting();
		// Save to file
		save();

	}

	/**
	 * Register a value
	 * 
	 * @param key
	 *            Key
	 * @param data
	 *            Data to store
	 */
	public void registerValue(String key, String data) {
		prop.setProperty(key, data);
	}

	/**
	 * Get a value
	 * 
	 * @param key
	 *            Value key
	 * @return Stored data
	 */
	public String getValue(String key) {
		String res = prop.getProperty(key);
		return res;
	}

	private int getVersion() {
		String b = getValue("SettingsVersion");
		if (b == null)
			b = "1";
		int a = Integer.parseInt(b);
		return a;
	}

	/**
	 * Save to file
	 */
	public void save() {
		try {
			prop.store(new FileOutputStream(settings), "Voxel Settings");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update values from runtime data
	 */
	public void updateSetting() {
		registerValue("SettingsVersion", Integer.toString(version));
		registerValue("useShadows", Boolean.toString(VoxelVariables.useShadows));
		registerValue("useVolumetricLight", Boolean.toString(VoxelVariables.useVolumetricLight));
		registerValue("useFXAA", Boolean.toString(VoxelVariables.useFXAA));
		registerValue("useMotionBlur", Boolean.toString(VoxelVariables.useMotionBlur));
		registerValue("useDOF", Boolean.toString(VoxelVariables.useDOF));
		registerValue("useReflections", Boolean.toString(VoxelVariables.useReflections));
		registerValue("useParallax", Boolean.toString(VoxelVariables.useParallax));
		registerValue("VSYNC", Boolean.toString(VoxelVariables.VSYNC));
		registerValue("FPS", Integer.toString(VoxelVariables.FPS));
		registerValue("UPS", Integer.toString(VoxelVariables.UPS));
		registerValue("DrawDistance", Integer.toString(VoxelVariables.radius));
		registerValue("FOV", Integer.toString(VoxelVariables.FOV));
		registerValue("RenderingPipeline", VoxelVariables.renderingPipeline);
	}

}
