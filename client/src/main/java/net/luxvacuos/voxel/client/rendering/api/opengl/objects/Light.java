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

package net.luxvacuos.voxel.client.rendering.api.opengl.objects;

import net.luxvacuos.igl.vector.Vector3f;

public class Light {

	private Vector3f position;
	private Vector3f color;
	private Vector3f direction;
	private float radius, inRadius;
	private int type;

	public Light(Vector3f position, Vector3f color, Vector3f direction, float radius, float inRadius) {
		this.position = position;
		this.color = color;
		this.direction = direction;
		this.radius = radius;
		this.inRadius = inRadius;
		type = 1;
	}

	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
		type = 0;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getColor() {
		return color;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public float getRadius() {
		return radius;
	}

	public float getInRadius() {
		return inRadius;
	}

	public int getType() {
		return type;
	}

}
