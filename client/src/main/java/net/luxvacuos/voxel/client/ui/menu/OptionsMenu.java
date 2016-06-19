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

public class OptionsMenu {

	private Button exitButton;
	private Button dofButton;
	private Button shadowsButton;
	private Button godraysButton;
	private Button fxaaButton;
	private Button motionBlurButton;
	private Button reflectionsButton;
	private Button parallaxButton;

	public OptionsMenu(GameResources gm) {
		exitButton = new Button(new Vector2f(GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 100, 35),
				new Vector2f(200, 40));
		godraysButton = new Button(new Vector2f(40, GameResources.getInstance().getDisplay().getDisplayHeight() - 110),
				new Vector2f(200, 40));
		shadowsButton = new Button(new Vector2f(40, GameResources.getInstance().getDisplay().getDisplayHeight() - 170),
				new Vector2f(200, 40));
		dofButton = new Button(new Vector2f(40, GameResources.getInstance().getDisplay().getDisplayHeight() - 230),
				new Vector2f(200, 40));
		fxaaButton = new Button(new Vector2f(40, GameResources.getInstance().getDisplay().getDisplayHeight() - 290),
				new Vector2f(200, 40));
		motionBlurButton = new Button(
				new Vector2f(40, GameResources.getInstance().getDisplay().getDisplayHeight() - 350),
				new Vector2f(200, 40));

		reflectionsButton = new Button(
				new Vector2f(260, GameResources.getInstance().getDisplay().getDisplayHeight() - 110),
				new Vector2f(200, 40));
		parallaxButton = new Button(
				new Vector2f(260, GameResources.getInstance().getDisplay().getDisplayHeight() - 170),
				new Vector2f(200, 40));
	}

	public void update() {
	}

	public void render() {
		VectorsRendering.renderWindow("Options", "Roboto-Bold", 20, 20,
				GameResources.getInstance().getDisplay().getDisplayWidth() - 40,
				GameResources.getInstance().getDisplay().getDisplayHeight() - 40);

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
