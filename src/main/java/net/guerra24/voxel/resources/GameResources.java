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

import net.guerra24.voxel.core.GlobalStates;
import net.guerra24.voxel.graphics.FrameBuffer;
import net.guerra24.voxel.graphics.Frustum;
import net.guerra24.voxel.graphics.GuiRenderer;
import net.guerra24.voxel.graphics.MasterRenderer;
import net.guerra24.voxel.graphics.MasterShadowRenderer;
import net.guerra24.voxel.graphics.PostProcessingRenderer;
import net.guerra24.voxel.graphics.SkyboxRenderer;
import net.guerra24.voxel.graphics.TextMasterRenderer;
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

	private Random rand;
	private Loader loader;
	private Camera camera;
	private Camera sun_Camera;
	private MasterRenderer renderer;
	private SkyboxRenderer skyboxRenderer;
	private GuiRenderer guiRenderer;
	private TextMasterRenderer textMasterRenderer;
	private TextHandler textHandler;
	private GlobalStates globalStates;
	private ParticleController particleController;
	private PostProcessingRenderer postProcessing;
	private MasterShadowRenderer masterShadowRenderer;
	private SoundSystem soundSystem;
	private Frustum frustum;
	private Gson gson;
	private FrameBuffer waterFBO;
	private Physics physics;

	/**
	 * Constructor, Create the Game Resources and Init Loader
	 * 
	 */
	public GameResources() {
	}

	/**
	 * Initialize the Game Objects
	 * 
	 */
	public void init() {
		loader = new Loader();
		camera = new Camera();
		sun_Camera = new Camera();
		sun_Camera.setPosition(new Vector3f(0, 70, 0));
		sun_Camera.setPitch(70);
		sun_Camera.setYaw(20);
		gson = new Gson();
		renderer = new MasterRenderer(loader);
		guiRenderer = new GuiRenderer(loader);
		skyboxRenderer = new SkyboxRenderer(loader, renderer.getProjectionMatrix());
		textMasterRenderer = new TextMasterRenderer(loader);
		textHandler = new TextHandler(this);
		particleController = new ParticleController(loader);
		postProcessing = new PostProcessingRenderer(loader);
		waterFBO = new FrameBuffer(false, false, 128, 128);
		masterShadowRenderer = new MasterShadowRenderer();
		physics = new Physics(this);
		frustum = new Frustum();
		rand = new Random();
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
		} catch (SoundSystemException e) {
			Logger.error("Unable to bind SoundSystem Libs");
		}
		soundSystem = new SoundSystem();
		globalStates = new GlobalStates(loader);
		Block.initBasicBlocks();

	}

	/**
	 * Load Music
	 * 
	 */
	public void music() {
		soundSystem.backgroundMusic("menu1", "menu1.ogg", true);
	}

	/**
	 * Load Resources like Mobs
	 * 
	 */
	public void addRes() {
	}

	/**
	 * Disposes all objects
	 * 
	 */
	public void cleanUp() {
		particleController.dispose();
		textMasterRenderer.cleanUp();
		masterShadowRenderer.cleanUp();
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

	public SkyboxRenderer getSkyboxRenderer() {
		return skyboxRenderer;
	}

	public GuiRenderer getGuiRenderer() {
		return guiRenderer;
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

	public TextMasterRenderer getTextMasterRenderer() {
		return textMasterRenderer;
	}

	public Physics getPhysics() {
		return physics;
	}

	public FrameBuffer getWaterFBO() {
		return waterFBO;
	}

	public TextHandler getTextHandler() {
		return textHandler;
	}

	public GlobalStates getGlobalStates() {
		return globalStates;
	}

	public MasterShadowRenderer getMasterShadowRenderer() {
		return masterShadowRenderer;
	}

	public Camera getSun_Camera() {
		return sun_Camera;
	}

}