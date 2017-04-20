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
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_TOP;

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering.ButtonStyle;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.RootComponent;
import net.luxvacuos.voxel.client.ui.ScrollArea;
import net.luxvacuos.voxel.client.ui.Slider;
import net.luxvacuos.voxel.client.ui.Text;
import net.luxvacuos.voxel.client.ui.TitleBarButton;
import net.luxvacuos.voxel.client.ui.ToggleButton;
import net.luxvacuos.voxel.universal.core.TaskManager;

public class OptionsMenu extends RootComponent {

	private TitleBarButton backButton;

	public OptionsMenu(float x, float y, float w, float h) {
		super(x, y, w, h, "Options");
	}

	@Override
	public void initApp(Window window) {
		super.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);
		super.setResizable(false);

		backButton = new TitleBarButton(0, -1, 28, 28);
		backButton.setAlignment(Alignment.RIGHT_BOTTOM);
		backButton.setWindowAlignment(Alignment.LEFT_TOP);
		backButton.setColor("#646464C8");
		backButton.setHighlightColor("#FFFFFFC8");
		backButton.setStyle(ButtonStyle.LEFT_ARROW);
		backButton.setEnabled(false);

		super.getTitleBar().addComponent(backButton);

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
		ToggleButton godraysButton = new ToggleButton(-50, -10, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/volumetricLight"));
		ToggleButton shadowsButton = new ToggleButton(-50, -50, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/shadows"));
		ToggleButton dofButton = new ToggleButton(-50, -90, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/dof"));
		ToggleButton fxaaButton = new ToggleButton(-50, -130, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/fxaa"));
		ToggleButton motionBlurButton = new ToggleButton(-50, -170, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/motionBlur"));
		ToggleButton reflectionsButton = new ToggleButton(-50, -210, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/reflections"));
		ToggleButton parallaxButton = new ToggleButton(-50, -250, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/parallax"));
		ToggleButton ambientOccButton = new ToggleButton(-50, -290, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/ambientOcclusion"));
		ToggleButton chromaticAberrationButton = new ToggleButton(-50, -330, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/chromaticAberration"));
		ToggleButton lensFlaresButton = new ToggleButton(-50, -370, 80, 30,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/lensFlares"));

		godraysButton.setWindowAlignment(Alignment.RIGHT_TOP);
		godraysButton.setAlignment(Alignment.LEFT_BOTTOM);
		shadowsButton.setWindowAlignment(Alignment.RIGHT_TOP);
		shadowsButton.setAlignment(Alignment.LEFT_BOTTOM);
		dofButton.setWindowAlignment(Alignment.RIGHT_TOP);
		dofButton.setAlignment(Alignment.LEFT_BOTTOM);
		fxaaButton.setWindowAlignment(Alignment.RIGHT_TOP);
		fxaaButton.setAlignment(Alignment.LEFT_BOTTOM);
		motionBlurButton.setWindowAlignment(Alignment.RIGHT_TOP);
		motionBlurButton.setAlignment(Alignment.LEFT_BOTTOM);
		reflectionsButton.setWindowAlignment(Alignment.RIGHT_TOP);
		reflectionsButton.setAlignment(Alignment.LEFT_BOTTOM);
		parallaxButton.setWindowAlignment(Alignment.RIGHT_TOP);
		parallaxButton.setAlignment(Alignment.LEFT_BOTTOM);
		ambientOccButton.setWindowAlignment(Alignment.RIGHT_TOP);
		ambientOccButton.setAlignment(Alignment.LEFT_BOTTOM);
		chromaticAberrationButton.setWindowAlignment(Alignment.RIGHT_TOP);
		chromaticAberrationButton.setAlignment(Alignment.LEFT_BOTTOM);
		lensFlaresButton.setWindowAlignment(Alignment.RIGHT_TOP);
		lensFlaresButton.setAlignment(Alignment.LEFT_BOTTOM);

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
		
		Text godText = new Text("Volumetric Light", 20, -25);
		godText.setWindowAlignment(Alignment.LEFT_TOP);
		Text shadowsText = new Text("Shadows", 20, -65);
		shadowsText.setWindowAlignment(Alignment.LEFT_TOP);
		Text dofText = new Text("Depht of Field", 20, -105);
		dofText.setWindowAlignment(Alignment.LEFT_TOP);
		Text fxaaText = new Text("FXAA", 20, -145);
		fxaaText.setWindowAlignment(Alignment.LEFT_TOP);
		Text motionBlurText = new Text("Motion Blur", 20, -185);
		motionBlurText.setWindowAlignment(Alignment.LEFT_TOP);
		Text reflectionsText = new Text("Reflections", 20, -225);
		reflectionsText.setWindowAlignment(Alignment.LEFT_TOP);
		Text parallaxText = new Text("Parallax Mapping", 20, -265);
		parallaxText.setWindowAlignment(Alignment.LEFT_TOP);
		Text ambientOccText = new Text("Ambient Occlusion", 20, -305);
		ambientOccText.setWindowAlignment(Alignment.LEFT_TOP);
		Text chromaticAberrationText = new Text("Chromatic Aberration", 20, -345);
		chromaticAberrationText.setWindowAlignment(Alignment.LEFT_TOP);
		Text lensFlaresText = new Text("Lens Flares", 20, -385);
		lensFlaresText.setWindowAlignment(Alignment.LEFT_TOP);
		
		ScrollArea area = new ScrollArea(0, 0, w, h, 0, 400);
		
		area.addComponent(godraysButton);
		area.addComponent(shadowsButton);
		area.addComponent(dofButton);
		area.addComponent(fxaaButton);
		area.addComponent(motionBlurButton);
		area.addComponent(reflectionsButton);
		area.addComponent(parallaxButton);
		area.addComponent(ambientOccButton);
		area.addComponent(chromaticAberrationButton);
		area.addComponent(lensFlaresButton);
		
		area.addComponent(godText);
		area.addComponent(shadowsText);
		area.addComponent(dofText);
		area.addComponent(fxaaText);
		area.addComponent(motionBlurText);
		area.addComponent(reflectionsText);
		area.addComponent(parallaxText);
		area.addComponent(ambientOccText);
		area.addComponent(chromaticAberrationText);
		area.addComponent(lensFlaresText);
		
		super.addComponent(area);
		
		backButton.setEnabled(true);
	}

	private void wmOptions() {
		float border = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/borderSize");
		Text wmBorderText = new Text("Window Border: " + border, 40, -40);
		Slider wmBorder = new Slider(40, -60, 200, 20, border / 40f);

		wmBorderText.setWindowAlignment(Alignment.LEFT_TOP);
		wmBorderText.setFontSize(20);
		wmBorderText.setAlign(NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
		wmBorder.setWindowAlignment(Alignment.LEFT_TOP);
		wmBorder.setAlignment(Alignment.RIGHT_BOTTOM);
		wmBorder.setPrecision(40f);
		wmBorder.useCustomPrecision(true);

		wmBorder.setOnPress(() -> {
			float val = wmBorder.getPosition() * 40f;
			REGISTRY.register("/Voxel/Settings/WindowManager/borderSize", val);
			wmBorderText.setText("Window Border: " + val);
		});

		super.addComponent(wmBorderText);
		super.addComponent(wmBorder);
		backButton.setEnabled(true);
	}

	@Override
	public void onClose() {
		ClientInternalSubsystem.getInstance().getGameSettings().update();
		ClientInternalSubsystem.getInstance().getGameSettings().save();
	}

}
