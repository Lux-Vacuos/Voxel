/*
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.luxvacuos.igl.vector;

import java.io.Serializable;
import java.nio.DoubleBuffer;

/**
 * Holds a 4x4 float matrix.
 *
 * @author foo
 */
public class Matrix4d extends Matrixd implements Serializable {
	private static final long serialVersionUID = 1L;

	public double m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33;

	/**
	 * Construct a new matrix, initialized to the identity.
	 */
	public Matrix4d() {
		super();
		setIdentity();
	}

	public Matrix4d(final Matrix4d src) {
		super();
		load(src);
	}

	/**
	 * Returns a string representation of this matrix
	 */
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(m00).append(' ').append(m10).append(' ').append(m20).append(' ').append(m30).append('\n');
		buf.append(m01).append(' ').append(m11).append(' ').append(m21).append(' ').append(m31).append('\n');
		buf.append(m02).append(' ').append(m12).append(' ').append(m22).append(' ').append(m32).append('\n');
		buf.append(m03).append(' ').append(m13).append(' ').append(m23).append(' ').append(m33).append('\n');
		return buf.toString();
	}

	/**
	 * Set this matrix to be the identity matrix.
	 * 
	 * @return this
	 */
	public Matrixd setIdentity() {
		return setIdentity(this);
	}

	/**
	 * Set the given matrix to be the identity matrix.
	 * 
	 * @param m
	 *            The matrix to set to the identity
	 * @return m
	 */
	public static Matrix4d setIdentity(Matrix4d m) {
		m.m00 = 1.0f;
		m.m01 = 0.0f;
		m.m02 = 0.0f;
		m.m03 = 0.0f;
		m.m10 = 0.0f;
		m.m11 = 1.0f;
		m.m12 = 0.0f;
		m.m13 = 0.0f;
		m.m20 = 0.0f;
		m.m21 = 0.0f;
		m.m22 = 1.0f;
		m.m23 = 0.0f;
		m.m30 = 0.0f;
		m.m31 = 0.0f;
		m.m32 = 0.0f;
		m.m33 = 1.0f;

		return m;
	}

	/**
	 * Set this matrix to 0.
	 * 
	 * @return this
	 */
	public Matrixd setZero() {
		return setZero(this);
	}

	/**
	 * Set the given matrix to 0.
	 * 
	 * @param m
	 *            The matrix to set to 0
	 * @return m
	 */
	public static Matrix4d setZero(Matrix4d m) {
		m.m00 = 0.0f;
		m.m01 = 0.0f;
		m.m02 = 0.0f;
		m.m03 = 0.0f;
		m.m10 = 0.0f;
		m.m11 = 0.0f;
		m.m12 = 0.0f;
		m.m13 = 0.0f;
		m.m20 = 0.0f;
		m.m21 = 0.0f;
		m.m22 = 0.0f;
		m.m23 = 0.0f;
		m.m30 = 0.0f;
		m.m31 = 0.0f;
		m.m32 = 0.0f;
		m.m33 = 0.0f;

		return m;
	}

	/**
	 * Load from another matrix4f
	 * 
	 * @param src
	 *            The source matrix
	 * @return this
	 */
	public Matrix4d load(Matrix4d src) {
		return load(src, this);
	}

	/**
	 * Copy the source matrix to the destination matrix
	 * 
	 * @param src
	 *            The source matrix
	 * @param dest
	 *            The destination matrix, or null of a new one is to be created
	 * @return The copied matrix
	 */
	public static Matrix4d load(Matrix4d src, Matrix4d dest) {
		if (dest == null)
			dest = new Matrix4d();
		dest.m00 = src.m00;
		dest.m01 = src.m01;
		dest.m02 = src.m02;
		dest.m03 = src.m03;
		dest.m10 = src.m10;
		dest.m11 = src.m11;
		dest.m12 = src.m12;
		dest.m13 = src.m13;
		dest.m20 = src.m20;
		dest.m21 = src.m21;
		dest.m22 = src.m22;
		dest.m23 = src.m23;
		dest.m30 = src.m30;
		dest.m31 = src.m31;
		dest.m32 = src.m32;
		dest.m33 = src.m33;

		return dest;
	}

	/**
	 * Load from a float buffer. The buffer stores the matrix in column major
	 * (OpenGL) order.
	 *
	 * @param buf
	 *            A float buffer to read from
	 * @return this
	 */
	public Matrixd load(DoubleBuffer buf) {

		m00 = buf.get();
		m01 = buf.get();
		m02 = buf.get();
		m03 = buf.get();
		m10 = buf.get();
		m11 = buf.get();
		m12 = buf.get();
		m13 = buf.get();
		m20 = buf.get();
		m21 = buf.get();
		m22 = buf.get();
		m23 = buf.get();
		m30 = buf.get();
		m31 = buf.get();
		m32 = buf.get();
		m33 = buf.get();

		return this;
	}

	/**
	 * Load from a float buffer. The buffer stores the matrix in row major
	 * (maths) order.
	 *
	 * @param buf
	 *            A float buffer to read from
	 * @return this
	 */
	public Matrixd loadTranspose(DoubleBuffer buf) {

		m00 = buf.get();
		m10 = buf.get();
		m20 = buf.get();
		m30 = buf.get();
		m01 = buf.get();
		m11 = buf.get();
		m21 = buf.get();
		m31 = buf.get();
		m02 = buf.get();
		m12 = buf.get();
		m22 = buf.get();
		m32 = buf.get();
		m03 = buf.get();
		m13 = buf.get();
		m23 = buf.get();
		m33 = buf.get();

		return this;
	}

	/**
	 * Store this matrix in a float buffer. The matrix is stored in column major
	 * (openGL) order.
	 * 
	 * @param buf
	 *            The buffer to store this matrix in
	 */
	public Matrixd store(DoubleBuffer buf) {
		buf.put(m00);
		buf.put(m01);
		buf.put(m02);
		buf.put(m03);
		buf.put(m10);
		buf.put(m11);
		buf.put(m12);
		buf.put(m13);
		buf.put(m20);
		buf.put(m21);
		buf.put(m22);
		buf.put(m23);
		buf.put(m30);
		buf.put(m31);
		buf.put(m32);
		buf.put(m33);
		return this;
	}

	/**
	 * Store this matrix in a float buffer. The matrix is stored in row major
	 * (maths) order.
	 * 
	 * @param buf
	 *            The buffer to store this matrix in
	 */
	public Matrixd storeTranspose(DoubleBuffer buf) {
		buf.put(m00);
		buf.put(m10);
		buf.put(m20);
		buf.put(m30);
		buf.put(m01);
		buf.put(m11);
		buf.put(m21);
		buf.put(m31);
		buf.put(m02);
		buf.put(m12);
		buf.put(m22);
		buf.put(m32);
		buf.put(m03);
		buf.put(m13);
		buf.put(m23);
		buf.put(m33);
		return this;
	}

	/**
	 * Store the rotation portion of this matrix in a float buffer. The matrix
	 * is stored in column major (openGL) order.
	 * 
	 * @param buf
	 *            The buffer to store this matrix in
	 */
	public Matrixd store3f(DoubleBuffer buf) {
		buf.put(m00);
		buf.put(m01);
		buf.put(m02);
		buf.put(m10);
		buf.put(m11);
		buf.put(m12);
		buf.put(m20);
		buf.put(m21);
		buf.put(m22);
		return this;
	}

	/**
	 * Add two matrices together and place the result in a third matrix.
	 * 
	 * @param left
	 *            The left source matrix
	 * @param right
	 *            The right source matrix
	 * @param dest
	 *            The destination matrix, or null if a new one is to be created
	 * @return the destination matrix
	 */
	public static Matrix4d add(Matrix4d left, Matrix4d right, Matrix4d dest) {
		if (dest == null)
			dest = new Matrix4d();

		dest.m00 = left.m00 + right.m00;
		dest.m01 = left.m01 + right.m01;
		dest.m02 = left.m02 + right.m02;
		dest.m03 = left.m03 + right.m03;
		dest.m10 = left.m10 + right.m10;
		dest.m11 = left.m11 + right.m11;
		dest.m12 = left.m12 + right.m12;
		dest.m13 = left.m13 + right.m13;
		dest.m20 = left.m20 + right.m20;
		dest.m21 = left.m21 + right.m21;
		dest.m22 = left.m22 + right.m22;
		dest.m23 = left.m23 + right.m23;
		dest.m30 = left.m30 + right.m30;
		dest.m31 = left.m31 + right.m31;
		dest.m32 = left.m32 + right.m32;
		dest.m33 = left.m33 + right.m33;

		return dest;
	}

	/**
	 * Subtract the right matrix from the left and place the result in a third
	 * matrix.
	 * 
	 * @param left
	 *            The left source matrix
	 * @param right
	 *            The right source matrix
	 * @param dest
	 *            The destination matrix, or null if a new one is to be created
	 * @return the destination matrix
	 */
	public static Matrix4d sub(Matrix4d left, Matrix4d right, Matrix4d dest) {
		if (dest == null)
			dest = new Matrix4d();

		dest.m00 = left.m00 - right.m00;
		dest.m01 = left.m01 - right.m01;
		dest.m02 = left.m02 - right.m02;
		dest.m03 = left.m03 - right.m03;
		dest.m10 = left.m10 - right.m10;
		dest.m11 = left.m11 - right.m11;
		dest.m12 = left.m12 - right.m12;
		dest.m13 = left.m13 - right.m13;
		dest.m20 = left.m20 - right.m20;
		dest.m21 = left.m21 - right.m21;
		dest.m22 = left.m22 - right.m22;
		dest.m23 = left.m23 - right.m23;
		dest.m30 = left.m30 - right.m30;
		dest.m31 = left.m31 - right.m31;
		dest.m32 = left.m32 - right.m32;
		dest.m33 = left.m33 - right.m33;

		return dest;
	}

	/**
	 * Multiply the right matrix by the left and place the result in a third
	 * matrix.
	 * 
	 * @param left
	 *            The left source matrix
	 * @param right
	 *            The right source matrix
	 * @param dest
	 *            The destination matrix, or null if a new one is to be created
	 * @return the destination matrix
	 */
	public static Matrix4d mul(Matrix4d left, Matrix4d right, Matrix4d dest) {
		if (dest == null)
			dest = new Matrix4d();

		double m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02 + left.m30 * right.m03;
		double m01 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02 + left.m31 * right.m03;
		double m02 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02 + left.m32 * right.m03;
		double m03 = left.m03 * right.m00 + left.m13 * right.m01 + left.m23 * right.m02 + left.m33 * right.m03;
		double m10 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12 + left.m30 * right.m13;
		double m11 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12 + left.m31 * right.m13;
		double m12 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12 + left.m32 * right.m13;
		double m13 = left.m03 * right.m10 + left.m13 * right.m11 + left.m23 * right.m12 + left.m33 * right.m13;
		double m20 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22 + left.m30 * right.m23;
		double m21 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22 + left.m31 * right.m23;
		double m22 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22 + left.m32 * right.m23;
		double m23 = left.m03 * right.m20 + left.m13 * right.m21 + left.m23 * right.m22 + left.m33 * right.m23;
		double m30 = left.m00 * right.m30 + left.m10 * right.m31 + left.m20 * right.m32 + left.m30 * right.m33;
		double m31 = left.m01 * right.m30 + left.m11 * right.m31 + left.m21 * right.m32 + left.m31 * right.m33;
		double m32 = left.m02 * right.m30 + left.m12 * right.m31 + left.m22 * right.m32 + left.m32 * right.m33;
		double m33 = left.m03 * right.m30 + left.m13 * right.m31 + left.m23 * right.m32 + left.m33 * right.m33;

		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m03 = m03;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m13 = m13;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;
		dest.m23 = m23;
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;

		return dest;
	}

	/**
	 * Transform a Vectord by a matrix and return the result in a destination
	 * vector.
	 * 
	 * @param left
	 *            The left matrix
	 * @param right
	 *            The right vector
	 * @param dest
	 *            The destination vector, or null if a new one is to be created
	 * @return the destination vector
	 */
	public static Vector4d transform(Matrix4d left, Vector4d right, Vector4d dest) {
		if (dest == null)
			dest = new Vector4d();

		double x = left.m00 * right.x + left.m10 * right.y + left.m20 * right.z + left.m30 * right.w;
		double y = left.m01 * right.x + left.m11 * right.y + left.m21 * right.z + left.m31 * right.w;
		double z = left.m02 * right.x + left.m12 * right.y + left.m22 * right.z + left.m32 * right.w;
		double w = left.m03 * right.x + left.m13 * right.y + left.m23 * right.z + left.m33 * right.w;

		dest.x = x;
		dest.y = y;
		dest.z = z;
		dest.w = w;

		return dest;
	}

	/**
	 * Transpose this matrix
	 * 
	 * @return this
	 */
	public Matrixd transpose() {
		return transpose(this);
	}

	/**
	 * Translate this matrix
	 * 
	 * @param vec
	 *            The vector to translate by
	 * @return this
	 */
	public Matrix4d translate(Vector2d vec) {
		return translate(vec, this);
	}

	/**
	 * Translate this matrix
	 * 
	 * @param vec
	 *            The vector to translate by
	 * @return this
	 */
	public Matrix4d translate(Vector3d vec) {
		return translate(vec, this);
	}

	/**
	 * Scales this matrix
	 * 
	 * @param vec
	 *            The vector to scale by
	 * @return this
	 */
	public Matrix4d scale(Vector3d vec) {
		return scale(vec, this, this);
	}

	/**
	 * Scales the source matrix and put the result in the destination matrix
	 * 
	 * @param vec
	 *            The vector to scale by
	 * @param src
	 *            The source matrix
	 * @param dest
	 *            The destination matrix, or null if a new matrix is to be
	 *            created
	 * @return The scaled matrix
	 */
	public static Matrix4d scale(Vector3d vec, Matrix4d src, Matrix4d dest) {
		if (dest == null)
			dest = new Matrix4d();
		dest.m00 = src.m00 * vec.x;
		dest.m01 = src.m01 * vec.x;
		dest.m02 = src.m02 * vec.x;
		dest.m03 = src.m03 * vec.x;
		dest.m10 = src.m10 * vec.y;
		dest.m11 = src.m11 * vec.y;
		dest.m12 = src.m12 * vec.y;
		dest.m13 = src.m13 * vec.y;
		dest.m20 = src.m20 * vec.z;
		dest.m21 = src.m21 * vec.z;
		dest.m22 = src.m22 * vec.z;
		dest.m23 = src.m23 * vec.z;
		return dest;
	}

	public Vector3d unproject(Vector3d winCoords, Vector4d viewport, Vector3d dest) {
		double winX = winCoords.x;
		double winY = winCoords.y;
		double winZ = winCoords.z;
		double a = m00 * m11 - m01 * m10;
		double b = m00 * m12 - m02 * m10;
		double c = m00 * m13 - m03 * m10;
		double d = m01 * m12 - m02 * m11;
		double e = m01 * m13 - m03 * m11;
		double f = m02 * m13 - m03 * m12;
		double g = m20 * m31 - m21 * m30;
		double h = m20 * m32 - m22 * m30;
		double i = m20 * m33 - m23 * m30;
		double j = m21 * m32 - m22 * m31;
		double k = m21 * m33 - m23 * m31;
		double l = m22 * m33 - m23 * m32;
		double det = a * l - b * k + c * j + d * i - e * h + f * g;
		det = 1.0f / det;
		double im00 = (m11 * l - m12 * k + m13 * j) * det;
		double im01 = (-m01 * l + m02 * k - m03 * j) * det;
		double im02 = (m31 * f - m32 * e + m33 * d) * det;
		double im03 = (-m21 * f + m22 * e - m23 * d) * det;
		double im10 = (-m10 * l + m12 * i - m13 * h) * det;
		double im11 = (m00 * l - m02 * i + m03 * h) * det;
		double im12 = (-m30 * f + m32 * c - m33 * b) * det;
		double im13 = (m20 * f - m22 * c + m23 * b) * det;
		double im20 = (m10 * k - m11 * i + m13 * g) * det;
		double im21 = (-m00 * k + m01 * i - m03 * g) * det;
		double im22 = (m30 * e - m31 * c + m33 * a) * det;
		double im23 = (-m20 * e + m21 * c - m23 * a) * det;
		double im30 = (-m10 * j + m11 * h - m12 * g) * det;
		double im31 = (m00 * j - m01 * h + m02 * g) * det;
		double im32 = (-m30 * d + m31 * b - m32 * a) * det;
		double im33 = (m20 * d - m21 * b + m22 * a) * det;
		double ndcX = (winX - viewport.x) / viewport.z * 2.0f - 1.0f;
		double ndcY = (winY - viewport.y) / viewport.w * 2.0f - 1.0f;
		double ndcZ = 2.0f * winZ - 1.0f;
		dest.x = im00 * ndcX + im10 * ndcY + im20 * ndcZ + im30;
		dest.y = im01 * ndcX + im11 * ndcY + im21 * ndcZ + im31;
		dest.z = im02 * ndcX + im12 * ndcY + im22 * ndcZ + im32;
		double w = im03 * ndcX + im13 * ndcY + im23 * ndcZ + im33;
		dest.div(w);
		return dest;
	}

	//
	public static Vector2d Project(Vector3d pos, Matrix4d projection, Matrix4d view, Vector4d viewport) {
		Vector4d winCoordsDest = new Vector4d();
		winCoordsDest.set(pos.x, pos.y, pos.z, 1.0f);
		Matrix4d.transform(view, winCoordsDest, winCoordsDest);
		Matrix4d.transform(projection, winCoordsDest, winCoordsDest);
		winCoordsDest.div(winCoordsDest.w);
		winCoordsDest.x = (winCoordsDest.x * 0.5f + 0.5f) * viewport.z + viewport.x;
		winCoordsDest.y = (winCoordsDest.y * 0.5f + 0.5f) * viewport.w + viewport.y;
		winCoordsDest.z = (1.0f + winCoordsDest.z) * 0.5f;
		return new Vector2d(winCoordsDest.x, winCoordsDest.y);
	}

	/**
	 * Rotates the matrix around the given axis the specified angle
	 * 
	 * @param angle
	 *            the angle, in radians.
	 * @param axis
	 *            The vector representing the rotation axis. Must be normalized.
	 * @return this
	 */
	public Matrix4d rotate(double angle, Vector3d axis) {
		return rotate(angle, axis, this);
	}

	/**
	 * Rotates the matrix around the given axis the specified angle
	 * 
	 * @param angle
	 *            the angle, in radians.
	 * @param axis
	 *            The vector representing the rotation axis. Must be normalized.
	 * @param dest
	 *            The matrix to put the result, or null if a new matrix is to be
	 *            created
	 * @return The rotated matrix
	 */
	public Matrix4d rotate(double angle, Vector3d axis, Matrix4d dest) {
		return rotate(angle, axis, this, dest);
	}

	/**
	 * Rotates the source matrix around the given axis the specified angle and
	 * put the result in the destination matrix.
	 * 
	 * @param angle
	 *            the angle, in radians.
	 * @param axis
	 *            The vector representing the rotation axis. Must be normalized.
	 * @param src
	 *            The matrix to rotate
	 * @param dest
	 *            The matrix to put the result, or null if a new matrix is to be
	 *            created
	 * @return The rotated matrix
	 */
	public static Matrix4d rotate(double angle, Vector3d axis, Matrix4d src, Matrix4d dest) {
		if (dest == null)
			dest = new Matrix4d();
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		double oneminusc = 1.0f - c;
		double xy = axis.x * axis.y;
		double yz = axis.y * axis.z;
		double xz = axis.x * axis.z;
		double xs = axis.x * s;
		double ys = axis.y * s;
		double zs = axis.z * s;

		double f00 = axis.x * axis.x * oneminusc + c;
		double f01 = xy * oneminusc + zs;
		double f02 = xz * oneminusc - ys;
		// n[3] not used
		double f10 = xy * oneminusc - zs;
		double f11 = axis.y * axis.y * oneminusc + c;
		double f12 = yz * oneminusc + xs;
		// n[7] not used
		double f20 = xz * oneminusc + ys;
		double f21 = yz * oneminusc - xs;
		double f22 = axis.z * axis.z * oneminusc + c;

		double t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02;
		double t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02;
		double t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02;
		double t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02;
		double t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12;
		double t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12;
		double t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12;
		double t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12;
		dest.m20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22;
		dest.m21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22;
		dest.m22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22;
		dest.m23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22;
		dest.m00 = t00;
		dest.m01 = t01;
		dest.m02 = t02;
		dest.m03 = t03;
		dest.m10 = t10;
		dest.m11 = t11;
		dest.m12 = t12;
		dest.m13 = t13;
		return dest;
	}

	/**
	 * Translate this matrix and stash the result in another matrix
	 * 
	 * @param vec
	 *            The vector to translate by
	 * @param dest
	 *            The destination matrix or null if a new matrix is to be
	 *            created
	 * @return the translated matrix
	 */
	public Matrix4d translate(Vector3d vec, Matrix4d dest) {
		return translate(vec, this, dest);
	}

	/**
	 * Translate the source matrix and stash the result in the destination
	 * matrix
	 * 
	 * @param vec
	 *            The vector to translate by
	 * @param src
	 *            The source matrix
	 * @param dest
	 *            The destination matrix or null if a new matrix is to be
	 *            created
	 * @return The translated matrix
	 */
	public static Matrix4d translate(Vector3d vec, Matrix4d src, Matrix4d dest) {
		if (dest == null)
			dest = new Matrix4d();

		dest.m30 += src.m00 * vec.x + src.m10 * vec.y + src.m20 * vec.z;
		dest.m31 += src.m01 * vec.x + src.m11 * vec.y + src.m21 * vec.z;
		dest.m32 += src.m02 * vec.x + src.m12 * vec.y + src.m22 * vec.z;
		dest.m33 += src.m03 * vec.x + src.m13 * vec.y + src.m23 * vec.z;

		return dest;
	}

	/**
	 * Translate this matrix and stash the result in another matrix
	 * 
	 * @param vec
	 *            The vector to translate by
	 * @param dest
	 *            The destination matrix or null if a new matrix is to be
	 *            created
	 * @return the translated matrix
	 */
	public Matrix4d translate(Vector2d vec, Matrix4d dest) {
		return translate(vec, this, dest);
	}

	/**
	 * Translate the source matrix and stash the result in the destination
	 * matrix
	 * 
	 * @param vec
	 *            The vector to translate by
	 * @param src
	 *            The source matrix
	 * @param dest
	 *            The destination matrix or null if a new matrix is to be
	 *            created
	 * @return The translated matrix
	 */
	public static Matrix4d translate(Vector2d vec, Matrix4d src, Matrix4d dest) {
		if (dest == null)
			dest = new Matrix4d();

		dest.m30 += src.m00 * vec.x + src.m10 * vec.y;
		dest.m31 += src.m01 * vec.x + src.m11 * vec.y;
		dest.m32 += src.m02 * vec.x + src.m12 * vec.y;
		dest.m33 += src.m03 * vec.x + src.m13 * vec.y;

		return dest;
	}

	/**
	 * Transpose this matrix and place the result in another matrix
	 * 
	 * @param dest
	 *            The destination matrix or null if a new matrix is to be
	 *            created
	 * @return the transposed matrix
	 */
	public Matrix4d transpose(Matrix4d dest) {
		return transpose(this, dest);
	}

	/**
	 * Transpose the source matrix and place the result in the destination
	 * matrix
	 * 
	 * @param src
	 *            The source matrix
	 * @param dest
	 *            The destination matrix or null if a new matrix is to be
	 *            created
	 * @return the transposed matrix
	 */
	public static Matrix4d transpose(Matrix4d src, Matrix4d dest) {
		if (dest == null)
			dest = new Matrix4d();
		double m00 = src.m00;
		double m01 = src.m10;
		double m02 = src.m20;
		double m03 = src.m30;
		double m10 = src.m01;
		double m11 = src.m11;
		double m12 = src.m21;
		double m13 = src.m31;
		double m20 = src.m02;
		double m21 = src.m12;
		double m22 = src.m22;
		double m23 = src.m32;
		double m30 = src.m03;
		double m31 = src.m13;
		double m32 = src.m23;
		double m33 = src.m33;

		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m03 = m03;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m13 = m13;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;
		dest.m23 = m23;
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;

		return dest;
	}

	/**
	 * @return the determinant of the matrix
	 */
	public double determinant() {
		double f = m00 * ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32) - m13 * m22 * m31 - m11 * m23 * m32
				- m12 * m21 * m33);
		f -= m01 * ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32) - m13 * m22 * m30 - m10 * m23 * m32
				- m12 * m20 * m33);
		f += m02 * ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31) - m13 * m21 * m30 - m10 * m23 * m31
				- m11 * m20 * m33);
		f -= m03 * ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31) - m12 * m21 * m30 - m10 * m22 * m31
				- m11 * m20 * m32);
		return f;
	}

	/**
	 * Calculate the determinant of a 3x3 matrix
	 * 
	 * @return result
	 */

	private static double determinant3x3(double t00, double t01, double t02, double t10, double t11, double t12,
			double t20, double t21, double t22) {
		return t00 * (t11 * t22 - t12 * t21) + t01 * (t12 * t20 - t10 * t22) + t02 * (t10 * t21 - t11 * t20);
	}

	/**
	 * Invert this matrix
	 * 
	 * @return this if successful, null otherwise
	 */
	public Matrixd invert() {
		return invert(this, this);
	}

	/**
	 * Invert the source matrix and put the result in the destination
	 * 
	 * @param src
	 *            The source matrix
	 * @param dest
	 *            The destination matrix, or null if a new matrix is to be
	 *            created
	 * @return The inverted matrix if successful, null otherwise
	 */
	public static Matrix4d invert(Matrix4d src, Matrix4d dest) {
		double determinant = src.determinant();

		if (determinant != 0) {
			/*
			 * m00 m01 m02 m03 m10 m11 m12 m13 m20 m21 m22 m23 m30 m31 m32 m33
			 */
			if (dest == null)
				dest = new Matrix4d();
			double determinant_inv = 1f / determinant;

			// first row
			double t00 = determinant3x3(src.m11, src.m12, src.m13, src.m21, src.m22, src.m23, src.m31, src.m32,
					src.m33);
			double t01 = -determinant3x3(src.m10, src.m12, src.m13, src.m20, src.m22, src.m23, src.m30, src.m32,
					src.m33);
			double t02 = determinant3x3(src.m10, src.m11, src.m13, src.m20, src.m21, src.m23, src.m30, src.m31,
					src.m33);
			double t03 = -determinant3x3(src.m10, src.m11, src.m12, src.m20, src.m21, src.m22, src.m30, src.m31,
					src.m32);
			// second row
			double t10 = -determinant3x3(src.m01, src.m02, src.m03, src.m21, src.m22, src.m23, src.m31, src.m32,
					src.m33);
			double t11 = determinant3x3(src.m00, src.m02, src.m03, src.m20, src.m22, src.m23, src.m30, src.m32,
					src.m33);
			double t12 = -determinant3x3(src.m00, src.m01, src.m03, src.m20, src.m21, src.m23, src.m30, src.m31,
					src.m33);
			double t13 = determinant3x3(src.m00, src.m01, src.m02, src.m20, src.m21, src.m22, src.m30, src.m31,
					src.m32);
			// third row
			double t20 = determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m31, src.m32,
					src.m33);
			double t21 = -determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m30, src.m32,
					src.m33);
			double t22 = determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m30, src.m31,
					src.m33);
			double t23 = -determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m30, src.m31,
					src.m32);
			// fourth row
			double t30 = -determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m21, src.m22,
					src.m23);
			double t31 = determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m20, src.m22,
					src.m23);
			double t32 = -determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m20, src.m21,
					src.m23);
			double t33 = determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m20, src.m21,
					src.m22);

			// transpose and divide by the determinant
			dest.m00 = t00 * determinant_inv;
			dest.m11 = t11 * determinant_inv;
			dest.m22 = t22 * determinant_inv;
			dest.m33 = t33 * determinant_inv;
			dest.m01 = t10 * determinant_inv;
			dest.m10 = t01 * determinant_inv;
			dest.m20 = t02 * determinant_inv;
			dest.m02 = t20 * determinant_inv;
			dest.m12 = t21 * determinant_inv;
			dest.m21 = t12 * determinant_inv;
			dest.m03 = t30 * determinant_inv;
			dest.m30 = t03 * determinant_inv;
			dest.m13 = t31 * determinant_inv;
			dest.m31 = t13 * determinant_inv;
			dest.m32 = t23 * determinant_inv;
			dest.m23 = t32 * determinant_inv;
			return dest;
		} else
			return null;
	}

	/**
	 * Negate this matrix
	 * 
	 * @return this
	 */
	public Matrixd negate() {
		return negate(this);
	}

	/**
	 * Negate this matrix and place the result in a destination matrix.
	 * 
	 * @param dest
	 *            The destination matrix, or null if a new matrix is to be
	 *            created
	 * @return the negated matrix
	 */
	public Matrix4d negate(Matrix4d dest) {
		return negate(this, dest);
	}

	/**
	 * Negate this matrix and place the result in a destination matrix.
	 * 
	 * @param src
	 *            The source matrix
	 * @param dest
	 *            The destination matrix, or null if a new matrix is to be
	 *            created
	 * @return The negated matrix
	 */
	public static Matrix4d negate(Matrix4d src, Matrix4d dest) {
		if (dest == null)
			dest = new Matrix4d();

		dest.m00 = -src.m00;
		dest.m01 = -src.m01;
		dest.m02 = -src.m02;
		dest.m03 = -src.m03;
		dest.m10 = -src.m10;
		dest.m11 = -src.m11;
		dest.m12 = -src.m12;
		dest.m13 = -src.m13;
		dest.m20 = -src.m20;
		dest.m21 = -src.m21;
		dest.m22 = -src.m22;
		dest.m23 = -src.m23;
		dest.m30 = -src.m30;
		dest.m31 = -src.m31;
		dest.m32 = -src.m32;
		dest.m33 = -src.m33;

		return dest;
	}
}
