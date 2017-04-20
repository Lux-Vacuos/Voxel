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

package net.luxvacuos.voxel.client.ui;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;

public class ContextMenu extends RootComponent {

	public ContextMenu() {
		super(Mouse.getX() - 5, Mouse.getY() + 5, 180, 100, "Panel");
	}

	@Override
	public void initApp(Window window) {
		super.toggleTitleBar();
		super.setDecorations(false);
		super.setBackgroundColor(0.6f, 0.6f, 0.6f, 0.0f);
		
		ContextMenuButton btn1 = new ContextMenuButton(0, 0, 180, 25, "Test1");
		ContextMenuButton btn2 = new ContextMenuButton(0, -25, 180, 25, "Test2");
		ContextMenuButton btn3 = new ContextMenuButton(0, -50, 180, 25, "Test3");
		ContextMenuButton btn4 = new ContextMenuButton(0, -75, 180, 25, "Test4");
		
		btn1.setWindowAlignment(Alignment.LEFT_TOP);
		btn1.setAlignment(Alignment.RIGHT_BOTTOM);
		btn2.setWindowAlignment(Alignment.LEFT_TOP);
		btn2.setAlignment(Alignment.RIGHT_BOTTOM);
		btn3.setWindowAlignment(Alignment.LEFT_TOP);
		btn3.setAlignment(Alignment.RIGHT_BOTTOM);
		btn4.setWindowAlignment(Alignment.LEFT_TOP);
		btn4.setAlignment(Alignment.RIGHT_BOTTOM);
		
		super.addComponent(btn1);
		super.addComponent(btn2);
		super.addComponent(btn3);
		super.addComponent(btn4);

		super.initApp(window);
	}

	@Override
	public void alwaysUpdateApp(float delta, Window window) {
		if ((Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) && !insideWindow())
			super.closeWindow();
		super.alwaysUpdateApp(delta, window);
	}

}
