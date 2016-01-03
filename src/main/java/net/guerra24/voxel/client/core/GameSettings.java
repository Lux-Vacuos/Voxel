/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.guerra24.voxel.client.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class GameSettings {

	private Properties prop;
	private File settings;

	private int version = 3;

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
			new File("assets/game/").mkdirs();
		}
		if (getVersion() == 1) {
			VoxelVariables.useShadows = Boolean.parseBoolean(getValue("useShadows"));
			VoxelVariables.useVolumetricLight = Boolean.parseBoolean(getValue("useVolumetricLight"));
			VoxelVariables.useFXAA = Boolean.parseBoolean(getValue("useFXAA"));
		} else if (getVersion() == 2) {
			VoxelVariables.useShadows = Boolean.parseBoolean(getValue("useShadows"));
			VoxelVariables.useVolumetricLight = Boolean.parseBoolean(getValue("useVolumetricLight"));
			VoxelVariables.useFXAA = Boolean.parseBoolean(getValue("useFXAA"));
			VoxelVariables.VSYNC = Boolean.parseBoolean(getValue("VSYNC"));
			VoxelVariables.FPS = Integer.parseInt(getValue("FPS"));
			VoxelVariables.UPS = Integer.parseInt(getValue("UPS"));
			VoxelVariables.radius = Integer.parseInt(getValue("DrawDistance"));
		} else if (getVersion() == 3) {
			VoxelVariables.useShadows = Boolean.parseBoolean(getValue("useShadows"));
			VoxelVariables.useVolumetricLight = Boolean.parseBoolean(getValue("useVolumetricLight"));
			VoxelVariables.useFXAA = Boolean.parseBoolean(getValue("useFXAA"));
			VoxelVariables.useMotionBlur = Boolean.parseBoolean(getValue("useMotionBlur"));
			VoxelVariables.useDOF = Boolean.parseBoolean(getValue("useDOF"));
			VoxelVariables.VSYNC = Boolean.parseBoolean(getValue("VSYNC"));
			VoxelVariables.FPS = Integer.parseInt(getValue("FPS"));
			VoxelVariables.UPS = Integer.parseInt(getValue("UPS"));
			VoxelVariables.radius = Integer.parseInt(getValue("DrawDistance"));
		} else {
			updateSetting();
			save();
		}
	}

	public void registerValue(String key, String data) {
		prop.setProperty(key, data);
	}

	public String getValue(String key) {
		String res = prop.getProperty(key);
		if (res == null)
			return "1";
		else
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
		registerValue("VSYNC", Boolean.toString(VoxelVariables.VSYNC));
		registerValue("FPS", Integer.toString(VoxelVariables.FPS));
		registerValue("UPS", Integer.toString(VoxelVariables.UPS));
		registerValue("DrawDistance", Integer.toString(VoxelVariables.radius));
	}

}
