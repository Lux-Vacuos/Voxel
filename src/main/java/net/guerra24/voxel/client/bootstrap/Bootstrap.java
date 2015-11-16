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

package net.guerra24.voxel.client.bootstrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import net.guerra24.voxel.client.core.Voxel;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.util.Logger;

/**
 * Initialize the basic game code
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class Bootstrap {
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

	/**
	 * Launcher main function
	 * 
	 * @param args
	 *            Not Used
	 */
	public static void main(String[] args) {
		Thread.currentThread().setName("Voxel Main");
		if (VoxelVariables.debug) {
			System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
		} else {
			PrintStream out;
			try {
				out = new PrintStream(new FileOutputStream("assets/log.txt"));
				System.setOut(out);
				System.setErr(out);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		try {
			parseArgs(args);
		} catch (ArrayIndexOutOfBoundsException aioe) {
			Logger.error("Error: Arguments were wrong", aioe);
			System.exit(1);
		} catch (Exception ex) {
			Logger.error(ex);
			System.exit(1);
		}
		new Voxel();
	}

	/**
	 * Handles all Voxel available args
	 * 
	 * @param args
	 *            Array of args
	 */
	private static void parseArgs(String[] args) {
		boolean gaveWidth = false, gaveHeight = false, gaveFov = false;
		boolean gaveFps = false, gaveRadius = false, gaveShadows = false;
		boolean gaveAutostart = false, gaveFXAA = false, gaveDOF = false;
		boolean gaveHQWater = false;

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
			case "-fov":
				if (gaveFov)
					throw new IllegalStateException("FOV already given");
				VoxelVariables.FOV = Integer.parseInt(args[++i]);
				if (VoxelVariables.FOV <= 20 || VoxelVariables.FOV >= 120)
					throw new IllegalArgumentException("FOV must be in (20, 120) range");
				gaveFov = true;
				break;
			case "-fps":
				if (gaveFps)
					throw new IllegalStateException("FPS already given");
				VoxelVariables.FPS = Integer.parseInt(args[++i]);
				if (VoxelVariables.FPS <= 0) {
					throw new IllegalArgumentException("FPS must be positive");
				}
				gaveFps = true;
				break;
			case "-radius":
				if (gaveRadius)
					throw new IllegalStateException("Radius already given");
				VoxelVariables.radius = Integer.parseInt(args[++i]);
				if (VoxelVariables.radius < 2)
					throw new IllegalArgumentException("Radius must be equal or greater than 2");
				gaveRadius = true;
				break;
			case "-useShadows":
				if (gaveShadows)
					throw new IllegalStateException("Shadows already given");
				VoxelVariables.useShadows = true;
				gaveShadows = true;
				break;
			case "-useFXAA":
				if (gaveFXAA)
					throw new IllegalStateException("FXAA already given");
				if (VoxelVariables.useDOF)
					throw new IllegalArgumentException("FXAA only be activated if DOF is disabled");
				VoxelVariables.useFXAA = true;
				gaveFXAA = true;
				break;
			case "-useDOF":
				if (gaveDOF)
					throw new IllegalStateException("DOF already given");
				if (VoxelVariables.useFXAA)
					throw new IllegalArgumentException("DOF only be activated if FXAA is disabled");
				VoxelVariables.useDOF = true;
				gaveDOF = true;
				break;

			case "-useHQWater":
				if (gaveHQWater)
					throw new IllegalStateException("HQWater already given");
				VoxelVariables.useHQWater = true;
				break;
			case "-autostart":
				if (gaveAutostart)
					throw new IllegalStateException("Autostart already given");
				VoxelVariables.autostart = true;
				gaveAutostart = true;
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

}