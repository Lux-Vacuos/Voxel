package net.guerra24.voxel.client.menu;

import net.guerra24.voxel.client.resources.GameResources;

/**
 * 
 * @author danirod
 */
public class Menu {

	public final MainMenu mainMenu;

	public final PauseMenu pauseMenu;

	public final OptionsMenu optionsMenu;

	public Menu(GameResources gm) {
		mainMenu = new MainMenu(gm);
		pauseMenu = new PauseMenu();
		optionsMenu = new OptionsMenu();
	}

}
