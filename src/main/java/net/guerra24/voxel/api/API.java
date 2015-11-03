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

package net.guerra24.voxel.api;

import java.util.HashMap;
import java.util.Map;

import net.guerra24.voxel.api.mod.Mod;
import net.guerra24.voxel.util.Logger;
import net.guerra24.voxel.world.MobManager;
import net.guerra24.voxel.world.entities.IEntity;

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
	private static Map<Integer, Mod> mods;
	private static ModLoader modLoader;
	private static MobManager mobManager;

	public API() {
		mods = new HashMap<Integer, Mod>();
		modLoader = new ModLoader();
		modLoader.loadMods();
	}

	/**
	 * Pre initialize the mod, load config data
	 * 
	 */
	public void preInit() {
		Logger.log("Pre Initializing Mods");
		for (int x = 0; x < mods.size(); x++) {
			try {
				mods.get(x).preInit();
			} catch (NoSuchMethodError e) {
				Logger.warn("Mod " + mods.get(x).getName() + " has failed to load in PreInit");
				Logger.warn("Method not found");
				e.printStackTrace();
			} catch (NoSuchFieldError e) {
				Logger.warn("Mod " + mods.get(x).getName() + " has failed to load in PreInit");
				Logger.warn("Field not found");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initialize the mod, load textures
	 * 
	 */
	public void init() {
		Logger.log("Initializing Mods");
		for (int x = 0; x < mods.size(); x++) {
			try {
				mods.get(x).init();
			} catch (NoSuchMethodError e) {
				Logger.warn("Mod " + mods.get(x).getName() + " has failed to load in Init");
				Logger.warn("Method not found");
				e.printStackTrace();
			} catch (NoSuchFieldError e) {
				Logger.warn("Mod " + mods.get(x).getName() + " has failed to load in Init");
				Logger.warn("Field not found");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Post initialize the mod, register all mod data
	 * 
	 */
	public void postInit() {
		Logger.log("Post Initializing Mods");
		for (int x = 0; x < mods.size(); x++) {
			try {
				mods.get(x).postInit();
				Logger.log("Succesfully Loaded: " + mods.get(x).getName() + " ID: " + mods.get(x).getID());
			} catch (NoSuchMethodError e) {
				Logger.warn("Mod " + mods.get(x).getName() + " has failed to load in PostInit");
				Logger.warn("Method not found");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Registers the mod to the code
	 * 
	 * @param id
	 *            Mod ID
	 * @param result
	 *            Mod
	 */
	public static void registerMod(Mod mod) {
		mods.put(mod.getID(), mod);
	}

	public static void registetMob(IEntity mob) {
		mobManager.registerMob(mob);
	}

	/**
	 * Gets the Mod from ID
	 * 
	 * @param id
	 *            ID
	 * @return Mod
	 * 
	 */
	public static Mod getMod(int id) {
		return mods.get(id);
	}

	/**
	 * Get last avaiable ID
	 * 
	 * @return ID
	 */
	public static int getLastID() {
		return mods.size();
	}

	/**
	 * Dispose the API
	 * 
	 */
	public void dispose() {
		mods.clear();
	}

	public static ModLoader getModLoader() {
		return modLoader;
	}

	public void setMobManager(MobManager mobManager) {
		API.mobManager = mobManager;
	}

}
