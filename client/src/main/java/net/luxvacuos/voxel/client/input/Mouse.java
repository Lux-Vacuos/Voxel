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

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;

public class Mouse {

	private static boolean grabbed = false;

	private static int lastX = 0;
	private static int lastY = 0;

	private static int latestX = 0;
	private static int latestY = 0;

	private static int x = 0;
	private static int y = 0;

	private static int lastDWheel = 0;

	private static EventQueue queue = new EventQueue(32);

	private static int[] buttonEvents = new int[queue.getMaxEvents()];
	private static int[] wheelEvents = new int[queue.getMaxEvents()];
	private static boolean[] buttonEventStates = new boolean[queue.getMaxEvents()];
	private static int[] xEvents = new int[queue.getMaxEvents()];
	private static int[] yEvents = new int[queue.getMaxEvents()];
	private static int[] lastxEvents = new int[queue.getMaxEvents()];
	private static int[] lastyEvents = new int[queue.getMaxEvents()];
	private static long[] nanoTimeEvents = new long[queue.getMaxEvents()];

	private static boolean clipPostionToDisplay = true;

	private static boolean isMouseInsideWindow = true;

	public static void addMoveEvent(double mouseX, double mouseY) {
		latestX = (int) mouseX;
		latestY = ClientInternalSubsystem.getInstance().getGameWindow().getHeight() - (int) mouseY;

		lastxEvents[queue.getNextPos()] = xEvents[queue.getNextPos()];
		lastyEvents[queue.getNextPos()] = yEvents[queue.getNextPos()];

		xEvents[queue.getNextPos()] = latestX;
		yEvents[queue.getNextPos()] = latestY;

		buttonEvents[queue.getNextPos()] = -1;
		buttonEventStates[queue.getNextPos()] = false;

		wheelEvents[queue.getNextPos()] = 0;

		nanoTimeEvents[queue.getNextPos()] = WindowManager.getNanoTime();

		queue.add();
	}

	public static void addButtonEvent(int button, boolean pressed) {
		lastxEvents[queue.getNextPos()] = xEvents[queue.getNextPos()];
		lastyEvents[queue.getNextPos()] = yEvents[queue.getNextPos()];

		xEvents[queue.getNextPos()] = latestX;
		yEvents[queue.getNextPos()] = latestY;

		buttonEvents[queue.getNextPos()] = button;
		buttonEventStates[queue.getNextPos()] = pressed;

		wheelEvents[queue.getNextPos()] = 0;

		nanoTimeEvents[queue.getNextPos()] = WindowManager.getNanoTime();

		queue.add();
	}

	public static void addWheelEvent(int wheel) {
		lastxEvents[queue.getNextPos()] = xEvents[queue.getNextPos()];
		lastyEvents[queue.getNextPos()] = yEvents[queue.getNextPos()];

		xEvents[queue.getNextPos()] = latestX;
		yEvents[queue.getNextPos()] = latestY;

		buttonEvents[queue.getNextPos()] = -1;
		buttonEventStates[queue.getNextPos()] = false;

		wheelEvents[queue.getNextPos()] = wheel;
		lastDWheel = wheel;

		nanoTimeEvents[queue.getNextPos()] = WindowManager.getNanoTime();

		queue.add();
	}

	public static void setMouseInsideWindow(boolean mouseInsideWindow) {
		isMouseInsideWindow = mouseInsideWindow;
	}

	public static void poll() {
		lastX = x;
		lastY = y;

		if (!grabbed && clipPostionToDisplay) {
			Window window = ClientInternalSubsystem.getInstance().getGameWindow();
			if (latestX < 0)
				latestX = 0;
			if (latestY < 0)
				latestY = 0;
			if (latestX > window.getWidth() - 1)
				latestX = window.getWidth() - 1;
			if (latestY > window.getHeight() - 1)
				latestY = window.getHeight() - 1;
		}

		x = latestX;
		y = latestY;
	}

	public static void setGrabbed(boolean grab) {
		GLFW.glfwSetInputMode(ClientInternalSubsystem.getInstance().getGameWindow().getID(), GLFW.GLFW_CURSOR,
				grab ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
		grabbed = grab;
	}

	public static boolean isGrabbed() {
		return grabbed;
	}

	public static boolean isButtonDown(int button) {
		return GLFW.glfwGetMouseButton(ClientInternalSubsystem.getInstance().getGameWindow().getID(), button) == GLFW.GLFW_PRESS;
	}

	public static boolean next() {
		return queue.next();
	}

	public static int getEventX() {
		return xEvents[queue.getCurrentPos()];
	}

	public static int getEventY() {
		return yEvents[queue.getCurrentPos()];
	}

	public static int getEventDX() {
		return xEvents[queue.getCurrentPos()] - lastxEvents[queue.getCurrentPos()];
	}

	public static int getEventDY() {
		return yEvents[queue.getCurrentPos()] - lastyEvents[queue.getCurrentPos()];
	}

	public static long getEventNanoseconds() {
		return nanoTimeEvents[queue.getCurrentPos()];
	}

	public static int getEventButton() {
		return buttonEvents[queue.getCurrentPos()];
	}

	public static boolean getEventButtonState() {
		return buttonEventStates[queue.getCurrentPos()];
	}

	public static int getEventDWheel() {
		return wheelEvents[queue.getCurrentPos()];
	}

	public static int getX() {
		return x;
	}

	public static int getY() {
		return y;
	}

	public static int getDX() {
		return x - lastX;
	}

	public static int getDY() {
		return y - lastY;
	}

	public static int getDWheel() {
		int dwheel = lastDWheel;
		lastDWheel = 0;
		return dwheel;
	}

	public static int getButtonCount() {
		return 8; // max mouse buttons supported by GLFW
	}

	public static boolean isInsideWindow() {
		return isMouseInsideWindow;
	}

	public static void setClipMouseCoordinatesToWindow(boolean clip) {
		clipPostionToDisplay = clip;
	}

	public static void setCursorPosition(int new_x, int new_y) {
		GLFW.glfwSetCursorPos(ClientInternalSubsystem.getInstance().getGameWindow().getID(), new_x, new_y);
	}

}
