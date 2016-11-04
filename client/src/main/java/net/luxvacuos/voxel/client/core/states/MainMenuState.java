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

package net.luxvacuos.voxel.client.core.states;

import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.UIWindow;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

/**
 * Main Menu State, this is the menu show after the splash screen fade out.
 * 
 * @author danirod
 */
public class MainMenuState extends AbstractFadeState {

	private Button playButton;
	private Button exitButton;
	private Button optionsButton;
	private Button aboutButton;
	private Button playMPButton;
	private UIWindow uiWindow;

	public MainMenuState() {
		super(StateNames.MAIN_MENU);
		Window window = GameResources.getInstance().getGameWindow();
		uiWindow = new UIWindow(20, window.getHeight() - 20, window.getWidth() - 40, window.getHeight() - 40,
				"Main Menu");

		playButton = new Button(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() / 2 + 120 - 20, 200, 40,
				"Singleplayer");
		playMPButton = new Button(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() / 2 + 60 - 20, 200, 40,
				"Multiplayer");
		optionsButton = new Button(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() / 2 - 20, 200, 40, "Options");
		aboutButton = new Button(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() / 2 - 60 - 20, 200, 40, "About");
		exitButton = new Button(uiWindow.getWidth() / 2 - 100, -uiWindow.getHeight() / 2 - 120 - 20, 200, 40, "Exit");

		playButton.setPreicon(UIRendering.ICON_BLACK_RIGHT_POINTING_TRIANGLE);
		playMPButton.setPreicon(UIRendering.ICON_BLACK_RIGHT_POINTING_TRIANGLE);
		optionsButton.setPreicon(UIRendering.ICON_GEAR);
		aboutButton.setPreicon(UIRendering.ICON_INFORMATION_SOURCE);
		exitButton.setPreicon(UIRendering.ICON_LOGIN);

		playButton.setOnButtonPress((button, delta) -> {
			this.switchTo(StateNames.SP_SELECTION);
		});

		playMPButton.setOnButtonPress((button, delta) -> {
			this.switchTo(StateNames.MP_SELECTION);
		});

		optionsButton.setOnButtonPress((button, delta) -> {
			this.switchTo(StateNames.OPTIONS);
		});

		aboutButton.setOnButtonPress((button, delta) -> {
			this.switchTo(StateNames.ABOUT);
		});

		exitButton.setOnButtonPress((button, delta) -> {
			StateMachine.dispose();
		});

		uiWindow.addChildren(playButton);
		uiWindow.addChildren(playMPButton);
		uiWindow.addChildren(optionsButton);
		uiWindow.addChildren(aboutButton);
		uiWindow.addChildren(exitButton);
	}

	@Override
	public void start() {
		uiWindow.setFadeAlpha(0);
	}

	@Override
	public void end() {
		uiWindow.setFadeAlpha(1);
	}

	@Override
	public void render(AbstractVoxel voxel, float delta) {
		Window window = ((GameResources) voxel.getGameResources()).getGameWindow();
		Renderer.prepare(1, 1, 1, 1);
		window.beingNVGFrame();
		uiWindow.render(window.getID());
		UIRendering.renderMouse(window.getID());
		window.endNVGFrame();
	}

	@Override
	public void update(AbstractVoxel voxel, float delta) {
		uiWindow.update(delta);

		super.update(voxel, delta);
	}

	@Override
	protected boolean fadeIn(float delta) {
		return this.uiWindow.fadeIn(4, delta);
	}

	@Override
	protected boolean fadeOut(float delta) {
		return this.uiWindow.fadeOut(4, delta);
	}

}
