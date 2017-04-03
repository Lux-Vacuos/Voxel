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

import net.luxvacuos.voxel.client.rendering.api.nanovg.WM;
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
		ClientVariables.useShadows = Boolean.parseBoolean(getValue("useShadows", "false"));
		ClientVariables.useVolumetricLight = Boolean.parseBoolean(getValue("useVolumetricLight", "false"));
		ClientVariables.useFXAA = Boolean.parseBoolean(getValue("useFXAA", "false"));
		ClientVariables.VSYNC = Boolean.parseBoolean(getValue("VSYNC", "false"));
		ClientVariables.FPS = Integer.parseInt(getValue("FPS", "60"));
		ClientVariables.UPS = Integer.parseInt(getValue("UPS", "60"));
		ClientVariables.chunk_radius = Integer.parseInt(getValue("chunkRadius", "4"));
		ClientVariables.useMotionBlur = Boolean.parseBoolean(getValue("useMotionBlur", "false"));
		ClientVariables.useDOF = Boolean.parseBoolean(getValue("useDOF", "false"));
		ClientVariables.useReflections = Boolean.parseBoolean(getValue("useReflections", "false"));
		ClientVariables.useParallax = Boolean.parseBoolean(getValue("useParallax", "false"));
		ClientVariables.FOV = Integer.parseInt(getValue("FOV", "90"));
		ClientVariables.renderingPipeline = getValue("DeferredPipeline", "MultiPass");
		ClientVariables.assets = getValue("Assets", "voxel");
		ClientVariables.useAmbientOcclusion = Boolean.parseBoolean(getValue("useAmbientOcclusion", "false"));
		ClientVariables.shadowMapResolution = Integer.parseInt(getValue("shadowMapResolution", "1024"));
		ClientVariables.shadowMapDrawDistance = Integer.parseInt(getValue("shadowMapDrawDistance", "200"));
		ClientVariables.useChromaticAberration = Boolean.parseBoolean(getValue("useChromaticAberration", "false"));
		ClientVariables.useLensFlares = Boolean.parseBoolean(getValue("useLensFlares", "false"));
		WM.invertWindowButtons = Boolean.parseBoolean(getValue("WM_InvertButtons", "false"));
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
		registerValue("useAmbientOcclusion", Boolean.toString(ClientVariables.useAmbientOcclusion));
		registerValue("VSYNC", Boolean.toString(ClientVariables.VSYNC));
		registerValue("FPS", Integer.toString(ClientVariables.FPS));
		registerValue("UPS", Integer.toString(ClientVariables.UPS));
		registerValue("chunkRadius", Integer.toString(ClientVariables.chunk_radius));
		registerValue("FOV", Integer.toString(ClientVariables.FOV));
		registerValue("DeferredPipeline", ClientVariables.renderingPipeline);
		registerValue("Assets", ClientVariables.assets);
		registerValue("shadowMapResolution", Integer.toString(ClientVariables.shadowMapResolution));
		registerValue("shadowMapDrawDistance", Integer.toString(ClientVariables.shadowMapDrawDistance));
		registerValue("useChromaticAberration", Boolean.toString(ClientVariables.useChromaticAberration));
		registerValue("useLensFlares", Boolean.toString(ClientVariables.useLensFlares));
		registerValue("WM_InvertButtons", Boolean.toString(WM.invertWindowButtons));
	}

}
