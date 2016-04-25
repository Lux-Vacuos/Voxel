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
import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;

public class MPSelectionMenu {

	private Button exitButton;
	private Button playButton;
	private String ip = "";

	public MPSelectionMenu(GameResources gm) {
		exitButton = new Button(new Vector2f(655, 30), new Vector2f(215, 80));
		playButton = new Button(new Vector2f(410, 30), new Vector2f(215, 80));
	}

	public void render() {
		VectorsRendering.renderWindow("Multiplayer", "Roboto-Bold", 20 * VoxelVariables.XSCALE,
				20 * VoxelVariables.YSCALE, 1240 * VoxelVariables.XSCALE, 540 * VoxelVariables.YSCALE);
		VectorsRendering.renderText("IP:  ", "Roboto-Regular", 270 * VoxelVariables.XSCALE, 275 * VoxelVariables.YSCALE,
				60 * VoxelVariables.YSCALE, VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
		VectorsRendering.renderWindow(20 * VoxelVariables.XSCALE, 570 * VoxelVariables.YSCALE,
				1240 * VoxelVariables.XSCALE, 130 * VoxelVariables.YSCALE);
		while (Keyboard.next())
			ip = Keyboard.keyWritten(ip);
		VectorsRendering.renderSearchBox(ip, "Roboto-Regular", "Entypo", 340 * VoxelVariables.XSCALE,
				260 * VoxelVariables.YSCALE, 600 * VoxelVariables.XSCALE, 40 * VoxelVariables.YSCALE);

		exitButton.render("Back");
		playButton.render("Play");
	}

	public Button getExitButton() {
		return exitButton;
	}

	public Button getPlayButton() {
		return playButton;
	}

	public String getIP() {
		return ip;
	}

}
