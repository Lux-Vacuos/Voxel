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

import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleMaster;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.Dimension;
import net.luxvacuos.voxel.client.world.PhysicsSystem;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

/**
 * 
 * @author danirod
 * @deprecated Removed due to new UI system.
 */
public class GameSPInventoryState extends AbstractState {
	
	public GameSPInventoryState() {
		super(StateNames.SP_INVENTORY);
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		GameResources gm = (GameResources) voxel.getGameResources();

		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksGeneration(gm, delta);

		for (Dimension dim : gm.getWorldsHandler().getActiveWorld().getDimensions().values()) {
			dim.getPhysicsEngine().update(delta);
			dim.getPhysicsEngine().getSystem(PhysicsSystem.class).processItems(gm);
			dim.getPhysicsEngine().getSystem(PhysicsSystem.class).processEntities(gm);
			dim.getPhysicsEngine().getSystem(PhysicsSystem.class).doSpawn(gm);
		}

		gm.update(gm.getWorldSimulation().update(delta), delta);
		ParticleMaster.getInstance().update(delta, gm.getCamera());
		while (next()) {
			if (isKeyDown(KEY_F1))
				VoxelVariables.debug = !VoxelVariables.debug;
			if (isKeyDown(KEY_F2))
				VoxelVariables.hideHud = !VoxelVariables.hideHud;
			if (isKeyDown(KEY_ESCAPE) || isKeyDown(KEY_E)) {
				((PlayerCamera) gm.getCamera()).setMouse();
				//gm.getGlobalStates().setState(GameState.SP);
				StateMachine.setCurrentState(StateNames.SINGLEPLAYER);
			}
		}

		// if (!display.isDisplayFocused()) {
		// gm.getCamera().unlockMouse();
		// states.setState(GameState.IN_PAUSE);
		// }
	}

	@Override
	public void render(AbstractVoxel voxel, float delta) {
		GameResources gm = (GameResources) voxel.getGameResources();

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

		gm.getRenderingPipeline().begin();
		gm.getRenderer().prepare();
		gm.getSkyboxRenderer().render(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, delta, gm);
		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksRender(gm, false);
		gm.getRenderer().renderEntity(
				gm.getWorldsHandler().getActiveWorld().getActiveDimension().getPhysicsEngine().getEntities(), gm);
		gm.getItemsDropRenderer().render(gm);
		gm.getRenderingPipeline().end();

		gm.getRenderer().prepare();
		gm.getRenderingPipeline().render(gm);
		gm.getWorldsHandler().getActiveWorld().getActiveDimension().updateChunksRender(gm, true);
		ParticleMaster.getInstance().render(gm.getCamera(), gm.getRenderer().getProjectionMatrix());
		gm.getDisplay().beingNVGFrame();

		gm.getItemsGuiRenderer().getTess().begin(BlocksResources.getTessellatorTextureAtlas().getTexture(),
				BlocksResources.getNormalMap(), BlocksResources.getHeightMap(), BlocksResources.getSpecularMap());
		((PlayerCamera) gm.getCamera()).getInventory().render(gm);
		gm.getItemsGuiRenderer().getTess().end();

		UIRendering.renderMouse();
		gm.getDisplay().endNVGFrame();
		gm.getItemsGuiRenderer().render(gm);

	}

}