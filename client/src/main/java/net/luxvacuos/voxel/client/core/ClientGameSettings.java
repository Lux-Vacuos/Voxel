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
import static net.luxvacuos.voxel.universal.core.subsystems.CoreSubsystem.REGISTRY;

import net.luxvacuos.voxel.universal.core.AbstractGameSettings;
import net.luxvacuos.voxel.universal.util.registry.Key;

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
		REGISTRY.register(new Key("/Voxel/Settings/Core/fps", true), 						60);
		REGISTRY.register(new Key("/Voxel/Settings/Core/ups", true), 						60);
		REGISTRY.register(new Key("/Voxel/Settings/Core/fov", true), 						90);
		
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/shadows", true),				false);
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/shadowsResolution", true), 		1024);
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/shadowsDrawDistance", true), 	200);
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/volumetricLight", true), 		false);
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/fxaa", true), 					false);
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/vsync", true), 					false);
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/motionBlur", true), 			false);
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/dof", true), 					false);
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/reflections", true), 			false);
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/parallax", true),				false);
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/ambientOcclusion", true), 		false);
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/chromaticAberration", true), 	false);
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/lensFlares", true), 			false);
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/pipeline", true), 				"MultiPass");
		REGISTRY.register(new Key("/Voxel/Settings/Graphics/assets", true), 				"voxel");
		
		REGISTRY.register(new Key("/Voxel/Settings/WindowManager/invertButtons", true), 	false);
		REGISTRY.register(new Key("/Voxel/Settings/WindowManager/borderSize", true), 		10f);
		REGISTRY.register(new Key("/Voxel/Settings/WindowManager/titleBarHeight", true), 	30f);
		REGISTRY.register(new Key("/Voxel/Settings/WindowManager/scrollBarSize", true), 	16f);
		REGISTRY.register(new Key("/Voxel/Settings/WindowManager/titleBarBorder", true), 	false);
		REGISTRY.register(new Key("/Voxel/Settings/WindowManager/theme", true), 			"Nano");
		
		REGISTRY.register(new Key("/Voxel/Settings/World/chunkRadius", true), 				4);
		REGISTRY.register(new Key("/Voxel/Settings/World/chunkManagerThreads", true), 		3);
	}

	/**
	 * Update values from runtime data
	 */
	@Override
	public void update() {
	}

}
