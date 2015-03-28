package voxel.client.core.engine.init;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Input {

	public static boolean renderText = false;

	public static void input() {
		if (Mouse.isButtonDown(0)) {
			Mouse.setGrabbed(true);
		}
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_F3)) {
					renderText = !renderText;
				}
			}
		}
	}
}
