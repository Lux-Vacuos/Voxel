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

import static net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem.REGISTRY;

import org.lwjgl.glfw.GLFW;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.lightengine.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.lightengine.client.ecs.ClientComponents;
import net.luxvacuos.lightengine.client.ecs.entities.CameraEntity;
import net.luxvacuos.lightengine.client.ecs.entities.Sun;
import net.luxvacuos.lightengine.client.input.KeyboardHandler;
import net.luxvacuos.lightengine.client.input.Mouse;
import net.luxvacuos.lightengine.client.rendering.api.glfw.Window;
import net.luxvacuos.lightengine.client.rendering.api.opengl.LightRenderer;
import net.luxvacuos.lightengine.client.rendering.api.opengl.ParticleDomain;
import net.luxvacuos.lightengine.client.rendering.api.opengl.Renderer;
import net.luxvacuos.lightengine.client.ui.windows.GameWindow;
import net.luxvacuos.lightengine.client.util.Maths;
import net.luxvacuos.lightengine.universal.core.TaskManager;
import net.luxvacuos.lightengine.universal.core.states.AbstractState;
import net.luxvacuos.lightengine.universal.core.states.StateMachine;
import net.luxvacuos.lightengine.universal.util.registry.Key;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.ecs.entities.PlayerCamera;
import net.luxvacuos.voxel.client.rendering.api.opengl.BlockOutlineRenderer;
import net.luxvacuos.voxel.client.ui.windows.PauseWindow;
import net.luxvacuos.voxel.client.world.RenderWorld;
import net.luxvacuos.voxel.client.world.dimension.RenderDimension;
import net.luxvacuos.voxel.universal.ecs.entities.ChunkLoaderEntity;
import net.luxvacuos.voxel.universal.world.IWorld;

public class SPWorldState extends AbstractState {

	private Sun sun;
	private CameraEntity camera;
	private BlockOutlineRenderer blockOutlineRenderer;
	private ChunkLoaderEntity spawnChunks;
	private GameWindow gameWindow;
	private PauseWindow pauseWindow;
	private LightRenderer lightRenderer;

	private IWorld world;
	private boolean loaded;

	public SPWorldState() {
		super(StateNames.SP_WORLD);
	}

