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
import net.luxvacuos.voxel.client.input.Keyboard;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;

public class MPSelectionMenu {

	private Button exitButton;
	private Button playButton;
	private String ip = "";

	public MPSelectionMenu(GameResources gm) {
		exitButton = new Button(new Vector2f(GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 210, 35),
				new Vector2f(200, 40));
		playButton = new Button(new Vector2f(GameResources.getInstance().getDisplay().getDisplayWidth() / 2f + 10, 35),
				new Vector2f(200, 40));
	}

	public void render() {
		VectorsRendering.renderWindow("Multiplayer", "Roboto-Bold", 20, 20,
				GameResources.getInstance().getDisplay().getDisplayWidth() - 40,
				GameResources.getInstance().getDisplay().getDisplayHeight() - 40);
		VectorsRendering.renderText("IP:  ", "Roboto-Regular",
				GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 170f,
				GameResources.getInstance().getDisplay().getDisplayHeight() / 2f, 30,
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
		while (Keyboard.next())
			ip = Keyboard.keyWritten(ip);
		VectorsRendering.renderSearchBox(ip, "Roboto-Regular", "Entypo",
				GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 150f,
				GameResources.getInstance().getDisplay().getDisplayHeight() / 2f - 10f, 300, 20);

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
