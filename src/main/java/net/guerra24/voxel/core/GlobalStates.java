package net.guerra24.voxel.core;

import java.util.Random;

import net.guerra24.voxel.api.VAPI;
import net.guerra24.voxel.graphics.opengl.Display;
import net.guerra24.voxel.input.Keyboard;
import net.guerra24.voxel.menu.MainMenu;
import net.guerra24.voxel.menu.PauseMenu;
import net.guerra24.voxel.resources.GameResources;
import net.guerra24.voxel.resources.Loader;
import net.guerra24.voxel.util.vector.Vector3f;
import net.guerra24.voxel.world.WorldsHandler;

public class GlobalStates {

	public boolean loop = false;
	private GameState state;

	private MainMenu mainMenu;
	private PauseMenu pauseMenu;

	private boolean removeMainMenuText = false;
	private boolean removeGameSPText = false;

	public enum GameState {
		GAME_SP, MAINMENU, IN_PAUSE, LOADING_WORLD;
	}

	public GlobalStates(Loader loader) {
		mainMenu = new MainMenu(loader);
		pauseMenu = new PauseMenu();
		loop = true;
		state = GameState.MAINMENU;
	}

	public void updateUpdateThread(GameResources gm, WorldsHandler worlds, VAPI api, Display display) {

		if (state == GameState.MAINMENU && mainMenu.getPlayButton().pressed()) {
			removeMainMenuText = true;
			state = GameState.LOADING_WORLD;
			Random seed;
			if (VoxelVariables.isCustomSeed) {
				seed = new Random(VoxelVariables.seed.hashCode());
			} else {
				seed = new Random();
			}
			worlds.getActiveWorld().startWorld("World-0", seed, 0, api, gm);
			gm.getCamera().setMouse();
			gm.getSoundSystem().stop("menu1");
			gm.getSoundSystem().rewind("menu1");
			state = GameState.GAME_SP;
		}

		if (state == GameState.MAINMENU && mainMenu.getExitButton().pressed()) {
			loop = false;
		}

		if (state == GameState.IN_PAUSE && pauseMenu.getBackToMain().pressed()) {
			worlds.getActiveWorld().clearDimension(gm);
			gm.getSoundSystem().play("menu1");
			gm.getCamera().setPosition(new Vector3f(0, 0, 1));
			gm.getCamera().setPitch(0);
			gm.getCamera().setYaw(0);
			removeGameSPText = true;
			state = GameState.MAINMENU;
			gm.getSoundSystem().setVolume("menu1", 1f);
		}

		if (state == GameState.MAINMENU) {
			if (mainMenu.getPlayButton().insideButton())
				mainMenu.getList().get(0).changeScale(0.074f);
			else
				mainMenu.getList().get(0).changeScale(0.07f);
			if (mainMenu.getExitButton().insideButton())
				mainMenu.getList().get(1).changeScale(0.074f);
			else
				mainMenu.getList().get(1).changeScale(0.07f);
		}

		if (state == GameState.GAME_SP && !display.isDisplayFocused() && !VoxelVariables.debug) {
			gm.getCamera().unlockMouse();
			state = GameState.IN_PAUSE;
		}

		if (Display.isCloseRequested())
			loop = false;

		while (Keyboard.next()) {
			if (state == GameState.GAME_SP && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				gm.getCamera().unlockMouse();
				state = GameState.IN_PAUSE;
			} else if (state == GameState.IN_PAUSE && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				gm.getCamera().setMouse();
				state = GameState.GAME_SP;
			}
		}
	}

	public void updateRenderThread(GameResources gm, WorldsHandler worlds, VAPI api, Display display) {
		if (removeMainMenuText) {
			gm.getTextHandler().remove(gm.getTextHandler().getMainMenuText(), gm.getTextMasterRenderer());
			gm.getTextHandler().add(gm.getTextHandler().getGameSPText(), gm.getTextMasterRenderer());
			removeMainMenuText = false;
		}
		if (removeGameSPText) {
			gm.getTextHandler().remove(gm.getTextHandler().getGameSPText(), gm.getTextMasterRenderer());
			gm.getTextHandler().add(gm.getTextHandler().getMainMenuText(), gm.getTextMasterRenderer());
			removeGameSPText = false;
		}
	}

	public GameState getState() {
		return state;
	}

	public MainMenu getMainMenu() {
		return mainMenu;
	}

}
