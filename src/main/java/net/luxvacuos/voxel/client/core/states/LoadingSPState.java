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

import java.util.Random;

import net.luxvacuos.voxel.client.core.GlobalStates;
import net.luxvacuos.voxel.client.core.State;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;

/**
 * Loading Screen State
 * 
 * @author danirod
 * @category Kernel
 */
public class LoadingSPState implements State {

	private float xScale, yScale;

	public LoadingSPState() {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
	}

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();

		Random seed;
		if (VoxelVariables.isCustomSeed) {
			seed = new Random(VoxelVariables.seed.hashCode());
		} else {
			seed = new Random();
		}
		voxel.getWorldsHandler().getActiveWorld().startWorld(gm.getMenuSystem().worldSelectionMenu.getWorldName(), seed, 0, voxel.getGameResources());
		((PlayerCamera) gm.getCamera()).setMouse(gm.getDisplay());
		gm.getSoundSystem().rewind("menu1");
		gm.getSoundSystem().stop("menu1");
		gm.getSoundSystem().rewind("menu2");
		gm.getSoundSystem().stop("menu2");
		states.setState(GameState.GAME_SP);

	}

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		gm.getRenderer().prepare();
		gm.getDisplay().beingNVGFrame();
		VectorsRendering.renderText("Loading World...", "Roboto-Bold", 530 * xScale, 358 * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		gm.getDisplay().endNVGFrame();
	}

}
