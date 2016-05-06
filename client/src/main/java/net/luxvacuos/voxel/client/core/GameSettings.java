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

public class GameSettings {

	private Properties prop;
	private File settings;

	private int version = 6;

	public GameSettings() {
		settings = new File(VoxelVariables.settings);
		prop = new Properties();
		if (settings.exists()) {
			try {
				prop.load(new FileInputStream(settings));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			new File(Bootstrap.getPrefix() + "voxel/").mkdirs();
		}
		if (getVersion() >= 1) {
			VoxelVariables.useShadows = Boolean.parseBoolean(getValue("useShadows"));
			VoxelVariables.useVolumetricLight = Boolean.parseBoolean(getValue("useVolumetricLight"));
			VoxelVariables.useFXAA = Boolean.parseBoolean(getValue("useFXAA"));
		}
		if (getVersion() >= 2) {
			VoxelVariables.VSYNC = Boolean.parseBoolean(getValue("VSYNC"));
			VoxelVariables.FPS = Integer.parseInt(getValue("FPS"));
			VoxelVariables.UPS = Integer.parseInt(getValue("UPS"));
			VoxelVariables.radius = Integer.parseInt(getValue("DrawDistance"));
		}
		if (getVersion() >= 3) {
			VoxelVariables.useMotionBlur = Boolean.parseBoolean(getValue("useMotionBlur"));
			VoxelVariables.useDOF = Boolean.parseBoolean(getValue("useDOF"));
		}
		if (getVersion() >= 4) {
			VoxelVariables.useReflections = Boolean.parseBoolean(getValue("useReflections"));
			VoxelVariables.useParallax = Boolean.parseBoolean(getValue("useParallax"));
		}
		if (getVersion() >= 5) {
			VoxelVariables.FOV = Integer.parseInt(getValue("FOV"));
		}
		if (getVersion() >= 6) {
			VoxelVariables.renderingPipeline = getValue("RenderingPipeline");
		}
		updateSetting();
		save();

	}

	public void registerValue(String key, String data) {
		prop.setProperty(key, data);
	}

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

	public void save() {
		try {
			prop.store(new FileOutputStream(settings), "Voxel Settings");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
