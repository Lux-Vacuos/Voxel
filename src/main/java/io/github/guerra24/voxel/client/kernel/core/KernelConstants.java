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

public class KernelConstants {
	// Display Settings
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	public static int FPS = 60;
	public static boolean VSYNC = false;
	public static String Title = "Voxel";
	// Game Settings
	public static int FOV = 90;
	public static float NEAR_PLANE = 0.05f;
	public static float FAR_PLANE = 1000f;
	public static float RED = 0.375f;
	public static float GREEN = 0.555f;
	public static float BLUE = 0.655f;
	public static boolean advancedOpenGL = false;
	// Game Variables
	public static int build = 50;
	public static String version = "0.0.1";
	public static boolean debug = true;
	public static boolean isLoading = false;
	public static boolean postPro = false;
	// World Settings
	public static int radius = 2;
	public static int genRadius = radius + 2;
	public static int octaveCount = 7;
	public static int viewDistance = 8;
	public static boolean isCustomSeed = false;
	public static String seed = "";
	// Chunk Settings
	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_HEIGHT = 128;
	// Graphics Settings
	public static final int MAX_LIGHTS = 1;
	// Water Settings
	public static float WAVE_SPEED = 0.03f;
	// Skybox Settings
	public static float SIZE = 500f;
	public static final float ROTATE_SPEED = 0.2f;
	// Shader Settings
	public static final String VERTEX_FILE_ENTITY = "VertexShaderEntity.glsl";
	public static final String FRAGMENT_FILE_ENTITY = "FragmentShaderEntity.glsl";
	public static final String VERTEX_FILE_GUI = "VertexShaderGui.glsl";
	public static final String FRAGMENT_FILE_GUI = "FragmentShaderGui.glsl";
	public static final String VERTEX_FILE_SKYBOX = "VertexShaderSkybox.glsl";
	public static final String FRAGMENT_FILE_SKYBOX = "FragmentShaderSkybox.glsl";
	public final static String VERTEX_FILE_WATER = "VertexShaderWater.glsl";
	public final static String FRAGMENT_FILE_WATER = "FragmentShaderWater.glsl";
	// World Save Path
	public static String camPath = "assets/world/Camera.json";
	public static String worldPath = "assets/world/World.json";
	public static String isPrePath = "assets/world/State.json";
	public static String entitiesPath = "assets/world/Entities.json";
	public static String userPath = "assets/game/User.json";
	public static String chunks = "assets/game/Chunk";

}
