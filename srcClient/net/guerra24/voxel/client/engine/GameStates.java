package net.guerra24.voxel.client.engine;

import java.util.ArrayList;

import net.guerra24.voxel.client.engine.entities.Entity;
import net.guerra24.voxel.client.engine.menu.Button;
import net.guerra24.voxel.client.engine.menu.MenuScreen;
import net.guerra24.voxel.client.engine.world.chunks.Chunk;

import org.lwjgl.input.Keyboard;

public class GameStates {

	public boolean loop;

	public State state;

	public GameStates() {
		loop = true;
		state = State.MAINMENU;
	}

	public enum State {
		GAME, MAINMENU, MULTIPLAY_SCREEN, WORLDSELECTION, IN_PAUSE;
	}

	public void switchStates() {
		if (state == State.MAINMENU && Button.isInButtonPlay()) {
			state = State.WORLDSELECTION;
		}
		if (state == State.MAINMENU && Button.isInButtonMutli()) {
			state = State.MULTIPLAY_SCREEN;
		}

		if (state == State.MAINMENU && Button.isInButtonExit()) {
			loop = false;
		}
		if (state == State.WORLDSELECTION && Button.isInButtonBacK()) {
			state = State.MAINMENU;
		}
		if (state == State.MULTIPLAY_SCREEN && Button.isInButtonBacK()) {
			state = State.MAINMENU;
		}

		if (state == State.IN_PAUSE && Button.backToMainMenu()) {
			Engine.gameResources.SoundSystem.rewind("MainMenuMusic");
			Engine.gameResources.SoundSystem.play("MainMenuMusic");
			Engine.world.saveGame();
			Chunk.cubes = new ArrayList<Entity>();
			MenuScreen.isPlaying = false;
			MenuScreen.isPrePlay = true;
			state = State.MAINMENU;
		}
		while (Keyboard.next()) {
			if (state == State.GAME && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Engine.world.saveGame();
				Engine.gameResources.camera.unlockMouse();
				state = State.IN_PAUSE;
			} else if (state == State.IN_PAUSE
					&& Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Engine.gameResources.camera.setMouse();
				state = State.GAME;
			}
		}
	}
}
