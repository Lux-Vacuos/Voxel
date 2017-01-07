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

import org.lwjgl.glfw.GLFW;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.input.KeyboardHandler;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleDomain;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorBasicShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorShader;
import net.luxvacuos.voxel.client.rendering.utils.BlockFaceAtlas;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.RenderWorld;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.client.world.block.RenderBlock;
import net.luxvacuos.voxel.client.world.block.types.WaterBlock;
import net.luxvacuos.voxel.client.world.dimension.RenderDimension;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.client.world.entities.Sun;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;
import net.luxvacuos.voxel.universal.material.BlockMaterial;
import net.luxvacuos.voxel.universal.material.MaterialModder;
import net.luxvacuos.voxel.universal.world.IWorld;
import net.luxvacuos.voxel.universal.world.block.Blocks;

public class SPWorldState extends AbstractState {

	private Sun sun;
	private ClientWorldSimulation worldSimulation;
	private Camera camera;
	private Renderer renderer;

	private IWorld world;

	private SPPauseState pausesState;

	public SPWorldState() {
		super(StateNames.SP_WORLD);
	}

	@Override
	public void start() {
		super.start();
		world = new RenderWorld(ClientVariables.worldNameToLoad);
		world.loadDimension(0);
		world.setActiveDimension(0);
		((PlayerCamera) camera).setMouse();
		camera.setPosition(new Vector3d(0, 256, 0));
		world.getActiveDimension().getEntitiesManager().addEntity(camera);
	}

	@Override
	public void end() {
		super.end();
		world.dispose();
	}

	@Override
	public void init() {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();

		Matrix4d shadowProjectionMatrix = Maths.orthographic(-ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, false);
		Matrix4d projectionMatrix = Renderer.createProjectionMatrix(window.getWidth(), window.getHeight(),
				ClientVariables.FOV, ClientVariables.NEAR_PLANE, ClientVariables.FAR_PLANE);

		camera = new PlayerCamera(projectionMatrix, window);
		camera.setPosition(new Vector3d(0, 2, 0));
		sun = new Sun(shadowProjectionMatrix);

		//ShaderProgram.loadToVFS(new ShaderIncludes("/common/common.glsl"));
		//ShaderProgram.loadToVFS(new ShaderIncludes("/common/Lighting-PBR.glsl"));
		//ShaderProgram.loadToVFS(new ShaderIncludes("/common/Materials.glsl"));

		worldSimulation = new ClientWorldSimulation();
		renderer = new Renderer(window, camera, sun.getCamera());

		renderer.setDeferredPass((camera, sunCamera, frustum, shadowMap) -> {
			((RenderDimension) world.getActiveDimension()).render(camera, sunCamera, worldSimulation, frustum,
					shadowMap);
		});
		renderer.setShadowPass((camera, sunCamera, frustum, shadowMap) -> {
			((RenderDimension) world.getActiveDimension()).renderShadow(sunCamera, frustum);
		});
		renderer.setOcclusionPass((camera, sunCamera, frustum, shadowMap) -> {
			// ((RenderDimension)
			// world.getActiveDimension()).renderOcclusion(camera, frustum);
		});

		BlocksResources.createBlocks(window.getResourceLoader());
		TessellatorShader.getShader();
		TessellatorBasicShader.getShader();

		TessellatorShader.getShader().start();
		TessellatorShader.getShader().loadProjectionMatrix(camera.getProjectionMatrix());
		TessellatorShader.getShader().loadBiasMatrix(sun.getCamera().getProjectionMatrix());
		TessellatorShader.getShader().stop();

		MaterialModder matMod = new MaterialModder();

		Blocks.startRegister("voxel");
		BlockMaterial airMat = new BlockMaterial("air");
		airMat = (BlockMaterial) matMod.modify(airMat).canBeBroken(false).setBlocksMovement(false).setOpacity(0f)
				.done();
		Blocks.register(new RenderBlock(airMat, new BlockFaceAtlas("air")));
		Blocks.register(new RenderBlock(new BlockMaterial("stone"), new BlockFaceAtlas("stone")));
		Blocks.register(new RenderBlock(new BlockMaterial("grass"), new BlockFaceAtlas("grass", "dirt", "grassSide")));
		Blocks.register(new RenderBlock(new BlockMaterial("dirt"), new BlockFaceAtlas("dirt")));
		Blocks.register(new RenderBlock(new BlockMaterial("sand"), new BlockFaceAtlas("sand")));
		Blocks.register(new RenderBlock(new BlockMaterial("cobblestone"), new BlockFaceAtlas("cobblestone")));
		Blocks.register(new RenderBlock(new BlockMaterial("wood"), new BlockFaceAtlas("wood")));
		Blocks.register(new RenderBlock(new BlockMaterial("leaves"), new BlockFaceAtlas("leaves")));
		Blocks.register(new RenderBlock(new BlockMaterial("glass"), new BlockFaceAtlas("glass")));
		BlockMaterial waterMat = new BlockMaterial("water");
		waterMat = (BlockMaterial) matMod.modify(waterMat).canBeBroken(false).setBlocksMovement(false)
				.affectedByGravity(true).liquid().done();
		Blocks.register(new WaterBlock(waterMat, new BlockFaceAtlas("water")));
		Blocks.finishRegister();

		pausesState = new SPPauseState();
		pausesState.init();
	}

	@Override
	public void dispose() {
		renderer.cleanUp();
		TessellatorShader.getShader().cleanUp();
		TessellatorBasicShader.getShader().cleanUp();
		pausesState.dispose();
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();

		renderer.render(world.getActiveDimension().getEntitiesManager().getEntities(), ParticleDomain.getParticles(),
				camera, sun.getCamera(), worldSimulation, sun.getSunPosition(), sun.getInvertedSunPosition(), alpha);

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

			sun.update(camera.getPosition(), worldSimulation.update(delta), delta);
			ParticleDomain.update(delta, camera);

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
