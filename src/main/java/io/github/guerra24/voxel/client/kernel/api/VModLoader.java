package io.github.guerra24.voxel.client.kernel.api;

import io.github.guerra24.voxel.client.kernel.util.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VModLoader {

	public VModLoader() {
	}

	public void loadMods() {
		try {
			Files.walk(Paths.get("assets/mods")).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					try {
						URLClassLoader child = new URLClassLoader(new URL[] { filePath.toFile().toURI().toURL() },
								this.getClass().getClassLoader());
						Class<?> classToLoad = Class.forName("voxel.api.Loader", true, child);
						Method method = classToLoad.getDeclaredMethod("loadMod");
						Object instance = classToLoad.newInstance();
						method.invoke(instance);
					} catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | SecurityException
							| InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						Logger.error(Thread.currentThread(), "Error Loading Mod");
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			Logger.error(Thread.currentThread(), "Error Loading Mod: " + e.getMessage());
		}
	}
}
