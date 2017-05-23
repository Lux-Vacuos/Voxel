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

package net.luxvacuos.voxel.client.ui.windows;

import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.states.StateNames;
import net.luxvacuos.voxel.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.themes.Theme;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.ModalWindow;
import net.luxvacuos.voxel.client.ui.RootComponentWindow;
import net.luxvacuos.voxel.universal.core.TaskManager;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

import static net.luxvacuos.voxel.universal.core.subsystems.CoreSubsystem.*;

public class MainWindow extends RootComponentWindow {

	public MainWindow(float x, float y, float w, float h) {
		super(x, y, w, h, LANG.getRegistryItem("voxel.mainwindow.name"));
	}

	@Override
	public void initApp(Window window) {
		super.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);

		Button playButton = new Button(0, 120, 200, 40, LANG.getRegistryItem("voxel.mainwindow.btnplay"));
		Button playMPButton = new Button(0, 60, 200, 40, LANG.getRegistryItem("voxel.mainwindow.btnmp"));
		Button optionsButton = new Button(0, 0, 200, 40, LANG.getRegistryItem("voxel.mainwindow.btnoptions"));
		Button aboutButton = new Button(0, -60, 200, 40, LANG.getRegistryItem("voxel.mainwindow.btnabout"));
		Button exitButton = new Button(0, -120, 200, 40, LANG.getRegistryItem("voxel.mainwindow.btnexit"));

		playButton.setPreicon(Theme.ICON_BLACK_RIGHT_POINTING_TRIANGLE);
		playMPButton.setPreicon(Theme.ICON_BLACK_RIGHT_POINTING_TRIANGLE);
		optionsButton.setPreicon(Theme.ICON_GEAR);
		aboutButton.setPreicon(Theme.ICON_INFORMATION_SOURCE);
		exitButton.setPreicon(Theme.ICON_LOGIN);

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
			if (ClientVariables.TEST_MODE) {
				GraphicalSubsystem.getWindowManager().toggleShell();
				super.setWindowClose(WindowClose.DISPOSE);
				super.closeWindow();
				StateMachine.setCurrentState(StateNames.TEST);
			} else
				GraphicalSubsystem.getWindowManager()
						.addWindow(new WorldWindow(w / 2 - 420 + x, y - 40, 840, 600, this));
		});

		playMPButton.setOnButtonPress(() -> {
			GraphicalSubsystem.getWindowManager()
					.addWindow(new MultiplayerMenu(w / 2 - 250 + x, y - 100, 500, 400, this));
		});

		optionsButton.setOnButtonPress(() -> {
			GraphicalSubsystem.getWindowManager().addWindow(new OptionsWindow(w / 2 - 420 + x, y - 40, 840, 600));
		});

		aboutButton.setOnButtonPress(() -> {
			GraphicalSubsystem.getWindowManager().addWindow(new AboutWindow(w / 2 - 420 + x, y - 40, 840, 600));
		});

		exitButton.setOnButtonPress(() -> {
			StateMachine.stop();
		});

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
		ModalWindow window = new ModalWindow(340, 200, LANG.getRegistryItem("voxel.mainwindow.modal.txtclose"),
				LANG.getRegistryItem("voxel.mainwindow.modal.name"));
		GraphicalSubsystem.getWindowManager().addWindow(window);
		TaskManager.addTask(() -> {
			window.setOnAccept(() -> {
				StateMachine.stop();
			});
		});
	}

}
