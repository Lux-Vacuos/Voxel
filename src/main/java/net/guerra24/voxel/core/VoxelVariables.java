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

package net.guerra24.voxel.core;

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
	public static boolean VSYNC = false;
	public static final String Title = "Voxel";
	/**
	 * Game Settings
	 */
	public static boolean debug = true;
	public static final String version = "0.0.9";
	public static final String apiVersion = "0.0.4";
	public static final String state = "ALPHA";
	public static final int build = 110;
	public static int FOV = 90;
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	public static final float NEAR_PLANE = 0.2f;
	public static final float FAR_PLANE = 1000f;
	public static final float RED = 0.375f;
	public static final float GREEN = 0.555f;
	public static final float BLUE = 0.655f;
	public static boolean runningOnMac = false;
	/**
	 * World Settings
	 */
	public static int radius = 2;
	public static int radiusLimit = 8;
	public static int genRadius = radius + radiusLimit;
	public static boolean isCustomSeed = false;
	public static String seed = "";
	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_HEIGHT = 16;
	public static final int DIM_0 = 0;
	public static final int DIM_1 = 1;
	/**
	 * Graphics Settings
	 */
	public static final float WAVE_SPEED = 0.03f;
	public static final float SIZE = 500f;
	public static final float ROTATE_SPEED = 0.2f;
	/**
	 * Shader Files
	 */
	public static final String VERTEX_FILE_ENTITY = "VertexEntity.glsl";
	public static final String FRAGMENT_FILE_ENTITY = "FragmentEntity.glsl";
	public static final String VERTEX_FILE_GUI = "VertexGui.glsl";
	public static final String FRAGMENT_FILE_GUI = "FragmentGui.glsl";
	public static final String VERTEX_FILE_SKYBOX = "VertexSkybox.glsl";
	public static final String FRAGMENT_FILE_SKYBOX = "FragmentSkybox.glsl";
	public static final String VERTEX_FILE_WATER = "VertexWater.glsl";
	public static final String FRAGMENT_FILE_WATER = "FragmentWater.glsl";
	public static final String VERTEX_FILE_PARTICLE = "VertexParticle.glsl";
	public static final String FRAGMENT_FILE_PARTICLE = "FragmentParticle.glsl";
	public static final String VERTEX_FILE_POST = "VertexPost.glsl";
	public static final String FRAGMENT_FILE_POST = "FragmentPost.glsl";
	public static final String VERTEX_FILE_FONT = "VertexFont.glsl";
	public static final String FRAGMENT_FILE_FONT = "FragmentFont.glsl";
	public static final String VERTEX_FILE_SHADOW = "VertexShadow.glsl";
	public static final String FRAGMENT_FILE_SHADOW = "FragmentShadow.glsl";
	/**
	 * World Folder Path
	 */
	public static final String worldPath = "assets/game/world/";
	public static final String blockPath = "assets/blocks/";

	/**
	 * Update Global Variables
	 */
	public static void update() {
		genRadius = radius + radiusLimit;
	}

}
