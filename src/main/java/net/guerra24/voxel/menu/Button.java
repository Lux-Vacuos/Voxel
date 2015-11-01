package net.guerra24.voxel.menu;

import net.guerra24.voxel.input.Mouse;
import net.guerra24.voxel.util.vector.Vector2f;

public class Button {
	private Vector2f bottom_left;
	private Vector2f top_right;

	public Button(Vector2f bottom_left, Vector2f top_right) {
		this.bottom_left = bottom_left;
		this.top_right = top_right;
	}

	public boolean insideButton() {
		if (Mouse.getX() > bottom_left.getX() && Mouse.getY() > bottom_left.getY() && Mouse.getX() < top_right.getX()
				&& Mouse.getY() < top_right.getY())
			return true;
		else
			return false;
	}

	public boolean pressed() {
		if (insideButton())
			if (Mouse.isButtonDown(0))
				return true;
			else
				return false;
		else 
			return false;
	}
}
