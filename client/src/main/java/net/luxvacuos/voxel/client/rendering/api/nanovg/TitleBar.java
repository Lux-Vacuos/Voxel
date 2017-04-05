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

package net.luxvacuos.voxel.client.rendering.api.nanovg;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;

public class TitleBar implements ITitleBar {

	public static final float HEIGHT = 30;

	private boolean enabled = true;
	private String title, font = "Roboto-Bold";
	private Event exit, maximize, drag;
	private boolean pressedExit, pressedMaximize;

	public TitleBar(String title) {
		this.title = title;
	}

	@Override
	public void render(Window window, IWindow iWindow) {
		if (enabled) {
			NRendering.renderTitleBar(window.getNVGID(), title, font, iWindow.getX() + 2,
					window.getHeight() - iWindow.getY(), iWindow.getWidth() - 2, HEIGHT, WM.invertWindowButtons,
					iWindow.isResizable());
		}
	}

	@Override
	public void update(Window window, IWindow iWindow) {
		if (enabled) {
			if (Mouse.isButtonDown(0) && maximize(iWindow) && !pressedMaximize)
				maximize.event(window);
			pressedMaximize = Mouse.isButtonDown(0) && maximize(iWindow);

			if (Mouse.isButtonDown(0) && exit(iWindow) && !pressedExit)
				exit.event(window);
			pressedExit = Mouse.isButtonDown(0) && exit(iWindow);

			if (Mouse.isButtonDown(0))
				if (Mouse.getX() > iWindow.getX() && Mouse.getY() < iWindow.getY()
						&& Mouse.getX() < iWindow.getX() + iWindow.getWidth() && Mouse.getY() > iWindow.getY() - 32)
					drag.event(window);
		}
	}

	private boolean maximize(IWindow iWindow) {
		if (WM.invertWindowButtons)
			return Mouse.getX() > iWindow.getX() + 33 && Mouse.getY() < iWindow.getY() - 2
					&& Mouse.getX() < iWindow.getX() + 62 && Mouse.getY() > iWindow.getY() - 31;
		else
			return Mouse.getX() > iWindow.getX() + iWindow.getWidth() - 62 && Mouse.getY() < iWindow.getY() - 2
					&& Mouse.getX() < iWindow.getX() + iWindow.getWidth() - 33 && Mouse.getY() > iWindow.getY() - 31;

	}

	private boolean exit(IWindow iWindow) {
		if (WM.invertWindowButtons)
			return Mouse.getX() > iWindow.getX() + 2 && Mouse.getY() < iWindow.getY() - 2
					&& Mouse.getX() < iWindow.getX() + 32 && Mouse.getY() > iWindow.getY() - 31;
		else
			return Mouse.getX() > iWindow.getX() + iWindow.getWidth() - 31 && Mouse.getY() < iWindow.getY() - 2
					&& Mouse.getX() < iWindow.getX() + iWindow.getWidth() - 2 && Mouse.getY() > iWindow.getY() - 31;

	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void setOnExit(Event event) {
		this.exit = event;
	}

	@Override
	public void setOnMaximize(Event event) {
		this.maximize = event;
	}

	@Override
	public void setOnDrag(Event event) {
		this.drag = event;
	}

}
