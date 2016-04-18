/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.resources;

import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_API;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;

import java.util.Random;

import com.esotericsoftware.kryo.Kryo;

import net.luxvacuos.igl.CustomLog;
import net.luxvacuos.igl.Logger;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.GameSettings;
import net.luxvacuos.voxel.client.core.GlobalStates;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.core.WorldSimulation;
import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.network.VoxelClient;
import net.luxvacuos.voxel.client.rendering.api.glfw.ContextFormat;
import net.luxvacuos.voxel.client.rendering.api.glfw.Display;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.DeferredShadingRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.Frustum;
import net.luxvacuos.voxel.client.rendering.api.opengl.ItemsDropRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.ItemsGuiRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterShadowRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleMaster;
import net.luxvacuos.voxel.client.rendering.api.opengl.SkyboxRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.ShaderProgram;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorBasicShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorShader;
import net.luxvacuos.voxel.client.resources.models.ParticleTexture;
import net.luxvacuos.voxel.client.sound.LibraryLWJGLOpenAL;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystem;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystemConfig;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystemException;
import net.luxvacuos.voxel.client.sound.soundsystem.codecs.CodecJOgg;
import net.luxvacuos.voxel.client.ui.menu.Menu;
import net.luxvacuos.voxel.client.util.LoggerSoundSystem;
import net.luxvacuos.voxel.client.world.WorldsHandler;
import net.luxvacuos.voxel.client.world.block.Block;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.client.world.entities.SunCamera;
import net.luxvacuos.voxel.universal.resources.UGameResources;

