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

import static org.lwjgl.opengl.GL11.GL_NEAREST;

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
import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.ecs.entities.CameraEntity;
import net.luxvacuos.voxel.client.ecs.entities.PlayerCamera;
import net.luxvacuos.voxel.client.ecs.entities.Sun;
import net.luxvacuos.voxel.client.ecs.EntityResources;
import net.luxvacuos.voxel.client.ecs.entities.BasicEntity;
import net.luxvacuos.voxel.client.input.KeyboardHandler;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleDomain;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Light;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Material;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.ParticleTexture;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Texture;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.TexturedModel;
import net.luxvacuos.voxel.client.rendering.utils.BlockFaceAtlas;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.client.world.block.RenderBlock;
import net.luxvacuos.voxel.client.world.particles.ParticleSystem;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
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

	private BasicEntity mat1, mat2, mat3, mat4, mat5, rocket, plane, character;

	public TestState() {
		super(StateNames.TEST);
	}

	@Override
	public void init() {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		ResourceLoader loader = window.getResourceLoader();

		Matrix4d[] shadowProjectionMatrix = new Matrix4d[4];

		shadowProjectionMatrix[0] = Maths.orthographic(-ClientVariables.shadowMapDrawDistance / 32,
				ClientVariables.shadowMapDrawDistance / 32, -ClientVariables.shadowMapDrawDistance / 32,
				ClientVariables.shadowMapDrawDistance / 32, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, false);
		shadowProjectionMatrix[1] = Maths.orthographic(-ClientVariables.shadowMapDrawDistance / 10,
				ClientVariables.shadowMapDrawDistance / 10, -ClientVariables.shadowMapDrawDistance / 10,
				ClientVariables.shadowMapDrawDistance / 10, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, false);
		shadowProjectionMatrix[2] = Maths.orthographic(-ClientVariables.shadowMapDrawDistance / 4,
				ClientVariables.shadowMapDrawDistance / 4, -ClientVariables.shadowMapDrawDistance / 4,
				ClientVariables.shadowMapDrawDistance / 4, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, false);
		shadowProjectionMatrix[3] = Maths.orthographic(-ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, false);
		Matrix4d projectionMatrix = Renderer.createProjectionMatrix(window.getWidth(), window.getHeight(),
				ClientVariables.FOV, ClientVariables.NEAR_PLANE, ClientVariables.FAR_PLANE);

		camera = new PlayerCamera(projectionMatrix, window);
		camera.setPosition(new Vector3d(0, 2, 0));
		sun = new Sun(shadowProjectionMatrix);

		EntityResources.load(loader);

		worldSimulation = new ClientWorldSimulation(10000);
		engine = new Engine();
		physicsSystem = new PhysicsSystem(null);
		physicsSystem.addBox(new BoundingBox(new Vector3(-50, -1, -50), new Vector3(50, 0, 50)));
		engine.addSystem(physicsSystem);
		BlocksResources.createBlocks(loader);

		Texture test = loader.loadTexture("test_state/rusted_iron");
		Texture test_n = loader.loadTextureMisc("test_state/rusted_iron-n");
		Texture test_r = loader.loadTextureMisc("test_state/rusted_iron-r");
		Texture test_m = loader.loadTextureMisc("test_state/rusted_iron-m");

		tess = new Tessellator(BlocksResources.getMaterial());
		RenderBlock t = new RenderBlock(new BlockMaterial("test"), new BlockFaceAtlas("Ice"));
		t.setID(1);

		tess.begin();
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				tess.generateCube(20 + x, 0, z, 1, true, true, true, true, true, true, t);
			}
		}
		tess.end();

		Renderer.setShadowPass((camera, sunCamera, frustum, shadowMap) -> {
			tess.drawShadow(sunCamera);
		});

		Renderer.setDeferredPass((camera, sunCamera, frustum, shadowMap) -> {
			tess.draw(camera, sunCamera, worldSimulation, shadowMap);
		});

		Renderer.getLightRenderer().addLight(new Light(new Vector3d(-8, 5, -8), new Vector3f(1, 1, 1)));
		Renderer.getLightRenderer().addLight(new Light(new Vector3d(-8, 5, 8), new Vector3f(1, 1, 1)));
		Renderer.getLightRenderer().addLight(new Light(new Vector3d(8, 5, -8), new Vector3f(1, 1, 1)));
		Renderer.getLightRenderer().addLight(new Light(new Vector3d(8, 5, 8), new Vector3f(1, 1, 1)));
		Renderer.getLightRenderer().addLight(new Light(new Vector3d(0, 5, 0), new Vector3f(1, 1, 1)));

		RawModel sphere = loader.loadObjModel("test_state/sphere");

		mat1 = new BasicEntity(
				new TexturedModel(sphere, new Material(new Vector4f(1f), 1f, 1f, test, test_n, test_r, test_m)));
		mat1.getComponent(Position.class).set(0, 1, 0);

		mat2 = new BasicEntity(
				new TexturedModel(sphere, new Material(new Vector4f(1f), 1f, 1f, test, test_n, test_r, test_m)));
		mat2.getComponent(Position.class).set(3, 1, 0);

		mat3 = new BasicEntity(
				new TexturedModel(sphere, new Material(new Vector4f(1f), 1f, 1f, test, test_n, test_r, test_m)));
		mat3.getComponent(Position.class).set(6, 1, 0);

		mat4 = new BasicEntity(
				new TexturedModel(sphere, new Material(new Vector4f(1f), 1f, 1f, test, test_n, test_r, test_m)));
		mat4.getComponent(Position.class).set(9, 1, 0);

		mat5 = new BasicEntity(new TexturedModel(loader.loadObjModel("test_state/dragon"),
				new Material(new Vector4f(1), 1f, 1f, loader.loadTexture("test_state/gold-scuffed"),
						loader.loadTextureMisc("test_state/gold-scuffed_n"),
						loader.loadTextureMisc("test_state/gold-scuffed_r"),
						loader.loadTextureMisc("test_state/gold-scuffed_m"))));
		mat5.getComponent(Position.class).set(-7, 0, 0);
		mat5.getComponent(Scale.class).setScale(0.5f);

		rocket = new BasicEntity(new TexturedModel(loader.loadObjModel("test_state/Rocket"),
				new Material(new Vector4f(0.8f, 0.8f, 0.8f, 1.0f), 0.5f, 0, null, null, null, null)));
		rocket.getComponent(Position.class).set(0, 0, -5);

		plane = new BasicEntity(new TexturedModel(loader.loadObjModel("test_state/plane"),
				new Material(new Vector4f(1), 1f, 0, loader.loadTexture("test_state/mahogfloor"),
						loader.loadTextureMisc("test_state/mahogfloor_n"),
						loader.loadTextureMisc("test_state/mahogfloor_r"), null)));
		plane.getComponent(Scale.class).setScale(2f);

		character = new BasicEntity(new TexturedModel(loader.loadObjModel("test_state/character"),
				new Material(new Vector4f(1), 1, 0, loader.loadTexture("test_state/character"), null, null, null)));
		character.getComponent(Position.class).set(0, 0, 5);
		character.getComponent(Scale.class).setScale(0.21f);

		particleSystem = new ParticleSystem(new ParticleTexture(loader.loadTexture("particles/fire0"), 4), 1000, 1, -1f,
				3f, 6f);
		particleSystem.setDirection(new Vector3d(0, -1, 0), 0.4f);
		particlesPoint = new Vector3d(0, 1.7f, -5);
	}

	@Override
	public void dispose() {
		tess.cleanUp();
	}

	@Override
	public void start() {
		physicsSystem.getEngine().addEntity(camera);
		physicsSystem.getEngine().addEntity(plane);
		physicsSystem.getEngine().addEntity(mat1);
		physicsSystem.getEngine().addEntity(mat2);
		physicsSystem.getEngine().addEntity(mat3);
		physicsSystem.getEngine().addEntity(mat4);
		physicsSystem.getEngine().addEntity(mat5);
		physicsSystem.getEngine().addEntity(rocket);
		physicsSystem.getEngine().addEntity(character);
		((PlayerCamera) camera).setMouse();
	}

	@Override
	public void end() {
		physicsSystem.getEngine().removeAllEntities();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		KeyboardHandler kbh = ClientInternalSubsystem.getInstance().getGameWindow().getKeyboardHandler();

		engine.update(delta);
		sun.update(camera.getPosition(), worldSimulation.update(delta), delta);
		particleSystem.generateParticles(particlesPoint, delta);
		ParticleDomain.update(delta, camera);

		if (kbh.isKeyPressed(GLFW.GLFW_KEY_F1))
			ClientVariables.debug = !ClientVariables.debug;
		if (kbh.isKeyPressed(GLFW.GLFW_KEY_R))
			ClientVariables.raining = !ClientVariables.raining;
		if (kbh.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
			kbh.ignoreKeyUntilRelease(GLFW.GLFW_KEY_ESCAPE);
			((PlayerCamera) camera).unlockMouse();
			StateMachine.setCurrentState(StateNames.MAIN_MENU);
		}

	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();

		Renderer.render(physicsSystem.getEngine().getEntities(), ParticleDomain.getParticles(), camera, sun.getCamera(),
				worldSimulation, sun.getSunPosition(), sun.getInvertedSunPosition(), alpha);

		window.beingNVGFrame();
		if (ClientVariables.debug) {
			UIRendering.renderText(window.getID(), "Voxel " + " (" + ClientVariables.version + ")", "Roboto-Bold", 5,
					12, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(),
					"Used VRam: " + WindowManager.getUsedVRAM() + "KB " + " UPS: " + CoreInfo.ups, "Roboto-Bold", 5, 95,
					20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(), "Loaded Chunks: " + 0 + "   Rendered Chunks: " + 0, "Roboto-Bold", 5,
					115, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(),
					"Position XYZ:  " + camera.getPosition().getX() + "  " + camera.getPosition().getY() + "  "
							+ camera.getPosition().getZ(),
					"Roboto-Bold", 5, 135, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(),
					"Pitch Yaw Roll: " + camera.getRotation().getX() + " " + camera.getRotation().getY() + " "
							+ camera.getRotation().getZ(),
					"Roboto-Bold", 5, 155, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			Timers.renderDebugDisplay(5, 24, 200, 55);
		}
		window.endNVGFrame();
	}

}
