package net.guerra24.voxel.client.engine;

import java.util.ArrayList;

import net.guerra24.voxel.client.engine.entities.Entity;
import net.guerra24.voxel.client.engine.entities.types.Light;
import net.guerra24.voxel.client.engine.entities.types.Player;
import net.guerra24.voxel.client.engine.menu.Button;
import net.guerra24.voxel.client.engine.menu.MenuScreen;
import net.guerra24.voxel.client.engine.network.ID;
import net.guerra24.voxel.client.engine.resources.GameResources;
import net.guerra24.voxel.client.engine.resources.GuiResources;
import net.guerra24.voxel.client.engine.util.Logger;
import net.guerra24.voxel.client.engine.util.SystemInfo;
import net.guerra24.voxel.client.engine.world.Blocks;
import net.guerra24.voxel.client.engine.world.World;
import net.guerra24.voxel.client.engine.world.chunks.Chunk;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Engine {

	public static boolean loop = true;
	public static boolean debug = false;

	public static State state = State.MAINMENU;
	public static boolean isLoading = false;
	private static int build = 3;

	public static void StartGame() {

		Logger.log("Loading");
		Logger.log("Voxel Game BUILD: " + build);
		DisplayManager.createDisplay();

		GameResources.init();

		GuiResources.loadingGui();
		GameResources.guiRenderer.render(GameResources.guis5);
		DisplayManager.updateDisplay();

		SystemInfo.chechOpenGl32();
		SystemInfo.printSystemInfo();

		ID.generateUUID();
		ID.writeUUID();

		Blocks.createBlocks();
		GuiResources.loadGuiTexture();
		GuiResources.addGuiTextures();

		GameResources.player = new Player(Blocks.cubeGlass, new Vector3f(-10,
				68, -10), 0, 0, 90, 1);
		GameResources.sun = new Light(new Vector3f(0, 10000000000f, 0),
				new Vector3f(1f, 1f, 1f));
		// spot = new Light(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0),
		// new Vector3f(1, 0.01f, 0.002f));
		// lights.add(spot);
		GameResources.lights.add(GameResources.sun);
		GameResources.allObjects.add(GameResources.player);
		// guis.add(gui2);
		GameResources.allEntities.addAll(GameResources.allObjects);

		while (loop) {
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
				// fbos.bindReflectionFrameBuffer();
				// renderer.renderScene(allCubes, lights, camera);
				// fbos.unbindCurrentFrameBuffer();
				// spot.setPosition(player.getPosition());
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
			switchStates();
			DisplayManager.updateDisplay();
		}
		GameResources.guiRenderer.render(GameResources.guis5);
		DisplayManager.updateDisplay();
		Logger.log("Closing Game");
		GameResources.cleanUp();
		DisplayManager.closeDisplay();
	}

	public enum State {
		GAME, MAINMENU, MULTIPLAY_SCREEN, WORLDSELECTION, IN_PAUSE;
	}

	private static void switchStates() {
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
			World.saveGame();
			Chunk.cubes = new ArrayList<Entity>();
			MenuScreen.isPlaying = false;
			MenuScreen.isPrePlay = true;
			state = State.MAINMENU;
		}
		while (Keyboard.next()) {
			if (state == State.GAME && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				World.saveGame();
				GameResources.camera.unlockMouse();
				state = State.IN_PAUSE;
			} else if (state == State.IN_PAUSE
					&& Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				GameResources.camera.setMouse();
				state = State.GAME;
			}
		}
	}

	public static void debugMode() {
		System.out.println("X" + Mouse.getX() + "Y" + Mouse.getY());
	}

	public static void main(String[] args) {
		StartGame();
	}
}
