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

import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;

public class PauseMenu {
	private Button exitButton;
	private Button optionsButton;

	public PauseMenu(GameResources gm) {
		exitButton = new Button(new Vector2f(GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 100, 35),
				new Vector2f(200, 40));
		optionsButton = new Button(
				new Vector2f(GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 100, 115),
				new Vector2f(200, 40));
	}

	public void render() {
		VectorsRendering.renderWindow("Pause", "Roboto-Bold", 20, 20,
				GameResources.getInstance().getDisplay().getDisplayWidth() - 40,
				GameResources.getInstance().getDisplay().getDisplayHeight() - 40);
		exitButton.render("Back to Main Menu");
		optionsButton.render("Options");
	}

	public Button getExitButton() {
		return exitButton;
	}

	public Button getOptionsButton() {
		return optionsButton;
	}

}
