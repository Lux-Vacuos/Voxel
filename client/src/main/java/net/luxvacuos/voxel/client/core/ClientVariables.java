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

package net.luxvacuos.voxel.client.core;

import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.universal.core.GlobalVariables;
import net.luxvacuos.voxel.universal.remote.User;

/**
 * Voxel Global Variables
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Kernel
 */
public class ClientVariables extends GlobalVariables {

	/**
	 * Users Stuff
	 */
	public static User user;
	/**
	 * Game Settings
	 */
	public static String version = "Development Version";

	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000f;
	public static float RED = 0.32f;
	public static float GREEN = 0.8f;
	public static float BLUE = 1f;
	public static Vector3d skyColor = new Vector3d(ClientVariables.RED, ClientVariables.GREEN, ClientVariables.BLUE);
	public static boolean raining = false;
	public static final boolean WSL = false;
	public static String server = "";

	/**
	 * All Settings
	 */
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	public static final float WAVE_SPEED = 4f;
	/**
	 * World Settings
	 */
	public static boolean generateChunks = true;
	public static String worldNameToLoad = "";
	public static boolean paused = false, exitWorld = false;
	/**
	 * Shader Files
	 */
	public static final String VERTEX_FILE_ENTITY = "V_Entity.glsl";
	public static final String FRAGMENT_FILE_ENTITY = "F_Entity.glsl";
	public static final String VERTEX_FILE_ENTITY_BASIC = "V_EntityBasic.glsl";
	public static final String FRAGMENT_FILE_ENTITY_BASIC = "F_EntityBasic.glsl";
	public static final String VERTEX_FILE_SKYBOX = "V_Skybox.glsl";
	public static final String FRAGMENT_FILE_SKYBOX = "F_Skybox.glsl";
	public static final String VERTEX_FILE_PARTICLE = "V_Particle.glsl";
	public static final String FRAGMENT_FILE_PARTICLE = "F_Particle.glsl";
	public static final String VERTEX_FILE_TESSELLATOR = "V_Tessellator.glsl";
	public static final String FRAGMENT_FILE_TESSELLATOR = "F_Tessellator.glsl";
	public static final String VERTEX_FILE_TESSELLATOR_BASIC = "V_TessellatorBasic.glsl";
	public static final String FRAGMENT_FILE_TESSELLATOR_BASIC = "F_TessellatorBasic.glsl";
	public static final String VERTEX_FILE_BLOCK_OUTLINE = "V_BlockOutline.glsl";
	public static final String FRAGMENT_FILE_BLOCK_OUTLINE = "F_BlockOutline.glsl";
	public static final String VERTEX_IRRADIANCE_CAPTURE = "V_IrradianceCapture.glsl";
	public static final String FRAGMENT_IRRADIANCE_CAPTURE = "F_IrradianceCapture.glsl";
	public static final String VERTEX_PRE_FILTERED_ENV = "V_PreFilteredEnvironment.glsl";
	public static final String FRAGMENT_PRE_FILTERED_ENV = "F_PreFilteredEnvironment.glsl";
	public static final String VERTEX_BRDF_INTEGRATION_MAP = "V_BRDFIntegrationMap.glsl";
	public static final String FRAGMENT_BRDF_INTEGRATION_MAP = "F_BRDFIntegrationMap.glsl";
	
	
	public static void initRuntimeVariables(){
		REGISTRY.register("/Voxel/Display/width", WIDTH);
		REGISTRY.register("/Voxel/Display/height", HEIGHT);
		GlobalVariables.REGISTRY.register("/Voxel/Settings/WindowManager/shellHeight", 0f);
	}

}
