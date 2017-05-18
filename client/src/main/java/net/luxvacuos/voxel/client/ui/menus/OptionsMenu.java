/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

package net.luxvacuos.voxel.client.ui.menus;

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;

import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering.ButtonStyle;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.Container;
import net.luxvacuos.voxel.client.ui.Direction;
import net.luxvacuos.voxel.client.ui.FlowLayout;
import net.luxvacuos.voxel.client.ui.RootComponentWindow;
import net.luxvacuos.voxel.client.ui.ScrollArea;
import net.luxvacuos.voxel.client.ui.Slider;
import net.luxvacuos.voxel.client.ui.Text;
import net.luxvacuos.voxel.client.ui.TitleBarButton;
import net.luxvacuos.voxel.client.ui.ToggleButton;
import net.luxvacuos.voxel.universal.core.CoreSubsystem;
import net.luxvacuos.voxel.universal.core.TaskManager;

public class OptionsMenu extends RootComponentWindow {

	private TitleBarButton backButton;

	public OptionsMenu(float x, float y, float w, float h) {
		super(x, y, w, h, "Options");
	}

	@Override
	public void initApp(Window window) {
		super.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);
		super.setResizable(false);

		backButton = new TitleBarButton(0, -1, 28, 28);
		backButton.setWindowAlignment(Alignment.LEFT_TOP);
		backButton.setAlignment(Alignment.RIGHT_BOTTOM);
		backButton.setColor("#646464C8");
		backButton.setHighlightColor("#FFFFFFC8");
		backButton.setStyle(ButtonStyle.LEFT_ARROW);
		backButton.setEnabled(false);

		super.getTitleBar().getLeft().addComponent(backButton);

		mainMenu(window);

