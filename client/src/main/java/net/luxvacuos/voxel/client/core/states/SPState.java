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

import static net.luxvacuos.voxel.client.input.Keyboard.KEY_E;
import static net.luxvacuos.voxel.client.input.Keyboard.KEY_ESCAPE;
import static net.luxvacuos.voxel.client.input.Keyboard.KEY_F1;
import static net.luxvacuos.voxel.client.input.Keyboard.KEY_F2;
import static net.luxvacuos.voxel.client.input.Keyboard.isKeyDown;
import static net.luxvacuos.voxel.client.input.Keyboard.next;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.core.State;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleMaster;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.Dimension;
import net.luxvacuos.voxel.client.world.PhysicsSystem;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.universal.api.MoltenAPI;

/**
 * Single Player GameState
 * 
 * @author danirod
 * @category Kernel
 */
public class SPState extends State {
	FloatBuffer p;
	FloatBuffer c;

	public SPState() {
		p = BufferUtils.createFloatBuffer(1);
		c = BufferUtils.createFloatBuffer(3);
	}

	@Override
	public void update(Voxel voxel, float delta) throws Exception {
		GameResources gm = voxel.getGameResources();

		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksGeneration(gm, delta);
		((PlayerCamera) gm.getCamera()).update(delta, gm, gm.getWorldsHandler().getActiveWorld().getActiveDimension());

		for (Dimension dim : gm.getWorldsHandler().getActiveWorld().getDimensions().values()) {
			dim.getPhysicsEngine().update(delta);
			dim.getPhysicsEngine().getSystem(PhysicsSystem.class).processItems(gm);
			dim.getPhysicsEngine().getSystem(PhysicsSystem.class).processEntities(gm);
			dim.getPhysicsEngine().getSystem(PhysicsSystem.class).doSpawn(gm);
		}

		gm.update(gm.getWorldSimulation().update(delta));
		ParticleMaster.getInstance().update(delta, gm.getCamera());
		while (next()) {
			if (isKeyDown(KEY_F1))
				VoxelVariables.debug = !VoxelVariables.debug;
			if (isKeyDown(KEY_F2))
				VoxelVariables.hideHud = !VoxelVariables.hideHud;
			if (isKeyDown(KEY_ESCAPE)) {
				((PlayerCamera) gm.getCamera()).unlockMouse();
				gm.getGlobalStates().setState(GameState.SP_PAUSE);
			}
			if (isKeyDown(KEY_E)) {
				((PlayerCamera) gm.getCamera()).unlockMouse();
				gm.getGlobalStates().setState(GameState.GAME_SP_INVENTORY);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_R))
				VoxelVariables.raining = !VoxelVariables.raining;
		}

		// if (!display.isDisplayFocused()) {
		// gm.getCamera().unlockMouse();
		// states.setState(GameState.IN_PAUSE);
		// }
	}

	@Override
	public void render(Voxel voxel, float delta) {
		GameResources gm = voxel.getGameResources();

		gm.getWorldsHandler().getActiveWorld().getActiveDimension().lighting();
		gm.getSun_Camera().setPosition(gm.getCamera().getPosition());
		gm.getFrustum().calculateFrustum(gm.getMasterShadowRenderer().getProjectionMatrix(), gm.getSun_Camera());
		if (VoxelVariables.useShadows) {
			gm.getMasterShadowRenderer().being();
			MasterRenderer.prepare();
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksShadow(gm);
			gm.getItemsDropRenderer().getTess().drawShadow(gm.getSun_Camera());
			gm.getMasterShadowRenderer().renderEntity(
					gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine().getEntities(), gm);
			gm.getMasterShadowRenderer().end();
		}
		gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());
		MasterRenderer.prepare();
		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksOcclusion(gm);

		gm.getRenderingPipeline().begin();
		MasterRenderer.prepare();
		gm.getSkyboxRenderer().render(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, delta, gm);
		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksRender(gm, false);
		gm.getRenderer().renderEntity(
				gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine().getEntities(), gm);
		p.clear();
		glReadPixels(gm.getDisplay().getDisplayWidth() / 2, gm.getDisplay().getDisplayHeight() / 2, 1, 1,
				GL_DEPTH_COMPONENT, GL_FLOAT, p);
		c.clear();
		glReadBuffer(GL_COLOR_ATTACHMENT2);
		glReadPixels(gm.getDisplay().getDisplayWidth() / 2, gm.getDisplay().getDisplayHeight() / 2, 1, 1, GL_RGB,
				GL_FLOAT, c);
		gm.getCamera().depth = p.get(0);
		gm.getCamera().normal.x = c.get(0);
		gm.getCamera().normal.y = c.get(1);
		gm.getCamera().normal.z = c.get(2);
		gm.getItemsDropRenderer().render(gm);
		gm.getRenderingPipeline().end();

		MasterRenderer.prepare();
		gm.getRenderingPipeline().render(gm);

		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksRender(gm, true);
		gm.getCamera().render();

		ParticleMaster.getInstance().render(gm.getCamera(), gm.getRenderer().getProjectionMatrix());
		gm.getDisplay().beingNVGFrame();
		if (VoxelVariables.debug) {
			UIRendering.renderText(
					"Voxel " + " (" + VoxelVariables.version + ")" + " Molten API" + " (" + MoltenAPI.apiVersion + "/"
							+ MoltenAPI.build + ")",
					"Roboto-Bold", 5, 12, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText("Used VRam: " + gm.getDisplay().getUsedVRAM() + "KB " + " UPS: " + CoreInfo.ups,
					"Roboto-Bold", 5, 95, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(
					"Loaded Chunks: " + gm.getWorldsHandler().getActiveWorld().getActiveDimension().getLoadedChunks()
							+ "   Rendered Chunks: "
							+ gm.getWorldsHandler().getActiveWorld().getActiveDimension().getRenderedChunks(),
					"Roboto-Bold", 5, 115, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(
					"Position XYZ:  " + gm.getCamera().getPosition().getX() + "  " + gm.getCamera().getPosition().getY()
							+ "  " + gm.getCamera().getPosition().getZ(),
					"Roboto-Bold", 5, 135, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			UIRendering.renderText(
					"Pitch:  " + gm.getCamera().getPitch() + "   Yaw: " + gm.getCamera().getYaw() + "   Roll: "
							+ gm.getCamera().getRoll(),
					"Roboto-Bold", 5, 155, 20, UIRendering.rgba(220, 220, 220, 255, UIRendering.colorA),
					UIRendering.rgba(255, 255, 255, 255, UIRendering.colorB));
			Timers.renderDebugDisplay(5, 24, 200, 55);
		}
		gm.getDisplay().endNVGFrame();
		gm.getItemsGuiRenderer().render(gm);

	}

}
