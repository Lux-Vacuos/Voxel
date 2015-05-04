package net.guerra24.voxel.client.engine.menu;

import org.lwjgl.input.Mouse;

public class Button {

	public static boolean isInButtonPlay() {
		boolean isPressed = false;
		int buttonx = 459;
		int buttony = 429;
		int buttonx1 = 820;
		int buttony1 = 505;
		if (Mouse.getY() > buttony && Mouse.getX() > buttonx
				&& Mouse.getY() < buttony1 && Mouse.getX() < buttonx1
				&& Mouse.isButtonDown(0)) {
			isPressed = true;
		}
		return isPressed;
	}

	public static boolean isInButtonExit() {
		boolean isPressed = false;
		int buttonx = 459;
		int buttony = 320;
		int buttonx1 = 820;
		int buttony1 = 397;
		if (Mouse.getY() > buttony && Mouse.getX() > buttonx
				&& Mouse.getY() < buttony1 && Mouse.getX() < buttonx1
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
		if (Mouse.getY() > buttony && Mouse.getX() > buttonx
				&& Mouse.getY() < buttony1 && Mouse.getX() < buttonx1
				&& Mouse.isButtonDown(0)) {
			isPressed = true;
		}
		return isPressed;
	}
}
