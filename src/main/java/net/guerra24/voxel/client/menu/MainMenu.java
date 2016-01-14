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

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.graphics.MenuRendering;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.universal.util.vector.Vector2f;

public class MainMenu {

	private Button playButton;
	private Button exitButton;
	private Button optionsButton;
	private Button newsRefreshButton;

	private WebRenderer webRenderer;

	private float xScale, yScale;

	public MainMenu(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		playButton = new Button(new Vector2f(177, 532), new Vector2f(215, 80), xScale, yScale);
		exitButton = new Button(new Vector2f(177, 224), new Vector2f(215, 80), xScale, yScale);
		optionsButton = new Button(new Vector2f(177, 376), new Vector2f(215, 80), xScale, yScale);
		newsRefreshButton = new Button(new Vector2f(1096, 627), new Vector2f(100, 40), xScale, yScale);
		webRenderer = new WebRenderer(VoxelVariables.web + "news/menu.webtag", 460 * xScale, 120 * yScale);
		webRenderer.update();
	}

	float b = 0;

	public void render() {
		playButton.render("Play");
		optionsButton.render("Options");
		exitButton.render("Exit");

		MenuRendering.renderText(
				"Voxel " + VoxelVariables.version + " " + VoxelVariables.state + " Build " + VoxelVariables.build,
				"Roboto-Bold", 0, 710 * yScale, 20, MenuRendering.rgba(255, 255, 255, 160, MenuRendering.colorA),
				MenuRendering.rgba(255, 255, 255, 160, MenuRendering.colorB));
		MenuRendering.renderWindow("Voxel News", "Roboto-Bold", 450 * xScale, 50 * yScale, 750 * xScale, 600 * yScale);
		webRenderer.render();

		newsRefreshButton.render("Reload", MenuRendering.rgba(80, 80, 80, 80, MenuRendering.colorA));

		MenuRendering.renderButton(null, "Reload", "Roboto-Bold", 1096 * xScale, 53 * yScale, 100 * xScale, 40 * yScale,
				MenuRendering.rgba(80, 80, 80, 80, MenuRendering.colorA), newsRefreshButton.insideButton());

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

}
