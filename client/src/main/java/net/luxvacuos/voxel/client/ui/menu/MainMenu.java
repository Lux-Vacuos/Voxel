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
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.WebRenderer;

public class MainMenu {

	private Button playButton;
	private Button exitButton;
	private Button optionsButton;
	private Button newsRefreshButton;
	private Button aboutButton;
	private Button playMPButton;

	private WebRenderer webRenderer;

	private float xScale, yScale;

	public MainMenu(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		playButton = new Button(new Vector2f(177, 568), new Vector2f(215, 80), xScale, yScale);
		playMPButton = new Button(new Vector2f(177, 468), new Vector2f(215, 80), xScale, yScale);
		optionsButton = new Button(new Vector2f(177, 368), new Vector2f(215, 80), xScale, yScale);
		aboutButton = new Button(new Vector2f(177, 268), new Vector2f(215, 80), xScale, yScale);
		exitButton = new Button(new Vector2f(177, 168), new Vector2f(215, 80), xScale, yScale);
		newsRefreshButton = new Button(new Vector2f(1096, 627), new Vector2f(100, 40), xScale, yScale);
		webRenderer = new WebRenderer(VoxelVariables.web + "news/menu.webtag", 460 * xScale, 120 * yScale);
		webRenderer.update();
	}

	public void render() {
		VectorsRendering.renderWindow(160 * xScale, 50 * yScale, 250 * xScale, 600 * yScale);
		playButton.render("Play", VectorsRendering.ICON_BLACK_RIGHT_POINTING_TRIANGLE);
		playMPButton.render("Multiplayer", VectorsRendering.ICON_BLACK_RIGHT_POINTING_TRIANGLE);
		optionsButton.render("Options", VectorsRendering.ICON_GEAR);
		aboutButton.render("About", VectorsRendering.ICON_INFORMATION_SOURCE);
		exitButton.render("Exit", VectorsRendering.ICON_LOGIN);

		VectorsRendering.renderWindow("Voxel News", "Roboto-Bold", 450 * xScale, 50 * yScale, 750 * xScale,
				600 * yScale);
		webRenderer.render();

		newsRefreshButton.render("Reload", VectorsRendering.rgba(80, 80, 80, 80, VectorsRendering.colorA));
	}

	public void update() {
		if (newsRefreshButton.pressed())
			webRenderer.update();
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
