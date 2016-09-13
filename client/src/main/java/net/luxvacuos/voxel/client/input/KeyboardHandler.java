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

package net.luxvacuos.voxel.client.input;

import org.lwjgl.glfw.GLFW;

import net.luxvacuos.voxel.client.input.keyboard.KeyboardCharCallback;
import net.luxvacuos.voxel.client.input.keyboard.KeyboardCharModsCallback;
import net.luxvacuos.voxel.client.input.keyboard.KeyboardKeyCallback;
import net.luxvacuos.voxel.universal.resources.IDisposable;

public final class KeyboardHandler implements IDisposable {
	
	private final KeyboardKeyCallback keyCallback;
	private final KeyboardCharCallback charCallback;
	private final KeyboardCharModsCallback modCallback;
	
	private final long windowID;
	private long lastPress = 0l;

	public KeyboardHandler(long windowID) {
		this.keyCallback = new KeyboardKeyCallback(windowID);
		this.charCallback = new KeyboardCharCallback(windowID);
		this.modCallback = new KeyboardCharModsCallback(windowID);
		
		this.windowID = windowID;
		
		//Register the callbacks with GLFW
		GLFW.glfwSetKeyCallback(windowID, this.keyCallback);
		GLFW.glfwSetCharCallback(windowID, this.charCallback);
		GLFW.glfwSetCharModsCallback(windowID, this.modCallback);
	}
	
	public boolean isKeyPressedRaw(int keycode) {
		return GLFW.glfwGetKey(this.windowID, keycode) == GLFW.GLFW_PRESS;
	}
	
	public boolean isKeyPressed(int keycode, boolean repeat) {
		return this.keyCallback.isKeyPressed(keycode) && (repeat ? true : !this.keyCallback.wasKeyRepeated(keycode));
	}
	
	public boolean isKeyPressed(int keycode) {
		return this.isKeyPressed(keycode, true);
	}
	
	public boolean wasKeyPressed(int keycode) {
		return this.keyCallback.wasKeyPressed(keycode);
	}
	
	public void enableTextInput() {
		this.charCallback.setEndabled(true);
	}
	
	public void setTextInputEnabled(boolean flag) {
		this.charCallback.setEndabled(flag);
	}
	
	public void disableTextInput() {
		this.charCallback.setEndabled(false);
	}
	
	public String handleInput(String input) {
		if(!this.charCallback.hasData()) return this.handleBackspace(input);
		String result = input;
		
		for(String in : this.charCallback.getData()) result += in;
		
		return result;
	}
	
	public boolean isShiftPressed() {
		return this.isKeyPressedRaw(GLFW.GLFW_KEY_LEFT_SHIFT) || this.isKeyPressedRaw(GLFW.GLFW_KEY_RIGHT_SHIFT);
	}
	
	public boolean isCtrlPressed() {
		return this.isKeyPressedRaw(GLFW.GLFW_KEY_LEFT_CONTROL) || this.isKeyPressedRaw(GLFW.GLFW_KEY_RIGHT_CONTROL);
	}
	
	public boolean isAltPressed() {
		return this.isKeyPressedRaw(GLFW.GLFW_KEY_LEFT_ALT) || this.isKeyPressedRaw(GLFW.GLFW_KEY_RIGHT_ALT);
	}
	
	public void update() {
		this.keyCallback.swapBuffers();
	}

	@Override
	public void dispose() {
		
	}
	
	private String handleBackspace(String input) {
		long currentPress = 0l;
		if(this.isKeyPressed(GLFW.GLFW_KEY_BACKSPACE) && ((currentPress = System.currentTimeMillis()) - this.lastPress) > 150) {
			String result = input;
			this.lastPress = currentPress;
			if(input.length() > 0) result = input.substring(0, input.length() - 1);
			
			return result;
		} else return input;
	}
	
	public static boolean isKeyPressedRaw(long windowID, int keycode) {
		return GLFW.glfwGetKey(windowID, keycode) == GLFW.GLFW_PRESS;
	}
	
}
