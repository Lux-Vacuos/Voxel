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

package net.luxvacuos.voxel.client.input.keyboard;

import java.util.BitSet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyboardKeyCallback extends GLFWKeyCallback {
	private BitSet keys = new BitSet(65536);
	private BitSet buffer = new BitSet(65536);
	private BitSet last = new BitSet(65536);
	
	private final long windowID;
	
	public KeyboardKeyCallback(long windowID) {
		this.windowID = windowID;
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if(window != this.windowID) return; //only care about the window this callback is assigned to
		
		this.keys.set(key, (action != GLFW.GLFW_RELEASE));
	}
	
	public boolean isKeyPressed(int keycode) {
		return this.buffer.get(keycode);
	}
	
	public boolean wasKeyPressed(int keycode) {
		return this.last.get(keycode);
	}
	
	public void swapBuffers() {
		this.last = this.buffer;
		this.buffer = this.keys;
	}

}
