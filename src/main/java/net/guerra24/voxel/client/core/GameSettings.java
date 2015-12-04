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
			VoxelVariables.useShadows = Boolean.parseBoolean(getValue("useShadows"));
			VoxelVariables.useVolumetricLight = Boolean.parseBoolean(getValue("useVolumetricLight"));
			VoxelVariables.useHQWater = Boolean.parseBoolean(getValue("useHQWater"));
			VoxelVariables.useFXAA = Boolean.parseBoolean(getValue("useFXAA"));
		} else {
			updateSetting();
			save();
		}
	}

	public void registerValue(String key, String data) {
		prop.setProperty(key, data);
	}

	public String getValue(String key) {
		return prop.getProperty(key);
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
		registerValue("useShadows", Boolean.toString(VoxelVariables.useShadows));
		registerValue("useVolumetricLight", Boolean.toString(VoxelVariables.useVolumetricLight));
		registerValue("useHQWater", Boolean.toString(VoxelVariables.useHQWater));
		registerValue("useFXAA", Boolean.toString(VoxelVariables.useFXAA));
	}

}
