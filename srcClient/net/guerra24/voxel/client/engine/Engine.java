package net.guerra24.voxel.client.engine;

import net.guerra24.voxel.client.engine.menu.MenuScreen;
import net.guerra24.voxel.client.engine.resources.GameResources;
import net.guerra24.voxel.client.engine.resources.GuiResources;
import net.guerra24.voxel.client.engine.util.Logger;
import net.guerra24.voxel.client.engine.util.SystemInfo;
import net.guerra24.voxel.client.engine.world.Blocks;
import net.guerra24.voxel.client.engine.world.World;

import org.lwjgl.input.Mouse;

public class Engine {

	public static boolean debug = false;

	public static boolean isLoading = false;
	private static int build = 4;

	public static GameResources gameResources;
	public static GuiResources guiResources;
	public static World world;

	public static void StartGame() {

		Logger.log("Loading");
		Logger.log("Voxel Game BUILD: " + build);
		DisplayManager.createDisplay();
		SystemInfo.printSystemInfo();

		gameResources = new GameResources();

		GuiResources.loadingGui();
		gameResources.guiRenderer.render(gameResources.guis5);
		DisplayManager.updateDisplay();

		guiResources = new GuiResources();

		Blocks.createBlocks();
		gameResources.addRes();
		gameResources.music();

		world = new World();

		while (gameResources.gameStates.loop) {
			switch (gameResources.gameStates.state) {
			case MAINMENU:
				gameResources.guiRenderer.render(gameResources.guis2);
				break;
			case WORLDSELECTION:
				MenuScreen.worldSelected();
				gameResources.guiRenderer.render(gameResources.guis3);
				break;
			case MULTIPLAY_SCREEN:
				MenuScreen.multiScreen();
				gameResources.guiRenderer.render(gameResources.guis3);
				break;
			case IN_PAUSE:
				gameResources.guiRenderer.render(gameResources.guis4);
				break;
			case GAME:
				gameResources.camera.move();
				gameResources.player.move();
				gameResources.renderer.renderScene(gameResources.allEntities,
						gameResources.lights, gameResources.camera);
				gameResources.waterRenderer.render(gameResources.waters,
						gameResources.camera);
				gameResources.guiRenderer.renderNoPrepare(gameResources.guis);
				break;
			}
			if (debug) {
				debugMode();
			}
			gameResources.gameStates.switchStates();
			DisplayManager.updateDisplay();
		}
		gameResources.guiRenderer.render(gameResources.guis5);
		DisplayManager.updateDisplay();
		Logger.log("Closing Game");
		gameResources.cleanUp();
		DisplayManager.closeDisplay();
	}

	public void setReflection() {
		gameResources.fbos.bindReflectionFrameBuffer();
		gameResources.renderer.renderScene(gameResources.allEntities,
				gameResources.lights, gameResources.camera);
		gameResources.fbos.unbindCurrentFrameBuffer();
	}

	public static void debugMode() {
		System.out.println("X" + Mouse.getX() + "Y" + Mouse.getY());
	}

	public static void main(String[] args) {
		StartGame();
	}
}
