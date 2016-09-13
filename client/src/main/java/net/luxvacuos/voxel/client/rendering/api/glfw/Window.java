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

package net.luxvacuos.voxel.client.rendering.api.glfw;

import net.luxvacuos.voxel.client.input.KeyboardHandler;

public class Window extends AbstractWindow {

	protected Window(long windowID) {
		this.windowID = windowID;
		this.kbHandle = new KeyboardHandler(windowID);
		
		this.setCallbacks();
	}

	@Override
	public void create(int width, int height, String title, boolean vsync, boolean visible, boolean resizable,
			ContextFormat format, String[] icons) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDisplay(int fps) {
		// TODO Auto-generated method stub

	}

}
