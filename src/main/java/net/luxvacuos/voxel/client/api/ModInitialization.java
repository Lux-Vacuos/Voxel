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

package net.luxvacuos.voxel.client.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.api.mod.MoltenAPIInitPhase;
import net.luxvacuos.voxel.client.api.mod.MoltenAPIMod;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.resources.GameResources;

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

	public ModInitialization(GameResources gm) {
		modLoader = new ModLoader();
		modLoader.loadMods();
		moltenAPI = new MoltenAPI(gm);
		instances = new HashMap<>();
	}

	/**
	 * Pre initialize the mod, load config data
	 * 
	 * @throws VersionException
	 * 
	 */
	public void preInit() throws VersionException {
		Logger.log("Starting Mods Load");
		Logger.log("ModLoader Pre Init phase");
		for (Class<?> mod : modLoader.getModsClass()) {
			Annotation annotation = mod.getAnnotation(MoltenAPIMod.class);
			MoltenAPIMod info = (MoltenAPIMod) annotation;
			Logger.log("Mod: " + info.name());
			Logger.log("Author: " + info.createdBy());
			Logger.log("Required Molten API Verion: " + info.requiredAPIVersion());
			if (info.requiredAPIVersion() > VoxelVariables.apiVersionNum)
				throw new VersionException("THE MOD " + info.name() + " REQUIRES AN API VERSION EQUAL OR MORE THAT "
						+ info.requiredAPIVersion());
			Method method;
			try {
				method = mod.getDeclaredMethod("preInit", MoltenAPI.class);
				if (method.isAnnotationPresent(MoltenAPIInitPhase.class)) {
					Annotation annotation_ = method.getAnnotation(MoltenAPIInitPhase.class);
					MoltenAPIInitPhase phase = (MoltenAPIInitPhase) annotation_;
					if (phase.enabled()) {
						Object instance = mod.newInstance();
						method.invoke(instance, moltenAPI);
						instances.put(mod, instance);
					}
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | InstantiationException | NoClassDefFoundError e) {
				Logger.error("Error in PreInit phase");
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	/**
	 * Initialize the mod, load textures
	 * 
	 * @throws VersionException
	 * 
	 */
	public void init() throws VersionException {
		Logger.log("ModLoader Init phase");
		for (Class<?> mod : modLoader.getModsClass()) {
			Method method;
			try {
				method = mod.getDeclaredMethod("init", MoltenAPI.class);
				if (method.isAnnotationPresent(MoltenAPIInitPhase.class)) {
					Annotation annotation_ = method.getAnnotation(MoltenAPIInitPhase.class);
					MoltenAPIInitPhase test = (MoltenAPIInitPhase) annotation_;
					if (test.enabled()) {
						Object instance = instances.get(mod);
						method.invoke(instance, moltenAPI);
					}
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoClassDefFoundError e) {
				Logger.error("Error in Init phase");
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	/**
	 * Post initialize the mod, register all mod data
	 * 
	 * @throws VersionException
	 * 
	 */
	public void postInit() throws VersionException {
		Logger.log("ModLoader Post Init phase");
		for (Class<?> mod : modLoader.getModsClass()) {
			Method method;
			try {
				method = mod.getDeclaredMethod("postInit", MoltenAPI.class);
				if (method.isAnnotationPresent(MoltenAPIInitPhase.class)) {
					Annotation annotation_ = method.getAnnotation(MoltenAPIInitPhase.class);
					MoltenAPIInitPhase test = (MoltenAPIInitPhase) annotation_;
					if (test.enabled()) {
						Object instance = instances.get(mod);
						method.invoke(instance, moltenAPI);
					}
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoClassDefFoundError e) {
				Logger.error("Error in PostInit phase");
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	/**
	 * Dispose the API
	 */
	public void dispose() {
		modLoader.getModsClass().clear();
	}

	public MoltenAPI getMoltenAPI() {
		return moltenAPI;
	}

}
