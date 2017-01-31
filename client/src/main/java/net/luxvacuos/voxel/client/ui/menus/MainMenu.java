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

import net.luxvacuos.voxel.client.core.states.StateNames;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering;
import net.luxvacuos.voxel.client.rendering.api.nanovg.WM;
import net.luxvacuos.voxel.client.ui.nextui.Alignment;
import net.luxvacuos.voxel.client.ui.nextui.Button;
import net.luxvacuos.voxel.client.ui.nextui.RootComponent;
import net.luxvacuos.voxel.client.ui.nextui.ScrollPane;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

public class MainMenu extends RootComponent {

	private boolean open = false; // X: Temporal Fix

	public MainMenu(float x, float y, float w, float h) {
		super(x, y, w, h, "Main Menu");
	}

	@Override
	public void initApp(Window window) {
		super.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);

		Button playButton = new Button(0, 120, 200, 40, "Singleplayer");
		Button playMPButton = new Button(0, 60, 200, 40, "Multiplayer");
		Button optionsButton = new Button(0, 0, 200, 40, "Options");
		Button aboutButton = new Button(0, -60, 200, 40, "About");
		Button exitButton = new Button(0, -120, 200, 40, "Exit");

		playButton.setPreicon(NRendering.ICON_BLACK_RIGHT_POINTING_TRIANGLE);
		playMPButton.setPreicon(NRendering.ICON_BLACK_RIGHT_POINTING_TRIANGLE);
		optionsButton.setPreicon(NRendering.ICON_GEAR);
		aboutButton.setPreicon(NRendering.ICON_INFORMATION_SOURCE);
		exitButton.setPreicon(NRendering.ICON_LOGIN);

		playButton.setAlignment(Alignment.CENTER);
		playButton.setWindowAlignment(Alignment.CENTER);
		playMPButton.setAlignment(Alignment.CENTER);
		playMPButton.setWindowAlignment(Alignment.CENTER);
		optionsButton.setAlignment(Alignment.CENTER);
		optionsButton.setWindowAlignment(Alignment.CENTER);
		aboutButton.setAlignment(Alignment.CENTER);
		aboutButton.setWindowAlignment(Alignment.CENTER);
		exitButton.setAlignment(Alignment.CENTER);
		exitButton.setWindowAlignment(Alignment.CENTER);

		playButton.setOnButtonPress(() -> {
			StateMachine.setCurrentState(StateNames.SP_SELECTION);
		});

		playMPButton.setOnButtonPress(() -> {
			WM.getWM().addWindow(new MultiplayerMenu(w / 2 - 420 + x, y - 40, 840, 600));
		});

		optionsButton.setOnButtonPress(() -> {
			WM.getWM().addWindow(new OptionsMenu(w / 2 - 420 + x, y - 40, 840, 600));
		});

		aboutButton.setOnButtonPress(() -> {
			WM.getWM().addWindow(new AboutMenu(w / 2 - 420 + x, y - 40, 840, 630));
		});

		exitButton.setOnButtonPress(() -> {
			StateMachine.stop();
		});

		ScrollPane pane = new ScrollPane(0, 0, 370, 200, 340f, 60f);
		pane.setItems(10);
		pane.setColls(1);

		// super.addComponent(pane);
		super.addComponent(playButton);
		super.addComponent(playMPButton);
		super.addComponent(optionsButton);
		super.addComponent(aboutButton);
		super.addComponent(exitButton);
		super.setWindowClose(WindowClose.DO_NOTHING);
		super.initApp(window);
	}

	@Override
	public void onClose() {
		if (!open) {
			open = true;
			RootComponent closeDialog = new RootComponent(w / 2 - 100, y - h / 2 + 50, 260, 100, "Exit Voxel?");
			Button close = new Button(60, 0, 100, 40, "Exit");
			close.setAlignment(Alignment.CENTER);
			close.setWindowAlignment(Alignment.CENTER);
			close.setOnButtonPress(() -> {
				StateMachine.dispose();
			});
			Button cancel = new Button(-60, 0, 100, 40, "Cancel");
			cancel.setAlignment(Alignment.CENTER);
			cancel.setWindowAlignment(Alignment.CENTER);
			cancel.setOnButtonPress(() -> {
				closeDialog.closeWindow();
				open = false;
			});
			closeDialog.addComponent(close);
			closeDialog.addComponent(cancel);
			closeDialog.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);
			closeDialog.setAlwaysOnTop(true);
			closeDialog.setResizable(false);
			WM.getWM().addWindow(closeDialog);
		}
	}

}
