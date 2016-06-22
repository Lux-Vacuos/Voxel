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

import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.core.GlobalStates.InternalState;
import net.luxvacuos.voxel.client.core.State;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.Window;

/**
 * Main Menu State
 * 
 * @author danirod
 * @category Kernel
 */
public class MainMenuState extends State {

	private Button playButton;
	private Button exitButton;
	private Button optionsButton;
	private Button aboutButton;
	private Button playMPButton;
	private Window window;

	public MainMenuState() {
		window = new Window(20, GameResources.getInstance().getDisplay().getDisplayHeight() - 20,
				GameResources.getInstance().getDisplay().getDisplayWidth() - 40,
				GameResources.getInstance().getDisplay().getDisplayHeight() - 40, "Main Menu");

		playButton = new Button(window.getWidth() / 2 - 100, -window.getHeight() / 2 + 120 - 20, 200, 40,
				"Singleplayer");
		playMPButton = new Button(window.getWidth() / 2 - 100, -window.getHeight() / 2 + 60 - 20, 200, 40,
				"Multiplayer");
		optionsButton = new Button(window.getWidth() / 2 - 100, -window.getHeight() / 2 - 20, 200, 40, "Options");
		aboutButton = new Button(window.getWidth() / 2 - 100, -window.getHeight() / 2 - 60 - 20, 200, 40, "About");
		exitButton = new Button(window.getWidth() / 2 - 100, -window.getHeight() / 2 - 120 - 20, 200, 40, "Exit");

		playButton.setPreicon(UIRendering.ICON_BLACK_RIGHT_POINTING_TRIANGLE);
		playMPButton.setPreicon(UIRendering.ICON_BLACK_RIGHT_POINTING_TRIANGLE);
		optionsButton.setPreicon(UIRendering.ICON_GEAR);
		aboutButton.setPreicon(UIRendering.ICON_INFORMATION_SOURCE);
		exitButton.setPreicon(UIRendering.ICON_LOGIN);

		playButton.setOnButtonPress(() -> {
			switchTo(GameState.SP_SELECTION);
		});

		playMPButton.setOnButtonPress(() -> {
			switchTo(GameState.MP_SELECTION);
		});

		optionsButton.setOnButtonPress(() -> {
			switchTo(GameState.OPTIONS);
		});

		aboutButton.setOnButtonPress(() -> {
			switchTo(GameState.ABOUT);
		});

		exitButton.setOnButtonPress(() -> {
			GameResources.getInstance().getGlobalStates().setInternalState(InternalState.STOPPED);
		});

		window.addChildren(playButton);
		window.addChildren(playMPButton);
		window.addChildren(optionsButton);
		window.addChildren(aboutButton);
		window.addChildren(exitButton);
	}

	@Override
	public void start() {
		window.setFadeAlpha(0);
	}

	@Override
	public void end() {
		window.setFadeAlpha(1);
	}

	@Override
	public void render(Voxel voxel, float delta) {
		GameResources gm = voxel.getGameResources();
		gm.getRenderer().prepare();
		gm.getDisplay().beingNVGFrame();
		window.render();
		UIRendering.renderMouse();
		gm.getDisplay().endNVGFrame();
	}

	@Override
	public void update(Voxel voxel, float delta) throws Exception {
		window.update();
		if (!switching)
			window.fadeIn(4, delta);
		if (switching)
			if (window.fadeOut(4, delta)) {
				readyForSwitch = true;
			}

	}

}
