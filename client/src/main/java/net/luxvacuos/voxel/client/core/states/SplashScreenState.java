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

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.WM;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.Image;
import net.luxvacuos.voxel.client.ui.RootComponentWindow;
import net.luxvacuos.voxel.client.ui.Spinner;
import net.luxvacuos.voxel.client.ui.menus.MainMenu;
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
		component = new RootComponentWindow(0, ClientVariables.HEIGHT, ClientVariables.WIDTH, ClientVariables.HEIGHT,
				"splash");
		component.toggleTitleBar();
		component.setDecorations(false);
		component.setBackgroundColor(1, 1, 1, 1);
		component.setBlurBehind(false);
		component.setAsBackground(true);
		Image lv = new Image(0, 0, 512, 512, ClientInternalSubsystem.getInstance().getGameWindow().getResourceLoader()
				.loadNVGTexture("LuxVacuos-Logo"));
		lv.setAlignment(Alignment.CENTER);
		lv.setWindowAlignment(Alignment.CENTER);

		Spinner spinner = new Spinner(0, -220, 20);
		spinner.setWindowAlignment(Alignment.CENTER);

		component.addComponent(lv);
		component.addComponent(spinner);

		WM.getWM().addWindow(component);
	}

	@Override
	public void end() {
		super.end();
		component.closeWindow();
		float borderSize = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/borderSize");
		float titleBarHeight = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/titleBarHeight");
		RootComponentWindow mainMenu = new MainMenu(borderSize + 10, ClientVariables.HEIGHT - titleBarHeight - 10,
				ClientVariables.WIDTH - borderSize * 2f - 20,
				ClientVariables.HEIGHT - titleBarHeight - borderSize - 20);
		WM.getWM().addWindow(mainMenu);
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Renderer.clearColors(1, 1, 1, 1);
		window.beingNVGFrame();
		WM.getWM().render();
		window.endNVGFrame();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		WM.getWM().update(delta);
		if (TaskManager.isEmpty())
			StateMachine.setCurrentState(StateNames.MAIN_MENU);
	}

}
