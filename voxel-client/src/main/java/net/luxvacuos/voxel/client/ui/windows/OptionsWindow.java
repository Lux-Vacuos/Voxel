/*
 * This file is part of Light Engine
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

package net.luxvacuos.voxel.client.ui.windows;

import static net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem.LANG;
import static net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem.REGISTRY;

import java.util.ArrayList;
import java.util.Arrays;

import net.luxvacuos.lightengine.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.lightengine.client.rendering.nanovg.WindowMessage;
import net.luxvacuos.lightengine.client.rendering.nanovg.themes.Theme.ButtonStyle;
import net.luxvacuos.lightengine.client.rendering.nanovg.themes.ThemeManager;
import net.luxvacuos.lightengine.client.ui.Alignment;
import net.luxvacuos.lightengine.client.ui.Button;
import net.luxvacuos.lightengine.client.ui.ComponentWindow;
import net.luxvacuos.lightengine.client.ui.Container;
import net.luxvacuos.lightengine.client.ui.Direction;
import net.luxvacuos.lightengine.client.ui.DropDown;
import net.luxvacuos.lightengine.client.ui.EditBox;
import net.luxvacuos.lightengine.client.ui.FlowLayout;
import net.luxvacuos.lightengine.client.ui.Notification;
import net.luxvacuos.lightengine.client.ui.ScrollArea;
import net.luxvacuos.lightengine.client.ui.Slider;
import net.luxvacuos.lightengine.client.ui.Text;
import net.luxvacuos.lightengine.client.ui.TitleBarButton;
import net.luxvacuos.lightengine.client.ui.ToggleButton;
import net.luxvacuos.lightengine.universal.core.TaskManager;
import net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem;
import net.luxvacuos.lightengine.universal.core.subsystems.EventSubsystem;
import net.luxvacuos.lightengine.universal.util.registry.Key;

public class OptionsWindow extends ComponentWindow {

	private TitleBarButton backButton;

	public OptionsWindow() {
		super((int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/width")) / 2 - 420,
				(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height")) / 2 + 300, 840, 600,
				LANG.getRegistryItem("voxel.optionswindow.name"));
	}

	@Override
	public void initApp() {
		super.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);

		backButton = new TitleBarButton(0, 0, 28, 28);
		backButton.setWindowAlignment(Alignment.LEFT_TOP);
		backButton.setAlignment(Alignment.RIGHT_BOTTOM);
		backButton.setStyle(ButtonStyle.LEFT_ARROW);
		backButton.setEnabled(false);

		super.getTitleBar().getLeft().addComponent(backButton);

		mainMenu();
		super.initApp();
	}

	private void mainMenu() {
		Button graphics = new Button(40, -40, 200, 40, LANG.getRegistryItem("voxel.optionswindow.btngraphics"));
		graphics.setWindowAlignment(Alignment.LEFT_TOP);
		graphics.setAlignment(Alignment.RIGHT_BOTTOM);

		graphics.setOnButtonPress(() -> {
			TaskManager.tm.addTaskRenderThread(() -> {
				super.disposeApp();
				graphicOptions();
				super.initApp();
				backButton.setOnButtonPress(() -> {
					TaskManager.tm.addTaskRenderThread(() -> {
						super.disposeApp();
						backButton.setEnabled(false);
						mainMenu();
						super.initApp();
					});
				});
			});
		});

		Button wm = new Button(40, -100, 200, 40, LANG.getRegistryItem("voxel.optionswindow.btnwm"));
		wm.setWindowAlignment(Alignment.LEFT_TOP);
		wm.setAlignment(Alignment.RIGHT_BOTTOM);

		wm.setOnButtonPress(() -> {
			TaskManager.tm.addTaskRenderThread(() -> {
				super.disposeApp();
				wmOptions();
				super.initApp();
				backButton.setOnButtonPress(() -> {
					TaskManager.tm.addTaskRenderThread(() -> {
						super.disposeApp();
						backButton.setEnabled(false);
						mainMenu();
						super.initApp();
					});
				});
			});
		});

		super.addComponent(graphics);
		super.addComponent(wm);
	}

	private void graphicOptions() {
		ToggleButton godraysButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Graphics/volumetricLight")));
		ToggleButton shadowsButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Graphics/shadows")));
		ToggleButton dofButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Graphics/dof")));
		ToggleButton fxaaButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Graphics/fxaa")));
		ToggleButton motionBlurButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Graphics/motionBlur")));
		ToggleButton reflectionsButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Graphics/reflections")));
		ToggleButton parallaxButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Graphics/parallax")));
		ToggleButton ambientOccButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Graphics/ambientOcclusion")));
		ToggleButton chromaticAberrationButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Graphics/chromaticAberration")));
		ToggleButton lensFlaresButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Graphics/lensFlares")));
		DropDown<Integer> shadowResDropdown = new DropDown<>(-50, 0, 180, 30,
				(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Graphics/shadowsResolution")),
				Arrays.asList(128, 256, 512, 1024, 2048, 4096));
		EditBox shadowDistance = new EditBox(-50, 0, 180, 30, Integer.toString(
				(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/Graphics/shadowsDrawDistance"))));
		EditBox chunkDistance = new EditBox(-50, 0, 180, 30, Integer.toString(
				(int) REGISTRY.getRegistryItem(new Key("/Voxel/Settings/World/chunkRadius"))));

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
		shadowResDropdown.setWindowAlignment(Alignment.RIGHT);
		shadowResDropdown.setAlignment(Alignment.RIGHT);
		shadowDistance.setWindowAlignment(Alignment.RIGHT);
		shadowDistance.setAlignment(Alignment.RIGHT);
		chunkDistance.setWindowAlignment(Alignment.RIGHT);
		chunkDistance.setAlignment(Alignment.RIGHT);

		shadowsButton.setOnButtonPress(
				() -> REGISTRY.register(new Key("/Light Engine/Settings/Graphics/shadows"), shadowsButton.getStatus()));
		dofButton.setOnButtonPress(
				() -> REGISTRY.register(new Key("/Light Engine/Settings/Graphics/dof"), dofButton.getStatus()));
		godraysButton.setOnButtonPress(() -> REGISTRY
				.register(new Key("/Light Engine/Settings/Graphics/volumetricLight"), godraysButton.getStatus()));
		fxaaButton.setOnButtonPress(
				() -> REGISTRY.register(new Key("/Light Engine/Settings/Graphics/fxaa"), fxaaButton.getStatus()));
		parallaxButton.setOnButtonPress(() -> REGISTRY.register(new Key("/Light Engine/Settings/Graphics/parallax"),
				parallaxButton.getStatus()));
		motionBlurButton.setOnButtonPress(() -> REGISTRY.register(new Key("/Light Engine/Settings/Graphics/motionBlur"),
				motionBlurButton.getStatus()));
		reflectionsButton.setOnButtonPress(() -> REGISTRY
				.register(new Key("/Light Engine/Settings/Graphics/reflections"), reflectionsButton.getStatus()));
		ambientOccButton.setOnButtonPress(() -> REGISTRY
				.register(new Key("/Light Engine/Settings/Graphics/ambientOcclusion"), ambientOccButton.getStatus()));
		chromaticAberrationButton.setOnButtonPress(() -> REGISTRY.register(
				new Key("/Light Engine/Settings/Graphics/chromaticAberration"), chromaticAberrationButton.getStatus()));
		lensFlaresButton.setOnButtonPress(() -> REGISTRY.register(new Key("/Light Engine/Settings/Graphics/lensFlares"),
				lensFlaresButton.getStatus()));
		shadowResDropdown.setOnButtonPress(() -> {
			REGISTRY.register(new Key("/Light Engine/Settings/Graphics/shadowsResolution"),
					shadowResDropdown.getValue().intValue());
			EventSubsystem.triggerEvent("lightengine.renderer.resetshadowmap");
		});
		shadowDistance.setOnUnselect(() -> {
			REGISTRY.register(new Key("/Light Engine/Settings/Graphics/shadowsDrawDistance"),
					Integer.parseInt(shadowDistance.getText()));
			EventSubsystem.triggerEvent("lightengine.renderer.resetshadowmatrix");
		});
		chunkDistance.setOnUnselect( () -> {
			REGISTRY.register(new Key("/Voxel/Settings/World/chunkRadius"), Integer.parseInt(chunkDistance.getText()));
			EventSubsystem.triggerEvent("voxel.world.chunkRadius");
		});

		Text godText = new Text(LANG.getRegistryItem("voxel.optionswindow.graphics.volumetriclight"), 20, 0);
		godText.setWindowAlignment(Alignment.LEFT);
		Text shadowsText = new Text(LANG.getRegistryItem("voxel.optionswindow.graphics.shadows"), 20, 0);
		shadowsText.setWindowAlignment(Alignment.LEFT);
		Text dofText = new Text(LANG.getRegistryItem("voxel.optionswindow.graphics.dof"), 20, 0);
		dofText.setWindowAlignment(Alignment.LEFT);
		Text fxaaText = new Text(LANG.getRegistryItem("voxel.optionswindow.graphics.fxaa"), 20, 0);
		fxaaText.setWindowAlignment(Alignment.LEFT);
		Text motionBlurText = new Text(LANG.getRegistryItem("voxel.optionswindow.graphics.motionblur"), 20, 0);
		motionBlurText.setWindowAlignment(Alignment.LEFT);
		Text reflectionsText = new Text(LANG.getRegistryItem("voxel.optionswindow.graphics.reflections"), 20, 0);
		reflectionsText.setWindowAlignment(Alignment.LEFT);
		Text parallaxText = new Text(LANG.getRegistryItem("voxel.optionswindow.graphics.parallax"), 20, 0);
		parallaxText.setWindowAlignment(Alignment.LEFT);
		Text ambientOccText = new Text(LANG.getRegistryItem("voxel.optionswindow.graphics.ao"), 20, 0);
		ambientOccText.setWindowAlignment(Alignment.LEFT);
		Text chromaticAberrationText = new Text(LANG.getRegistryItem("voxel.optionswindow.graphics.chromatic"), 20, 0);
		chromaticAberrationText.setWindowAlignment(Alignment.LEFT);
		Text lensFlaresText = new Text(LANG.getRegistryItem("voxel.optionswindow.graphics.lensflares"), 20, 0);
		lensFlaresText.setWindowAlignment(Alignment.LEFT);
		Text shadowResText = new Text(LANG.getRegistryItem("voxel.optionswindow.graphics.shadowres"), 20, 0);
		shadowResText.setWindowAlignment(Alignment.LEFT);
		Text shadowDisText = new Text(LANG.getRegistryItem("voxel.optionswindow.graphics.shadowdis"), 20, 0);
		shadowDisText.setWindowAlignment(Alignment.LEFT);
		Text chunkDisText = new Text(LANG.getRegistryItem("voxel.optionswindow.graphics.chunkdis"), 20, 0);
		chunkDisText.setWindowAlignment(Alignment.LEFT);

		ScrollArea area = new ScrollArea(0, 0, w, h, 0, 0);
		area.setLayout(new FlowLayout(Direction.DOWN, 10, 10));

		Container godrays = new Container(0, 0, w, 30);
		godrays.setWindowAlignment(Alignment.LEFT_TOP);
		godrays.setAlignment(Alignment.RIGHT_BOTTOM);
		Container shadows = new Container(0, 0, w, 30);
		shadows.setWindowAlignment(Alignment.LEFT_TOP);
		shadows.setAlignment(Alignment.RIGHT_BOTTOM);
		Container dof = new Container(0, 0, w, 30);
		dof.setWindowAlignment(Alignment.LEFT_TOP);
		dof.setAlignment(Alignment.RIGHT_BOTTOM);
		Container fxaa = new Container(0, 0, w, 30);
		fxaa.setWindowAlignment(Alignment.LEFT_TOP);
		fxaa.setAlignment(Alignment.RIGHT_BOTTOM);
		Container motionBlur = new Container(0, 0, w, 30);
		motionBlur.setWindowAlignment(Alignment.LEFT_TOP);
		motionBlur.setAlignment(Alignment.RIGHT_BOTTOM);
		Container reflections = new Container(0, 0, w, 30);
		reflections.setWindowAlignment(Alignment.LEFT_TOP);
		reflections.setAlignment(Alignment.RIGHT_BOTTOM);
		Container parallax = new Container(0, 0, w, 30);
		parallax.setWindowAlignment(Alignment.LEFT_TOP);
		parallax.setAlignment(Alignment.RIGHT_BOTTOM);
		Container occlusion = new Container(0, 0, w, 30);
		occlusion.setWindowAlignment(Alignment.LEFT_TOP);
		occlusion.setAlignment(Alignment.RIGHT_BOTTOM);
		Container aberration = new Container(0, 0, w, 30);
		aberration.setWindowAlignment(Alignment.LEFT_TOP);
		aberration.setAlignment(Alignment.RIGHT_BOTTOM);
		Container lens = new Container(0, 0, w, 30);
		lens.setWindowAlignment(Alignment.LEFT_TOP);
		lens.setAlignment(Alignment.RIGHT_BOTTOM);
		Container shadowRes = new Container(0, 0, w, 30);
		shadowRes.setWindowAlignment(Alignment.LEFT_TOP);
		shadowRes.setAlignment(Alignment.RIGHT_BOTTOM);
		Container shadowDis = new Container(0, 0, w, 30);
		shadowDis.setWindowAlignment(Alignment.LEFT_TOP);
		shadowDis.setAlignment(Alignment.RIGHT_BOTTOM);
		Container chunkDis = new Container(0, 0, w, 30);
		chunkDis.setWindowAlignment(Alignment.LEFT_TOP);
		chunkDis.setAlignment(Alignment.RIGHT_BOTTOM);

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
		shadowRes.addComponent(shadowResDropdown);
		shadowRes.addComponent(shadowResText);
		shadowDis.addComponent(shadowDistance);
		shadowDis.addComponent(shadowDisText);
		chunkDis.addComponent(chunkDistance);
		chunkDis.addComponent(chunkDisText);

		godrays.setResizeH(true);
		shadows.setResizeH(true);
		dof.setResizeH(true);
		fxaa.setResizeH(true);
		motionBlur.setResizeH(true);
		reflections.setResizeH(true);
		parallax.setResizeH(true);
		occlusion.setResizeH(true);
		aberration.setResizeH(true);
		lens.setResizeH(true);
		shadowRes.setResizeH(true);
		shadowDis.setResizeH(true);
		chunkDis.setResizeH(true);

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
		area.addComponent(shadowRes);
		area.addComponent(shadowDis);
		area.addComponent(chunkDis);

		super.addComponent(area);

		backButton.setEnabled(true);
	}

	private void wmOptions() {
		int border = (int) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/WindowManager/borderSize"));
		Text wmBorderText = new Text(LANG.getRegistryItem("voxel.optionswindow.wm.border") + ": " + border, 20, 0);
		wmBorderText.setWindowAlignment(Alignment.LEFT);
		int scroll = (int) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/WindowManager/scrollBarSize"));
		Text wmScrollText = new Text(LANG.getRegistryItem("voxel.optionswindow.wm.scrollsize") + ": " + scroll, 20, 0);
		wmScrollText.setWindowAlignment(Alignment.LEFT);
		int title = (int) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/WindowManager/titleBarHeight"));
		Text wmTitleText = new Text(LANG.getRegistryItem("voxel.optionswindow.wm.titlebarsize") + ": " + title, 20, 0);
		wmTitleText.setWindowAlignment(Alignment.LEFT);
		Text compositorText = new Text(LANG.getRegistryItem("voxel.optionswindow.wm.compositor"), 20, 0);
		compositorText.setWindowAlignment(Alignment.LEFT);
		Text themeText = new Text(LANG.getRegistryItem("voxel.optionswindow.wm.theme"), 20, 0);
		themeText.setWindowAlignment(Alignment.LEFT);

		Slider wmBorder = new Slider(-56, 0, 200, 20, border / 40f);
		wmBorder.setPrecision(40f);
		wmBorder.useCustomPrecision(true);
		Slider wmScroll = new Slider(-56, 0, 200, 20, scroll / 40f);
		wmScroll.setPrecision(40f);
		wmScroll.useCustomPrecision(true);
		Slider wmTitle = new Slider(-56, 0, 200, 20, title / 40f);
		wmTitle.setPrecision(40f);
		wmTitle.useCustomPrecision(true);
		ToggleButton compositorButton = new ToggleButton(-50, 0, 80, 30,
				(boolean) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/WindowManager/compositor")));
		DropDown<String> themeDropdown = new DropDown<String>(-50, 0, 180, 30,
				(String) REGISTRY.getRegistryItem(new Key("/Light Engine/Settings/WindowManager/theme")),
				new ArrayList<>(ThemeManager.getThemes().keySet()));

		wmBorder.setAlignment(Alignment.RIGHT);
		wmBorder.setWindowAlignment(Alignment.RIGHT);
		wmScroll.setAlignment(Alignment.RIGHT);
		wmScroll.setWindowAlignment(Alignment.RIGHT);
		wmTitle.setAlignment(Alignment.RIGHT);
		wmTitle.setWindowAlignment(Alignment.RIGHT);
		compositorButton.setWindowAlignment(Alignment.RIGHT);
		compositorButton.setAlignment(Alignment.RIGHT);
		themeDropdown.setWindowAlignment(Alignment.RIGHT);
		themeDropdown.setAlignment(Alignment.RIGHT);

		wmBorder.setOnPress(() -> {
			int val = (int) (wmBorder.getPosition() * 40f);
			REGISTRY.register(new Key("/Light Engine/Settings/WindowManager/borderSize"), val);
			wmBorderText.setText(LANG.getRegistryItem("voxel.optionswindow.wm.border") + ": " + val);
			GraphicalSubsystem.getWindowManager().notifyAllWindows(WindowMessage.WM_COMPOSITOR_RELOAD, null);
		});
		wmScroll.setOnPress(() -> {
			int val = (int) (wmScroll.getPosition() * 40f);
			REGISTRY.register(new Key("/Light Engine/Settings/WindowManager/scrollBarSize"), val);
			wmScrollText.setText(LANG.getRegistryItem("voxel.optionswindow.wm.scrollsize") + ": " + val);
			GraphicalSubsystem.getWindowManager().notifyAllWindows(WindowMessage.WM_COMPOSITOR_RELOAD, null);
		});
		wmTitle.setOnPress(() -> {
			int val = (int) (wmTitle.getPosition() * 40f);
			REGISTRY.register(new Key("/Light Engine/Settings/WindowManager/titleBarHeight"), val);
			wmTitleText.setText(LANG.getRegistryItem("voxel.optionswindow.wm.titlebarsize") + ": " + val);
			GraphicalSubsystem.getWindowManager().notifyAllWindows(WindowMessage.WM_COMPOSITOR_RELOAD, null);
		});
		compositorButton.setOnButtonPress(() -> {
			if (compositorButton.getStatus())
				GraphicalSubsystem.getWindowManager().enableCompositor();
			else
				GraphicalSubsystem.getWindowManager().disableCompositor();
		});
		themeDropdown.setOnButtonPress(() -> {
			REGISTRY.register(new Key("/Light Engine/Settings/WindowManager/theme"), themeDropdown.getValue());
			ThemeManager.setTheme(themeDropdown.getValue());
		});

		ScrollArea area = new ScrollArea(0, 0, w, h, 0, 0);
		area.setLayout(new FlowLayout(Direction.DOWN, 10, 10));

		Container borderC = new Container(0, 0, w, 20);
		borderC.setWindowAlignment(Alignment.LEFT_TOP);
		borderC.setAlignment(Alignment.RIGHT_BOTTOM);
		Container scrollC = new Container(0, 0, w, 20);
		scrollC.setWindowAlignment(Alignment.LEFT_TOP);
		scrollC.setAlignment(Alignment.RIGHT_BOTTOM);
		Container titleC = new Container(0, 0, w, 20);
		titleC.setWindowAlignment(Alignment.LEFT_TOP);
		titleC.setAlignment(Alignment.RIGHT_BOTTOM);
		Container compositor = new Container(0, 0, w, 30);
		compositor.setWindowAlignment(Alignment.LEFT_TOP);
		compositor.setAlignment(Alignment.RIGHT_BOTTOM);
		Container theme = new Container(0, 0, w, 30);
		theme.setWindowAlignment(Alignment.LEFT_TOP);
		theme.setAlignment(Alignment.RIGHT_BOTTOM);

		borderC.addComponent(wmBorderText);
		borderC.addComponent(wmBorder);
		scrollC.addComponent(wmScrollText);
		scrollC.addComponent(wmScroll);
		titleC.addComponent(wmTitleText);
		titleC.addComponent(wmTitle);
		compositor.addComponent(compositorButton);
		compositor.addComponent(compositorText);
		theme.addComponent(themeDropdown);
		theme.addComponent(themeText);

		borderC.setResizeH(true);
		scrollC.setResizeH(true);
		titleC.setResizeH(true);
		compositor.setResizeH(true);
		theme.setResizeH(true);

		area.addComponent(borderC);
		area.addComponent(scrollC);
		area.addComponent(titleC);
		area.addComponent(compositor);
		area.addComponent(theme);

		super.addComponent(area);

		backButton.setEnabled(true);
	}

	@Override
	public void processWindowMessage(int message, Object param) {
		if (message == WindowMessage.WM_CLOSE) {
			CoreSubsystem.REGISTRY.save();
			GraphicalSubsystem.getWindowManager().getShell().getNotificationsWindow().notifyWindow(
					WindowMessage.WM_SHELL_NOTIFICATION_ADD,
					new Notification("Settings Saved", "Settings has been saved correctly."));
		}
		super.processWindowMessage(message, param);
	}

}
