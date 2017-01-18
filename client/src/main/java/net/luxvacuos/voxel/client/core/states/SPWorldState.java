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

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.input.KeyboardHandler;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.BlockOutlineRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleDomain;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.RenderWorld;
import net.luxvacuos.voxel.client.world.dimension.RenderDimension;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.client.world.entities.Sun;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;
import net.luxvacuos.voxel.universal.world.IWorld;
import net.luxvacuos.voxel.universal.world.entities.ChunkLoaderEntity;

public class SPWorldState extends AbstractState {

	private Sun sun;
	private Camera camera;
	private BlockOutlineRenderer blockOutlineRenderer;
	private ChunkLoaderEntity spawnChunks;

	private IWorld world;

	private SPPauseState pausesState;

	FloatBuffer p;
	FloatBuffer c;

	public SPWorldState() {
		super(StateNames.SP_WORLD);
	}

	@Override
	public void start() {
		super.start();

		this.world = new RenderWorld(ClientVariables.worldNameToLoad);
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();

		Renderer.setDeferredPass((camera, sunCamera, frustum, shadowMap) -> {
			((RenderWorld) world).render(camera, sunCamera, frustum, shadowMap);
			glReadPixels(window.getWidth() / 2, window.getHeight() / 2, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, p);
			c.clear();
			glReadBuffer(GL_COLOR_ATTACHMENT2);
			glReadPixels(window.getWidth() / 2, window.getHeight() / 2, 1, 1, GL_RGB, GL_FLOAT, c);

			PlayerCamera cam = (PlayerCamera) camera;
			cam.setDepth(p.get(0));
			cam.getNormal().set(c.get(0), c.get(1), c.get(2));
		});
		Renderer.setShadowPass((camera, sunCamera, frustum, shadowMap) -> {
			((RenderWorld) world).renderShadow(sunCamera, frustum);
		});
		Renderer.setOcclusionPass((camera, sunCamera, frustum, shadowMap) -> {
			// ((RenderDimension)
			// world.getActiveDimension()).renderOcclusion(camera, frustum);
		});
		Renderer.setForwardPass((camera, sunCamera, frustum, shadowMap) -> {
			Vector3d pos = ((PlayerCamera) camera).getBlockOutlinePos();
			blockOutlineRenderer.render(camera,
					world.getActiveDimension().getBlockAt((int) pos.getX(), (int) pos.getY(), (int) pos.getZ()));
		});

		world.loadDimension(0);
		world.setActiveDimension(0);
		((PlayerCamera) camera).setMouse();
		camera.setPosition(new Vector3d(0, 256, 0));
		spawnChunks.setPosition(new Vector3d(0, 0, 0));
		world.getActiveDimension().getEntitiesManager().addEntity(camera);
		world.getActiveDimension().getEntitiesManager().addEntity(spawnChunks);
	}

	@Override
	public void end() {
		super.end();
		world.dispose();
	}

	@Override
	public void init() {
		p = BufferUtils.createFloatBuffer(1);
		c = BufferUtils.createFloatBuffer(3);
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();

		Matrix4d[] shadowProjectionMatrix = new Matrix4d[4];

		shadowProjectionMatrix[0] = Maths.orthographic(-ClientVariables.shadowMapDrawDistance / 32,
				ClientVariables.shadowMapDrawDistance / 32, -ClientVariables.shadowMapDrawDistance / 32,
				ClientVariables.shadowMapDrawDistance / 32, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, false);
		shadowProjectionMatrix[1] = Maths.orthographic(-ClientVariables.shadowMapDrawDistance / 16,
				ClientVariables.shadowMapDrawDistance / 16, -ClientVariables.shadowMapDrawDistance / 16,
				ClientVariables.shadowMapDrawDistance / 16, -ClientVariables.shadowMapDrawDistance,
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
		sun = new Sun(shadowProjectionMatrix);

		blockOutlineRenderer = new BlockOutlineRenderer(window.getResourceLoader());

		spawnChunks = new ChunkLoaderEntity(new Vector3d());

		pausesState = new SPPauseState();
		pausesState.init();
	}

	@Override
	public void dispose() {
		pausesState.dispose();
		blockOutlineRenderer.dispose();
		if (world != null)
			world.dispose();
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();

		Renderer.render(world.getActiveDimension().getEntitiesManager().getEntities(), ParticleDomain.getParticles(),
				camera, sun.getCamera(), world.getActiveDimension().getWorldSimulator(), sun.getSunPosition(),
				sun.getInvertedSunPosition(), alpha);

		window.beingNVGFrame();
		if (ClientVariables.debug) {
			UIRendering.renderText(window.getID(), "Voxel " + " (" + ClientVariables.version + ")", "Roboto-Bold", 5,
					12, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(),
					"Used VRam: " + WindowManager.getUsedVRAM() + "KB " + " UPS: " + CoreInfo.ups, "Roboto-Bold", 5, 95,
					20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(window.getID(),
					"Loaded Chunks: " + world.getActiveDimension().getLoadedChunks().size() + "   Rendered Chunks: "
							+ ((RenderDimension) world.getActiveDimension()).getRenderedChunks(),
					"Roboto-Bold", 5, 115, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
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
		if (ClientVariables.paused)
			pausesState.render(voxel, alpha);
		window.endNVGFrame();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		if (!ClientVariables.paused) {
			world.update(delta);

			KeyboardHandler kbh = ClientInternalSubsystem.getInstance().getGameWindow().getKeyboardHandler();

			sun.update(camera.getPosition(),
					((RenderDimension) this.world.getActiveDimension()).getWorldSimulator().getRotation(), delta);
			ParticleDomain.update(delta, camera);
			blockOutlineRenderer.getPosition().set(((PlayerCamera) camera).getBlockOutlinePos());

			if (kbh.isKeyPressed(GLFW.GLFW_KEY_F1))
				ClientVariables.debug = !ClientVariables.debug;
			if (kbh.isKeyPressed(GLFW.GLFW_KEY_R))
				ClientVariables.raining = !ClientVariables.raining;
			if (kbh.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
				kbh.ignoreKeyUntilRelease(GLFW.GLFW_KEY_ESCAPE);
				((PlayerCamera) camera).unlockMouse();
				ClientVariables.paused = true;
			}
		} else if (ClientVariables.exitWorld) {
			ClientVariables.exitWorld = false;
			ClientVariables.paused = false;
			StateMachine.setCurrentState(StateNames.MAIN_MENU);
		} else {
			pausesState.update(voxel, delta);
		}
	}

}
