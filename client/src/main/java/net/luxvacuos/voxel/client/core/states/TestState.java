/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

package net.luxvacuos.voxel.client.core.states;

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import org.lwjgl.glfw.GLFW;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.igl.vector.Vector4f;
import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.ecs.EntityResources;
import net.luxvacuos.voxel.client.ecs.entities.BasicEntity;
import net.luxvacuos.voxel.client.ecs.entities.CameraEntity;
import net.luxvacuos.voxel.client.ecs.entities.PlayerCamera;
import net.luxvacuos.voxel.client.ecs.entities.Sun;
import net.luxvacuos.voxel.client.input.KeyboardHandler;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.WM;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleDomain;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Light;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Material;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.ParticleTexture;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.TexturedModel;
import net.luxvacuos.voxel.client.rendering.utils.BlockFaceAtlas;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.ui.menus.GameWindow;
import net.luxvacuos.voxel.client.ui.menus.PauseWindow;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.client.world.block.RenderBlock;
import net.luxvacuos.voxel.client.world.particles.ParticleSystem;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.GlobalVariables;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Scale;
import net.luxvacuos.voxel.universal.material.BlockMaterial;
import net.luxvacuos.voxel.universal.world.dimension.PhysicsSystem;

/**
 * Test State
 * 
 * @author danirod
 */
public class TestState extends AbstractState {

	private PhysicsSystem physicsSystem;
	private Engine engine;
	private Sun sun;
	private ClientWorldSimulation worldSimulation;
	private CameraEntity camera;
	private Tessellator tess;
	private ParticleSystem particleSystem;
	private Vector3d particlesPoint;
	private GameWindow gameWindow;
	private PauseWindow pauseWindow;

	private BasicEntity mat1, mat2, mat3, mat4, mat5, rocket, plane, character, cerberus;

	private TexturedModel sphere, dragon, rocketM, planeM, characterM, cerberusM;
	private ParticleTexture fire;

	public TestState() {
		super(StateNames.TEST);
	}

