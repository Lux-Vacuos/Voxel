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

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;

public class NWM implements IWM {

	private List<INWindow> windows;
	private Window window;

	public NWM(Window window) {
		this.window = window;
		windows = new ArrayList<>();
	}

	@Override
	public void render() {
		for (INWindow nWindow : windows) {
			nWindow.render(window, this);
		}
	}

	@Override
	public void update(float delta) {
		List<INWindow> toRemove = new ArrayList<>();
		INWindow toTop = null;
		for (INWindow nWindow : windows) {
			if (nWindow.shouldClose()) {
				nWindow.dispose(window);
				toRemove.add(nWindow);
				continue;
			}
			if (nWindow.insideWindow() && Mouse.isButtonDown(0))
				toTop = nWindow;
		}
		windows.removeAll(toRemove);
		if (toTop != null) {
			INWindow top = windows.get(windows.size() - 1);
			if (top != toTop)
				if (!top.isAlwaysOnTop()) {
					windows.remove(toTop);
					windows.add(toTop);
				} else
					toTop.update(delta, window, this);
		}

		if (!windows.isEmpty()) {
			windows.get(windows.size() - 1).update(delta, window, this);
		}

	}

	@Override
	public void dispose() {
		for (INWindow nWindow : windows) {
			nWindow.dispose(window);
		}
	}

	public List<INWindow> getWindows() {
		return windows;
	}

	@Override
	public void addWindow(INWindow window) {
		window.initApp(this.window);
		window.update(0, this.window, this);
		this.windows.add(window);
	}

	@Override
	public void removeWindow(INWindow window) {
		window.dispose(this.window);
		this.windows.remove(window);
	}

}
