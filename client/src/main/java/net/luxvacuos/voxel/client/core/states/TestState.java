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
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.input.KeyboardHandler;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleMaster;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorBasicShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorShader;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.PhysicsSystem;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.client.world.entities.Dragon;
import net.luxvacuos.voxel.client.world.entities.EntityResources;
import net.luxvacuos.voxel.client.world.entities.Plane;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.client.world.entities.Sun;
import net.luxvacuos.voxel.client.world.entities.Test;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;
import net.luxvacuos.voxel.universal.ecs.Components;

/**
 * Single Player State, here the local world is updated and rendered.
 * 
 * @author danirod
 * @category Kernel
 */
public class TestState extends AbstractState {

	private PhysicsSystem physicsSystem;
	private Engine engine;
	private Test t;
	private Plane plane;
	private Dragon dragon0, dragon1, dragon2, dragon3;
	private Sun sun;
	private ClientWorldSimulation worldSimulation;
	private Camera camera;
	private Renderer renderer;

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
		camera.setPosition(new Vector3d(0,2,0));
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
		physicsSystem.addBox(new BoundingBox(new Vector3(-15, -1, -15), new Vector3(15, 0, 15)));
		engine.addSystem(physicsSystem);
		t = new Test(new Vector3d(0, 1, 0));
		plane = new Plane();
		dragon0 = new Dragon(new Vector3d(5, 0, 0));
		Components.SCALE.get(dragon0).setScale(0.25f);
		dragon1 = new Dragon(new Vector3d(-5, 0, 0));
		Components.SCALE.get(dragon1).setScale(0.25f);
		dragon2 = new Dragon(new Vector3d(0, 0, 5));
		Components.SCALE.get(dragon2).setScale(0.25f);
		dragon3 = new Dragon(new Vector3d(0, 0, -5));
		Components.SCALE.get(dragon3).setScale(0.25f);
	}

	@Override
	public void dispose() {
		renderer.cleanUp();
	}

	@Override
	public void start() {
		physicsSystem.getEngine().addEntity(camera);
		physicsSystem.getEngine().addEntity(t);
		physicsSystem.getEngine().addEntity(plane);
		physicsSystem.getEngine().addEntity(dragon0);
		physicsSystem.getEngine().addEntity(dragon1);
		physicsSystem.getEngine().addEntity(dragon2);
		physicsSystem.getEngine().addEntity(dragon3);
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
