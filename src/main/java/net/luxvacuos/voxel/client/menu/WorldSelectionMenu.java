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

import java.io.File;

import net.luxvacuos.igl.IOUtil;
import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;

public class WorldSelectionMenu {

	private float xScale, yScale;

	private Button exitButton;
	private Button playButton;

	private String worldName;
	private long worldSize;

	private WorldGui[] worlds;
	private Button[] worldsButtons;
	private int worldsNumber;

	public WorldSelectionMenu(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		exitButton = new Button(new Vector2f(655, 30), new Vector2f(215, 80), xScale, yScale);
		playButton = new Button(new Vector2f(410, 30), new Vector2f(215, 80), xScale, yScale);
		// worldsNumber = new File(VoxelVariables.worldPath).list().length;
		worldsNumber = 5;
		worlds = new WorldGui[worldsNumber];
		for (int i = 0; i < worlds.length; i++) {
			worlds[i] = new WorldGui("World-" + i, "World-" + i,
					((IOUtil.size(new File(VoxelVariables.worldPath + "World-" + i).toPath()) / 1024) / 1024), false);
		}
		worldName = "";
		worldsButtons = new Button[worldsNumber];
		worldsButtons[0] = new Button(new Vector2f(35, 165), new Vector2f(245, 80), xScale, yScale);
		worldsButtons[1] = new Button(new Vector2f(35, 265), new Vector2f(245, 80), xScale, yScale);
		worldsButtons[2] = new Button(new Vector2f(35, 365), new Vector2f(245, 80), xScale, yScale);
		worldsButtons[3] = new Button(new Vector2f(35, 465), new Vector2f(245, 80), xScale, yScale);
		worldsButtons[4] = new Button(new Vector2f(35, 565), new Vector2f(245, 80), xScale, yScale);
	}

	public void render() {
		VectorsRendering.renderWindow("Worlds", "Roboto-Bold", 20 * xScale, 20 * yScale, 275 * xScale, 540 * yScale);
		VectorsRendering.renderWindow("World Info: " + worldName, "Roboto-Bold", 310 * xScale, 20 * yScale, 950 * xScale,
				540 * yScale);
		VectorsRendering.renderText("World Size : " + worldSize + "MB", "Roboto-Regular", 330 * xScale, 130 * yScale,
				30 * yScale, VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
		VectorsRendering.renderText("World Seed: ", "Roboto-Regular", 330 * xScale, 100 * yScale,
				30 * yScale, VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
		VectorsRendering.renderWindow(20 * xScale, 570 * yScale, 1240 * xScale, 130 * yScale);
		for (int i = 0; i < worlds.length; i++) {
			if (worlds[i].isSelected())
				worldsButtons[i].render(worlds[i].getToRender(),
						VectorsRendering.rgba(100, 255, 100, 255, VectorsRendering.colorA));
			else
				worldsButtons[i].render(worlds[i].getToRender(),
						VectorsRendering.rgba(255, 100, 100, 255, VectorsRendering.colorA));
		}
		exitButton.render("Back");
		playButton.render("Play");
	}

	public void update() {
		for (int i = 0; i < worlds.length; i++) {
			if (worlds[i].isSelected()) {
				worldName = worlds[i].getName();
				worldSize = worlds[i].getSize();
			}
			if (worldsButtons[i].pressed()) {
				for (int j = 0; j < worlds.length; j++) {
					worlds[j].setSelected(false);
				}
				worlds[i].setSelected(true);
			}
		}
	}

	public Button getExitButton() {
		return exitButton;
	}

	public Button getPlayButton() {
		return playButton;
	}

	public String getWorldName() {
		return worldName;
	}

}
