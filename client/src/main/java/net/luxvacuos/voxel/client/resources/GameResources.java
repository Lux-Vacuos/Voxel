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

import net.luxvacuos.igl.CustomLog;
import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientGameSettings;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.rendering.api.glfw.PixelBufferHandle;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowHandle;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleMaster;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.ParticleTexture;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorBasicShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorShader;
import net.luxvacuos.voxel.client.sound.LibraryLWJGLOpenAL;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystem;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystemConfig;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystemException;
import net.luxvacuos.voxel.client.sound.soundsystem.codecs.CodecJOgg;
import net.luxvacuos.voxel.client.util.LoggerSoundSystem;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.client.world.entities.EntityResources;
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
	private Camera sunCamera;
	private Renderer renderer;

	private ClientWorldSimulation worldSimulation;

	private SoundSystem soundSystem;

	private Vector3d sunRotation = new Vector3d(5, 0, -35);
	private Vector3d lightPos = new Vector3d(0, 0, 0);
	private Vector3d invertedLightPosition = new Vector3d(0, 0, 0);
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
		Window window = WindowManager.getWindow(gameWindowID);
		ResourceLoader loader = window.getResourceLoader();
		scripting = new Scripting();
		rand = new Random();

		Matrix4d projectionMatrix = Renderer.createProjectionMatrix(window.getWidth(), window.getHeight(),
				ClientVariables.FOV, ClientVariables.NEAR_PLANE, ClientVariables.FAR_PLANE);
		Matrix4d shadowProjectionMatrix = Maths.orthographic(-ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, false);

		camera = new PlayerCamera(projectionMatrix, this.getGameWindow());
		sunCamera = new SunCamera(shadowProjectionMatrix);
		sunCamera.setPosition(new Vector3d(0, 0, 0));
		sunCamera.setPosition(new Vector3d(sunRotation.y, sunRotation.x, sunRotation.z));

		worldSimulation = new ClientWorldSimulation();
		renderer = new Renderer(getGameWindow(), camera, sunCamera);
		TessellatorShader.getInstance();
		TessellatorBasicShader.getInstance();
		ParticleMaster.getInstance().init(loader, camera.getProjectionMatrix());

		CustomLog.getInstance();
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
		} catch (SoundSystemException e) {
			e.printStackTrace();
		}
		SoundSystemConfig.setSoundFilesPackage("assets/sounds/");
		SoundSystemConfig.setLogger(new LoggerSoundSystem());
		soundSystem = new SoundSystem();
	}

	public void loadResources() {
		ResourceLoader loader = this.getGameWindow().getResourceLoader();
		torchTexture = new ParticleTexture(loader.loadTextureParticle("fire0"), 4);
		EntityResources.load(loader);
		// for (int x = 0; x < 32; x++) {
		// lightRenderer.addLight(new Light(new Vector3d(Maths.randInt(-128,
		// 128), 130, Maths.randInt(-128, 128))));
		// }
	}

	@Override
	public void postInit() {
	}

	public void update(float rot, float delta) {
		sunRotation.setY(rot);
		sunCamera.setRotation(new Vector3d(sunRotation.y, sunRotation.x, sunRotation.z));
		SunCamera cam = (SunCamera) sunCamera;
		((SunCamera) sunCamera).updateShadowRay(false);
		lightPos.set(cam.getDRay().direction.x * 10, cam.getDRay().direction.y * 10, cam.getDRay().direction.z * 10);

		((SunCamera) sunCamera).updateShadowRay(true);
		invertedLightPosition.set(cam.getDRay().direction.x * 10, cam.getDRay().direction.y * 10,
				cam.getDRay().direction.z * 10);
	}

	/**
	 * Disposes all objects
	 * 
	 */
	@Override
	public void dispose() {
		gameSettings.save();
		renderer.cleanUp();
		soundSystem.cleanup();
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

	public Renderer getRenderer() {
		return renderer;
	}

	public SoundSystem getSoundSystem() {
		return soundSystem;
	}

	public Camera getSunCamera() {
		return sunCamera;
	}

	public Vector3d getLightPos() {
		return lightPos;
	}

	public Vector3d getInvertedLightPosition() {
		return invertedLightPosition;
	}

	public ParticleTexture getTorchTexture() {
		return torchTexture;
	}

	public Vector3d getSunRotation() {
		return sunRotation;
	}

	public Window getGameWindow() {
		return WindowManager.getWindow(this.gameWindowID);
	}

	public ClientWorldSimulation getWorldSimulation() {
		return worldSimulation;
	}

	public Scripting getScripting() {
		return scripting;
	}

}