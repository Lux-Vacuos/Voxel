package io.github.guerra24.voxel.client.kernel.api;

import io.github.guerra24.voxel.client.kernel.api.mod.Mod;
import io.github.guerra24.voxel.client.kernel.util.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * API
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.2 Build-55
 * @since 0.0.1 Build-54
 * @category API
 */
public class API {
	/**
	 * Mods
	 */
	public static Map<Integer, Mod> mods;

	public API() {
		mods = new HashMap<Integer, Mod>();
		try {
			Files.walk(Paths.get("assets/mods"))
					.forEach(
							filePath -> {
								if (Files.isRegularFile(filePath)) {
									try {
										URLClassLoader child = new URLClassLoader(
												new URL[] { filePath.toFile()
														.toURL() }, this
														.getClass()
														.getClassLoader());
										Class<?> classToLoad = Class.forName(
												"Main", true, child);
										Method method = classToLoad
												.getDeclaredMethod("mod");
										Object instance = classToLoad
												.newInstance();
										method.invoke(instance);
									} catch (MalformedURLException
											| ClassNotFoundException
											| NoSuchMethodException
											| SecurityException
											| InstantiationException
											| IllegalAccessException
											| IllegalArgumentException
											| InvocationTargetException e) {
										Logger.log(
												Thread.currentThread(),
												"Error Loading Mod: "
														+ e.getMessage());
									}
								}
							});
		} catch (IOException e) {
			Logger.log(Thread.currentThread(),
					"Error Loading Mod: " + e.getMessage());
		}

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
			Logger.log(Thread.currentThread(), "Succesfully Loaded: "
					+ mods.get(x).getName() + " ID: " + mods.get(x).getID());
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

}
