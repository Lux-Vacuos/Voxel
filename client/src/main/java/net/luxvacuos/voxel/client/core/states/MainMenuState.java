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

import net.luxvacuos.voxel.client.core.GlobalStates;
import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.core.GlobalStates.InternalState;
import net.luxvacuos.voxel.client.core.State;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.exception.ThreadException;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;

/**
 * Main Menu State
 * 
 * @author danirod
 * @category Kernel
 */
public class MainMenuState implements State {

	@Override
	public void render(Voxel voxel, GlobalStates states, float delta) {
		GameResources gm = voxel.getGameResources();
		gm.getRenderer().prepare();
		gm.getDisplay().beingNVGFrame();
		gm.getMenuSystem().mainMenu.render();
		VectorsRendering.renderMouse();
		gm.getDisplay().endNVGFrame();
	}

	@Override
	public void update(Voxel voxel, GlobalStates states, float delta) throws Exception {
		GameResources gm = voxel.getGameResources();

		if (gm.getMenuSystem().mainMenu.getPlayButton().pressed()) {
			states.setState(GameState.WORLD_SELECTION);
		} else if (gm.getMenuSystem().mainMenu.getPlayMPButton().pressed()) {
			states.setState(GameState.MP_SELECTION);
		} else if (gm.getMenuSystem().mainMenu.getExitButton().pressed()) {
			states.setInternalState(InternalState.STOPPED);
		} else if (gm.getMenuSystem().mainMenu.getOptionsButton().pressed()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new ThreadException(e);
			}
			states.setState(GameState.OPTIONS);
		} else if (gm.getMenuSystem().mainMenu.getAboutButton().pressed()) {
			states.setState(GameState.ABOUT);
		}
		gm.getMenuSystem().mainMenu.update();
	}

}
