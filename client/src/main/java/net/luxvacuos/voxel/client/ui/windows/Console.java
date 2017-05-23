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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.RootComponentWindow;
import net.luxvacuos.voxel.client.ui.TextArea;
import net.luxvacuos.voxel.universal.commands.ICommandManager;

public class Console extends RootComponentWindow {

	private ICommandManager manager;
	private TextArea text;
	private String textBuffer = "";
	private boolean selected;
	private boolean running = true;

	public Console(float x, float y, float w, float h) {
		super(x, y, w, h, "Console");
	}

	@Override
	public void initApp(Window window) {
		super.setBackgroundColor("#1F1F1F78");

		text = new TextArea(textBuffer, 0, 0, w);
		text.setWindowAlignment(Alignment.LEFT_TOP);
		text.setFontSize(25);
		super.addComponent(text);
		super.initApp(window);
		Thread thread = new Thread(() -> {
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));
			String command;
			try {
				while (running && (command = bufferedreader.readLine()) != null) {
					// this.manager.command(command);
					textBuffer += command + "\n";
				}
			} catch (IOException ioe) {
			}
		});
		thread.setDaemon(true);
		thread.setName("Command Thread");
		thread.start();
		textBuffer = "Voxel Console (WIP) \n > ";
	}

	@Override
	public void disposeApp(Window window) {
		running = false;
		super.disposeApp(window);
	}

	@Override
	public void updateApp(float delta, Window window) {
		if (Mouse.isButtonDown(0)) {
			if (super.insideWindow()) {
				window.getKeyboardHandler().enableTextInput();
				window.getKeyboardHandler().clearInputData();
				selected = true;
			} else {
				selected = false;
			}
		}
		super.updateApp(delta, window);
	}
	
	@Override
	public void alwaysUpdateApp(float delta, Window window) {
		text.setText(textBuffer);
		super.alwaysUpdateApp(delta, window);
	}

}