	@Override
	public void init() {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		ResourceLoader loader = window.getResourceLoader();

		Matrix4d[] shadowProjectionMatrix = new Matrix4d[4];

		int shadowDrawDistance = (int) GlobalVariables.REGISTRY
				.getRegistryItem("/Voxel/Settings/Graphics/shadowsDrawDistance");

		shadowProjectionMatrix[0] = Maths.orthographic(-shadowDrawDistance / 32, shadowDrawDistance / 32,
				-shadowDrawDistance / 32, shadowDrawDistance / 32, -shadowDrawDistance, shadowDrawDistance, false);
		shadowProjectionMatrix[1] = Maths.orthographic(-shadowDrawDistance / 10, shadowDrawDistance / 10,
				-shadowDrawDistance / 10, shadowDrawDistance / 10, -shadowDrawDistance, shadowDrawDistance, false);
		shadowProjectionMatrix[2] = Maths.orthographic(-shadowDrawDistance / 4, shadowDrawDistance / 4,
				-shadowDrawDistance / 4, shadowDrawDistance / 4, -shadowDrawDistance, shadowDrawDistance, false);
		shadowProjectionMatrix[3] = Maths.orthographic(-shadowDrawDistance, shadowDrawDistance, -shadowDrawDistance,
				shadowDrawDistance, -shadowDrawDistance, shadowDrawDistance, false);
		Matrix4d projectionMatrix = Renderer.createProjectionMatrix(window.getWidth(), window.getHeight(),
				(int) REGISTRY.getRegistryItem("/Voxel/Settings/Core/fov"), ClientVariables.NEAR_PLANE,
				ClientVariables.FAR_PLANE);

		camera = new PlayerCamera(projectionMatrix, window);
		camera.setPosition(new Vector3d(0, 2, 0));
		sun = new Sun(shadowProjectionMatrix);

		EntityResources.load(loader);

		worldSimulation = new ClientWorldSimulation(10000);
		engine = new Engine();
		physicsSystem = new PhysicsSystem(null);
		physicsSystem.addBox(new BoundingBox(new Vector3(-50, -1, -50), new Vector3(50, 0, 50)));
		engine.addSystem(physicsSystem);

		Material sphereMaterial = new Material(new Vector4f(1f), 1f, 1f);
		sphereMaterial.setDiffuseTexture(loader.loadTexture("test_state/rusted_iron"));
		sphereMaterial.setNormalTexture(loader.loadTextureMisc("test_state/rusted_iron-n"));
		sphereMaterial.setRoughnessTexture(loader.loadTextureMisc("test_state/rusted_iron-r"));
		sphereMaterial.setMetallicTexture(loader.loadTextureMisc("test_state/rusted_iron-m"));
		sphere = new TexturedModel(loader.loadObjModel("test_state/sphere"), sphereMaterial);

		tess = new Tessellator(BlocksResources.getMaterial());

		Renderer.getLightRenderer().addLight(new Light(new Vector3d(-8, 5, -8), new Vector3f(1, 1, 1)));
		Renderer.getLightRenderer().addLight(new Light(new Vector3d(-8, 5, 8), new Vector3f(1, 1, 1)));
		Renderer.getLightRenderer().addLight(new Light(new Vector3d(8, 5, -8), new Vector3f(1, 1, 1)));
		Renderer.getLightRenderer().addLight(new Light(new Vector3d(8, 5, 8), new Vector3f(1, 1, 1)));
		Renderer.getLightRenderer().addLight(new Light(new Vector3d(0, 5, 0), new Vector3f(1, 1, 1)));

		mat1 = new BasicEntity(sphere);
		mat1.getComponent(Position.class).set(0, 1, 0);

		mat2 = new BasicEntity(sphere);
		mat2.getComponent(Position.class).set(3, 1, 0);

		mat3 = new BasicEntity(sphere);
		mat3.getComponent(Position.class).set(6, 1, 0);

		mat4 = new BasicEntity(sphere);
		mat4.getComponent(Position.class).set(9, 1, 0);

		Material dragonMat = new Material(new Vector4f(1), 1f, 0f);
		dragonMat.setDiffuseTexture(loader.loadTexture("test_state/scuffed-plastic"));
		dragonMat.setNormalTexture(loader.loadTextureMisc("test_state/scuffed-plastic_n"));
		dragonMat.setRoughnessTexture(loader.loadTextureMisc("test_state/scuffed-plastic_r"));

		dragon = new TexturedModel(loader.loadObjModel("test_state/dragon"), dragonMat);

		mat5 = new BasicEntity(dragon);

		mat5.getComponent(Position.class).set(-7, 0, 0);
		mat5.getComponent(Scale.class).setScale(0.5f);

		rocketM = new TexturedModel(loader.loadObjModel("test_state/Rocket"),
				new Material(new Vector4f(0.8f, 0.8f, 0.8f, 1.0f), 0.5f, 0));

		rocket = new BasicEntity(rocketM);
		rocket.getComponent(Position.class).set(0, 0, -5);

		Material planeMat = new Material(new Vector4f(1), 1f, 0);
		planeMat.setDiffuseTexture(loader.loadTexture("test_state/mahogfloor"));
		planeMat.setNormalTexture(loader.loadTextureMisc("test_state/mahogfloor_n"));
		planeMat.setRoughnessTexture(loader.loadTextureMisc("test_state/mahogfloor_r"));

		planeM = new TexturedModel(loader.loadObjModel("test_state/plane"), planeMat);

		plane = new BasicEntity(planeM);
		plane.getComponent(Scale.class).setScale(2f);

		Material characterMat = new Material(new Vector4f(1), 0.5f, 0);
		characterMat.setDiffuseTexture(loader.loadTexture("test_state/character"));

		characterM = new TexturedModel(loader.loadObjModel("test_state/character"), characterMat);

		character = new BasicEntity(characterM);
		character.getComponent(Position.class).set(0, 0, 5);
		character.getComponent(Scale.class).setScale(0.21f);

		Material cerberusMat = new Material(new Vector4f(1), 0.5f, 0);
		cerberusMat.setDiffuseTexture(loader.loadTexture("test_state/cerberus"));
		cerberusMat.setRoughnessTexture(loader.loadTextureMisc("test_state/cerberus_r"));
		cerberusMat.setNormalTexture(loader.loadTextureMisc("test_state/cerberus_n"));
		cerberusMat.setMetallicTexture(loader.loadTextureMisc("test_state/cerberus_m"));

		cerberusM = new TexturedModel(loader.loadObjModel("test_state/cerberus"), cerberusMat);

		cerberus = new BasicEntity(cerberusM);
		cerberus.getComponent(Position.class).set(5, 1.25f, 5);
		cerberus.getComponent(Scale.class).setScale(0.5f);

		fire = new ParticleTexture(loader.loadTexture("particles/fire0").getID(), 4);

		particleSystem = new ParticleSystem(fire, 1000, 1, -1f, 3f, 6f);
		particleSystem.setDirection(new Vector3d(0, -1, 0), 0.4f);
		particlesPoint = new Vector3d(0, 1.7f, -5);

	}

