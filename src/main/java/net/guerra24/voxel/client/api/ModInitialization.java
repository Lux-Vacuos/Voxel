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

package net.guerra24.voxel.client.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.guerra24.voxel.client.api.mod.MoltenAPIInitPhase;
import net.guerra24.voxel.client.api.mod.MoltenAPIMod;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.util.Logger;

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
		Logger.log("Starting ModLoader Pre Init phase");
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
		Logger.log("Starting ModLoader Init phase");
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
		Logger.log("Starting ModLoader Post Init phase");
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
