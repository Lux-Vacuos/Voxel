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

package io.github.guerra24.voxel.client.kernel.core;

/**
 * Locations of all global variables
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Kernel
 */
public class KernelConstants {
	/**
	 * Display Data
	 */
	public static int FPS = 60;
	public static boolean VSYNC = false;
	public static String Title = "Voxel";
	/**
	 * Game Settings
	 */
	public static boolean debug = true;
	public static String version = "0.0.5";
	public static int build = 88;
	public static int FOV = 90;
	public static float NEAR_PLANE = 0.2f;
	public static float FAR_PLANE = 1000f;
	public static float RED = 0.375f;
	public static float GREEN = 0.555f;
	public static float BLUE = 0.655f;
	/**
	 * World Settings
	 */
	public static int radius = 2;
	public static int radiusLimit = 8;
	public static int genRadius = radius + radiusLimit;
	public static boolean isCustomSeed = false;
	public static String seed = "";
	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_HEIGHT = 128;
	public static int DIM_0 = 0;
	public static int DIM_1 = 1;
	/**
	 * Graphics Settings
	 */
	public static final int MAX_LIGHTS = 8;
	public static float WAVE_SPEED = 0.03f;
	public static float SIZE = 500f;
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
	/**
	 * World Folder Path
	 */
	public static String worldPath = "assets/game/world/";
	public static String blockPath = "assets/blocks/";

	/**
	 * Update Global Variables
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void update() {
		genRadius = radius + radiusLimit;
	}

}
