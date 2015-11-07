package net.guerra24.voxel.client.menu;

import net.guerra24.voxel.client.input.Mouse;
import net.guerra24.voxel.universal.util.vector.Vector2f;

public class Button {
	private Vector2f bottom_left_pos;
	private Vector2f top_left_max;

	public Button(Vector2f bottom_left, Vector2f top_right) {
		this.bottom_left_pos = bottom_left;
		this.top_left_max = new Vector2f(bottom_left.x + top_right.x, bottom_left.y + top_right.y);
	}

	public boolean insideButton() {
		if (Mouse.getX() > bottom_left_pos.getX() && Mouse.getY() > bottom_left_pos.getY()
				&& Mouse.getX() < top_left_max.getX() && Mouse.getY() < top_left_max.getY())
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
