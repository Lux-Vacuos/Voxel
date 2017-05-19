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

package net.luxvacuos.voxel.client.core.states;

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import net.luxvacuos.voxel.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.Image;
import net.luxvacuos.voxel.client.ui.RootComponentWindow;
import net.luxvacuos.voxel.client.ui.Spinner;
import net.luxvacuos.voxel.client.ui.menus.MainMenu;
import net.luxvacuos.voxel.client.ui.menus.Shell;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.TaskManager;
import net.luxvacuos.voxel.universal.core.states.AbstractState;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

/**
 * Splash screen State, show only in the load.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class SplashScreenState extends AbstractState {

	private RootComponentWindow component;

	public SplashScreenState() {
		super(StateNames.SPLASH_SCREEN);
	}

	@Override
	public void init() {
		component = new RootComponentWindow(0, (int) REGISTRY.getRegistryItem("/Voxel/Display/height"),
				(int) REGISTRY.getRegistryItem("/Voxel/Display/width"),
				(int) REGISTRY.getRegistryItem("/Voxel/Display/height"), "splash");
		component.toggleTitleBar();
		component.setDecorations(false);
		component.setBackgroundColor(1, 1, 1, 1);
		component.setBlurBehind(false);
		component.setAsBackground(true);
		Image lv = new Image(0, 0, 512, 512,
				GraphicalSubsystem.getMainWindow().getResourceLoader().loadNVGTexture("LuxVacuos-Logo"));
		lv.setAlignment(Alignment.CENTER);
		lv.setWindowAlignment(Alignment.CENTER);

		Spinner spinner = new Spinner(0, -220, 20);
		spinner.setWindowAlignment(Alignment.CENTER);

		component.addComponent(lv);
		component.addComponent(spinner);

		GraphicalSubsystem.getWindowManager().addWindow(component);
	}

	@Override
	public void end() {
		super.end();
		component.closeWindow();
		Shell shell = new Shell(0, 30, (int) REGISTRY.getRegistryItem("/Voxel/Display/width"), 30);
		GraphicalSubsystem.getWindowManager().addWindow(shell);
		GraphicalSubsystem.getWindowManager().setShell(shell);
		float borderSize = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/borderSize");
		float titleBarHeight = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/titleBarHeight");
		int height = (int) REGISTRY.getRegistryItem("/Voxel/Display/height");
		RootComponentWindow mainMenu = new MainMenu(borderSize + 10, height - titleBarHeight - 10,
				(int) REGISTRY.getRegistryItem("/Voxel/Display/width") - borderSize * 2f - 20,
				height - titleBarHeight - borderSize - 50);
		GraphicalSubsystem.getWindowManager().addWindow(mainMenu);
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = GraphicalSubsystem.getMainWindow();
		Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Renderer.clearColors(1, 1, 1, 1);
		window.beingNVGFrame();
		GraphicalSubsystem.getWindowManager().render();
		window.endNVGFrame();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		GraphicalSubsystem.getWindowManager().update(delta);
		if (TaskManager.isEmpty())
			StateMachine.setCurrentState(StateNames.MAIN_MENU);
	}

}
