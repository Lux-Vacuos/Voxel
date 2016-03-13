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

package net.luxvacuos.voxel.client.menu;

import net.luxvacuos.voxel.client.input.Mouse;

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
