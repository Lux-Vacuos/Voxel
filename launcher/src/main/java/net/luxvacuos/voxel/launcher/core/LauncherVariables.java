/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

package net.luxvacuos.voxel.launcher.core;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.ImageCursor;

public class LauncherVariables {

	public static final String version = "0.0.12";
	public static boolean apt = false;
	public static final String host = "https://s3.luxvacuos.net";
	public static final String authHost = "https://luxvacuos.net";
	public static final String project = "voxel";
	public static final String config = "config";
	public static final String libraries = "libraries";
	public static final String separator = System.getProperty("path.separator");

	public static ImageCursor cursor;
	public static String username;
	public static List<String> userArgs = new ArrayList<>();

}
