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

package io.github.guerra24.voxel.client.kernel.api;

import java.util.HashMap;
import java.util.Map;

import io.github.guerra24.voxel.client.kernel.api.mod.Mod;
import io.github.guerra24.voxel.client.kernel.util.Logger;

/**
 * API
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category API
 */
public class VAPI {
	/**
	 * Mods
	 */
	private static Map<Integer, Mod> mods;
	private static WorldVAPI worldAPI;
	private static VModLoader modLoader;
	private static MobVAPI mobAPI;

	public VAPI() {
		mods = new HashMap<Integer, Mod>();
		worldAPI = new WorldVAPI();
		modLoader = new VModLoader();
		modLoader.loadMods();
		mobAPI = new MobVAPI();
	}

	/**
	 * Pre initialize the mod, load config data
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void preInit() {
		Logger.log(Thread.currentThread(), "Pre Initializing Mods");
		for (int x = 0; x < mods.size(); x++) {
			mods.get(x).preInit();
		}
	}

	/**
	 * Initialize the mod, load textures
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void init() {
		Logger.log(Thread.currentThread(), "Initializing Mods");
		for (int x = 0; x < mods.size(); x++) {
			mods.get(x).init();
		}
	}

	/**
	 * Post initialize the mod, register all mod data
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void postInit() {
		Logger.log(Thread.currentThread(), "Post Initializing Mods");
		for (int x = 0; x < mods.size(); x++) {
			mods.get(x).postInit();
			Logger.log(Thread.currentThread(),
					"Succesfully Loaded: " + mods.get(x).getName() + " ID: " + mods.get(x).getID());
		}
	}

	/**
	 * Registers the mod to the code
	 * 
	 * @param id
	 *            Mod ID
	 * @param result
	 *            Mod
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void registerMod(int id, Mod mod) {
		mods.put(id, mod);
	}

	/**
	 * Gets the Mod from ID
	 * 
	 * @param id
	 *            ID
	 * @return Mod
	 * @author Guerra24 <pablo230699@hotmail.com>
	 * 
	 */
	public static Mod getMod(int id) {
		return mods.get(id);
	}

	/**
	 * Get last avaiable ID
	 * 
	 * @return ID
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static int getLastID() {
		return mods.size();
	}

	/**
	 * Dispose the API
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void dispose() {
		mods.clear();
	}

	public static WorldVAPI getWorldAPI() {
		return worldAPI;
	}

	public static VModLoader getModLoader() {
		return modLoader;
	}

	public static MobVAPI getMobAPI() {
		return mobAPI;
	}

}
