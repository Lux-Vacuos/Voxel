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
		}
		if (getVersion() == 1) {
			VoxelVariables.useShadows = Boolean.parseBoolean(getValue("useShadows"));
			VoxelVariables.useVolumetricLight = Boolean.parseBoolean(getValue("useVolumetricLight"));
			VoxelVariables.useHQWater = Boolean.parseBoolean(getValue("useHQWater"));
			VoxelVariables.useFXAA = Boolean.parseBoolean(getValue("useFXAA"));
		} else if (getVersion() == 2) {
			VoxelVariables.useShadows = Boolean.parseBoolean(getValue("useShadows"));
			VoxelVariables.useVolumetricLight = Boolean.parseBoolean(getValue("useVolumetricLight"));
			VoxelVariables.useHQWater = Boolean.parseBoolean(getValue("useHQWater"));
			VoxelVariables.useFXAA = Boolean.parseBoolean(getValue("useFXAA"));
			VoxelVariables.VSYNC = Boolean.parseBoolean(getValue("VSYNC"));
			VoxelVariables.FPS = Integer.parseInt(getValue("FPS"));
			VoxelVariables.UPS = Integer.parseInt(getValue("UPS"));
			VoxelVariables.radius = Integer.parseInt(getValue("DrawDistance"));
		} else if (getVersion() == 3) {
			VoxelVariables.useShadows = Boolean.parseBoolean(getValue("useShadows"));
			VoxelVariables.useVolumetricLight = Boolean.parseBoolean(getValue("useVolumetricLight"));
			VoxelVariables.useHQWater = Boolean.parseBoolean(getValue("useHQWater"));
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
		registerValue("useHQWater", Boolean.toString(VoxelVariables.useHQWater));
		registerValue("useFXAA", Boolean.toString(VoxelVariables.useFXAA));
		registerValue("useMotionBlur", Boolean.toString(VoxelVariables.useMotionBlur));
		registerValue("useDOF", Boolean.toString(VoxelVariables.useDOF));
		registerValue("VSYNC", Boolean.toString(VoxelVariables.VSYNC));
		registerValue("FPS", Integer.toString(VoxelVariables.FPS));
		registerValue("UPS", Integer.toString(VoxelVariables.UPS));
		registerValue("DrawDistance", Integer.toString(VoxelVariables.radius));
	}

}
