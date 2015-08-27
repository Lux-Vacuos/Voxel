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
import io.github.guerra24.voxel.client.kernel.graphics.Frustum;
import io.github.guerra24.voxel.client.kernel.graphics.GuiRenderer;
import io.github.guerra24.voxel.client.kernel.graphics.MasterRenderer;
import io.github.guerra24.voxel.client.kernel.graphics.SkyboxRenderer;
import io.github.guerra24.voxel.client.kernel.graphics.WaterRenderer;
import io.github.guerra24.voxel.client.kernel.graphics.shaders.WaterShader;
import io.github.guerra24.voxel.client.kernel.menu.MainMenu;
import io.github.guerra24.voxel.client.kernel.resources.models.GuiTexture;
import io.github.guerra24.voxel.client.kernel.util.Logger;
import io.github.guerra24.voxel.client.kernel.util.MousePicker;
import io.github.guerra24.voxel.client.kernel.world.block.Block;
import io.github.guerra24.voxel.client.kernel.world.block.BlocksResources;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;
import io.github.guerra24.voxel.client.kernel.world.entities.Entity;
import io.github.guerra24.voxel.client.kernel.world.entities.Light;
import io.github.guerra24.voxel.client.kernel.world.entities.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOgg;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

import com.google.gson.Gson;

/**
 * Game Resources
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.2 Build-58
 * @since 0.0.1 Build-52
 * @category Assets
 */
public class GameResources {
	public List<GuiTexture> guis = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis2 = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis3 = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis4 = new ArrayList<GuiTexture>();

	public List<Entity> allObjects = new ArrayList<Entity>();
	public List<Entity> mainMenuModels = new ArrayList<Entity>();
	public List<Light> mainMenuLights = new ArrayList<Light>();
	public List<Light> lights = new ArrayList<Light>();

	public Random rand;
	public Player player;
	public Loader loader;
	public Camera camera;
	public MasterRenderer renderer;
	public WaterShader waterShader;
	public WaterRenderer waterRenderer;
	public SkyboxRenderer skyboxRenderer;
	public GuiRenderer guiRenderer;
	public GameStates gameStates;
	public MousePicker mouse;
	public Gson gson;
	public SoundSystem SoundSystem;
	public Frustum frustum;
	public float distance;

	/**
	 * Constructor, Create the Game Resources and Init Loader
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public GameResources() {
	}

	/**
	 * Initialize the Game Objects
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void init() {
		loader = new Loader();
		rand = new Random();
		camera = new Camera();
		gson = new Gson();
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
		} catch (SoundSystemException e) {
			Logger.error(Thread.currentThread(),
					"Unable to bind SoundSystem Libs");
		}
		SoundSystem = new SoundSystem();
		renderer = new MasterRenderer(loader);
		guiRenderer = new GuiRenderer(loader);
		waterShader = new WaterShader();
		waterRenderer = new WaterRenderer(loader, waterShader,
				renderer.getProjectionMatrix());
		skyboxRenderer = new SkyboxRenderer(loader,
				renderer.getProjectionMatrix());
		mouse = new MousePicker(camera, renderer.getProjectionMatrix());
		gameStates = new GameStates();
		frustum = new Frustum();
		Block.initBasicBlocks();
	}

	/**
	 * Load Music
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void music() {
		// SoundSystem.backgroundMusic("MainMenuMusic", "Water_Lily.ogg",
		// false);
	}

	/**
	 * Load Resources like Mobs
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void addRes() {
		MainMenu.loadModels(this);
		Entity planet = new Entity(MainMenu.planet, new Vector3f(-3, 0, -3), 0,
				90, 0, 1);
		Light sun = new Light(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1),
				new Vector3f(1, 0.1f, 0.09f));
		player = new Player(BlocksResources.cubeGlassUP,
				new Vector3f(0, 80, -4), 0, 0, 0, 1);
		allObjects.add(player);
		mainMenuModels.add(planet);
		mainMenuLights.add(sun);
	}

	/**
	 * Disposes all objects
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void cleanUp() {
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		SoundSystem.cleanup();
	}

}