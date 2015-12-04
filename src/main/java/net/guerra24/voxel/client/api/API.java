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

import java.util.HashMap;
import java.util.Map;

import net.guerra24.voxel.client.api.mod.Mod;
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
	private Map<Integer, Mod> mods;
	private ModLoader modLoader;
	private MobManager mobManager;
	private GameSettings gameSettings;

	public API(GameSettings gameSettings) {
		mods = new HashMap<Integer, Mod>();
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
		Logger.log("Pre Initializing Mods");
		for (int x = 0; x < mods.size(); x++) {
			mods.get(x).setAPI(this);
			if (mods.get(x).getKey().getApiVersion() >= VoxelVariables.apiVersionNum)
				mods.get(x).preInit();
			else
				throw new VersionException("The mod " + mods.get(x).getKey().getName()
						+ " only works in a version equals or more that " + VoxelVariables.apiVersion);
		}

	}

	/**
	 * Initialize the mod, load textures
	 * 
	 */
	public void init() {
		Logger.log("Initializing Mods");
		for (int x = 0; x < mods.size(); x++) {
			mods.get(x).init();
		}
	}

	/**
	 * Post initialize the mod, register all mod data
	 * 
	 */
	public void postInit() {
		Logger.log("Post Initializing Mods");
		for (int x = 0; x < mods.size(); x++) {
			mods.get(x).postInit();
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
	public void registerMod(Mod mod) {
		mods.put(mod.getKey().getId(), mod);
	}

	public void registetMob(IEntity mob) {
		mobManager.registerMob(mob);
	}

	public void registerSaveData(String key, String value) {
		gameSettings.registerValue(key, value);
	}

	/**
	 * Gets the Mod from ID
	 * 
	 * @param id
	 *            ID
	 * @return Mod
	 * 
	 */
	public Mod getMod(ModKey id) {
		return mods.get(id);
	}

	/**
	 * 
	 * @return ID
	 */
	public int getTotalMods() {
		return mods.size();
	}

	/**
	 * Dispose the API
	 * 
	 */
	public void dispose() {
		mods.clear();
	}

	public void setMobManager(MobManager mobManager) {
		this.mobManager = mobManager;
	}

}
