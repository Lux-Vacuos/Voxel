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

import com.esotericsoftware.minlog.Log;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.universal.bootstrap.AbstractBootstrap;
import net.luxvacuos.voxel.universal.bootstrap.Platform;

/**
 * Bootstrap, this initializes the game path using <b>AppData</b> on Windows and
 * <b>user.home</b> on Linux and OS X
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class Bootstrap extends AbstractBootstrap {

	public Bootstrap(String[] args) {
		super(args);
	}

	@Override
	public void init() {
		// Minglog Log level
		Log.set(Log.LEVEL_INFO);
		Thread.currentThread().setName("Voxel-Client");
	}

	@Override
	public void parseArgs(String[] args) {
		// Booleans to prevent setting a previously set value
		boolean gaveWidth = false, gaveHeight = false;

		// Iterate through array
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			// Check for window width
			case "-width":
				if (gaveWidth)
					throw new IllegalStateException("Width already given");
				// Convert and set the width
				VoxelVariables.WIDTH = Integer.parseInt(args[++i]);
				if (VoxelVariables.WIDTH <= 0)
					throw new IllegalArgumentException("Width must be positive");
				gaveWidth = true;
				break;
			// Check for height
			case "-height":
				if (gaveHeight)
					throw new IllegalStateException("Height already given");
				// Convert and set height
				VoxelVariables.HEIGHT = Integer.parseInt(args[++i]);
				if (VoxelVariables.HEIGHT <= 0)
					throw new IllegalArgumentException("Height must be positive");
				gaveHeight = true;
				break;
			// Check for username
			case "-username":
				// Set username
				VoxelVariables.username = args[++i];
				break;
			default:
				// If there is an unknown arg throw exception
				if (args[i].startsWith("-")) {
					throw new IllegalArgumentException("Unknown argument: " + args[i].substring(1));
				} else {
					throw new IllegalArgumentException("Unknown token: " + args[i]);
				}
			}
		}
	}

	@Override
	public String getPrefix() {
		if (prefix == null) {
			if (getPlatform().equals(Platform.WINDOWS_32) || getPlatform().equals(Platform.WINDOWS_64))
				prefix = System.getenv("AppData");
			else if (getPlatform().equals(Platform.LINUX_32) || getPlatform().equals(Platform.LINUX_64))
				prefix = System.getProperty("user.home");
			else if (getPlatform().equals(Platform.MACOSX)) {
				prefix = System.getProperty("user.home");
				prefix += "/Library/Application Support";
			}
			prefix += "/.voxel";
		}
		return prefix;
	}

	/**
	 * Main method
	 * 
	 * @param args
	 *            Args
	 */
	public static void main(String[] args) {
		new Voxel(new Bootstrap(args));
	}

}
