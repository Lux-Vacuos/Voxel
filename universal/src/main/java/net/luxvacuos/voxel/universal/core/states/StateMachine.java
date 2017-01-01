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

package net.luxvacuos.voxel.universal.core.states;

import java.util.HashMap;
import java.util.Map;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.EngineType;
import net.luxvacuos.voxel.universal.resources.IDisposable;

public final class StateMachine {

	private static IState currentState, previousState;

	private static InternalState internalState = InternalState.STOPPED;

	private static Map<String, IState> registeredStates = new HashMap<String, IState>();

	private StateMachine() {
	}

	public static void run() {
		Logger.log("StateMachine running");
		internalState = InternalState.RUNNING;
	}

	public static boolean isRunning() {
		return internalState == InternalState.RUNNING;
	}

	public static boolean registerState(IState state) {
		if (!registeredStates.containsKey(state.getName())) {
			Logger.log("Registering State: " + state.getName());
			state.init(); // Initialize the state
			registeredStates.put(state.getName(), state);
			return true;
		} else
			return false;
	}

	public static boolean update(AbstractVoxel voxel, float deltaTime) {
		if (currentState == null)
			return false;

		currentState.update(voxel, deltaTime);
		return true;
	}

	public static boolean render(AbstractVoxel voxel, float alpha) {
		if (currentState == null || voxel.getEngineType() != EngineType.CLIENT)
			return false;

		currentState.render(voxel, alpha);
		return true;
	}

	public static boolean setCurrentState(String name) {
		if (name != null || registeredStates.containsKey(name)) {
			IState state = registeredStates.get(name);
			Logger.log("Setting current state to " + state.getName());
			if (currentState != null) {
				if (currentState.equals(state))
					return false;

				currentState.end();
				previousState = currentState;
			}

			currentState = state;
			currentState.start();
			return true;
		} else
			return false;
	}

	public static IState getCurrentState() {
		return currentState;
	}

	public static IState getPreviousState() {
		return previousState;
	}

	public static void dispose() {
		internalState = InternalState.STOPPED;
		for (IState state : registeredStates.values()) {
			if (state.isRunning())
				state.end();
			if (state instanceof IDisposable)
				((IDisposable) state).dispose();
		}
	}

	public static InternalState getInternalState() {
		return internalState;
	}

	public enum InternalState {
		STOPPED, RUNNING
	}

}
