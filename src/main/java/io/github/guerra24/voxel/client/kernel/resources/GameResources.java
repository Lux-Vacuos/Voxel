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

package io.github.guerra24.voxel.client.kernel.resources;

import io.github.guerra24.voxel.client.kernel.core.GameStates;
import io.github.guerra24.voxel.client.kernel.graphics.GuiRenderer;
import io.github.guerra24.voxel.client.kernel.graphics.MasterRenderer;
import io.github.guerra24.voxel.client.kernel.graphics.WaterRenderer;
import io.github.guerra24.voxel.client.kernel.graphics.shaders.WaterShader;
import io.github.guerra24.voxel.client.kernel.resources.models.GuiTexture;
import io.github.guerra24.voxel.client.kernel.resources.models.WaterTile;
import io.github.guerra24.voxel.client.kernel.util.ArrayList3;
import io.github.guerra24.voxel.client.kernel.util.MousePicker;
import io.github.guerra24.voxel.client.kernel.world.block.Block;
import io.github.guerra24.voxel.client.kernel.world.block.BlocksResources;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;
import io.github.guerra24.voxel.client.kernel.world.entities.Entity;
import io.github.guerra24.voxel.client.kernel.world.entities.Light;
import io.github.guerra24.voxel.client.kernel.world.entities.Player;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.util.vector.Vector3f;

import com.google.gson.Gson;

public class GameResources {
	public ArrayList3<GuiTexture> guis = new ArrayList3<GuiTexture>();
	public ArrayList3<GuiTexture> guis2 = new ArrayList3<GuiTexture>();
	public ArrayList3<GuiTexture> guis3 = new ArrayList3<GuiTexture>();
	public ArrayList3<GuiTexture> guis4 = new ArrayList3<GuiTexture>();
	public ArrayList3<GuiTexture> guis5 = new ArrayList3<GuiTexture>();

	public ArrayList3<Entity> allObjects = new ArrayList3<Entity>();
	public Queue<Entity> cubes = new ConcurrentLinkedQueue<Entity>();
	public Queue<WaterTile> waters = new ConcurrentLinkedQueue<WaterTile>();
	public ArrayList3<Light> lights = new ArrayList3<Light>();

	public Random rand;
	public Player player;
	public Light spot;
	public Loader loader;
	public Camera camera;
	public MasterRenderer renderer;
	public WaterShader waterShader;
	public WaterRenderer waterRenderer;
	public GuiRenderer guiRenderer;
	public GameStates gameStates;
	public MousePicker mouse;
	public Gson gson;
	// public SoundSystem SoundSystem;
	public float distance;

	public GameResources() {
		loader = new Loader();
		guiRenderer = new GuiRenderer(loader);
	}

	public void init() {
		rand = new Random();
		camera = new Camera();
		gson = new Gson();
		/*
		 * try { SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
		 * SoundSystemConfig.setCodec("ogg", CodecJOgg.class); } catch
		 * (SoundSystemException e) { Logger.error(Thread.currentThread(),
		 * "Unable to bind SoundSystem Libs"); } SoundSystem = new
		 * SoundSystem();
		 */
		renderer = new MasterRenderer(loader);
		waterShader = new WaterShader();
		waterRenderer = new WaterRenderer(loader, waterShader,
				renderer.getProjectionMatrix());
		mouse = new MousePicker(camera, renderer.getProjectionMatrix());
		gameStates = new GameStates();
		Block.initBasicBlocks();
	}

	public void music() {
		// SoundSystem.backgroundMusic("MainMenuMusic", "Water_Lily.ogg",
		// false);
	}

	public void addRes() {
		player = new Player(BlocksResources.cubeGlassUP,
				new Vector3f(0, 80, -4), 0, 0, 0, 1);
		spot = new Light(new Vector3f(256, 70, 256), new Vector3f(5, 5, 5),
				new Vector3f(1, 0.1f, 0.09f));
		lights.add(spot);
		allObjects.add(player);
	}

	public void cleanUp() {
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		// SoundSystem.cleanup();
	}

}