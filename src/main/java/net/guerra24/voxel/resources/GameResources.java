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

package net.guerra24.voxel.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;

import net.guerra24.voxel.core.GameStates;
import net.guerra24.voxel.graphics.FrameBuffer;
import net.guerra24.voxel.graphics.Frustum;
import net.guerra24.voxel.graphics.GuiRenderer;
import net.guerra24.voxel.graphics.MasterRenderer;
import net.guerra24.voxel.graphics.PostProcessingRenderer;
import net.guerra24.voxel.graphics.SkyboxRenderer;
import net.guerra24.voxel.graphics.TextRenderer;
import net.guerra24.voxel.graphics.WaterRenderer;
import net.guerra24.voxel.graphics.shaders.WaterShader;
import net.guerra24.voxel.menu.MainMenu;
import net.guerra24.voxel.particle.ParticleController;
import net.guerra24.voxel.resources.models.GuiTexture;
import net.guerra24.voxel.sound.LibraryLWJGLOpenAL;
import net.guerra24.voxel.sound.soundsystem.SoundSystem;
import net.guerra24.voxel.sound.soundsystem.SoundSystemConfig;
import net.guerra24.voxel.sound.soundsystem.SoundSystemException;
import net.guerra24.voxel.sound.soundsystem.codecs.CodecJOgg;
import net.guerra24.voxel.util.Logger;
import net.guerra24.voxel.util.vector.Vector3f;
import net.guerra24.voxel.world.Physics;
import net.guerra24.voxel.world.block.Block;
import net.guerra24.voxel.world.entities.Camera;
import net.guerra24.voxel.world.entities.IEntity;
import net.guerra24.voxel.world.entities.Mob;

/**
 * Game Resources
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class GameResources {

	/**
	 * GameResources Data
	 */
	public List<GuiTexture> guis = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis2 = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis3 = new ArrayList<GuiTexture>();
	public List<GuiTexture> guis4 = new ArrayList<GuiTexture>();

	public List<IEntity> mainMenuModels = new ArrayList<IEntity>();

	private Random rand;
	private Loader loader;
	private Camera camera;
	private MasterRenderer renderer;
	private WaterShader waterShader;
	private WaterRenderer waterRenderer;
	private SkyboxRenderer skyboxRenderer;
	private GuiRenderer guiRenderer;
	private GameStates gameStates;
	private TextRenderer textRenderer;

	private ParticleController particleController;
	private PostProcessingRenderer postProcessing;
	private SoundSystem soundSystem;
	private Frustum frustum;
	private Gson gson;
	private FrameBuffer waterFBO;
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
		camera = new Camera();
		gson = new Gson();
		renderer = new MasterRenderer(loader);
		waterShader = new WaterShader();
		guiRenderer = new GuiRenderer(loader);
		waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix());
		skyboxRenderer = new SkyboxRenderer(loader, renderer.getProjectionMatrix());
		particleController = new ParticleController(loader);
		postProcessing = new PostProcessingRenderer(loader);
		gameStates = new GameStates();
		waterFBO = new FrameBuffer(false, false, 128, 128);
		physics = new Physics(this);
		frustum = new Frustum();
		rand = new Random();
		textRenderer = new TextRenderer("Voxel");
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
		} catch (SoundSystemException e) {
			Logger.error("Unable to bind SoundSystem Libs");
		}
		soundSystem = new SoundSystem();

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
		mainMenuModels.add(planet);
	}

	/**
	 * Disposes all objects
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void cleanUp() {
		particleController.dispose();
		waterShader.cleanUp();
		waterFBO.cleanUp();
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

	public Random getRand() {
		return rand;
	}

	public Loader getLoader() {
		return loader;
	}

	public Gson getGson() {
		return gson;
	}

	public Camera getCamera() {
		return camera;
	}

	public MasterRenderer getRenderer() {
		return renderer;
	}

	public WaterShader getWaterShader() {
		return waterShader;
	}

	public TextRenderer getTextRenderer() {
		return textRenderer;
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

	public SoundSystem getSoundSystem() {
		return soundSystem;
	}

	public ParticleController getParticleController() {
		return particleController;
	}

	public PostProcessingRenderer getPostProcessing() {
		return postProcessing;
	}

	public Frustum getFrustum() {
		return frustum;
	}

	public Physics getPhysics() {
		return physics;
	}

	public FrameBuffer getWaterFBO() {
		return waterFBO;
	}

}