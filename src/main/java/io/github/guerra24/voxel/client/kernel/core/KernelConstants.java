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
 * @version 0.0.1 Build-52
 * @since 0.0.1 Build-52
 * @category Kernel
 */
public class KernelConstants {
	// Display
	/**
	 * Display Width Resolution
	 */
	public static int WIDTH = 1280;
	/**
	 * Display Height Resolution
	 */
	public static int HEIGHT = 720;
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
	public static float NEAR_PLANE = 0.05f;
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
	/**
	 * Game Advanced Rendering
	 */
	public static boolean advancedOpenGL = false;
	// Game Variables
	/**
	 * Game Build Number
	 */
	public static int build = 52;
	/**
	 * Game Version
	 */
	public static String version = "0.0.1";
	/**
	 * Game Debug Mode
	 */
	public static boolean debug = true;
	public static boolean postPro = false;
	// World Settings
	/**
	 * Game Draw Distance Radius
	 */
	public static int radius = 2;
	/**
	 * Game Chunk Gen Radius
	 */
	public static int genRadius = radius + 10;
	/**
	 * Perlin octave
	 * 
	 * @deprecated
	 */
	public static int octaveCount = 7;
	/**
	 * Game World Size
	 * 
	 * @deprecated
	 */
	public static int viewDistance = 8;
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
	public static final int MAX_LIGHTS = 1;
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
	 * Entity Vertex Shader File
	 */
	public static final String VERTEX_FILE_ENTITY = "VertexShaderEntity.glsl";
	/**
	 * Entity Fragment Shader File
	 */
	public static final String FRAGMENT_FILE_ENTITY = "FragmentShaderEntity.glsl";
	/**
	 * Gui Vertex Shader File
	 */
	public static final String VERTEX_FILE_GUI = "VertexShaderGui.glsl";
	/**
	 * Gui Fragment Shader File
	 */
	public static final String FRAGMENT_FILE_GUI = "FragmentShaderGui.glsl";
	/**
	 * Skybox Vertex Shader File
	 */
	public static final String VERTEX_FILE_SKYBOX = "VertexShaderSkybox.glsl";
	/**
	 * Skybox Fragment Shader File
	 */
	public static final String FRAGMENT_FILE_SKYBOX = "FragmentShaderSkybox.glsl";
	/**
	 * Water Vertex Shader File
	 */
	public final static String VERTEX_FILE_WATER = "VertexShaderWater.glsl";
	/**
	 * Water Fragment Shader File
	 */
	public final static String FRAGMENT_FILE_WATER = "FragmentShaderWater.glsl";
	// World Save Path
	/**
	 * World Folder Path
	 */
	public static String worldPath = "assets/game/world/";
	/**
	 * Camera Storage Path
	 * 
	 * @deprecated
	 */
	public static String camPath = "assets/world/Camera.json";

	/**
	 * Update Global Variables
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static void update() {
		genRadius = radius + 10;
	}

}
