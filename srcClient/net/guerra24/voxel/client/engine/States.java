package net.guerra24.voxel.client.engine;

import java.util.ArrayList;

import net.guerra24.voxel.client.engine.Engine.State;
import net.guerra24.voxel.client.engine.entities.Entity;
import net.guerra24.voxel.client.engine.menu.Button;
import net.guerra24.voxel.client.engine.menu.MenuScreen;
import net.guerra24.voxel.client.engine.resources.GameResources;
import net.guerra24.voxel.client.engine.world.World;
import net.guerra24.voxel.client.engine.world.chunks.Chunk;

import org.lwjgl.input.Keyboard;

public class States {

	public boolean loop;

	public States() {
		loop = true;
	}

	public void switchStates() {
		if (Engine.state == State.MAINMENU && Button.isInButtonPlay()) {
			Engine.state = State.WORLDSELECTION;
		}
		if (Engine.state == State.MAINMENU && Button.isInButtonMutli()) {
			Engine.state = State.MULTIPLAY_SCREEN;
		}

		if (Engine.state == State.MAINMENU && Button.isInButtonExit()) {
			loop = false;
		}
		if (Engine.state == State.WORLDSELECTION && Button.isInButtonBacK()) {
			Engine.state = State.MAINMENU;
		}
		if (Engine.state == State.MULTIPLAY_SCREEN && Button.isInButtonBacK()) {
			Engine.state = State.MAINMENU;
		}

		if (Engine.state == State.IN_PAUSE && Button.backToMainMenu()) {
			GameResources.SoundSystem.rewind("MainMenuMusic");
			GameResources.SoundSystem.play("MainMenuMusic");
			World.saveGame();
			Chunk.cubes = new ArrayList<Entity>();
			MenuScreen.isPlaying = false;
			MenuScreen.isPrePlay = true;
			Engine.state = State.MAINMENU;
		}
		while (Keyboard.next()) {
			if (Engine.state == State.GAME
					&& Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				World.saveGame();
				GameResources.camera.unlockMouse();
				Engine.state = State.IN_PAUSE;
			} else if (Engine.state == State.IN_PAUSE
					&& Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				GameResources.camera.setMouse();
				Engine.state = State.GAME;
			}
		}
	}
}
