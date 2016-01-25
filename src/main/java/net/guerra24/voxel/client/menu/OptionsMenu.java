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
import net.guerra24.voxel.client.graphics.VectorsRendering;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.universal.util.vector.Vector2f;

public class OptionsMenu {

	private Button exitButton;
	private Button dofButton;
	private Button shadowsButton;
	private Button godraysButton;
	private Button fxaaButton;
	private Button motionBlurButton;
	private Button reflectionsButton;
	private Button parallaxButton;

	private Slider drawDistanceSlider;
	private Slider fovSlider;

	private float xScale, yScale;

	public OptionsMenu(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		drawDistanceSlider = new Slider(900 * xScale, 540 * yScale, 315 * xScale, 80 * yScale);
		fovSlider = new Slider(900 * xScale, 440 * yScale, 315 * xScale, 80 * yScale);
		exitButton = new Button(new Vector2f(530, 35), new Vector2f(230, 80), xScale, yScale);
		godraysButton = new Button(new Vector2f(40, 560), new Vector2f(230, 80), xScale, yScale);
		shadowsButton = new Button(new Vector2f(40, 460), new Vector2f(230, 80), xScale, yScale);
		dofButton = new Button(new Vector2f(40, 360), new Vector2f(230, 80), xScale, yScale);
		fxaaButton = new Button(new Vector2f(40, 260), new Vector2f(230, 80), xScale, yScale);
		motionBlurButton = new Button(new Vector2f(40, 160), new Vector2f(230, 80), xScale, yScale);

		reflectionsButton = new Button(new Vector2f(290, 560), new Vector2f(230, 80), xScale, yScale);
		parallaxButton = new Button(new Vector2f(290, 460), new Vector2f(230, 80), xScale, yScale);

		drawDistanceSlider.setPos(VoxelVariables.radius / 32f);
		fovSlider.setPos(VoxelVariables.FOV / 140f);
	}

	public void update() {
		drawDistanceSlider.setPos(VoxelVariables.radius / 32f);
		drawDistanceSlider.update();
		fovSlider.setPos(VoxelVariables.FOV / 140f);
		fovSlider.update();
	}

	public void render() {
		VectorsRendering.renderWindow("Options", "Roboto-Bold", 20 * xScale, 20 * yScale, 1240 * xScale, 680 * yScale);

		if (VoxelVariables.useVolumetricLight)
			godraysButton.render("Light Rays: ON", VectorsRendering.rgba(100, 255, 100, 255, VectorsRendering.colorA));
		else
			godraysButton.render("Light Rays: OFF", VectorsRendering.rgba(255, 100, 100, 255, VectorsRendering.colorA));

		if (VoxelVariables.useShadows)
			shadowsButton.render("Shadows: ON", VectorsRendering.rgba(100, 255, 100, 255, VectorsRendering.colorA));
		else
			shadowsButton.render("Shadows: OFF", VectorsRendering.rgba(255, 100, 100, 255, VectorsRendering.colorA));

		if (VoxelVariables.useDOF)
			dofButton.render("DoF: ON", VectorsRendering.rgba(100, 255, 100, 255, VectorsRendering.colorA));
		else
			dofButton.render("DoF: OFF", VectorsRendering.rgba(255, 100, 100, 255, VectorsRendering.colorA));

		if (VoxelVariables.useFXAA)
			fxaaButton.render("FXAA: ON", VectorsRendering.rgba(100, 255, 100, 255, VectorsRendering.colorA));
		else
			fxaaButton.render("FXAA: OFF", VectorsRendering.rgba(255, 100, 100, 255, VectorsRendering.colorA));

		if (VoxelVariables.useMotionBlur)
			motionBlurButton.render("Motion Blur: ON",
					VectorsRendering.rgba(100, 255, 100, 255, VectorsRendering.colorA));
		else
			motionBlurButton.render("Motion Blur: OFF",
					VectorsRendering.rgba(255, 100, 100, 255, VectorsRendering.colorA));

		if (VoxelVariables.useReflections)
			reflectionsButton.render("Reflections: ON",
					VectorsRendering.rgba(100, 255, 100, 255, VectorsRendering.colorA));
		else
			reflectionsButton.render("Reflections: OFF",
					VectorsRendering.rgba(255, 100, 100, 255, VectorsRendering.colorA));

		if (VoxelVariables.useParallax)
			parallaxButton.render("Parallax: ON", VectorsRendering.rgba(100, 255, 100, 255, VectorsRendering.colorA));
		else
			parallaxButton.render("Parallax: OFF", VectorsRendering.rgba(255, 100, 100, 255, VectorsRendering.colorA));

		exitButton.render("Back");
		VectorsRendering.renderLabel("Draw Distance: " + VoxelVariables.radius, "Roboto-Bold", 970 * xScale,
				90 * yScale, 315 * xScale, 20 * yScale, 25f * yScale);
		int r = (int) (drawDistanceSlider.getPos() * 32f);
		if (r < 2)
			r = 2;
		VoxelVariables.radius = r;
		VectorsRendering.renderSlider(VoxelVariables.radius / 32f, 900 * xScale, 100 * yScale, 315 * xScale,
				80 * yScale);

		VectorsRendering.renderLabel("Field of View: " + VoxelVariables.FOV, "Roboto-Bold", 970 * xScale, 190 * yScale,
				315 * xScale, 20 * yScale, 25f * yScale);
		int t = (int) (fovSlider.getPos() * 140f);
		if (t < 20)
			t = 20;
		else if (t > 140)
			t = 140;
		VoxelVariables.FOV = t;
		VectorsRendering.renderSlider(VoxelVariables.FOV / 140f, 900 * xScale, 200 * yScale, 315 * xScale, 80 * yScale);
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

	public Button getFxaaButton() {
		return fxaaButton;
	}

	public Button getMotionBlurButton() {
		return motionBlurButton;
	}

	public Button getParallaxButton() {
		return parallaxButton;
	}

	public Button getReflectionsButton() {
		return reflectionsButton;
	}

}
