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
	public ClientGameSettings() { }

	public void read() {
		// Parse Shadow, Volumetric Light and FXAA
		VoxelVariables.useShadows = Boolean.parseBoolean(getValue("useShadows", "false"));
		VoxelVariables.useVolumetricLight = Boolean.parseBoolean(getValue("useVolumetricLight", "false"));
		VoxelVariables.useFXAA = Boolean.parseBoolean(getValue("useFXAA", "false"));

		// Parse VSync, FPS, UPS and radius
		VoxelVariables.VSYNC = Boolean.parseBoolean(getValue("VSYNC", "false"));
		VoxelVariables.FPS = Integer.parseInt(getValue("FPS", "60"));
		VoxelVariables.UPS = Integer.parseInt(getValue("UPS", "60"));
		VoxelVariables.radius = Integer.parseInt(getValue("DrawDistance", "6"));

		// Parse Motion Blur and DoF
		VoxelVariables.useMotionBlur = Boolean.parseBoolean(getValue("useMotionBlur", "false"));
		VoxelVariables.useDOF = Boolean.parseBoolean(getValue("useDOF", "false"));
		
		// Parse Reflections and Parallax
		VoxelVariables.useReflections = Boolean.parseBoolean(getValue("useReflections", "false"));
		VoxelVariables.useParallax = Boolean.parseBoolean(getValue("useParallax", "false"));
		
		// Parse FoV
		VoxelVariables.FOV = Integer.parseInt(getValue("FOV", "90"));
		
		// Parse rendering pipeline
		VoxelVariables.renderingPipeline = getValue("RenderingPipeline", "MultiPass");
		
		// Parse assetsPack
		VoxelVariables.assets = getValue("Assets", "voxel");
	}

	/**
	 * Update values from runtime data
	 */
	@Override
	public void update() {
		registerValue("SettingsVersion", Integer.toString(ClientGameSettings.VERSION));
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
		registerValue("Assets", VoxelVariables.assets);
	}

}
