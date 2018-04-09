/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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

package net.luxvacuos.voxel.client.core;

import net.luxvacuos.voxel.universal.remote.User;

public class ClientVariables extends net.luxvacuos.lightengine.client.core.ClientVariables {

	/**
	 * Users Stuff
	 */
	public static User user;

	/**
	 * World Settings
	 */
	public static boolean generateChunks = true;
	public static String worldNameToLoad = "";
	public static boolean paused = false, exitWorld = false;

	public static String server;
	/**
	 * Shader Files
	 */
	public static final String VERTEX_FILE_TESSELLATOR = "Tessellator.vs";
	public static final String FRAGMENT_FILE_TESSELLATOR = "Tessellator.fs";
	public static final String GEOMETRY_FILE_TESSELLATOR = "Tessellator.gs";
	public static final String VERTEX_FILE_TESSELLATOR_BASIC = "TessellatorBasic.vs";
	public static final String FRAGMENT_FILE_TESSELLATOR_BASIC = "TessellatorBasic.fs";
	public static final String VERTEX_FILE_BLOCK_OUTLINE = "BlockOutline.vs";
	public static final String FRAGMENT_FILE_BLOCK_OUTLINE = "BlockOutline.fs";
}
