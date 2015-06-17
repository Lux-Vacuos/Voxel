package io.github.guerra24.voxel.client.kernel;

import io.github.guerra24.voxel.client.menu.Button;
import io.github.guerra24.voxel.client.menu.MenuScreen;

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
			Kernel.gameResources.guis3.remove(Kernel.guiResources.button3);
			Kernel.gameResources.guis3.remove(Kernel.guiResources.world);
			Kernel.gameResources.guis3.remove(Kernel.guiResources.wselect);
			Kernel.gameResources.guis3.add(Kernel.guiResources.wnoselect);
			Kernel.gameResources.guis3.add(Kernel.guiResources.button3);
			Kernel.gameResources.guis3.add(Kernel.guiResources.world);
			MenuScreen.selected = false;
			state = State.MAINMENU;
		}
		if (state == State.MULTIPLAY_SCREEN && Button.isInButtonBacK()) {
			Kernel.gameResources.guis3.remove(Kernel.guiResources.button3);
			Kernel.gameResources.guis3.remove(Kernel.guiResources.world);
			Kernel.gameResources.guis3.remove(Kernel.guiResources.wselect);
			Kernel.gameResources.guis3.add(Kernel.guiResources.wnoselect);
			Kernel.gameResources.guis3.add(Kernel.guiResources.button3);
			Kernel.gameResources.guis3.add(Kernel.guiResources.world);
			MenuScreen.selected = false;
			state = State.MAINMENU;
		}

		if (state == State.IN_PAUSE && Button.backToMainMenu()) {
			Kernel.gameResources.SoundSystem.rewind("MainMenuMusic");
			Kernel.gameResources.SoundSystem.play("MainMenuMusic");
			MenuScreen.isPlaying = false;
			Kernel.gameResources.allEntities.clear();
			for (int x = 0; x < Kernel.world.viewDistance; x++) {
				for (int z = 0; z < Kernel.world.viewDistance; z++) {
					Kernel.world.chunks[x][z].dispose();
				}
			}
			Kernel.gameResources.guis3.remove(Kernel.guiResources.button3);
			Kernel.gameResources.guis3.remove(Kernel.guiResources.world);
			Kernel.gameResources.guis3.remove(Kernel.guiResources.wselect);
			Kernel.gameResources.guis3.add(Kernel.guiResources.wnoselect);
			Kernel.gameResources.guis3.add(Kernel.guiResources.button3);
			Kernel.gameResources.guis3.add(Kernel.guiResources.world);
			MenuScreen.selected = false;
			state = State.MAINMENU;
		}
		while (Keyboard.next()) {
			if (state == State.GAME && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Kernel.gameResources.camera.unlockMouse();
				state = State.IN_PAUSE;
			} else if (state == State.IN_PAUSE
					&& Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Kernel.gameResources.camera.setMouse();
				state = State.GAME;
			}
		}
	}
}
