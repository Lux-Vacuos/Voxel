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

package net.luxvacuos.voxel.client.menu;

import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.resources.GameResources;

public class PauseMenu {
	private Button exitButton;
	private Button optionsButton;
	private float yScale, xScale;

	public PauseMenu(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		exitButton = new Button(new Vector2f(500, 35), new Vector2f(280, 80), xScale, yScale);
		optionsButton = new Button(new Vector2f(500, 135), new Vector2f(280, 80), xScale, yScale);
	}

	public void render() {
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
