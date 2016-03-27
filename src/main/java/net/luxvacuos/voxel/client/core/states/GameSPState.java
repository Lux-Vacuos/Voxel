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

import static net.luxvacuos.voxel.client.input.Keyboard.KEY_ESCAPE;
import static net.luxvacuos.voxel.client.input.Keyboard.KEY_F1;
import static net.luxvacuos.voxel.client.input.Keyboard.KEY_F2;
import static net.luxvacuos.voxel.client.input.Keyboard.isKeyDown;
import static net.luxvacuos.voxel.client.input.Keyboard.next;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glReadPixels;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import net.luxvacuos.voxel.client.core.GlobalStates;
import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.core.State;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.particle.ParticleMaster;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.Dimension;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.client.world.physics.PhysicsSystem;

/**
 * Single Player GameState
 * 
 * @author danirod
 * @category Kernel
 */
public class GameSPState implements State {

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();

		((PlayerCamera) gm.getCamera()).update(delta, gm, gm.getWorldsHandler().getActiveWorld().getActiveDimension());
		gm.getMenuSystem().gameSP.update();
		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksGeneration(gm, delta);

		for (Dimension dim : gm.getWorldsHandler().getActiveWorld().getDimensions().values()) {
			dim.getPhysicsEngine().update(delta);
			dim.getPhysicsEngine().getSystem(PhysicsSystem.class).processItems(gm);
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
				gm.getGlobalStates().setState(GameState.IN_PAUSE);
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
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();

		gm.getWorldsHandler().getActiveWorld().getActiveDimension().lighting();
		gm.getSun_Camera().setPosition(gm.getCamera().getPosition());
		gm.getFrustum().calculateFrustum(gm.getMasterShadowRenderer().getProjectionMatrix(), gm.getSun_Camera());
		if (VoxelVariables.useShadows) {
			gm.getMasterShadowRenderer().being();
			gm.getRenderer().prepare();
			gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksShadow(gm);
			gm.getItemsDropRenderer().getTess().drawShadow(gm.getSun_Camera());
			gm.getMasterShadowRenderer().renderEntity(
					gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine().getEntities(), gm);
			gm.getMasterShadowRenderer().end();
		}
		gm.getFrustum().calculateFrustum(gm.getRenderer().getProjectionMatrix(), gm.getCamera());
		gm.getRenderer().prepare();
		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksOcclusion(gm);

		gm.getDeferredShadingRenderer().getPost_fbo().begin();
		gm.getRenderer().prepare();
		gm.getSkyboxRenderer().render(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, delta, gm);
		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksRender(gm);
		FloatBuffer p = BufferUtils.createFloatBuffer(1);
		glReadPixels(gm.getDisplay().getDisplayWidth() / 2, gm.getDisplay().getDisplayHeight() / 2, 1, 1,
				GL_DEPTH_COMPONENT, GL_FLOAT, p);
		gm.getCamera().depth = p.get(0);
		gm.getRenderer().renderEntity(
				gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine().getEntities(), gm);
		gm.getItemsDropRenderer().render(gm);
		gm.getDeferredShadingRenderer().getPost_fbo().end();

		gm.getRenderer().prepare();
		gm.getDeferredShadingRenderer().render(gm);
		ParticleMaster.getInstance().render(gm.getCamera(), gm.getRenderer().getProjectionMatrix());
		gm.getDisplay().beingNVGFrame();
		gm.getMenuSystem().gameSP.render(gm, gm.getWorldsHandler().getActiveWorld().getActiveDimension());
		gm.getDisplay().endNVGFrame();

	}

}
