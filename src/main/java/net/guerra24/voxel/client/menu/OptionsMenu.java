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

public class OptionsMenu {

	private Button exitButton;
	private Button dofButton;
	private Button shadowsButton;
	private Button godraysButton;

	private Slider slider;

	private float xScale, yScale;

	public OptionsMenu(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		slider = new Slider(900 * xScale, 540 * yScale, 315 * xScale, 80 * yScale);
		exitButton = new Button(new Vector2f(530, 35), new Vector2f(215, 80), xScale, yScale);
		godraysButton = new Button(new Vector2f(32, 560), new Vector2f(215, 80), xScale, yScale);
		shadowsButton = new Button(new Vector2f(32, 460), new Vector2f(215, 80), xScale, yScale);
		dofButton = new Button(new Vector2f(32, 360), new Vector2f(215, 80), xScale, yScale);
		slider.setPos(VoxelVariables.radius / 32f);
	}

	public void update() {
		slider.setPos(VoxelVariables.radius / 32f);
		slider.update();
	}

	public void render() {
		MenuRendering.renderWindow("Options", "Roboto-Bold", 20 * xScale, 20 * yScale, 1240 * xScale, 680 * yScale);
		if (VoxelVariables.useVolumetricLight) {
			godraysButton.render("Light Rays: ON", MenuRendering.rgba(100, 255, 100, 255, MenuRendering.colorA));
		} else {
			godraysButton.render("Light Rays: OFF", MenuRendering.rgba(255, 100, 100, 255, MenuRendering.colorA));
		}
		if (VoxelVariables.useShadows) {
			shadowsButton.render("Shadows: ON", MenuRendering.rgba(100, 255, 100, 255, MenuRendering.colorA));
		} else {
			shadowsButton.render("Shadows: OFF", MenuRendering.rgba(255, 100, 100, 255, MenuRendering.colorA));
		}
		if (VoxelVariables.useDOF) {
			dofButton.render("DoF: ON", MenuRendering.rgba(100, 255, 100, 255, MenuRendering.colorA));
		} else {
			dofButton.render("DoF: OFF", MenuRendering.rgba(255, 100, 100, 255, MenuRendering.colorA));
		}
		exitButton.render("Back");
		MenuRendering.renderLabel("Draw Distance: " + VoxelVariables.radius, "Roboto-Bold", 970 * xScale, 90 * yScale,
				315 * xScale, 20 * yScale, 25f * yScale);
		int r = (int) (slider.getPos() * 32f);
		if (r < 2)
			r = 2;
		VoxelVariables.radius = r;
		MenuRendering.renderSlider(VoxelVariables.radius / 32f, 900 * xScale, 100 * yScale, 315 * xScale, 80 * yScale);
	}

	public Button getExitButton() {
		return exitButton;
	}

	public Button getDofButton() {
		return dofButton;
	}

	public Button getShadowsButton() {
		return shadowsButton;
	}

	public Button getGodraysButton() {
		return godraysButton;
	}

}
