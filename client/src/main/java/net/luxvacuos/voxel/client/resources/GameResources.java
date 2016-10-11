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

import java.io.File;
import java.util.Random;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;

import net.luxvacuos.igl.CustomLog;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.ClientGameSettings;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.network.VoxelClient;
import net.luxvacuos.voxel.client.rendering.api.glfw.PixelBufferHandle;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowHandle;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.opengl.Frustum;
import net.luxvacuos.voxel.client.rendering.api.opengl.ItemsDropRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.ItemsGuiRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.LightRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterShadowRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleMaster;
import net.luxvacuos.voxel.client.rendering.api.opengl.RenderingPipeline;
import net.luxvacuos.voxel.client.rendering.api.opengl.SkyboxRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Light;
import net.luxvacuos.voxel.client.rendering.api.opengl.pipeline.MultiPass;
import net.luxvacuos.voxel.client.rendering.api.opengl.pipeline.SinglePass;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorBasicShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorShader;
import net.luxvacuos.voxel.client.resources.models.ParticleTexture;
import net.luxvacuos.voxel.client.sound.LibraryLWJGLOpenAL;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystem;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystemConfig;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystemException;
import net.luxvacuos.voxel.client.sound.soundsystem.codecs.CodecJOgg;
import net.luxvacuos.voxel.client.util.LoggerSoundSystem;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.WorldsHandler;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.client.world.entities.SunCamera;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.Scripting;
import net.luxvacuos.voxel.universal.resources.AbstractGameResources;

