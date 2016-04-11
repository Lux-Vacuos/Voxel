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

package net.luxvacuos.voxel.universal.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

/**
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class Vector8f extends Vector implements Serializable {
	private static final long serialVersionUID = 1L;
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

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}

	public float getI() {
		return i;
	}

	public void setI(float i) {
		this.i = i;
	}

	public float getJ() {
		return j;
	}

	public void setJ(float j) {
		this.j = j;
	}

	public float getK() {
		return k;
	}

	public void setK(float k) {
		this.k = k;
	}

	public float getL() {
		return l;
	}

	public void setL(float l) {
		this.l = l;
	}

	@Override
	public float lengthSquared() {
		return x * x + y * y + z * z + w * w + i * i + j * j + k * k + l * l;
	}

	@Override
	public Vector load(FloatBuffer buf) {
		return null;
	}

	@Override
	public Vector negate() {
		return null;
	}

	@Override
	public Vector store(FloatBuffer buf) {
		return null;
	}

	@Override
	public Vector scale(float scale) {
		return null;
	}

}
