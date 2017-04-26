/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

package net.luxvacuos.voxel.client.ui;

public class FlowLayout extends Layout {

	private Direction direction;
	private float x, y, margin, borderMargin;

	public FlowLayout(Direction direction, float margin, float borderMargin) {
		this.direction = direction;
		this.margin = margin;
		this.borderMargin = borderMargin;
	}

	@Override
	public void preBuild() {
		switch (direction) {
		case DOWN:
			y = -borderMargin;
			break;
		case LEFT:
			x = -borderMargin;
			break;
		case RIGHT:
			x = borderMargin;
			break;
		case UP:
			y = borderMargin;
			break;
		default:
			break;
		}
	}

	@Override
	public void build(Component component) {
		switch (direction) {
		case UP:
			component.y = y;
			y += component.h + margin;
			break;
		case DOWN:
			component.y = y;
			y -= component.h + margin;
			break;
		case LEFT:
			component.x = x;
			x -= component.w + margin;
			break;
		case RIGHT:
			component.x = x;
			x += component.w + margin;
			break;
		default:
			break;
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}
