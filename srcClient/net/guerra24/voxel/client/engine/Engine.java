package net.guerra24.voxel.client.engine;

import net.guerra24.voxel.client.engine.menu.MenuScreen;
import net.guerra24.voxel.client.engine.resources.GameResources;
import net.guerra24.voxel.client.engine.resources.GuiResources;
import net.guerra24.voxel.client.engine.util.Logger;
import net.guerra24.voxel.client.engine.util.SystemInfo;
import net.guerra24.voxel.client.engine.world.Blocks;

import org.lwjgl.input.Mouse;

public class Engine {

	public static boolean debug = false;

	public static State state = State.MAINMENU;
	public static boolean isLoading = false;
	private static int build = 4;

	public static GameResources gameResources;

	public static void StartGame() {

		Logger.log("Loading");
		Logger.log("Voxel Game BUILD: " + build);
		DisplayManager.createDisplay();
		SystemInfo.printSystemInfo();

		gameResources = new GameResources();

		GuiResources.loadingGui();
		GameResources.guiRenderer.render(GameResources.guis5);
		DisplayManager.updateDisplay();

		Blocks.createBlocks();
		GuiResources.loadGuiTexture();
		GuiResources.addGuiTextures();
		gameResources.addRes();
		gameResources.music();

		while (GameResources.state.loop) {
			switch (state) {
			case MAINMENU:
				GameResources.guiRenderer.render(GameResources.guis2);
				break;
			case WORLDSELECTION:
				MenuScreen.worldSelected();
				GameResources.guiRenderer.render(GameResources.guis3);
				break;
			case MULTIPLAY_SCREEN:
				MenuScreen.multiScreen();
				GameResources.guiRenderer.render(GameResources.guis3);
				break;
			case IN_PAUSE:
				GameResources.guiRenderer.render(GameResources.guis4);
				break;
			case GAME:
				GameResources.camera.move();
				GameResources.player.move();

				GameResources.renderer.renderScene(GameResources.allEntities,
						GameResources.lights, GameResources.camera);
				GameResources.waterRenderer.render(GameResources.waters,
						GameResources.camera);
				GameResources.guiRenderer.renderNoPrepare(GameResources.guis);
				break;
			}
			if (debug) {
				debugMode();
			}
			GameResources.state.switchStates();
			DisplayManager.updateDisplay();
		}
		GameResources.guiRenderer.render(GameResources.guis5);
		DisplayManager.updateDisplay();
		Logger.log("Closing Game");
		gameResources.cleanUp();
		DisplayManager.closeDisplay();
	}

	public enum State {
		GAME, MAINMENU, MULTIPLAY_SCREEN, WORLDSELECTION, IN_PAUSE;
	}

	public static void debugMode() {
		System.out.println("X" + Mouse.getX() + "Y" + Mouse.getY());
	}

	public static void main(String[] args) {
		StartGame();
	}
}
