/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Lux Vacuos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
