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

public class MainMenu {

	private Button playButton;
	private Button exitButton;
	private Button optionsButton;
	private Button aboutButton;
	private Button playMPButton;

	public MainMenu(GameResources gm) {
		playButton = new Button(
				new Vector2f(GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 100,
						GameResources.getInstance().getDisplay().getDisplayHeight() / 2f + 120 - 20),
				new Vector2f(200, 40));
		playMPButton = new Button(
				new Vector2f(GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 100,
						GameResources.getInstance().getDisplay().getDisplayHeight() / 2f + 60 - 20),
				new Vector2f(200, 40));
		optionsButton = new Button(new Vector2f(GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 100,
				GameResources.getInstance().getDisplay().getDisplayHeight() / 2 - 20), new Vector2f(200, 40));
		aboutButton = new Button(
				new Vector2f(GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 100,
						GameResources.getInstance().getDisplay().getDisplayHeight() / 2f - 60 - 20),
				new Vector2f(200, 40));
		exitButton = new Button(
				new Vector2f(GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 100,
						GameResources.getInstance().getDisplay().getDisplayHeight() / 2f - 120 - 20),
				new Vector2f(200, 40));
	}

	public void render() {
		VectorsRendering.renderWindow("Main Menu", "Roboto-Bold", 20, 20,
				GameResources.getInstance().getDisplay().getDisplayWidth() - 40,
				GameResources.getInstance().getDisplay().getDisplayHeight() - 40);
		playButton.render("Play", VectorsRendering.ICON_BLACK_RIGHT_POINTING_TRIANGLE);
		playMPButton.render("Multiplayer", VectorsRendering.ICON_BLACK_RIGHT_POINTING_TRIANGLE);
		optionsButton.render("Options", VectorsRendering.ICON_GEAR);
		aboutButton.render("About", VectorsRendering.ICON_INFORMATION_SOURCE);
		exitButton.render("Exit", VectorsRendering.ICON_LOGIN);

	}

	public void update() {
	}

	public Button getPlayButton() {
		return playButton;
	}

	public Button getExitButton() {
		return exitButton;
	}

	public Button getOptionsButton() {
		return optionsButton;
	}

	public Button getAboutButton() {
		return aboutButton;
	}

	public Button getPlayMPButton() {
		return playMPButton;
	}

}
