package net.guerra24.voxel.client.core;

import net.guerra24.voxel.client.core.states.GameSPState;
import net.guerra24.voxel.client.core.states.InPauseState;
import net.guerra24.voxel.client.core.states.LoadingState;
import net.guerra24.voxel.client.core.states.MainMenuState;
import net.guerra24.voxel.client.core.states.OptionsState;
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
	public GameState state;

	public enum GameState {
		GAME_SP(new GameSPState()),
		MAINMENU(new MainMenuState()),
		IN_PAUSE(new InPauseState()),
		LOADING_WORLD(new LoadingState()),
		OPTIONS(new OptionsState());
		
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
				state = GameState.IN_PAUSE;
			} else if (state == GameState.IN_PAUSE && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				voxel.getGameResources().getCamera().setMouse();
				state = GameState.GAME_SP;
			}
		}
	}

	public void doRender(Voxel voxel, float delta) {
		state.state.render(voxel, this, delta);
	}

	public GameState getState() {
		return state;
	}
}
