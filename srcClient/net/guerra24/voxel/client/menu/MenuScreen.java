package net.guerra24.voxel.client.menu;

import net.guerra24.voxel.client.kernel.Kernel;
import net.guerra24.voxel.client.kernel.GameStates.State;

import org.lwjgl.input.Mouse;

public class MenuScreen {

	public static boolean selected = false;
	public static boolean isPlaying = false;

	public static void worldSelected() {
		while (Mouse.next()) {
			if (Button.isWorldSelected() && selected) {
				Kernel.gameResources.gameStates.state = State.GAME;
				Kernel.gameResources.SoundSystem.pause("MainMenuMusic");

				if (Kernel.isLoading && !isPlaying) {
					isPlaying = true;
				} else if (!Kernel.isLoading && !isPlaying) {
					Kernel.world.startWorld();
					isPlaying = true;
				}
				Kernel.gameResources.camera.setMouse();
			}
			if (Button.isWorldSelected() && !selected) {
				Kernel.gameResources.guis3.remove(Kernel.guiResources.button3);
				Kernel.gameResources.guis3.remove(Kernel.guiResources.world);
				Kernel.gameResources.guis3
						.remove(Kernel.guiResources.wnoselect);
				Kernel.gameResources.guis3.add(Kernel.guiResources.wselect);
				Kernel.gameResources.guis3.add(Kernel.guiResources.button3);
				Kernel.gameResources.guis3.add(Kernel.guiResources.world);
				selected = true;
			}
			if (Button.isWorldNotSelected() && selected) {
				Kernel.gameResources.guis3.remove(Kernel.guiResources.button3);
				Kernel.gameResources.guis3.remove(Kernel.guiResources.world);
				Kernel.gameResources.guis3.remove(Kernel.guiResources.wselect);
				Kernel.gameResources.guis3.add(Kernel.guiResources.wnoselect);
				Kernel.gameResources.guis3.add(Kernel.guiResources.button3);
				Kernel.gameResources.guis3.add(Kernel.guiResources.world);
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
				Kernel.gameResources.guis3.remove(Kernel.guiResources.button3);
				Kernel.gameResources.guis3.remove(Kernel.guiResources.world);
				Kernel.gameResources.guis3
						.remove(Kernel.guiResources.wnoselect);
				Kernel.gameResources.guis3.add(Kernel.guiResources.wselect);
				Kernel.gameResources.guis3.add(Kernel.guiResources.button3);
				Kernel.gameResources.guis3.add(Kernel.guiResources.world);
				selected = true;
			}
			if (Button.isWorldNotSelected() && selected) {
				Kernel.gameResources.guis3.remove(Kernel.guiResources.button3);
				Kernel.gameResources.guis3.remove(Kernel.guiResources.world);
				Kernel.gameResources.guis3.remove(Kernel.guiResources.wselect);
				Kernel.gameResources.guis3.add(Kernel.guiResources.wnoselect);
				Kernel.gameResources.guis3.add(Kernel.guiResources.button3);
				Kernel.gameResources.guis3.add(Kernel.guiResources.world);
				selected = false;
			}
		}
	}
}
