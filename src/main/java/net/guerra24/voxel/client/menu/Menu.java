package net.guerra24.voxel.client.menu;

import net.guerra24.voxel.client.resources.Loader;

/**
 * 
 * @author danirod
 */
public class Menu {

	public final MainMenu mainMenu;

	public final PauseMenu pauseMenu;

	public final OptionsMenu optionsMenu;

	public Menu(Loader loader) {
		mainMenu = new MainMenu(loader);
		pauseMenu = new PauseMenu();
		optionsMenu = new OptionsMenu();
	}

}
