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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;

import io.github.guerra24.voxel.client.kernel.core.GameStates;
import io.github.guerra24.voxel.client.kernel.graphics.Frustum;
import io.github.guerra24.voxel.client.kernel.graphics.GuiRenderer;
import io.github.guerra24.voxel.client.kernel.graphics.MasterRenderer;
import io.github.guerra24.voxel.client.kernel.graphics.FrameBuffer;
import io.github.guerra24.voxel.client.kernel.graphics.SkyboxRenderer;
import io.github.guerra24.voxel.client.kernel.graphics.WaterRenderer;
import io.github.guerra24.voxel.client.kernel.graphics.shaders.WaterShader;
import io.github.guerra24.voxel.client.kernel.menu.MainMenu;
import io.github.guerra24.voxel.client.kernel.resources.models.GuiTexture;
import io.github.guerra24.voxel.client.kernel.sound.LibraryLWJGLOpenAL;
import io.github.guerra24.voxel.client.kernel.sound.soundsystem.SoundSystem;
import io.github.guerra24.voxel.client.kernel.sound.soundsystem.SoundSystemConfig;
import io.github.guerra24.voxel.client.kernel.sound.soundsystem.SoundSystemException;
import io.github.guerra24.voxel.client.kernel.sound.soundsystem.codecs.CodecJOgg;
import io.github.guerra24.voxel.client.kernel.util.Logger;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.Physics;
import io.github.guerra24.voxel.client.kernel.world.block.Block;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;
import io.github.guerra24.voxel.client.kernel.world.entities.IEntity;
import io.github.guerra24.voxel.client.kernel.world.entities.Light;
import io.github.guerra24.voxel.client.kernel.world.entities.Mob;

/**
 * Game Resources
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class GameResources {
	public List<GuiTexture> guis = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis2 = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis3 = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis4 = new ArrayList<GuiTexture>();

	public List<IEntity> mainMenuModels = new ArrayList<IEntity>();
	public List<Light> mainMenuLights = new ArrayList<Light>();
	public List<Light> lights = new ArrayList<Light>();

	private Random rand;
	private Loader loader;
	private Camera camera;
	private MasterRenderer renderer;
	private WaterShader waterShader;
	private WaterRenderer waterRenderer;
	private SkyboxRenderer skyboxRenderer;
	private GuiRenderer guiRenderer;
	private GameStates gameStates;
	private Gson gson;
	private SoundSystem soundSystem;
	private Frustum frustum;
	private FrameBuffer frameBuffer;
	private Physics physics;

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
			Logger.error(Thread.currentThread(), "Unable to bind SoundSystem Libs");
		}
		soundSystem = new SoundSystem();
		renderer = new MasterRenderer(loader);
		guiRenderer = new GuiRenderer(loader);
		waterShader = new WaterShader();
		waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix());
		skyboxRenderer = new SkyboxRenderer(loader, renderer.getProjectionMatrix());
		gameStates = new GameStates();
		frustum = new Frustum();
		frameBuffer = new FrameBuffer();
		physics = new Physics(this);
		Block.initBasicBlocks();
	}

	/**
	 * Load Music
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void music() {
		soundSystem.backgroundMusic("menu1", "menu1.ogg", true);
	}

	/**
	 * Load Resources like Mobs
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void addRes() {
		MainMenu.loadModels(this);
		Mob planet = new Mob(MainMenu.planet, new Vector3f(-1, 0, -3), 0, 90, 0, 1);
		Light sun = new Light(new Vector3f(-3, 0, -2), new Vector3f(1, 1, 1), new Vector3f(1, 0.1f, 0.09f));
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
		frameBuffer.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		soundSystem.cleanup();
	}

	public List<GuiTexture> getGuis() {
		return guis;
	}

	public List<GuiTexture> getGuis2() {
		return guis2;
	}

	public List<GuiTexture> getGuis3() {
		return guis3;
	}

	public List<GuiTexture> getGuis4() {
		return guis4;
	}

	public List<IEntity> getMainMenuModels() {
		return mainMenuModels;
	}

	public List<Light> getMainMenuLights() {
		return mainMenuLights;
	}

	public List<Light> getLights() {
		return lights;
	}

	public Random getRand() {
		return rand;
	}

	public Loader getLoader() {
		return loader;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public MasterRenderer getRenderer() {
		return renderer;
	}

	public WaterShader getWaterShader() {
		return waterShader;
	}

	public WaterRenderer getWaterRenderer() {
		return waterRenderer;
	}

	public SkyboxRenderer getSkyboxRenderer() {
		return skyboxRenderer;
	}

	public GuiRenderer getGuiRenderer() {
		return guiRenderer;
	}

	public GameStates getGameStates() {
		return gameStates;
	}

	public Gson getGson() {
		return gson;
	}

	public SoundSystem getSoundSystem() {
		return soundSystem;
	}

	public Frustum getFrustum() {
		return frustum;
	}

	public Physics getPhysics() {
		return physics;
	}

	public FrameBuffer getFrameBuffer() {
		return frameBuffer;
	}

}