/**
 * Game Resources
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class GameResources extends AbstractGameResources {

	private static GameResources instance = null;

	public static GameResources getInstance() {
		if (instance == null)
			instance = new GameResources();
		return instance;
	}

	private long gameWindowID;
	private Scripting scripting;

	private Random rand;
	private Camera camera;
	private Camera sun_Camera;
	private MasterRenderer renderer;
	private SkyboxRenderer skyboxRenderer;

	private RenderingPipeline renderingPipeline;
	private MasterShadowRenderer masterShadowRenderer;
	private ItemsDropRenderer itemsDropRenderer;
	private ItemsGuiRenderer itemsGuiRenderer;
	private LightRenderer lightRenderer;

	private VoxelClient voxelClient;
	private ClientWorldSimulation worldSimulation;
	private WorldsHandler worldsHandler;

	private SoundSystem soundSystem;
	private Frustum frustum;

	private Vector3f sunRotation = new Vector3f(5, 0, -45);
	private Vector3f lightPos = new Vector3f(0, 0, 0);
	private Vector3f invertedLightPosition = new Vector3f(0, 0, 0);
	private ParticleTexture torchTexture;

	private GameResources() {
	}

	@Override
	public void preInit() {
		gameSettings = new ClientGameSettings();
		gameSettings.load(new File(ClientVariables.SETTINGS_PATH));
		gameSettings.read();

		String[] icons = new String[] { "assets/" + ClientVariables.assets + "/icons/icon32.png",
				"assets/" + ClientVariables.assets + "/icons/icon64.png" };

		WindowHandle handle = WindowManager.generateHandle(ClientVariables.WIDTH, ClientVariables.HEIGHT, "Voxel");
		handle.canResize(false).isVisible(false).setIcon(icons);
		PixelBufferHandle pb = new PixelBufferHandle();
		pb.setDepthBits(32);
		pb.setRedBits(16);
		pb.setBlueBits(16);
		pb.setGreenBits(16);
		handle.setPixelBuffer(pb);
		this.gameWindowID = WindowManager.createWindow(handle, ClientVariables.VSYNC);
		Window window = WindowManager.getWindow(this.gameWindowID);

		ResourceLoader loader = window.getResourceLoader();
		loader.loadNVGFont("Roboto-Bold", "Roboto-Bold");
		loader.loadNVGFont("Roboto-Regular", "Roboto-Regular");
		loader.loadNVGFont("Entypo", "Entypo", 40);
	}

	@Override
	public void init(AbstractVoxel voxel) {
		ResourceLoader loader = WindowManager.getWindow(this.gameWindowID).getResourceLoader();
		scripting = new Scripting();
		rand = new Random();

		masterShadowRenderer = new MasterShadowRenderer();
		renderer = new MasterRenderer(this);
		skyboxRenderer = new SkyboxRenderer(loader, renderer.getProjectionMatrix());
		lightRenderer = new LightRenderer();

		itemsDropRenderer = new ItemsDropRenderer();
		TessellatorShader.getInstance();
		TessellatorBasicShader.getInstance();
		ParticleMaster.getInstance().init(loader, renderer.getProjectionMatrix());
		sun_Camera = new SunCamera(masterShadowRenderer.getProjectionMatrix());
		sun_Camera.setPosition(new Vector3f(0, 0, 0));
		sun_Camera.setYaw(sunRotation.x);
		sun_Camera.setPitch(sunRotation.y);
		sun_Camera.setRoll(sunRotation.z);
		camera = new PlayerCamera(renderer.getProjectionMatrix(), this.getGameWindow());
		itemsGuiRenderer = new ItemsGuiRenderer(this);
		kryo = new Kryo();
		kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
		frustum = new Frustum();
		worldSimulation = new ClientWorldSimulation();

		CustomLog.getInstance();
		voxelClient = new VoxelClient(this);
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
		} catch (SoundSystemException e) {
			e.printStackTrace();
		}
		SoundSystemConfig.setSoundFilesPackage("assets/sounds/");
		SoundSystemConfig.setLogger(new LoggerSoundSystem());
		soundSystem = new SoundSystem();
		worldsHandler = new WorldsHandler();
	}

	public void loadResources() {
		ResourceLoader loader = this.getGameWindow().getResourceLoader();
		torchTexture = new ParticleTexture(loader.loadTextureParticle("fire0"), 4);
		EntityResources.loadEntityResources(loader);
		//for (int x = 0; x < 64; x++) {
		//	lightRenderer.addLight(new Light(new Vector3f(Maths.randInt(-128, 128), 130, Maths.randInt(-128, 128))));
		//}
	}

	@Override
	public void postInit() {
		if (ClientVariables.renderingPipeline.equals("SinglePass"))
			renderingPipeline = new SinglePass();
		else if (ClientVariables.renderingPipeline.equals("MultiPass"))
			renderingPipeline = new MultiPass();
	}

	public void update(float rot, float delta) {
		sunRotation.setY(rot);
		sun_Camera.setYaw(sunRotation.x);
		sun_Camera.setPitch(sunRotation.y);
		sun_Camera.setRoll(sunRotation.z);
		((SunCamera) sun_Camera).updateShadowRay(this, false);
		lightPos.set(sun_Camera.getDRay().direction.x * 10, sun_Camera.getDRay().direction.y * 10,
				sun_Camera.getDRay().direction.z * 10);

		((SunCamera) sun_Camera).updateShadowRay(this, true);
		invertedLightPosition.set(sun_Camera.getDRay().direction.x * 10, sun_Camera.getDRay().direction.y * 10,
				sun_Camera.getDRay().direction.z * 10);
	}

	/**
	 * Disposes all objects
	 * 
	 */
	@Override
	public void dispose() {
		gameSettings.save();
		TessellatorShader.getInstance().cleanUp();
		TessellatorBasicShader.getInstance().cleanUp();
		masterShadowRenderer.cleanUp();
		itemsDropRenderer.cleanUp();
		ParticleMaster.getInstance().cleanUp();
		renderingPipeline.dispose();
		itemsGuiRenderer.cleanUp();
		renderer.cleanUp();
		soundSystem.cleanup();
		voxelClient.dispose();
	}

	public Random getRand() {
		return rand;
	}

	public ResourceLoader getResourceLoader() {
		return WindowManager.getWindow(this.gameWindowID).getResourceLoader();
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

	public RenderingPipeline getRenderingPipeline() {
		return renderingPipeline;
	}

	public Frustum getFrustum() {
		return frustum;
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

	public ParticleTexture getTorchTexture() {
		return torchTexture;
	}

	public Vector3f getSunRotation() {
		return sunRotation;
	}

	public Window getGameWindow() {
		return WindowManager.getWindow(this.gameWindowID);
	}

	public VoxelClient getVoxelClient() {
		return voxelClient;
	}

	public ItemsDropRenderer getItemsDropRenderer() {
		return itemsDropRenderer;
	}

	public ClientWorldSimulation getWorldSimulation() {
		return worldSimulation;
	}

	public WorldsHandler getWorldsHandler() {
		return worldsHandler;
	}

	public ItemsGuiRenderer getItemsGuiRenderer() {
		return itemsGuiRenderer;
	}

	public Scripting getScripting() {
		return scripting;
	}

	public LightRenderer getLightRenderer() {
		return lightRenderer;
	}

}