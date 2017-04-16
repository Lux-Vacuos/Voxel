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

package net.luxvacuos.voxel.client.core;
import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.universal.core.AbstractGameSettings;

/**
 * 
 * Here all Voxel's settings are stored to a file.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @author HACKhalo2 <hackhalotwo@gmail.com>
 *
 */
public final class ClientGameSettings extends AbstractGameSettings {

	/**
	 * Create a GameSettings instance that will set the path, create the
	 * Properties object and check for existing file.
	 */
	public ClientGameSettings() {
	}

	@Override
	public void read() {
		Logger.log("Loading data to registry...");
			
		REGISTRY.register("/Voxel/Settings/Core/fps", 						Integer.parseInt(getValue("FPS", "60")));
		REGISTRY.register("/Voxel/Settings/Core/ups", 						Integer.parseInt(getValue("UPS", "60")));
		REGISTRY.register("/Voxel/Settings/Core/fov", 						Integer.parseInt(getValue("FOV", "90")));
		
		REGISTRY.register("/Voxel/Settings/Graphics/shadows",			 	Boolean.parseBoolean(getValue("useShadows", "false")));
		REGISTRY.register("/Voxel/Settings/Graphics/shadowsResolution", 	Integer.parseInt(getValue("shadowMapResolution", "1024")));
		REGISTRY.register("/Voxel/Settings/Graphics/shadowsDrawDistance", 	Integer.parseInt(getValue("shadowMapDrawDistance", "200")));
		REGISTRY.register("/Voxel/Settings/Graphics/volumetricLight", 		Boolean.parseBoolean(getValue("useVolumetricLight", "false")));
		REGISTRY.register("/Voxel/Settings/Graphics/fxaa", 					Boolean.parseBoolean(getValue("useFXAA", "false")));
		REGISTRY.register("/Voxel/Settings/Graphics/vsync", 				Boolean.parseBoolean(getValue("VSYNC", "false")));
		REGISTRY.register("/Voxel/Settings/Graphics/motionBlur", 			Boolean.parseBoolean(getValue("useMotionBlur", "false")));
		REGISTRY.register("/Voxel/Settings/Graphics/dof", 					Boolean.parseBoolean(getValue("useDOF", "false")));
		REGISTRY.register("/Voxel/Settings/Graphics/reflections", 			Boolean.parseBoolean(getValue("useReflections", "false")));
		REGISTRY.register("/Voxel/Settings/Graphics/parallax",				Boolean.parseBoolean(getValue("useParallax", "false")));
		REGISTRY.register("/Voxel/Settings/Graphics/ambientOcclusion", 		Boolean.parseBoolean(getValue("useAmbientOcclusion", "false")));
		REGISTRY.register("/Voxel/Settings/Graphics/chromaticAberration", 	Boolean.parseBoolean(getValue("useChromaticAberration", "false")));
		REGISTRY.register("/Voxel/Settings/Graphics/lensFlares", 			Boolean.parseBoolean(getValue("useLensFlares", "false")));
		REGISTRY.register("/Voxel/Settings/Graphics/pipeline", 				getValue("DeferredPipeline", "MultiPass"));
		REGISTRY.register("/Voxel/Settings/Graphics/assets", 				getValue("Assets", "voxel"));
		
		REGISTRY.register("/Voxel/Settings/WindowManager/invertButtons", 	Boolean.parseBoolean(getValue("WM_InvertButtons", "false")));
		REGISTRY.register("/Voxel/Settings/WindowManager/borderSize", 		Float.parseFloat(getValue("WM_BorderSize", "10")));
		REGISTRY.register("/Voxel/Settings/WindowManager/titleBarHeight", 	Float.parseFloat(getValue("WM_TitleBarHeight", "30")));
		
		REGISTRY.register("/Voxel/Settings/World/chunkRadius", 				Integer.parseInt(getValue("chunkRadius", "4")));
		
		Logger.log("Load completed");
	}

	/**
	 * Update values from runtime data
	 */
	@Override
	public void update() {
		Logger.log("Updating registry...");
		registerValue("SettingsVersion", Integer.toString(ClientGameSettings.VERSION));
		
		registerValue("FPS", 					Integer.toString((int) REGISTRY.getRegistryItem("/Voxel/Settings/Core/fps")));
		registerValue("UPS", 					Integer.toString((int) REGISTRY.getRegistryItem("/Voxel/Settings/Core/ups")));
		registerValue("FOV", 					Integer.toString((int) REGISTRY.getRegistryItem("/Voxel/Settings/Core/fov")));
		
		registerValue("useShadows", 			Boolean.toString((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/shadows")));
		registerValue("shadowMapResolution",  	Integer.toString((int) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/shadowsResolution")));
		registerValue("shadowMapDrawDistance", 	Integer.toString((int) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/shadowsDrawDistance")));
		registerValue("useVolumetricLight", 	Boolean.toString((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/volumetricLight")));
		registerValue("useFXAA",				Boolean.toString((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/fxaa")));
		registerValue("VSYNC",  				Boolean.toString((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/vsync")));
		registerValue("useMotionBlur", 			Boolean.toString((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/motionBlur")));
		registerValue("useDOF", 				Boolean.toString((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/dof")));
		registerValue("useReflections", 		Boolean.toString((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/reflections")));
		registerValue("useParallax", 			Boolean.toString((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/parallax")));
		registerValue("useAmbientOcclusion", 	Boolean.toString((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/ambientOcclusion")));
		registerValue("useChromaticAberration", Boolean.toString((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/chromaticAberration")));
		registerValue("useLensFlares", 			Boolean.toString((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/lensFlares")));
		registerValue("DeferredPipeline", 		(String) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/pipeline"));
		registerValue("Assets", 				(String) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/assets"));
		
		registerValue("WM_InvertButtons", 		Boolean.toString((boolean) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/invertButtons")));
		registerValue("WM_BorderSize", 			Float.toString((float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/borderSize")));
		registerValue("WM_TitleBarHeight", 		Float.toString((float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/titleBarHeight")));
		
		registerValue("chunkRadius", 			Integer.toString((int) REGISTRY.getRegistryItem("/Voxel/Settings/World/chunkRadius")));
		Logger.log("Updated completed");
	}

}
