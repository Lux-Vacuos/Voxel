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

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.RootComponent;
import net.luxvacuos.voxel.client.ui.TextArea;
import net.luxvacuos.voxel.universal.commands.ICommandManager;

public class Console extends RootComponent {

	private ICommandManager manager;
	private TextArea text;
	private boolean selected;

	public Console(float x, float y, float w, float h) {
		super(x, y, w, h, "Console");
	}

	@Override
	public void initApp(Window window) {
		super.setBackgroundColor("#190a3dff");

		text = new TextArea("Voxel Console (WIP) \n > ", 0, 0, super.appW);
		text.setWindowAlignment(Alignment.LEFT_TOP);
		text.setFontSize(16);
		super.addComponent(text);

		super.initApp(window);
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
		if (selected) {
			text.setText(window.getKeyboardHandler().handleInput(text.getText()));
		}

		super.updateApp(delta, window);
	}

}
