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

package net.luxvacuos.voxel.client.input.callbacks;

import org.lwjgl.glfw.GLFWCharModsCallback;

public class KeyboardCharModsCallback extends GLFWCharModsCallback {
	
	private int mods;
	private final long windowID;

	public KeyboardCharModsCallback(long windowID) {
		this.windowID = windowID;
	}

	@Override
	public void invoke(long window, int codepoint, int mods) {
		if(this.windowID != window) return;
		
		this.mods = mods;
	}
	
	public int getMods() {
		return this.mods;
	}
}
