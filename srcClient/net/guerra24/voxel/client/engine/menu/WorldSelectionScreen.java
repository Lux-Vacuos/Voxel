package net.guerra24.voxel.client.engine.menu;

import org.lwjgl.input.Mouse;

import net.guerra24.voxel.client.engine.Engine;
import net.guerra24.voxel.client.engine.resources.GuiResources;
import net.guerra24.voxel.client.engine.util.Logger;
import net.guerra24.voxel.client.engine.world.World;

public class WorldSelectionScreen {

	private static boolean selected = false;
	public static boolean isPrePlay = false;
	public static boolean isPlaying = false;

	public static void worldSelected() {
		while (Mouse.next()) {
			if (Button.isWorldSelected() && selected) {
				Engine.state = Engine.State.GAME;

				if (Engine.isLoading && !isPlaying && !isPrePlay) {
					World.loadGame();
					isPlaying = true;
				} else if (!Engine.isLoading && !isPlaying && !isPrePlay) {
					Logger.log("Generating World with size: "
							+ World.WORLD_SIZE);
					World.init();
					Logger.log("World Generation completed with size: "
							+ World.WORLD_SIZE);
					isPlaying = true;
				} else if (!isPlaying && isPrePlay) {
					World.loadGame();
					isPlaying = true;
				}
				Engine.camera.setMouse();
			}
			if (Button.isWorldSelected() && !selected) {
				Engine.guis3.remove(GuiResources.button3);
				Engine.guis3.remove(GuiResources.world);
				Engine.guis3.remove(GuiResources.wnoselect);
				Engine.guis3.add(GuiResources.wselect);
				Engine.guis3.add(GuiResources.button3);
				Engine.guis3.add(GuiResources.world);
				selected = true;
			}
			if (Button.isWorldNotSelected() && selected) {
				Engine.guis3.remove(GuiResources.button3);
				Engine.guis3.remove(GuiResources.world);
				Engine.guis3.remove(GuiResources.wselect);
				Engine.guis3.add(GuiResources.wnoselect);
				Engine.guis3.add(GuiResources.button3);
				Engine.guis3.add(GuiResources.world);
				selected = false;
			}
		}
	}
}
