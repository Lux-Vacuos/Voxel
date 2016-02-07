/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
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

package net.guerra24.voxel.client.core;

import net.guerra24.voxel.client.bootstrap.Bootstrap;

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
	public static int FPS = 60;
	public static int UPS = 30;
	public static boolean VSYNC = false;
	public static final String Title = "Voxel";
	/**
	 * Game Settings
	 */
	public static boolean debug = false;
	public static boolean hideHud = false;
	public static boolean onServer = false;
	public static final String version = "0.0.10";
	public static final String apiVersion = "0.0.6";
	public static final int apiVersionNum = 000006;
	public static final String state = "ALPHA";
	public static final int build = 170;
	public static int FOV = 90;
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000f;
	public static float RED = 0.32f;
	public static float GREEN = 0.8f;
	public static float BLUE = 1f;
	public static boolean runningOnMac = false;
	public static boolean autostart = false;
	public static boolean christmas = false;
	public static final String settings = Bootstrap.getPrefix() + "voxel/assets/game/settings.conf";

	/**
	 * External Data
	 */
	public static final String web = "https://guerra24.github.io/";
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
	public static float fogDensity = 0.02f;
	/**
	 * World Settings
	 */
	public static int radius = 2;
	public static int radiusLimit = 2;
	public static int genRadius = radius + radiusLimit;
	public static boolean isCustomSeed = false;
	public static String seed = "";
	public static boolean generateChunks = true;
	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_HEIGHT = 16;
	public static final int DIM_0 = 0;
	public static final int DIM_1 = 1;
	public static final float GRAVITY = -10;
	/**
	 * Graphics Settings
	 */
	public static final float WAVE_SPEED = 4f;
	/**
	 * Shader Files
	 */
	public static final String VERTEX_FILE_ENTITY = "VertexEntity.glsl";
	public static final String FRAGMENT_FILE_ENTITY = "FragmentEntity.glsl";
	public static final String VERTEX_FILE_ENTITY_BASIC = "VertexEntityBasic.glsl";
	public static final String FRAGMENT_FILE_ENTITY_BASIC = "FragmentEntityBasic.glsl";
	public static final String VERTEX_FILE_GUI = "VertexGui.glsl";
	public static final String FRAGMENT_FILE_GUI = "FragmentGui.glsl";
	public static final String VERTEX_FILE_SKYBOX = "VertexSkybox.glsl";
	public static final String FRAGMENT_FILE_SKYBOX = "FragmentSkybox.glsl";
	public static final String VERTEX_FILE_WATER = "VertexWater.glsl";
	public static final String FRAGMENT_FILE_WATER = "FragmentWater.glsl";
	public static final String VERTEX_FILE_WATER_BASIC = "VertexWaterBasic.glsl";
	public static final String FRAGMENT_FILE_WATER_BASIC = "FragmentWaterBasic.glsl";
	public static final String VERTEX_FILE_PARTICLE = "VertexParticle.glsl";
	public static final String FRAGMENT_FILE_PARTICLE = "FragmentParticle.glsl";
	public static final String VERTEX_FILE_FONT = "VertexFont.glsl";
	public static final String FRAGMENT_FILE_FONT = "FragmentFont.glsl";
	public static final String VERTEX_FILE_COMPOSITE = "VertexComposite";
	public static final String FRAGMENT_FILE_COMPOSITE = "FragmentComposite";
	public static final String VERTEX_FILE_TESSELLATOR = "VertexTessellator.glsl";
	public static final String FRAGMENT_FILE_TESSELLATOR = "FragmentTessellator.glsl";
	public static final String VERTEX_FILE_TESSELLATOR_BASIC = "VertexTessellatorBasic.glsl";
	public static final String FRAGMENT_FILE_TESSELLATOR_BASIC = "FragmentTessellatorBasic.glsl";
	/**
	 * World Folder Path
	 */
	public static final String worldPath = Bootstrap.getPrefix() + "voxel/assets/game/world/";

	/**
	 * Update Global Variables
	 */
	public static void update() {
		genRadius = radius + radiusLimit;
	}

}
