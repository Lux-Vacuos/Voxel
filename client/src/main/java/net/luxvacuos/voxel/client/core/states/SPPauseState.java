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

import org.lwjgl.glfw.GLFW;

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.ui.UIButton;
import net.luxvacuos.voxel.client.ui.UIWindow;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.AbstractState;

/**
 * Singleplayer Pause State.
 * 
 * @author danirod
 */
public class SPPauseState extends AbstractState {

	private UIWindow uiWindow;
	private UIButton exitButton;
	private UIButton optionsButton;

	public SPPauseState() {
		super(StateNames.SP_PAUSE);
	}

	@Override
	public void init() {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		uiWindow = new UIWindow(20, window.getHeight() - 20, window.getWidth() - 40, window.getHeight() - 40, "Pause");
		exitButton = new UIButton(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() + 35, 200, 40,
				"Back to Main Menu");
		optionsButton = new UIButton(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() + 85, 200, 40, "Options");
		exitButton.setOnButtonPress((button, delta) -> {
			ClientVariables.exitWorld = true;
		});
		optionsButton.setOnButtonPress((button, delta) -> {
		});
		optionsButton.setEnabled(false);
		uiWindow.addChildren(exitButton);
		uiWindow.addChildren(optionsButton);
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		uiWindow.update(delta);
		if (window.getKeyboardHandler().isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
			window.getKeyboardHandler().ignoreKeyUntilRelease(GLFW.GLFW_KEY_ESCAPE);
			Mouse.setGrabbed(true);
			ClientVariables.paused = false;
		}
	}

	@Override
	public void render(AbstractVoxel voxel, float alpha) {
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();
		uiWindow.render(window.getID());
	}

}
