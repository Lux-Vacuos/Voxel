package net.guerra24.voxel.client.menu;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.input.Mouse;

public class Slider {

	private float pos;
	private float x, y, w, h;

	public Slider(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public void update() {
		if (Mouse.getX() > x && Mouse.getX() < x + w && Mouse.getY() > y && Mouse.getY() < y + h
				&& Mouse.isButtonDown(0)) {
			pos = Mouse.getX() / w - 2.6f - 0.23f;
		}
	}

	public void setPos(float pos) {
		this.pos = pos;
	}

	public float getPos() {
		return pos;
	}

}