	@Override
	public void start() {
		super.start();
		Renderer.setDeferredPass((camera, sunCamera, frustum, shadowMap) -> {
			((RenderWorld) world).render(camera, frustum);
		});
		Renderer.setShadowPass((camera, sunCamera, frustum, shadowMap) -> {
			((RenderWorld) world).renderShadow(sunCamera, frustum);
		});
		Renderer.setForwardPass((camera, sunCamera, frustum, shadowMap) -> {
			Vector3d pos = ((PlayerCamera) camera).getBlockOutlinePos();
			blockOutlineRenderer.render(camera,
					world.getActiveDimension().getBlockAt((int) pos.getX(), (int) pos.getY(), (int) pos.getZ()));
		});
		Renderer.setOnResize(() -> {
			ClientComponents.PROJECTION_MATRIX.get(camera)
					.setProjectionMatrix(Renderer.createProjectionMatrix(
							(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/width")),
							(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height")),
							(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Core/fov")),
							ClientVariables.NEAR_PLANE, ClientVariables.FAR_PLANE));
		});
		world = new RenderWorld(ClientVariables.worldNameToLoad);
		ClientVariables.worldNameToLoad = "";
		world.loadDimension(0);
		world.setActiveDimension(0);
		camera.setPosition(new Vector3d(0, 256, 0));
		spawnChunks.setPosition(new Vector3d(0, 0, 0));
		world.getActiveDimension().getEntitiesManager().addEntity(camera);
		world.getActiveDimension().getEntitiesManager().addEntity(spawnChunks);
		TaskManager.addTask(() -> {
			Mouse.setGrabbed(true);
			lightRenderer = new LightRenderer();
			Renderer.render(world.getActiveDimension().getEntitiesManager().getEntities(),
					ParticleDomain.getParticles(), null, lightRenderer, camera,
					world.getActiveDimension().getWorldSimulator(), sun, 0);
			gameWindow = new GameWindow(0, (int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height")),
					(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/width")),
					(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height")));
			GraphicalSubsystem.getWindowManager().addWindow(gameWindow);
			loaded = true;
		});
	}

	@Override
	public void end() {
		super.end();
		world.dispose();
		TaskManager.addTask(() -> {
			lightRenderer.dispose();
		});
	}

	@Override
	public void init() {
		Window window = GraphicalSubsystem.getMainWindow();

		Matrix4d[] shadowProjectionMatrix = new Matrix4d[4];

		int shadowDrawDistance = (int) REGISTRY
				.getRegistryItem(new Key("/Light Engine/Settings/Graphics/shadowsDrawDistance"));

		shadowProjectionMatrix[0] = Maths.orthographic(-shadowDrawDistance / 32, shadowDrawDistance / 32,
				-shadowDrawDistance / 32, shadowDrawDistance / 32, -shadowDrawDistance, shadowDrawDistance, false);
		shadowProjectionMatrix[1] = Maths.orthographic(-shadowDrawDistance / 10, shadowDrawDistance / 10,
				-shadowDrawDistance / 10, shadowDrawDistance / 10, -shadowDrawDistance, shadowDrawDistance, false);
		shadowProjectionMatrix[2] = Maths.orthographic(-shadowDrawDistance / 4, shadowDrawDistance / 4,
				-shadowDrawDistance / 4, shadowDrawDistance / 4, -shadowDrawDistance, shadowDrawDistance, false);
		shadowProjectionMatrix[3] = Maths.orthographic(-shadowDrawDistance, shadowDrawDistance, -shadowDrawDistance,
				shadowDrawDistance, -shadowDrawDistance, shadowDrawDistance, false);
		Matrix4d projectionMatrix = Renderer.createProjectionMatrix(window.getWidth(), window.getHeight(),
				(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Core/fov")), ClientVariables.NEAR_PLANE,
				ClientVariables.FAR_PLANE);

		camera = new PlayerCamera(projectionMatrix, ClientVariables.user.getUsername(),
				ClientVariables.user.getUUID().toString());
		sun = new Sun(new Vector3d(5, 0, 40), shadowProjectionMatrix);
		TaskManager.addTask(() -> blockOutlineRenderer = new BlockOutlineRenderer(window.getResourceLoader()));

		spawnChunks = new ChunkLoaderEntity(new Vector3d());
	}

	@Override
	public void dispose() {
		blockOutlineRenderer.dispose();
		if (world != null)
			world.dispose();
	}

	@Override
	public void render(float alpha) {
		if (!loaded)
			return;
		Renderer.render(world.getActiveDimension().getEntitiesManager().getEntities(), ParticleDomain.getParticles(),
				null, lightRenderer, camera, world.getActiveDimension().getWorldSimulator(), sun, alpha);
	}

	@Override
	public void update(float delta) {
		if (!loaded)
			return;
		Window window = GraphicalSubsystem.getMainWindow();
		KeyboardHandler kbh = window.getKeyboardHandler();
		if (!ClientVariables.paused) {
			world.update(delta);
			sun.update(camera.getPosition(),
					((RenderDimension) this.world.getActiveDimension()).getWorldSimulator().getRotation(), delta);
			ParticleDomain.update(delta, camera);
			blockOutlineRenderer.getPosition().set(((PlayerCamera) camera).getBlockOutlinePos());

			if (kbh.isKeyPressed(GLFW.GLFW_KEY_R))
				ClientVariables.raining = !ClientVariables.raining;
			if (kbh.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
				kbh.ignoreKeyUntilRelease(GLFW.GLFW_KEY_ESCAPE);
				((PlayerCamera) camera).unlockMouse();
				ClientVariables.paused = true;
				float borderSize = (float) REGISTRY
						.getRegistryItem(new Key("/Light Engine/Settings/WindowManager/borderSize"));
				float titleBarHeight = (float) REGISTRY
						.getRegistryItem(new Key("/Light Engine/Settings/WindowManager/titleBarHeight"));
				int height = (int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height"));
				pauseWindow = new PauseWindow(borderSize + 10, height - titleBarHeight - 10,
						(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/width")) - borderSize * 2f - 20,
						height - titleBarHeight - borderSize - 50);
				GraphicalSubsystem.getWindowManager().addWindow(pauseWindow);
				GraphicalSubsystem.getWindowManager().toggleShell();
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
				GraphicalSubsystem.getWindowManager().toggleShell();
			}
		}
	}

}
