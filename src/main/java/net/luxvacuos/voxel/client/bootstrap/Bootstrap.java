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

package net.luxvacuos.voxel.client.bootstrap;

import java.io.File;
import java.util.Calendar;

import com.esotericsoftware.minlog.Log;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.VoxelVariables;

/**
 * Initialize the basic game code
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class Bootstrap {

	private static String prefix;

	static {
		if (getPlatform().equals(Platform.WINDOWS_32) || getPlatform().equals(Platform.WINDOWS_64))
			prefix = System.getenv("AppData");
		else if (getPlatform().equals(Platform.LINUX_32) || getPlatform().equals(Platform.LINUX_64))
			prefix = System.getProperty("user.home");
		else if (getPlatform().equals(Platform.MACOSX)) {
			prefix = System.getProperty("user.home");
			prefix += "/Library/Application Support";
		}
		prefix += "/.";
	}

	private Bootstrap() {
	}

	/**
	 * OS info
	 */
	private static Platform platform;

	/**
	 * 
	 * Gets the OS
	 * 
	 * @return The OS and the architecture
	 * 
	 */
	public static Platform getPlatform() {
		if (platform == null) {
			final String OS = System.getProperty("os.name").toLowerCase();
			final String ARCH = System.getProperty("os.arch").toLowerCase();

			boolean isWindows = OS.contains("windows");
			boolean isLinux = OS.contains("linux");
			boolean isMac = OS.contains("mac");
			boolean is64Bit = ARCH.equals("amd64") || ARCH.equals("x86_64");

			platform = Platform.UNKNOWN;

			if (isWindows)
				platform = is64Bit ? Platform.WINDOWS_64 : Platform.WINDOWS_32;
			if (isLinux)
				platform = is64Bit ? Platform.LINUX_64 : Platform.LINUX_32;
			if (isMac)
				platform = Platform.MACOSX;
		}

		return platform;
	}

	/**
	 * Enumerator of the OS
	 * 
	 *
	 */
	public enum Platform {
		WINDOWS_32, WINDOWS_64, MACOSX, LINUX_32, LINUX_64, UNKNOWN;
	}

	static {
		File file = new File(Bootstrap.getPrefix() + "voxel/assets/game/logs");
		if (!file.exists())
			file.mkdirs();
	}

	/**
	 * Launcher main function
	 * 
	 * @param args
	 *            Not Used
	 */
	public static void main(String[] args) {
		Log.set(Log.LEVEL_INFO);
		Thread.currentThread().setName("Voxel Main");
		try {
			parseArgs(args);
		} catch (ArrayIndexOutOfBoundsException aioe) {
			Logger.error("Error: Arguments were wrong", aioe);
			System.exit(1);
		} catch (Exception ex) {
			Logger.error(ex);
			System.exit(1);
		}
		checkSomeValues();

		new Voxel();
	}

	private static void checkSomeValues() {
		Calendar christmas = Calendar.getInstance();
		if (christmas.get(Calendar.MONTH) == Calendar.DECEMBER) {
			VoxelVariables.christmas = true;
			VoxelVariables.RED = 0.882f;
			VoxelVariables.GREEN = 1;
			VoxelVariables.BLUE = 1;
		}
	}

	/**
	 * Handles all Voxel available args
	 * 
	 * @param args
	 *            Array of args
	 */
	private static void parseArgs(String[] args) {
		boolean gaveWidth = false, gaveHeight = false;
		boolean gaveAutostart = false;

		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "-width":
				if (gaveWidth)
					throw new IllegalStateException("Width already given");
				VoxelVariables.WIDTH = Integer.parseInt(args[++i]);
				if (VoxelVariables.WIDTH <= 0)
					throw new IllegalArgumentException("Width must be positive");
				gaveWidth = true;
				break;
			case "-height":
				if (gaveHeight)
					throw new IllegalStateException("Height already given");
				VoxelVariables.HEIGHT = Integer.parseInt(args[++i]);
				if (VoxelVariables.HEIGHT <= 0)
					throw new IllegalArgumentException("Height must be positive");
				gaveHeight = true;
				break;
			case "-autostart":
				if (gaveAutostart)
					throw new IllegalStateException("Autostart already given");
				VoxelVariables.autostart = true;
				gaveAutostart = true;
				break;
			case "-username":
				System.out.println(args[++i]);
				break;
			default:
				if (args[i].startsWith("-")) {
					throw new IllegalArgumentException("Unknown argument: " + args[i].substring(1));
				} else {
					throw new IllegalArgumentException("Unknown token: " + args[i]);
				}
			}
		}
	}

	public static String getPrefix() {
		return prefix;
	}

}