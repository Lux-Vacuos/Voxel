/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.guerra24.voxel.client.core;

import net.guerra24.voxel.client.core.states.GameSPState;
import net.guerra24.voxel.client.core.states.InPauseState;
import net.guerra24.voxel.client.core.states.LoadingSPState;
import net.guerra24.voxel.client.core.states.MainMenuState;
import net.guerra24.voxel.client.core.states.OptionsState;
import net.guerra24.voxel.client.core.states.WorldSelectionState;
import net.guerra24.voxel.client.graphics.opengl.Display;
import net.guerra24.voxel.client.input.Keyboard;
import net.guerra24.voxel.client.resources.Loader;

/**
 * States Handler
 * 
 * @author danirod
 * @category Kernel
 */
public class GlobalStates {

	public boolean loop = false;

	private GameState state;

	private GameState oldState;

	public enum GameState {
		GAME_SP(new GameSPState()), MAINMENU(new MainMenuState()), IN_PAUSE(new InPauseState()), LOADING_WORLD(
				new LoadingSPState()), OPTIONS(new OptionsState()), WORLD_SELECTION(new WorldSelectionState());

		GameState(State state) {
			this.state = state;
		}

		State state;
	}

	public GlobalStates(Loader loader) {
		loop = true;
		state = VoxelVariables.autostart ? GameState.LOADING_WORLD : GameState.MAINMENU;
	}

	public void doUpdate(Voxel voxel, float delta) {

		state.state.update(voxel, this, delta);

		if (Display.isCloseRequested())
			loop = false;

		while (Keyboard.next()) {
			if (state == GameState.GAME_SP && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				voxel.getGameResources().getCamera().unlockMouse();
				setState(GameState.IN_PAUSE);
			} else if (state == GameState.IN_PAUSE && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				voxel.getGameResources().getCamera().setMouse();
				setState(GameState.GAME_SP);
			}
		}
	}

	public void doRender(Voxel voxel, float delta) {
		state.state.render(voxel, this, delta);
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
