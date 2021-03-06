/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import net.luxvacuos.lightengine.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.lightengine.client.ecs.entities.CameraEntity;
import net.luxvacuos.lightengine.client.ecs.entities.Sun;
import net.luxvacuos.lightengine.client.ecs.entities.SunCamera;
import net.luxvacuos.lightengine.client.input.KeyboardHandler;
import net.luxvacuos.lightengine.client.input.MouseHandler;
import net.luxvacuos.lightengine.client.network.IRenderingData;
import net.luxvacuos.lightengine.client.rendering.glfw.Window;
import net.luxvacuos.lightengine.client.rendering.opengl.ParticleDomain;
import net.luxvacuos.lightengine.client.rendering.opengl.ShadowFBO;
import net.luxvacuos.lightengine.client.rendering.opengl.objects.CubeMapTexture;
import net.luxvacuos.lightengine.client.rendering.opengl.objects.Texture;
import net.luxvacuos.lightengine.client.rendering.opengl.IRenderPass.IForwardPass;
import net.luxvacuos.lightengine.client.rendering.opengl.IRenderPass.IGBufferPass;
import net.luxvacuos.lightengine.client.rendering.opengl.IRenderPass.IShadowPass;
import net.luxvacuos.lightengine.universal.core.TaskManager;
import net.luxvacuos.lightengine.universal.core.states.AbstractState;
import net.luxvacuos.lightengine.universal.core.states.StateMachine;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.ecs.entities.PlayerCamera;
import net.luxvacuos.voxel.client.rendering.api.opengl.BlockOutlineRenderer;
import net.luxvacuos.voxel.client.rendering.world.dimension.IRenderDimension;
import net.luxvacuos.voxel.client.ui.windows.PauseWindow;
import net.luxvacuos.voxel.client.world.RenderWorld;
import net.luxvacuos.voxel.universal.ecs.entities.ChunkLoaderEntity;
import net.luxvacuos.voxel.universal.world.IWorld;

public class SPWorldState extends AbstractState implements IGBufferPass, IShadowPass, IForwardPass {

	private BlockOutlineRenderer blockOutlineRenderer;
	private ChunkLoaderEntity spawnChunks;
	private PauseWindow pauseWindow;

	private IWorld world;
	private PlayerCamera camera;

	public SPWorldState() {
		super(StateNames.SP_WORLD);
	}

	@Override
	public void start() {
		super.start();
		GraphicalSubsystem.getRenderer().init();
		GraphicalSubsystem.getRenderer().setGBufferPass(this);
		GraphicalSubsystem.getRenderer().setShadowPass(this);
		GraphicalSubsystem.getRenderer().setForwardPass(this);
		MouseHandler.setGrabbed(GraphicalSubsystem.getMainWindow().getID(), true);
		camera = new PlayerCamera(ClientVariables.user.getUsername(), ClientVariables.user.getUUID().toString());
		camera.setPosition(0, 256, 0);
		world = new RenderWorld(ClientVariables.worldNameToLoad);
		ClientVariables.worldNameToLoad = "";
		world.loadDimension(0);
		world.setActiveDimension(0);
		spawnChunks.setPosition(new Vector3f(0, 0, 0));
		((IRenderDimension) world.getActiveDimension()).setPlayer(camera);
		world.getActiveDimension().getEntitiesManager().addEntity(spawnChunks);
	}

	@Override
	public void end() {
		super.end();
		GraphicalSubsystem.getRenderer().dispose();
		world.dispose();
	}

	@Override
	public void init() {
		TaskManager.tm.addTaskRenderThread(() -> blockOutlineRenderer = new BlockOutlineRenderer());
		spawnChunks = new ChunkLoaderEntity(new Vector3f());
	}

	@Override
	public void dispose() {
		TaskManager.tm.addTaskRenderThread(() -> blockOutlineRenderer.dispose());
		if (world != null)
			world.dispose();
	}

	@Override
	public void forwardPass(CameraEntity camera, Sun sun, ShadowFBO shadow, CubeMapTexture irradiance,
			CubeMapTexture environmentMap, Texture brdfLUT) {
		//Vector3f pos = this.camera.getBlockOutlinePos();
		//blockOutlineRenderer.render(camera,w
				//world.getActiveDimension().getBlockAt((int) pos.x(), (int) pos.y(), (int) pos.z()));
	}

	@Override
	public void shadowPass(SunCamera camera) {
		((RenderWorld) world).renderShadow(camera, null);
	}

	@Override
	public void gBufferPass(CameraEntity camera) {
		((RenderWorld) world).render(camera, null);
	}

	@Override
	public void render(float alpha) {
		GraphicalSubsystem.getRenderer().render((IRenderingData) world.getActiveDimension(), alpha);
	}

	@Override
	public void update(float delta) {
		Window window = GraphicalSubsystem.getMainWindow();

		KeyboardHandler kbh = window.getKeyboardHandler();
		if (!ClientVariables.paused) {
			ParticleDomain.update(delta, ((IRenderingData) world.getActiveDimension()).getCamera());
			world.update(delta);
			if (kbh.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
				kbh.ignoreKeyUntilRelease(GLFW.GLFW_KEY_ESCAPE);
				MouseHandler.setGrabbed(GraphicalSubsystem.getMainWindow().getID(), false);
				ClientVariables.paused = true;
				pauseWindow = new PauseWindow();
				GraphicalSubsystem.getWindowManager().addWindow(pauseWindow);
			}
		} else if (ClientVariables.exitWorld) {
			ClientVariables.exitWorld = false;
			ClientVariables.paused = false;
			StateMachine.setCurrentState(StateNames.MAIN);
		} else {
			if (kbh.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
				kbh.ignoreKeyUntilRelease(GLFW.GLFW_KEY_ESCAPE);
				pauseWindow.closeWindow();
			}
		}
	}

}
