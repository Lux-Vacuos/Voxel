/*
 * This file is part of Infinity Game Library
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

package net.luxvacuos.igl.vector;

import java.io.Serializable;
import java.nio.DoubleBuffer;

/**
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class Vector8f extends Vectord implements Serializable {
	private static final long serialVersionUID = 1L;
	private double x, y, z, w, i, j, k, l;

	public Vector8f(double x, double y, double z, double w, double i, double j, double k, double l) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		this.i = i;
		this.j = j;
		this.k = k;
		this.l = l;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	public double getI() {
		return i;
	}

	public void setI(double i) {
		this.i = i;
	}

	public double getJ() {
		return j;
	}

	public void setJ(double j) {
		this.j = j;
	}

	public double getK() {
		return k;
	}

	public void setK(double k) {
		this.k = k;
	}

	public double getL() {
		return l;
	}

	public void setL(double l) {
		this.l = l;
	}

	@Override
	public double lengthSquared() {
		return x * x + y * y + z * z + w * w + i * i + j * j + k * k + l * l;
	}

	@Override
	public Vectord load(DoubleBuffer buf) {
		return null;
	}

	@Override
	public Vectord negate() {
		return null;
	}

	@Override
	public Vectord store(DoubleBuffer buf) {
		return null;
	}

	@Override
	public Vectord scale(double scale) {
		return null;
	}

}
