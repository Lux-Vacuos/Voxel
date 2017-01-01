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

package net.luxvacuos.voxel.universal.core;

public class GlobalVariables {

	/** The path where all the world data resides */
	public static String WORLD_PATH;

	/** The path for the Settings file */
	public static String SETTINGS_PATH;

	/** The Version of the game */
	public static String version = "Prototype";

	/** Flag to enable debug mode */
	public static boolean debug = false;

	/** Updates per Second */
	public static int UPS = 60;

	/** Chunk Load/Unload Radius */
	public static int chunk_radius = 4;

	protected GlobalVariables() {
	}

}
