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
	// Display
	/**
	 * Display FPS
	 */
	public static int FPS = 60;
	/**
	 * Display VSync
	 */
	public static boolean VSYNC = false;
	/**
	 * Diplay Title
	 */
	public static String Title = "Voxel";
	// Game Settings
	/**
	 * Game Fov
	 */
	public static int FOV = 90;
	/**
	 * Game Near Plane
	 */
	public static float NEAR_PLANE = 0.001f;
	/**
	 * Game Far Plane
	 */
	public static float FAR_PLANE = 1000f;
	/**
	 * Game Red Background Color
	 */
	public static float RED = 0.375f;
	/**
	 * Game Greeb Background Color
	 */
	public static float GREEN = 0.555f;
	/**
	 * Game Blue Background Color
	 */
	public static float BLUE = 0.655f;
	// Game Variables
	/**
	 * Game Build Number
	 */
	public static int build = 79;
	/**
	 * Game Version
	 */
	public static String version = "0.0.3";
	/**
	 * Game Debug Mode
	 */
	public static boolean debug = false;
	// World Settings
	/**
	 * Game Draw Distance Radius
	 */
	public static int radius = 2;
	/**
	 * Game Save Game Limit
	 */
	public static int radiusLimit = 8;
	/**
	 * Game Chunk Gen Radius
	 */
	public static int genRadius = radius + radiusLimit;
	/**
	 * Using a custom seed
	 */
	public static boolean isCustomSeed = false;
	/**
	 * Game Seed
	 */
	public static String seed = "";
	// Chunk Settings
	/**
	 * Chunk Size
	 */
	public static final int CHUNK_SIZE = 16;
	/**
	 * Chunk Height
	 */
	public static final int CHUNK_HEIGHT = 128;
	// Graphics Settings
	/**
	 * Game Max Rendered Lights
	 */
	public static final int MAX_LIGHTS = 8;
	// Water Settings
	/**
	 * Water Wave Speed
	 */
	public static float WAVE_SPEED = 0.03f;
	// Skybox Settings
	/**
	 * Skybox size
	 */
	public static float SIZE = 500f;
	/**
	 * Skybox rotation speed
	 */
	public static final float ROTATE_SPEED = 0.2f;
	// Shader Settings
	/**
	 * Shader files Path
	 */
	public static final String VERTEX_FILE_ENTITY = "VertexShaderEntity.glsl";
	public static final String FRAGMENT_FILE_ENTITY = "FragmentShaderEntity.glsl";
	public static final String VERTEX_FILE_GUI = "VertexShaderGui.glsl";
	public static final String FRAGMENT_FILE_GUI = "FragmentShaderGui.glsl";
	public static final String VERTEX_FILE_SKYBOX = "VertexShaderSkybox.glsl";
	public static final String FRAGMENT_FILE_SKYBOX = "FragmentShaderSkybox.glsl";
	public static final String VERTEX_FILE_WATER = "VertexShaderWater.glsl";
	public static final String FRAGMENT_FILE_WATER = "FragmentShaderWater.glsl";
	public static final String VERTEX_FILE_PARTICLE = "VertexShaderParticle.glsl";
	public static final String FRAGMENT_FILE_PARTICLE = "FragmentShaderParticle.glsl";
	public static final String VERTEX_FILE_POST = "VertexPost.glsl";
	public static final String FRAGMENT_FILE_POST = "FragmentPost.glsl";
	// World Save Path
	/**
	 * World Folder Path
	 */
	public static String worldPath = "assets/game/world/";
	/**
	 * Checks if the Kernel has successfully loaded
	 */
	public static boolean loaded = false;

	public static int DIM_0 = 0;
	public static int DIM_1 = 1;

	/**
	 * Update Global Variables
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void update() {
		genRadius = radius + radiusLimit;
	}

}
