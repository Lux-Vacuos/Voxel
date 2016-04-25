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
import net.luxvacuos.voxel.client.ui.Slider;

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

	public OptionsMenu(GameResources gm) {
		drawDistanceSlider = new Slider(900 * VoxelVariables.XSCALE, 540 * VoxelVariables.YSCALE,
				315 * VoxelVariables.XSCALE, 80 * VoxelVariables.YSCALE);
		fovSlider = new Slider(900 * VoxelVariables.XSCALE, 440 * VoxelVariables.YSCALE, 315 * VoxelVariables.XSCALE,
				80 * VoxelVariables.YSCALE);
		exitButton = new Button(new Vector2f(530, 35), new Vector2f(230, 80));
		godraysButton = new Button(new Vector2f(40, 560), new Vector2f(230, 80));
		shadowsButton = new Button(new Vector2f(40, 460), new Vector2f(230, 80));
		dofButton = new Button(new Vector2f(40, 360), new Vector2f(230, 80));
		fxaaButton = new Button(new Vector2f(40, 260), new Vector2f(230, 80));
		motionBlurButton = new Button(new Vector2f(40, 160), new Vector2f(230, 80));

		reflectionsButton = new Button(new Vector2f(290, 560), new Vector2f(230, 80));
		parallaxButton = new Button(new Vector2f(290, 460), new Vector2f(230, 80));

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
		VectorsRendering.renderWindow("Options", "Roboto-Bold", 20 * VoxelVariables.XSCALE, 20 * VoxelVariables.YSCALE,
				1240 * VoxelVariables.XSCALE, 680 * VoxelVariables.YSCALE);

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
		VectorsRendering.renderLabel("Draw Distance: " + VoxelVariables.radius, "Roboto-Bold",
				970 * VoxelVariables.XSCALE, 90 * VoxelVariables.YSCALE, 315 * VoxelVariables.XSCALE,
				20 * VoxelVariables.YSCALE, 25f * VoxelVariables.YSCALE);
		int r = (int) (drawDistanceSlider.getPos() * 32f);
		if (r < 2)
			r = 2;
		VoxelVariables.radius = r;
		VectorsRendering.renderSlider(VoxelVariables.radius / 32f, 900 * VoxelVariables.XSCALE,
				100 * VoxelVariables.YSCALE, 315 * VoxelVariables.XSCALE, 80 * VoxelVariables.YSCALE);

		VectorsRendering.renderLabel("Field of View: " + VoxelVariables.FOV, "Roboto-Bold", 970 * VoxelVariables.XSCALE,
				190 * VoxelVariables.YSCALE, 315 * VoxelVariables.XSCALE, 20 * VoxelVariables.YSCALE,
				25f * VoxelVariables.YSCALE);
		int t = (int) (fovSlider.getPos() * 140f);
		if (t < 20)
			t = 20;
		else if (t > 140)
			t = 140;
		VoxelVariables.FOV = t;
		VectorsRendering.renderSlider(VoxelVariables.FOV / 140f, 900 * VoxelVariables.XSCALE,
				200 * VoxelVariables.YSCALE, 315 * VoxelVariables.XSCALE, 80 * VoxelVariables.YSCALE);
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
