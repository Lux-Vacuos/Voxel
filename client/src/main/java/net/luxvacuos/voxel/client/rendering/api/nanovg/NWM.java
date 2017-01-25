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

public class NWM implements IWM {

	private List<NWindow> windows;

	public NWM() {
		windows = new ArrayList<>();
	}

	@Override
	public void render(long windowID) {
		for (NWindow nWindow : windows) {
			nWindow.render(windowID, this);
		}
	}

	@Override
	public void update(float delta, long windowID) {
		List<NWindow> toRemove = new ArrayList<>();
		NWindow toTop = null;
		for (NWindow nWindow : windows) {
			if (nWindow.exit) {
				nWindow.dispose();
				toRemove.add(nWindow);
				continue;
			}
			if (nWindow.insideWindow() && Mouse.isButtonDown(0))
				toTop = nWindow;
			nWindow.update(delta, windowID, this);
		}
		windows.removeAll(toRemove);
		if (toTop != null) {
			if (windows.get(windows.size() - 1) != toTop) {
				windows.remove(toTop);
				windows.add(toTop);
			}
		}
	}
	
	@Override
	public void dispose() {
		for (NWindow nWindow : windows) {
			nWindow.dispose();
		}
	}

	public List<NWindow> getWindows() {
		return windows;
	}

	@Override
	public void addWindow(NWindow window) {
		this.windows.add(window);
	}

	@Override
	public void removeWindow(NWindow window) {
		window.dispose();
		this.windows.remove(window);
	}

}
