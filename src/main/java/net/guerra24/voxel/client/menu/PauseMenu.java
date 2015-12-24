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

public class PauseMenu {
	private Button exitButton;
	private Button optionsButton;
	private float yScale, xScale;

	public PauseMenu(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		exitButton = new Button(new Vector2f(500 * xScale, 35 * yScale), new Vector2f(280 * xScale, 80 * yScale));
		optionsButton = new Button(new Vector2f(500 * xScale, 135 * yScale), new Vector2f(280 * xScale, 80 * yScale));
	}

	public void render() {
		MenuRendering.renderButton(null, "Back to Main Menu", "Roboto-Bold", 500 * xScale, 607 * yScale, 280 * xScale,
				80 * yScale, MenuRendering.rgba(255, 255, 255, 255, MenuRendering.colorA), exitButton.insideButton());
		MenuRendering.renderButton(null, "Options", "Roboto-Bold", 500 * xScale, 502 * yScale, 280 * xScale,
				80 * yScale, MenuRendering.rgba(255, 255, 255, 255, MenuRendering.colorA),
				optionsButton.insideButton());
	}

	public Button getExitButton() {
		return exitButton;
	}

	public Button getOptionsButton() {
		return optionsButton;
	}

}
