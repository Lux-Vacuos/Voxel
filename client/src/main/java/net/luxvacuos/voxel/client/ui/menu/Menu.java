/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.ui.menu;

import net.luxvacuos.voxel.client.resources.GameResources;

/**
 * 
 * @author danirod
 */
public class Menu {

	public final MainMenu mainMenu;

	public final PauseMenu pauseMenu;

	public final OptionsMenu optionsMenu;

	public final GameSP gameSP;

	public final WorldSelectionMenu worldSelectionMenu;

	public final AboutMenu aboutMenu;
	
	public final MPSelectionMenu mpSelectionMenu;
	
	public final MPLoadingWorld mpLoadingWorld;
	
	public final GameSPInventory gameSPInventory;

	public Menu(GameResources gm) throws Exception {
		mainMenu = new MainMenu(gm);
		pauseMenu = new PauseMenu(gm);
		optionsMenu = new OptionsMenu(gm);
		gameSP = new GameSP(gm);
		worldSelectionMenu = new WorldSelectionMenu(gm);
		aboutMenu = new AboutMenu(gm);
		mpSelectionMenu = new MPSelectionMenu(gm);
		mpLoadingWorld = new MPLoadingWorld(gm);
		gameSPInventory = new GameSPInventory(gm);
	}

}
