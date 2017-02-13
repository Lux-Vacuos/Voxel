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
	 * Display Data
	 */
	public static int FPS = 60;
	public static boolean VSYNC = false;
	public static final String Title = "Voxel";

	/**
	 * Users Stuff
	 */
	public static User user;
	/**
	 * Game Settings
	 */
	public static boolean onServer = false;
	public static String version = "Development Version";
	public static int FOV = 90;
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000f;
	public static float RED = 0.32f;
	public static float GREEN = 0.8f;
	public static float BLUE = 1f;
	public static Vector3d skyColor = new Vector3d(ClientVariables.RED, ClientVariables.GREEN, ClientVariables.BLUE);
	public static boolean raining = false;
	public static String assets = "voxel";
	public static final boolean WSL = false;
	public static String server = "";

	/**
	 * Graphic Settings
	 */
	public static boolean useShadows = false;
	public static boolean useFXAA = false;
	public static boolean useDOF = false;
	public static boolean useMotionBlur = false;
	public static boolean useVolumetricLight = false;
	public static boolean useParallax = false;
	public static boolean useReflections = false;
	public static boolean useAmbientOcclusion = false;
	public static boolean useChromaticAberration = false;
	public static boolean useLensFlares = false;
	public static int shadowMapResolution = 512;
	public static int shadowMapDrawDistance = 200;
	public static final float WAVE_SPEED = 4f;
	public static String renderingPipeline = "MultiPass";
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
	
	public static final String VERTEX_WINDOW_MANAGER = "V_WindowManager.glsl";
	public static final String FRAGMENT_WINDOW_MANAGER = "F_WindowManager.glsl";

}
