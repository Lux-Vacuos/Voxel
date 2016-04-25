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

public class MPLoadingWorld {

	private Button exitButton;

	public MPLoadingWorld(GameResources gm) {
		exitButton = new Button(new Vector2f(533, 220), new Vector2f(215, 80));
	}

	public void render(String message) {
		VectorsRendering.renderWindow("Loading Multiplayer", "Roboto-Bold", 20 * VoxelVariables.XSCALE,
				20 * VoxelVariables.YSCALE, 1240 * VoxelVariables.XSCALE, 540 * VoxelVariables.YSCALE);
		VectorsRendering.renderText(message, "Roboto-Regular", 40 * VoxelVariables.XSCALE, 300 * VoxelVariables.YSCALE,
				60 * VoxelVariables.YSCALE, VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
		VectorsRendering.renderWindow(20 * VoxelVariables.XSCALE, 570 * VoxelVariables.YSCALE,
				1240 * VoxelVariables.XSCALE, 130 * VoxelVariables.YSCALE);
		exitButton.render("Cancel");
	}

	public Button getExitButton() {
		return exitButton;
	}

}
