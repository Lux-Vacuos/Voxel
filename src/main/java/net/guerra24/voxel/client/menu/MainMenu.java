/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
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

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.graphics.MenuRendering;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.universal.util.vector.Vector2f;

public class MainMenu {

	private Button playButton;
	private Button exitButton;
	private Button optionsButton;

	private float xScale, yScale;

	public MainMenu(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		playButton = new Button(new Vector2f(177 * xScale, 532 * yScale), new Vector2f(215 * xScale, 80 * yScale));
		exitButton = new Button(new Vector2f(177 * xScale, 224 * yScale), new Vector2f(215 * xScale, 80 * yScale));
		optionsButton = new Button(new Vector2f(177 * xScale, 376 * yScale), new Vector2f(215 * xScale, 80 * yScale));
	}

	public void render() {
		MenuRendering.renderButton(null, "Play", "Roboto-Bold", 170 * xScale, 112 * yScale, 215 * xScale, 80 * yScale,
				MenuRendering.rgba(255, 255, 255, 255, MenuRendering.colorA), playButton.insideButton());
		MenuRendering.renderButton(null, "Options", "Roboto-Bold", 170 * xScale, 270 * yScale, 215 * xScale,
				80 * yScale, MenuRendering.rgba(255, 255, 255, 255, MenuRendering.colorA),
				optionsButton.insideButton());
		MenuRendering.renderButton(null, "Exit", "Roboto-Bold", 170 * xScale, 425 * yScale, 215 * xScale, 80 * yScale,
				MenuRendering.rgba(255, 255, 255, 255, MenuRendering.colorA), exitButton.insideButton());
		MenuRendering.renderText(
				"Voxel " + VoxelVariables.version + " " + VoxelVariables.state + " Build " + VoxelVariables.build,
				"Roboto-Bold", 0, 710 * yScale, 20);
		MenuRendering.renderWindow("Voxel Develop News", "Roboto-Bold", 450 * xScale, 50 * yScale, 750 * xScale, 600 * yScale);
		MenuRendering.renderText("- Voxel 0.0.9 ALPHA DevLog", "Roboto-Bold", 460 * xScale, 120 * yScale, 25);
		MenuRendering.renderText("NanoVG crashes are delaying the official launch of the update, Im searching for fixes.", "Roboto-Bold", 460 * xScale, 150 * yScale, 15);
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

}
