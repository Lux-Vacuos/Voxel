/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.guerra24.voxel.client.kernel.menu;

import io.github.guerra24.voxel.client.kernel.input.Mouse;

/**
 * Hardcoded Buttons
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category GUI
 * @deprecated The buttons are fixed size and position, needs rewrite
 */
public class LegacyButtons {

	public static boolean isInButtonPlay() {
		boolean isPressed = false;
		int buttonx = 459;
		int buttony = 429;
		int buttonx1 = 820;
		int buttony1 = 505;
		if (Mouse.getY() > buttony && Mouse.getX() > buttonx && Mouse.getY() < buttony1 && Mouse.getX() < buttonx1
				&& Mouse.isButtonDown(0)) {
			isPressed = true;
		}
		return isPressed;
	}

	public static boolean isInButtonExit() {
		boolean isPressed = false;
		int buttonx = 457;
		int buttony = 214;
		int buttonx1 = 822;
		int buttony1 = 289;
		if (Mouse.getY() > buttony && Mouse.getX() > buttonx && Mouse.getY() < buttony1 && Mouse.getX() < buttonx1
				&& Mouse.isButtonDown(0)) {
			isPressed = true;
		}
		return isPressed;
	}

	public static boolean isInButtonBacK() {
		boolean isPressed = false;
		int buttonx = 840;
		int buttony = 70;
		int buttonx1 = 1080;
		int buttony1 = 145;
		if (Mouse.getY() > buttony && Mouse.getX() > buttonx && Mouse.getY() < buttony1 && Mouse.getX() < buttonx1
				&& Mouse.isButtonDown(0)) {
			isPressed = true;
		}
		return isPressed;
	}

	public static boolean backToMainMenu() {
		boolean isPressed = false;
		int buttonx = 520;
		int buttony = 70;
		int buttonx1 = 760;
		int buttony1 = 145;
		if (Mouse.getY() > buttony && Mouse.getX() > buttonx && Mouse.getY() < buttony1 && Mouse.getX() < buttonx1
				&& Mouse.isButtonDown(0)) {
			isPressed = true;
		}
		return isPressed;
	}
}
