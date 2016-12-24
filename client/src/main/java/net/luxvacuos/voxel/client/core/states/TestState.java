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

package net.luxvacuos.voxel.client.core.states;

import org.lwjgl.glfw.GLFW;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.igl.vector.Vector4f;
import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.input.KeyboardHandler;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleMaster;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Material;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Texture;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.TexturedModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorBasicShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorShader;
import net.luxvacuos.voxel.client.rendering.utils.BlockFaceAtlas;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.PhysicsSystem;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.client.world.block.RenderBlock;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.client.world.entities.EntityResources;
import net.luxvacuos.voxel.client.world.entities.Plane;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.client.world.entities.Sphere;
import net.luxvacuos.voxel.client.world.entities.Sun;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Rotation;
import net.luxvacuos.voxel.universal.material.BlockMaterial;

/**
 * Test State
 * 
 * @author danirod
 */
public class TestState extends AbstractState {

	private PhysicsSystem physicsSystem;
	private Engine engine;
	private Plane plane;
	private Sun sun;
	private ClientWorldSimulation worldSimulation;
	private Camera camera;
	private Renderer renderer;
	private Tessellator tess;

	private Sphere mat1, mat2, mat3, mat4, mat5;

	public TestState() {
		super(StateNames.TEST);
	}

	@Override
	public void init() {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		ResourceLoader loader = window.getResourceLoader();

		Matrix4d shadowProjectionMatrix = Maths.orthographic(-ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, false);
		Matrix4d projectionMatrix = Renderer.createProjectionMatrix(window.getWidth(), window.getHeight(),
				ClientVariables.FOV, ClientVariables.NEAR_PLANE, ClientVariables.FAR_PLANE);

		camera = new PlayerCamera(projectionMatrix, window);
		camera.setPosition(new Vector3d(0, 2, 0));
		sun = new Sun(shadowProjectionMatrix);

		worldSimulation = new ClientWorldSimulation();
		renderer = new Renderer(window, camera, sun.getCamera());
		TessellatorShader.getInstance();
		TessellatorBasicShader.getInstance();
		ParticleMaster.getInstance().init(loader, camera.getProjectionMatrix());

		EntityResources.load(loader);

		worldSimulation = new ClientWorldSimulation();
		engine = new Engine();
		physicsSystem = new PhysicsSystem();
		physicsSystem.addBox(new BoundingBox(new Vector3(-15, -1, -15), new Vector3(40, 0, 25)));
		engine.addSystem(physicsSystem);
		plane = new Plane();
		BlocksResources.createBlocks(loader);
		tess = new Tessellator(projectionMatrix, shadowProjectionMatrix);
		RenderBlock t = new RenderBlock(new BlockMaterial("test"), new BlockFaceAtlas("Ice"));
		t.setID(1);
		/*
		 * tess.begin(BlocksResources.getTessellatorTextureAtlas().getTexture(),
		 * BlocksResources.getNormalMap(), BlocksResources.getHeightMap(),
		 * BlocksResources.getPbrMap()); for (int x = 0; x < 16; x++) { for (int
		 * z = 0; z < 16; z++) { tess.generateCube(20 + x, 0, z, 1, true, true,
		 * true, true, true, true, t); } } tess.end();
		 * 
		 * renderer.setShadowPass((worldSimulation, camera, sunCamera,
		 * shadowMap, shadowData) -> { tess.drawShadow(sunCamera); });
		 * 
		 * renderer.setDeferredPass((worldSimulation, camera, sunCamera,
		 * shadowMap, shadowData) -> { tess.draw(camera, sunCamera,
		 * worldSimulation, shadowMap, shadowData, false); });
		 */
		// renderer.getLightRenderer().addLight(new Light(new Vector3d(2, 3,
		// 0)));
		// renderer.getLightRenderer().addLight(new Light(new Vector3d(-8, 2,
		// 0)));

		RawModel sphere = loader.loadObjModel("sphere");
		Texture test = loader.loadTexture("test1");
		Texture test_n = null;
		Texture test_r = loader.loadTexture("test1_r");

		mat1 = new Sphere(new TexturedModel(sphere,
				new Material(new Vector4f(1f), 1f, 1, 0, test, test_n, test_r, test_r, null)));
		mat1.getComponent(Position.class).set(0, 1, 0);

		mat2 = new Sphere(new TexturedModel(sphere,
				new Material(new Vector4f(1f), 1f, 1, 0, test, test_n, test_r, test_r, null)));
		mat2.getComponent(Position.class).set(3, 1, 0);

		mat3 = new Sphere(new TexturedModel(sphere,
				new Material(new Vector4f(1f), 1f, 1f, 1, test, test_n, test_r, test_r, null)));
		mat3.getComponent(Position.class).set(6, 1, 0);

		mat4 = new Sphere(new TexturedModel(sphere,
				new Material(new Vector4f(1f), 1f, 1f, 1, test, test_n, test_r, test_r, null)));
		mat4.getComponent(Position.class).set(9, 1, 0);

	}

	@Override
	public void dispose() {
		renderer.cleanUp();
	}

	@Override
	public void start() {
		physicsSystem.getEngine().addEntity(camera);
		physicsSystem.getEngine().addEntity(plane);
		physicsSystem.getEngine().addEntity(mat1);
		physicsSystem.getEngine().addEntity(mat2);
		physicsSystem.getEngine().addEntity(mat3);
		physicsSystem.getEngine().addEntity(mat4);
		((PlayerCamera) camera).setMouse();
	}

	@Override
	public void end() {
		physicsSystem.getEngine().removeAllEntities();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		engine.update(delta);
		sun.update(camera.getPosition(), worldSimulation.update(delta), delta);
		ParticleMaster.getInstance().update(delta, camera);
		KeyboardHandler kbh = ClientInternalSubsystem.getInstance().getGameWindow().getKeyboardHandler();
		if (kbh.isCtrlPressed() && kbh.isAltPressed() & kbh.isKeyPressed(GLFW.GLFW_KEY_F10))
			throw new RuntimeException("Crash caused by User. \n Generated using \"ctrl + alt + f10\".");

		mat1.getComponent(Rotation.class).setX(mat1.getComponent(Rotation.class).getX() + 5 * delta);
		if (kbh.isKeyPressed(GLFW.GLFW_KEY_F1))
			ClientVariables.debug = !ClientVariables.debug;
		if (kbh.isKeyPressed(GLFW.GLFW_KEY_F2))
			ClientVariables.hideHud = !ClientVariables.hideHud;
		if (kbh.isKeyPressed(GLFW.GLFW_KEY_R))
			ClientVariables.raining = !ClientVariables.raining;
		if (kbh.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
			kbh.ignoreKeyUntilRelease(GLFW.GLFW_KEY_ESCAPE);
			((PlayerCamera) camera).unlockMouse();
			StateMachine.setCurrentState(StateNames.SP_PAUSE);
		}

	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();

		renderer.render(null

		// TODO: Implement

				, physicsSystem.getEngine().getEntities(), camera, sun.getCamera(), worldSimulation,
				sun.getSunPosition(), sun.getInvertedSunPosition(), alpha);

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
