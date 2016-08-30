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
	public ClientGameSettings() {
	}

	@Override
	public void read() {
		// Parse Shadow, Volumetric Light and FXAA
		ClientVariables.useShadows = Boolean.parseBoolean(getValue("useShadows", "false"));
		ClientVariables.useVolumetricLight = Boolean.parseBoolean(getValue("useVolumetricLight", "false"));
		ClientVariables.useFXAA = Boolean.parseBoolean(getValue("useFXAA", "false"));

		// Parse VSync, FPS, UPS and radius
		ClientVariables.VSYNC = Boolean.parseBoolean(getValue("VSYNC", "false"));
		ClientVariables.FPS = Integer.parseInt(getValue("FPS", "60"));
		ClientVariables.UPS = Integer.parseInt(getValue("UPS", "60"));
		ClientVariables.radius = Integer.parseInt(getValue("DrawDistance", "6"));

		// Parse Motion Blur and DoF
		ClientVariables.useMotionBlur = Boolean.parseBoolean(getValue("useMotionBlur", "false"));
		ClientVariables.useDOF = Boolean.parseBoolean(getValue("useDOF", "false"));

		// Parse Reflections and Parallax
		ClientVariables.useReflections = Boolean.parseBoolean(getValue("useReflections", "false"));
		ClientVariables.useParallax = Boolean.parseBoolean(getValue("useParallax", "false"));

		// Parse FoV
		ClientVariables.FOV = Integer.parseInt(getValue("FOV", "90"));

		// Parse rendering pipeline
		ClientVariables.renderingPipeline = getValue("RenderingPipeline", "MultiPass");

		// Parse assetsPack
		ClientVariables.assets = getValue("Assets", "voxel");
	}

	/**
	 * Update values from runtime data
	 */
	@Override
	public void update() {
		registerValue("SettingsVersion", Integer.toString(ClientGameSettings.VERSION));
		registerValue("useShadows", Boolean.toString(ClientVariables.useShadows));
		registerValue("useVolumetricLight", Boolean.toString(ClientVariables.useVolumetricLight));
		registerValue("useFXAA", Boolean.toString(ClientVariables.useFXAA));
		registerValue("useMotionBlur", Boolean.toString(ClientVariables.useMotionBlur));
		registerValue("useDOF", Boolean.toString(ClientVariables.useDOF));
		registerValue("useReflections", Boolean.toString(ClientVariables.useReflections));
		registerValue("useParallax", Boolean.toString(ClientVariables.useParallax));
		registerValue("VSYNC", Boolean.toString(ClientVariables.VSYNC));
		registerValue("FPS", Integer.toString(ClientVariables.FPS));
		registerValue("UPS", Integer.toString(ClientVariables.UPS));
		registerValue("DrawDistance", Integer.toString(ClientVariables.radius));
		registerValue("FOV", Integer.toString(ClientVariables.FOV));
		registerValue("RenderingPipeline", ClientVariables.renderingPipeline);
		registerValue("Assets", ClientVariables.assets);
	}

}
