/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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

package net.luxvacuos.voxel.universal.world.utils;

public class Vector8f {
	private float x, y, z, w, i, j, k, l;

	public Vector8f(float x, float y, float z, float w, float i, float j, float k, float l) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		this.i = i;
		this.j = j;
		this.k = k;
		this.l = l;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public float getW() {
		return w;
	}

	public float getI() {
		return i;
	}

	public float getJ() {
		return j;
	}

	public float getK() {
		return k;
	}

	public float getL() {
		return l;
	}

}