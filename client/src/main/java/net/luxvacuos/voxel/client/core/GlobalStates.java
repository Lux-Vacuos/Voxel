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

package net.luxvacuos.voxel.client.core;

import net.luxvacuos.voxel.client.core.states.AboutState;
import net.luxvacuos.voxel.client.core.states.GameSPInventoryState;
import net.luxvacuos.voxel.client.core.states.MPLoadingState;
import net.luxvacuos.voxel.client.core.states.MPSelectionState;
import net.luxvacuos.voxel.client.core.states.MPState;
import net.luxvacuos.voxel.client.core.states.MainMenuState;
import net.luxvacuos.voxel.client.core.states.OptionsState;
import net.luxvacuos.voxel.client.core.states.SPCreateWorld;
import net.luxvacuos.voxel.client.core.states.SPLoadingState;
import net.luxvacuos.voxel.client.core.states.SPPauseState;
import net.luxvacuos.voxel.client.core.states.SPSelectionState;
import net.luxvacuos.voxel.client.core.states.SPState;
import net.luxvacuos.voxel.client.core.states.SplashScreenState;

/**
 * This controls the state of the engine and updates the object version of the
 * states.
 * 
 * @author danirod
 */
public class GlobalStates {

	/**
	 * Actual state
	 */
	private GameState state;

	/**
	 * Old State
	 */
	private GameState oldState;

	/**
	 * Engine internal state
	 */
	private InternalState internalState;

	/**
	 * Enum of available states.
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 *
	 */
	public enum GameState {
		SP(new SPState()), MAINMENU(new MainMenuState()), SP_PAUSE(new SPPauseState()), SP_LOADING_WORLD(
				new SPLoadingState()), OPTIONS(new OptionsState()), SP_SELECTION(new SPSelectionState()), ABOUT(
						new AboutState()), MP_SELECTION(new MPSelectionState()), LOADING_MP_WORLD(
								new MPLoadingState()), GAME_SP_INVENTORY(new GameSPInventoryState()), SPLASH_SCREEN(
										new SplashScreenState()), MP(new MPState()), SP_CREATE_WORLD(
												new SPCreateWorld());

		GameState(State state) {
			this.state = state;
		}

		State state;
	}

	/**
	 * Enum of Internal States
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 *
	 */
	public enum InternalState {
		STOPPED, RUNNIG;
	}

	/**
	 * Initialize the internalState to Stopped
	 */
	public GlobalStates() {
		internalState = InternalState.STOPPED;
	}

	/**
	 * Update the actual state
	 * 
	 * @param voxel
	 *            Voxel Instance
	 * @param delta
	 *            Delta for update
	 */
	public void doUpdate(Voxel voxel, float delta) {
		// Call the update method from the state
		state.state.update(voxel, delta);
		// Update stateSwitching of the state
		state.state.updateSwitch();
		// Check for display close request
		if (voxel.getGameResources().getDisplay().isCloseRequested())
			internalState = InternalState.STOPPED;
	}

	/**
	 * Render the actual state
	 * 
	 * @param voxel
	 *            Voxel Instance
	 * @param alpha
	 *            Render Alpha
	 */
	public void doRender(Voxel voxel, float alpha) {
		state.state.render(voxel, alpha);
	}

	/**
	 * Set the state
	 * 
	 * @param state
	 *            State
	 */
	public void setState(GameState state) {
		// Check for equal state
		if (!state.equals(this.state)) {
			// Check for actual null
			if (this.state != null)
				// End the actual statue
				this.state.state.end();
			// Set the old state
			this.oldState = this.state;
			// Switch state
			this.state = state;
			// Start the state
			this.state.state.start();
		}
	}

	/**
	 * Get the state
	 * 
	 * @return The actual state
	 */
	public GameState getState() {
		return state;
	}

	/**
	 * Get the old state
	 * 
	 * @return The old state
	 */
	public GameState getOldState() {
		return oldState;
	}

	/**
	 * Set internal state, use only in specific case
	 * 
	 * @param internalState
	 *            State to set
	 */
	public void setInternalState(InternalState internalState) {
		this.internalState = internalState;
	}

	/**
	 * Get the actual Internal State
	 * 
	 * @return Actual internal state
	 */
	public InternalState getInternalState() {
		return internalState;
	}
}