/**
 * Game Resources
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class GameResources extends UGameResources {

	private static GameResources instance = null;

	public static GameResources instance() {
		if (instance == null)
			instance = new GameResources();
		return instance;
	}

	/**
	 * GameResources Data
	 */
	private Display display;
	private Random rand;
	private Loader loader;
	private Camera camera;
	private Camera sun_Camera;
	private MasterRenderer renderer;
	private SkyboxRenderer skyboxRenderer;
	private GlobalStates globalStates;
	private DeferredShadingRenderer deferredShadingRenderer;
	private MasterShadowRenderer masterShadowRenderer;
	private ItemsDropRenderer itemsDropRenderer;
	private ItemsGuiRenderer itemsGuiRenderer;

	private VoxelClient voxelClient;
	private WorldSimulation worldSimulation;
	private WorldsHandler worldsHandler;

	private SoundSystem soundSystem;
	private Frustum frustum;
	private Kryo kryo;
	private Menu menuSystem;
	private GameSettings gameSettings;

	private Vector3f sunRotation = new Vector3f(5, 0, -45);
	private Vector3f lightPos = new Vector3f(0, 0, 0);
	private Vector3f invertedLightPosition = new Vector3f(0, 0, 0);
	private ParticleTexture torchTexture;

	private GameResources() {
	}

	public void preInit() throws Exception {
		gameSettings = new GameSettings();
		display = new Display();
		display.create(VoxelVariables.WIDTH, VoxelVariables.HEIGHT, "Voxel", VoxelVariables.VSYNC, false, false,
				new ContextFormat(3, 3, GLFW_OPENGL_API, GLFW_OPENGL_CORE_PROFILE, true),
				new String[] { "assets/icons/icon32.png", "assets/icons/icon64.png" });
		Keyboard.setDisplay(display);
		Mouse.setDisplay(display);
		VectorsRendering.setDisplay(display);
		Timers.setDisplay(display);
		ShaderProgram.setDisplay(display);
	}

	public void init(Voxel voxel) throws Exception {
		rand = new Random();
		if (display.isVk()) {
		}
		loader = new Loader(display);
		masterShadowRenderer = new MasterShadowRenderer(display);
		renderer = new MasterRenderer(this);
		skyboxRenderer = new SkyboxRenderer(loader, renderer.getProjectionMatrix());
		deferredShadingRenderer = new DeferredShadingRenderer(this);
		itemsDropRenderer = new ItemsDropRenderer(this);
		TessellatorShader.getInstance();
		TessellatorBasicShader.getInstance();
		ParticleMaster.getInstance().init(loader, renderer.getProjectionMatrix());
		sun_Camera = new SunCamera(masterShadowRenderer.getProjectionMatrix());
		sun_Camera.setPosition(new Vector3f(0, 0, 0));
		sun_Camera.setYaw(sunRotation.x);
		sun_Camera.setPitch(sunRotation.y);
		sun_Camera.setRoll(sunRotation.z);
		camera = new PlayerCamera(renderer.getProjectionMatrix(), display);
		itemsGuiRenderer = new ItemsGuiRenderer(this);
		kryo = new Kryo();
		frustum = new Frustum();
		worldSimulation = new WorldSimulation();

		CustomLog.getInstance();
		voxelClient = new VoxelClient(this);

		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
			SoundSystemConfig.setSoundFilesPackage("assets/sounds/");
			SoundSystemConfig.setLogger(new LoggerSoundSystem());
		} catch (SoundSystemException e) {
			Logger.error("Unable to setting up Sound System Configuration");
			e.printStackTrace();
			System.exit(-1);
		}
		soundSystem = new SoundSystem();
		globalStates = new GlobalStates();
		Block.initBasicBlocks();
		menuSystem = new Menu(this);
		worldsHandler = new WorldsHandler();
	}

	public void loadResources() throws Exception {
		loader.loadNVGFont("Roboto-Bold", "Roboto-Bold");
		loader.loadNVGFont("Roboto-Regular", "Roboto-Regular");
		loader.loadNVGFont("Entypo", "Entypo", 40);
		torchTexture = new ParticleTexture(loader.loadTextureParticle("fire0"), 4);
		EntityResources.loadEntityResources(loader);
	}

	public void update(float rot) {
		sunRotation.setY(rot);
		sun_Camera.setYaw(sunRotation.x);
		sun_Camera.setPitch(sunRotation.y);
		sun_Camera.setRoll(sunRotation.z);
		((SunCamera) sun_Camera).updateShadowRay(this, false);
		lightPos = new Vector3f(sun_Camera.getDRay().direction.x * 1000, sun_Camera.getDRay().direction.y * 1000,
				sun_Camera.getDRay().direction.z * 1000);
		Vector3f.add(sun_Camera.getPosition(), lightPos, lightPos);

		((SunCamera) sun_Camera).updateShadowRay(this, true);
		invertedLightPosition = new Vector3f(sun_Camera.getDRay().direction.x * 1000,
				sun_Camera.getDRay().direction.y * 1000, sun_Camera.getDRay().direction.z * 1000);
		Vector3f.add(sun_Camera.getPosition(), invertedLightPosition, invertedLightPosition);
	}

	/**
	 * Disposes all objects
	 * 
	 */
	public void cleanUp() throws Exception {
		gameSettings.save();
		TessellatorShader.getInstance().cleanUp();
		TessellatorBasicShader.getInstance().cleanUp();
		masterShadowRenderer.cleanUp();
		itemsDropRenderer.cleanUp();
		ParticleMaster.getInstance().cleanUp();
		deferredShadingRenderer.cleanUp();
		itemsGuiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		soundSystem.cleanup();
		voxelClient.dispose();
	}

	public Random getRand() {
		return rand;
	}

	public Loader getLoader() {
		return loader;
	}

	public Kryo getKryo() {
		return kryo;
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

	public SoundSystem getSoundSystem() {
		return soundSystem;
	}

	public DeferredShadingRenderer getDeferredShadingRenderer() {
		return deferredShadingRenderer;
	}

	public Frustum getFrustum() {
		return frustum;
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

	public Vector3f getLightPos() {
		return lightPos;
	}

	public Vector3f getInvertedLightPosition() {
		return invertedLightPosition;
	}

	public GameSettings getGameSettings() {
		return gameSettings;
	}

	public Menu getMenuSystem() {
		return menuSystem;
	}

	public ParticleTexture getTorchTexture() {
		return torchTexture;
	}

	public Vector3f getSunRotation() {
		return sunRotation;
	}

	public Display getDisplay() {
		return display;
	}

	public VoxelClient getVoxelClient() {
		return voxelClient;
	}

	public ItemsDropRenderer getItemsDropRenderer() {
		return itemsDropRenderer;
	}

	public WorldSimulation getWorldSimulation() {
		return worldSimulation;
	}

	public WorldsHandler getWorldsHandler() {
		return worldsHandler;
	}

	public ItemsGuiRenderer getItemsGuiRenderer() {
		return itemsGuiRenderer;
	}

}