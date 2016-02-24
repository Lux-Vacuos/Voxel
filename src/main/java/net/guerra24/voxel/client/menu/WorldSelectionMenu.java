/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.guerra24.voxel.client.menu;

import java.io.File;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.util.IOUtil;
import net.guerra24.voxel.universal.util.vector.Vector2f;

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
		exitButton = new Button(new Vector2f(1035, 30), new Vector2f(215, 80), xScale, yScale);
		playButton = new Button(new Vector2f(800, 30), new Vector2f(215, 80), xScale, yScale);
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