	@Override
	public void dispose() {
		tess.cleanUp();
		sphere.dispose();
		dragon.dispose();
		characterM.dispose();
		fire.dispose();
		planeM.dispose();
		rocketM.dispose();
		cerberusM.dispose();
	}

	@Override
	public void start() {
		Renderer.setShadowPass((camera, sunCamera, frustum, shadowMap) -> {
			tess.drawShadow(sunCamera);
		});

		Renderer.setDeferredPass((camera, sunCamera, frustum, shadowMap) -> {
			tess.draw(camera, worldSimulation);
		});

		RenderBlock t = new RenderBlock(new BlockMaterial("test"), new BlockFaceAtlas("leaves"));
		t.setID(1);
		tess.begin();
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				tess.generateCube(20 + x, 0, z, 1, true, true, true, true, true, true, t);
			}
		}
		tess.end();
		camera.setPosition(new Vector3d(0, 2, 0));
		physicsSystem.getEngine().addEntity(camera);
		physicsSystem.getEngine().addEntity(plane);
		physicsSystem.getEngine().addEntity(mat1);
		physicsSystem.getEngine().addEntity(mat2);
		physicsSystem.getEngine().addEntity(mat3);
		physicsSystem.getEngine().addEntity(mat4);
		physicsSystem.getEngine().addEntity(mat5);
		physicsSystem.getEngine().addEntity(rocket);
		physicsSystem.getEngine().addEntity(character);
		physicsSystem.getEngine().addEntity(cerberus);
		((PlayerCamera) camera).setMouse();
		Renderer.render(engine.getEntities(), ParticleDomain.getParticles(), camera, worldSimulation, sun, 0);
		gameWindow = new GameWindow(0, ClientVariables.HEIGHT, ClientVariables.WIDTH, ClientVariables.HEIGHT);
		WM.getWM().addWindow(0, gameWindow);
	}

	@Override
	public void end() {
		physicsSystem.getEngine().removeAllEntities();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		WM.getWM().update(delta);
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		KeyboardHandler kbh = window.getKeyboardHandler();
		if (!ClientVariables.paused) {

			engine.update(delta);
			sun.update(camera.getPosition(), worldSimulation.update(delta), delta);
			particleSystem.generateParticles(particlesPoint, delta);
			ParticleDomain.update(delta, camera);

			if (kbh.isKeyPressed(GLFW.GLFW_KEY_R))
				ClientVariables.raining = !ClientVariables.raining;
			if (kbh.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
				kbh.ignoreKeyUntilRelease(GLFW.GLFW_KEY_ESCAPE);
				((PlayerCamera) camera).unlockMouse();
				ClientVariables.paused = true;
				float borderSize = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/borderSize");
				float titleBarHeight = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/titleBarHeight");
				pauseWindow = new PauseWindow(borderSize + 10, ClientVariables.HEIGHT - titleBarHeight - 10,
						ClientVariables.WIDTH - borderSize * 2f - 20,
						ClientVariables.HEIGHT - titleBarHeight - borderSize - 20);
				WM.getWM().addWindow(pauseWindow);
			}
		} else if (ClientVariables.exitWorld) {
			gameWindow.closeWindow();
			pauseWindow.closeWindow();
			ClientVariables.exitWorld = false;
			ClientVariables.paused = false;
			StateMachine.setCurrentState(StateNames.MAIN_MENU);
		} else {
			if (kbh.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
				kbh.ignoreKeyUntilRelease(GLFW.GLFW_KEY_ESCAPE);
				Mouse.setGrabbed(true);
				ClientVariables.paused = false;
				pauseWindow.closeWindow();
			}
		}

	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Renderer.render(engine.getEntities(), ParticleDomain.getParticles(), camera, worldSimulation, sun, alpha);
		Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Renderer.clearColors(1, 1, 1, 1);
		WM.getWM().render();
	}

}
