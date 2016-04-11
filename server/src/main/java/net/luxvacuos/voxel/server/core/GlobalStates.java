/*
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

package net.luxvacuos.voxel.server.core;

import net.luxvacuos.voxel.server.api.mod.ModStateLoop;
import net.luxvacuos.voxel.server.core.states.GameMPState;

public class GlobalStates {

	public boolean loop = false;

	private GameState state;

	private GameState oldState;

	public enum GameState {
		GAME_MP(new GameMPState());

		GameState(State state) {
			this.state = state;
		}

		State state;
	}

	public GlobalStates() {
		loop = true;
		setState(GameState.GAME_MP);;
	}

	public void doUpdate(Voxel voxel, float delta) {
		state.state.update(voxel, this, delta);
		for (ModStateLoop modStateLoop : voxel.getApi().getMoltenAPI().getModStateLoops()) {
			modStateLoop.update(this.state.state, delta);
		}
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		if (!state.equals(this.state)) {
			this.oldState = this.state;
			this.state = state;
		}
	}

	public GameState getOldState() {
		return oldState;
	}
}
