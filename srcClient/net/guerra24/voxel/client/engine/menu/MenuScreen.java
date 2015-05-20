package net.guerra24.voxel.client.engine.menu;

import net.guerra24.voxel.client.engine.Engine;
import net.guerra24.voxel.client.engine.GameStates.State;

import org.lwjgl.input.Mouse;

public class MenuScreen {

	private static boolean selected = false;
	public static boolean isPrePlay = false;
	public static boolean isPlaying = false;

	public static void worldSelected() {
		while (Mouse.next()) {
			if (Button.isWorldSelected() && selected) {
				Engine.gameResources.gameStates.state = State.GAME;
				Engine.gameResources.SoundSystem.pause("MainMenuMusic");

				if (Engine.isLoading && !isPlaying && !isPrePlay) {
					Engine.world.saveGame();
					isPlaying = true;
				} else if (!Engine.isLoading && !isPlaying && !isPrePlay) {
					Engine.world.init(2);
					isPlaying = true;
				} else if (!isPlaying && isPrePlay) {
					Engine.world.loadGame();
					isPlaying = true;
				}
				Engine.gameResources.camera.setMouse();
			}
			if (Button.isWorldSelected() && !selected) {
				Engine.gameResources.guis3.remove(Engine.guiResources.button3);
				Engine.gameResources.guis3.remove(Engine.guiResources.world);
				Engine.gameResources.guis3
						.remove(Engine.guiResources.wnoselect);
				Engine.gameResources.guis3.add(Engine.guiResources.wselect);
				Engine.gameResources.guis3.add(Engine.guiResources.button3);
				Engine.gameResources.guis3.add(Engine.guiResources.world);
				selected = true;
			}
			if (Button.isWorldNotSelected() && selected) {
				Engine.gameResources.guis3.remove(Engine.guiResources.button3);
				Engine.gameResources.guis3.remove(Engine.guiResources.world);
				Engine.gameResources.guis3.remove(Engine.guiResources.wselect);
				Engine.gameResources.guis3.add(Engine.guiResources.wnoselect);
				Engine.gameResources.guis3.add(Engine.guiResources.button3);
				Engine.gameResources.guis3.add(Engine.guiResources.world);
				selected = false;
			}
		}
	}

	public static void multiScreen() {
		while (Mouse.next()) {
			if (Button.isWorldSelected() && selected) {
				// Engine.state = Engine.State.GAME;
				// Engine.gameResources.camera.setMouse();

			}
			if (Button.isWorldSelected() && !selected) {
				Engine.gameResources.guis3.remove(Engine.guiResources.button3);
				Engine.gameResources.guis3.remove(Engine.guiResources.world);
				Engine.gameResources.guis3
						.remove(Engine.guiResources.wnoselect);
				Engine.gameResources.guis3.add(Engine.guiResources.wselect);
				Engine.gameResources.guis3.add(Engine.guiResources.button3);
				Engine.gameResources.guis3.add(Engine.guiResources.world);
				selected = true;
			}
			if (Button.isWorldNotSelected() && selected) {
				Engine.gameResources.guis3.remove(Engine.guiResources.button3);
				Engine.gameResources.guis3.remove(Engine.guiResources.world);
				Engine.gameResources.guis3.remove(Engine.guiResources.wselect);
				Engine.gameResources.guis3.add(Engine.guiResources.wnoselect);
				Engine.gameResources.guis3.add(Engine.guiResources.button3);
				Engine.gameResources.guis3.add(Engine.guiResources.world);
				selected = false;
			}
		}
	}
}
