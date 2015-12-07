/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
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

import net.guerra24.voxel.client.api.mod.MoltenAPIInitPhase;
import net.guerra24.voxel.client.api.mod.MoltenAPIMod;
import net.guerra24.voxel.client.core.GameSettings;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.util.Logger;
import net.guerra24.voxel.client.world.MobManager;
import net.guerra24.voxel.client.world.entities.IEntity;

/**
 * API
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category API
 */
public class API {
	/**
	 * Mods
	 */
	private ModLoader modLoader;
	private MobManager mobManager;
	private GameSettings gameSettings;

	public API(GameSettings gameSettings) {
		modLoader = new ModLoader();
		modLoader.loadMods();
		this.gameSettings = gameSettings;
	}

	/**
	 * Pre initialize the mod, load config data
	 * 
	 * @throws VersionException
	 * 
	 */
	public void preInit() throws VersionException {
		for (Class<?> mod : modLoader.getModsClass()) {
			Annotation annotation = mod.getAnnotation(MoltenAPIMod.class);
			MoltenAPIMod info = (MoltenAPIMod) annotation;
			Logger.log("Mod: " + info.name());
			Logger.log("Author: " + info.createdBy());
			Logger.log("Required Molten API Verion: " + info.requiredAPIVersion());
			if (info.requiredAPIVersion() < VoxelVariables.apiVersionNum)
				throw new VersionException("THE MOD " + info.name() + " REQUIRES AN API VERSION EQUAL OR MORE THAT "
						+ info.requiredAPIVersion());
			Method method;
			try {
				method = mod.getDeclaredMethod("preInit", API.class);
				if (method.isAnnotationPresent(MoltenAPIInitPhase.class)) {
					Annotation annotation_ = method.getAnnotation(MoltenAPIInitPhase.class);
					MoltenAPIInitPhase phase = (MoltenAPIInitPhase) annotation_;
					if (phase.enabled()) {
						Object instance = mod.newInstance();
						method.invoke(instance, this);
					}
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initialize the mod, load textures
	 * 
	 */
	public void init() throws VersionException {
		for (Class<?> mod : modLoader.getModsClass()) {
			Method method;
			try {
				method = mod.getDeclaredMethod("init", API.class);
				if (method.isAnnotationPresent(MoltenAPIInitPhase.class)) {
					Annotation annotation_ = method.getAnnotation(MoltenAPIInitPhase.class);
					MoltenAPIInitPhase test = (MoltenAPIInitPhase) annotation_;
					if (test.enabled()) {
						Object instance = mod.newInstance();
						method.invoke(instance, this);
					}
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Post initialize the mod, register all mod data
	 * 
	 */
	public void postInit() throws VersionException {
		for (Class<?> mod : modLoader.getModsClass()) {
			Method method;
			try {
				method = mod.getDeclaredMethod("postInit", API.class);
				if (method.isAnnotationPresent(MoltenAPIInitPhase.class)) {
					Annotation annotation_ = method.getAnnotation(MoltenAPIInitPhase.class);
					MoltenAPIInitPhase test = (MoltenAPIInitPhase) annotation_;
					if (test.enabled()) {
						Object instance = mod.newInstance();
						method.invoke(instance, this);
					}
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
		}
	}

	public void registetMob(IEntity mob) {
		mobManager.registerMob(mob);
	}

	public void registerSaveData(String key, String value) {
		gameSettings.registerValue(key, value);
	}

	/**
	 * Dispose the API
	 * 
	 */
	public void dispose() {
	}

	public void setMobManager(MobManager mobManager) {
		this.mobManager = mobManager;
	}

}
