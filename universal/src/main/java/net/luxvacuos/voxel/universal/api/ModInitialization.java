/*
 * This file is part of AbstractVoxel
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

package net.luxvacuos.voxel.universal.api;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.universal.api.mod.MoltenAPIInitPhase;
import net.luxvacuos.voxel.universal.api.mod.MoltenAPIMod;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;

/**
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category API
 */
public class ModInitialization {
	/**
	 * Mods
	 */
	private ModLoader modLoader;
	private MoltenAPI moltenAPI;

	private Map<Class<?>, Object> instances;

	public ModInitialization(AbstractVoxel abstractVoxel) throws Exception {
		modLoader = new ModLoader();
		modLoader.loadMods(abstractVoxel.getBootstrap().getPrefix());
		moltenAPI = new MoltenAPI(abstractVoxel.getEngineType());
		instances = new HashMap<>();
	}

	public void preInit() throws Exception {
		Logger.log("Starting Mods Load");
		Logger.log("ModLoader Pre Init phase");
		for (Class<?> mod : modLoader.getModsClass()) {
			MoltenAPIMod info = (MoltenAPIMod) mod.getAnnotation(MoltenAPIMod.class);
			Logger.log("Mod: " + info.name());
			Logger.log("Author: " + info.createdBy());
			Logger.log("Required Molten API Version: " + info.requiredAPIVersion());
			if (info.requiredAPIVersion() > MoltenAPI.apiIntVersion)
				throw new VersionException(
						"The Mod " + info.name() + " requires an API version more than " + info.requiredAPIVersion());
			Method method = mod.getDeclaredMethod("preInit", MoltenAPI.class);
			if (method.isAnnotationPresent(MoltenAPIInitPhase.class)) {
				MoltenAPIInitPhase phase = (MoltenAPIInitPhase) method.getAnnotation(MoltenAPIInitPhase.class);
				if (phase.enabled()) {
					Object instance = mod.newInstance();
					method.invoke(instance, moltenAPI);
					instances.put(mod, instance);
				}
			}
		}
	}

	public void init() throws Exception {
		Logger.log("ModLoader Init phase");
		for (Class<?> mod : modLoader.getModsClass()) {
			Method method = mod.getDeclaredMethod("init", MoltenAPI.class);
			if (method.isAnnotationPresent(MoltenAPIInitPhase.class)) {
				MoltenAPIInitPhase test = (MoltenAPIInitPhase) method.getAnnotation(MoltenAPIInitPhase.class);
				if (test.enabled()) {
					method.invoke(instances.get(mod), moltenAPI);
				}
			}
		}
	}

	public void postInit() throws Exception {
		Logger.log("ModLoader Post Init phase");
		for (Class<?> mod : modLoader.getModsClass()) {
			Method method = mod.getDeclaredMethod("postInit", MoltenAPI.class);
			if (method.isAnnotationPresent(MoltenAPIInitPhase.class)) {
				MoltenAPIInitPhase test = (MoltenAPIInitPhase) method.getAnnotation(MoltenAPIInitPhase.class);
				if (test.enabled()) {
					method.invoke(instances.get(mod), moltenAPI);
				}
			}
		}
	}

	/**
	 * Dispose the API
	 */
	public void dispose() {
		modLoader.getModsClass().clear();
	}

}