		super.initApp(window);
	}

	private void mainMenu(Window window) {
		Button graphics = new Button(40, -40, 200, 40, "Graphics");
		graphics.setWindowAlignment(Alignment.LEFT_TOP);
		graphics.setAlignment(Alignment.RIGHT_BOTTOM);

		graphics.setOnButtonPress(() -> {
			TaskManager.addTask(() -> {
				super.disposeApp(window);
				graphicOptions();
				backButton.setOnButtonPress(() -> {
					TaskManager.addTask(() -> {
						super.disposeApp(window);
						backButton.setEnabled(false);
						mainMenu(window);
					});
				});
			});
		});

		Button wm = new Button(40, -100, 200, 40, "Window Manager");
		wm.setWindowAlignment(Alignment.LEFT_TOP);
		wm.setAlignment(Alignment.RIGHT_BOTTOM);

		wm.setOnButtonPress(() -> {
			TaskManager.addTask(() -> {
				super.disposeApp(window);
				wmOptions();
				backButton.setOnButtonPress(() -> {
					TaskManager.addTask(() -> {
						super.disposeApp(window);
						backButton.setEnabled(false);
						mainMenu(window);
					});
				});
			});
		});

		super.addComponent(graphics);
		super.addComponent(wm);
	}

	private void graphicOptions() {
		ToggleButton godraysButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/volumetricLight"));
		ToggleButton shadowsButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/shadows"));
		ToggleButton dofButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/dof"));
		ToggleButton fxaaButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/fxaa"));
		ToggleButton motionBlurButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/motionBlur"));
		ToggleButton reflectionsButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/reflections"));
		ToggleButton parallaxButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/parallax"));
		ToggleButton ambientOccButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/ambientOcclusion"));
		ToggleButton chromaticAberrationButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/chromaticAberration"));
		ToggleButton lensFlaresButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/lensFlares"));

		godraysButton.setWindowAlignment(Alignment.RIGHT);
		godraysButton.setAlignment(Alignment.RIGHT);
		shadowsButton.setWindowAlignment(Alignment.RIGHT);
		shadowsButton.setAlignment(Alignment.RIGHT);
		dofButton.setWindowAlignment(Alignment.RIGHT);
		dofButton.setAlignment(Alignment.RIGHT);
		fxaaButton.setWindowAlignment(Alignment.RIGHT);
		fxaaButton.setAlignment(Alignment.RIGHT);
		motionBlurButton.setWindowAlignment(Alignment.RIGHT);
		motionBlurButton.setAlignment(Alignment.RIGHT);
		reflectionsButton.setWindowAlignment(Alignment.RIGHT);
		reflectionsButton.setAlignment(Alignment.RIGHT);
		parallaxButton.setWindowAlignment(Alignment.RIGHT);
		parallaxButton.setAlignment(Alignment.RIGHT);
		ambientOccButton.setWindowAlignment(Alignment.RIGHT);
		ambientOccButton.setAlignment(Alignment.RIGHT);
		chromaticAberrationButton.setWindowAlignment(Alignment.RIGHT);
		chromaticAberrationButton.setAlignment(Alignment.RIGHT);
		lensFlaresButton.setWindowAlignment(Alignment.RIGHT);
		lensFlaresButton.setAlignment(Alignment.RIGHT);

		shadowsButton.setOnButtonPress(
				() -> REGISTRY.register("/Voxel/Settings/Graphics/shadows", shadowsButton.getStatus()));
		dofButton.setOnButtonPress(() -> REGISTRY.register("/Voxel/Settings/Graphics/dof", dofButton.getStatus()));
		godraysButton.setOnButtonPress(
				() -> REGISTRY.register("/Voxel/Settings/Graphics/volumetricLight", godraysButton.getStatus()));
		fxaaButton.setOnButtonPress(() -> REGISTRY.register("/Voxel/Settings/Graphics/fxaa", fxaaButton.getStatus()));
		parallaxButton.setOnButtonPress(
				() -> REGISTRY.register("/Voxel/Settings/Graphics/parallax", parallaxButton.getStatus()));
		motionBlurButton.setOnButtonPress(
				() -> REGISTRY.register("/Voxel/Settings/Graphics/motionBlur", motionBlurButton.getStatus()));
		reflectionsButton.setOnButtonPress(
				() -> REGISTRY.register("/Voxel/Settings/Graphics/reflections", reflectionsButton.getStatus()));
		ambientOccButton.setOnButtonPress(
				() -> REGISTRY.register("/Voxel/Settings/Graphics/ambientOcclusion", ambientOccButton.getStatus()));
		chromaticAberrationButton.setOnButtonPress(() -> REGISTRY
				.register("/Voxel/Settings/Graphics/chromaticAberration", chromaticAberrationButton.getStatus()));
		lensFlaresButton.setOnButtonPress(
				() -> REGISTRY.register("/Voxel/Settings/Graphics/lensFlares", lensFlaresButton.getStatus()));

		Text godText = new Text("Volumetric Light", 20, 0);
		godText.setWindowAlignment(Alignment.LEFT);
		Text shadowsText = new Text("Shadows", 20, 0);
		shadowsText.setWindowAlignment(Alignment.LEFT);
		Text dofText = new Text("Depht of Field", 20, 0);
		dofText.setWindowAlignment(Alignment.LEFT);
		Text fxaaText = new Text("FXAA", 20, 0);
		fxaaText.setWindowAlignment(Alignment.LEFT);
		Text motionBlurText = new Text("Motion Blur", 20, 0);
		motionBlurText.setWindowAlignment(Alignment.LEFT);
		Text reflectionsText = new Text("Reflections", 20, 0);
		reflectionsText.setWindowAlignment(Alignment.LEFT);
		Text parallaxText = new Text("Parallax Mapping", 20, 0);
		parallaxText.setWindowAlignment(Alignment.LEFT);
		Text ambientOccText = new Text("Ambient Occlusion", 20, 0);
		ambientOccText.setWindowAlignment(Alignment.LEFT);
		Text chromaticAberrationText = new Text("Chromatic Aberration", 20, 0);
		chromaticAberrationText.setWindowAlignment(Alignment.LEFT);
		Text lensFlaresText = new Text("Lens Flares", 20, 0);
		lensFlaresText.setWindowAlignment(Alignment.LEFT);

		ScrollArea area = new ScrollArea(0, 0, w, h, 0, 0);
		area.setLayout(new FlowLayout(Direction.DOWN, 10, 10));

		Container godrays = new Container(0, 0, w, 30);
		godrays.setWindowAlignment(Alignment.RIGHT_TOP);
		godrays.setAlignment(Alignment.LEFT_BOTTOM);
		Container shadows = new Container(0, 0, w, 30);
		shadows.setWindowAlignment(Alignment.RIGHT_TOP);
		shadows.setAlignment(Alignment.LEFT_BOTTOM);
		Container dof = new Container(0, 0, w, 30);
		dof.setWindowAlignment(Alignment.RIGHT_TOP);
		dof.setAlignment(Alignment.LEFT_BOTTOM);
		Container fxaa = new Container(0, 0, w, 30);
		fxaa.setWindowAlignment(Alignment.RIGHT_TOP);
		fxaa.setAlignment(Alignment.LEFT_BOTTOM);
		Container motionBlur = new Container(0, 0, w, 30);
		motionBlur.setWindowAlignment(Alignment.RIGHT_TOP);
		motionBlur.setAlignment(Alignment.LEFT_BOTTOM);
		Container reflections = new Container(0, 0, w, 30);
		reflections.setWindowAlignment(Alignment.RIGHT_TOP);
		reflections.setAlignment(Alignment.LEFT_BOTTOM);
		Container parallax = new Container(0, 0, w, 30);
		parallax.setWindowAlignment(Alignment.RIGHT_TOP);
		parallax.setAlignment(Alignment.LEFT_BOTTOM);
		Container occlusion = new Container(0, 0, w, 30);
		occlusion.setWindowAlignment(Alignment.RIGHT_TOP);
		occlusion.setAlignment(Alignment.LEFT_BOTTOM);
		Container aberration = new Container(0, 0, w, 30);
		aberration.setWindowAlignment(Alignment.RIGHT_TOP);
		aberration.setAlignment(Alignment.LEFT_BOTTOM);
		Container lens = new Container(0, 0, w, 30);
		lens.setWindowAlignment(Alignment.RIGHT_TOP);
		lens.setAlignment(Alignment.LEFT_BOTTOM);

		godrays.addComponent(godraysButton);
		godrays.addComponent(godText);
		shadows.addComponent(shadowsButton);
		shadows.addComponent(shadowsText);
		dof.addComponent(dofButton);
		dof.addComponent(dofText);
		fxaa.addComponent(fxaaButton);
		fxaa.addComponent(fxaaText);
		motionBlur.addComponent(motionBlurButton);
		motionBlur.addComponent(motionBlurText);
		reflections.addComponent(reflectionsButton);
		reflections.addComponent(reflectionsText);
		parallax.addComponent(parallaxButton);
		parallax.addComponent(parallaxText);
		occlusion.addComponent(ambientOccButton);
		occlusion.addComponent(ambientOccText);
		aberration.addComponent(chromaticAberrationButton);
		aberration.addComponent(chromaticAberrationText);
		lens.addComponent(lensFlaresButton);
		lens.addComponent(lensFlaresText);

		area.addComponent(godrays);
		area.addComponent(shadows);
		area.addComponent(dof);
		area.addComponent(fxaa);
		area.addComponent(motionBlur);
		area.addComponent(reflections);
		area.addComponent(parallax);
		area.addComponent(occlusion);
		area.addComponent(aberration);
		area.addComponent(lens);

		super.addComponent(area);

		backButton.setEnabled(true);
	}

	private void wmOptions() {
		float border = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/borderSize");
		Text wmBorderText = new Text("Window Border: " + border, 20, 0);
		Slider wmBorder = new Slider(-56, 0, 200, 20, border / 40f);

		wmBorderText.setWindowAlignment(Alignment.LEFT);

		wmBorder.setPrecision(40f);
		wmBorder.useCustomPrecision(true);
		wmBorder.setAlignment(Alignment.RIGHT);
		wmBorder.setWindowAlignment(Alignment.RIGHT);

		wmBorder.setOnPress(() -> {
			float val = wmBorder.getPosition() * 40f;
			REGISTRY.register("/Voxel/Settings/WindowManager/borderSize", val);
			wmBorderText.setText("Window Border: " + val);
		});

		Container borderC = new Container(0, 0, w, 20);
		borderC.setWindowAlignment(Alignment.RIGHT_TOP);
		borderC.setAlignment(Alignment.LEFT_BOTTOM);
		borderC.addComponent(wmBorderText);
		borderC.addComponent(wmBorder);

		float scroll = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/scrollBarSize");
		Text wmScrollText = new Text("Scroll Bar Size: " + scroll, 20, 0);
		Slider wmScroll = new Slider(-56, 0, 200, 20, scroll / 40f);

		wmScrollText.setWindowAlignment(Alignment.LEFT);

		wmScroll.setPrecision(40f);
		wmScroll.useCustomPrecision(true);
		wmScroll.setAlignment(Alignment.RIGHT);
		wmScroll.setWindowAlignment(Alignment.RIGHT);

		wmScroll.setOnPress(() -> {
			float val = wmScroll.getPosition() * 40f;
			REGISTRY.register("/Voxel/Settings/WindowManager/scrollBarSize", val);
			wmScrollText.setText("Scroll Bar Size: " + val);
		});

		Container scrollC = new Container(0, 0, w, 20);
		scrollC.setWindowAlignment(Alignment.RIGHT_TOP);
		scrollC.setAlignment(Alignment.LEFT_BOTTOM);
		scrollC.addComponent(wmScrollText);
		scrollC.addComponent(wmScroll);

		float title = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/titleBarHeight");
		Text wmTitleText = new Text("Title Bar Size: " + title, 20, 0);
		Slider wmTitle = new Slider(-56, 0, 200, 20, title / 40f);

		wmTitleText.setWindowAlignment(Alignment.LEFT);

		wmTitle.setPrecision(40f);
		wmTitle.useCustomPrecision(true);
		wmTitle.setAlignment(Alignment.RIGHT);
		wmTitle.setWindowAlignment(Alignment.RIGHT);

		wmTitle.setOnPress(() -> {
			float val = wmTitle.getPosition() * 40f;
			REGISTRY.register("/Voxel/Settings/WindowManager/titleBarHeight", val);
			wmTitleText.setText("Title Bar Size: " + val);
		});

		Container titleC = new Container(0, 0, w, 20);
		titleC.setWindowAlignment(Alignment.RIGHT_TOP);
		titleC.setAlignment(Alignment.LEFT_BOTTOM);
		titleC.addComponent(wmTitleText);
		titleC.addComponent(wmTitle);

		ToggleButton titleBorderButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/titleBarBorder"));

		titleBorderButton.setWindowAlignment(Alignment.RIGHT);
		titleBorderButton.setAlignment(Alignment.RIGHT);

		titleBorderButton.setOnButtonPress(
				() -> REGISTRY.register("/Voxel/Settings/WindowManager/titleBarBorder", titleBorderButton.getStatus()));

		Text titleBorderText = new Text("Title Bar Border", 20, 0);
		titleBorderText.setWindowAlignment(Alignment.LEFT);

		Container titleBorder = new Container(0, 0, w, 30);
		titleBorder.setWindowAlignment(Alignment.RIGHT_TOP);
		titleBorder.setAlignment(Alignment.LEFT_BOTTOM);

		titleBorder.addComponent(titleBorderButton);
		titleBorder.addComponent(titleBorderText);

		ScrollArea area = new ScrollArea(0, 0, w, h, 0, 0);
		area.setLayout(new FlowLayout(Direction.DOWN, 10, 10));

		area.addComponent(borderC);
		area.addComponent(scrollC);
		area.addComponent(titleC);
		area.addComponent(titleBorder);

		super.addComponent(area);

		backButton.setEnabled(true);
	}

	@Override
	public void onClose() {
		CoreSubsystem.getGameSettings().update();
		CoreSubsystem.getGameSettings().save();
	}

}
