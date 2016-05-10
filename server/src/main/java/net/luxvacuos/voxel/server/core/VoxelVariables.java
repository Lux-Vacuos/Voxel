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

package net.luxvacuos.voxel.server.core;

/**
 * Locations of all global variables
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Kernel
 */
public class VoxelVariables {
	/**
	 * Display Data
	 */
	public static int UPS = 60;
	/**
	 * Game Settings
	 */
	public static final String version = "0.0.10";

	public static final int build = 213;
	public static final String settings = "settings.conf";
	public static final float TIME_MULTIPLIER = 10;

	/**
	 * World Settings
	 */
	public static int radius = 2;
	public static int radiusLimit = 2;
	public static int genRadius = radius + radiusLimit;
	public static boolean isCustomSeed = false;
	public static String seed = "";
	public static boolean generateChunks = true;
	public static boolean raining;
	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_HEIGHT = 16;
	public static final int DIM_0 = 0;
	public static final int DIM_1 = 1;
	public static final float GRAVITY = -10;
	/**
	 * World Folder Path
	 */
	public static final String worldPath = "world/";

	/**
	 * Update Global Variables
	 */
	public static void update() {
		genRadius = radius + radiusLimit;
	}

}
