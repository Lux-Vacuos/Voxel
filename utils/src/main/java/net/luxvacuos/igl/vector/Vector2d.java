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
 *
 * Holds a 2-tuple vector.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision: 3418 $ $Id: Vector2d.java 3418 2010-09-28 21:11:35Z spasi
 *          $
 */

public class Vector2d extends Vectord implements Serializable, ReadableVector2d, WritableVector2d {

	private static final long serialVersionUID = 1L;

	public double x, y;

	/**
	 * Constructor for Vector3d.
	 */
	public Vector2d() {
		super();
	}

	/**
	 * Constructor
	 */
	public Vector2d(ReadableVector2d src) {
		set(src);
	}

	/**
	 * Constructor
	 */
	public Vector2d(double x, double y) {
		set(x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lwjgl.util.vector.WritableVector2f#set(float, float)
	 */
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Load from another Vector2d
	 * 
	 * @param src
	 *            The source vector
	 * @return this
	 */
	public Vector2d set(ReadableVector2d src) {
		x = src.getX();
		y = src.getY();
		return this;
	}

	/**
	 * @return the length squared of the vector
	 */
	public double lengthSquared() {
		return x * x + y * y;
	}

	/**
	 * Translate a vector
	 * 
	 * @param x
	 *            The translation in x
	 * @param y
	 *            the translation in y
	 * @return this
	 */
	public Vector2d translate(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	/**
	 * Negate a vector
	 * 
	 * @return this
	 */
	public Vectord negate() {
		x = -x;
		y = -y;
		return this;
	}

	/**
	 * Negate a vector and place the result in a destination vector.
	 * 
	 * @param dest
	 *            The destination vector or null if a new vector is to be
	 *            created
	 * @return the negated vector
	 */
	public Vector2d negate(Vector2d dest) {
		if (dest == null)
			dest = new Vector2d();
		dest.x = -x;
		dest.y = -y;
		return dest;
	}

	/**
	 * Normalise this vector and place the result in another vector.
	 * 
	 * @param dest
	 *            The destination vector, or null if a new vector is to be
	 *            created
	 * @return the normalised vector
	 */
	public Vector2d normalise(Vector2d dest) {
		double l = length();

		if (dest == null)
			dest = new Vector2d(x / l, y / l);
		else
			dest.set(x / l, y / l);

		return dest;
	}

	/**
	 * The dot product of two vectors is calculated as v1.x * v2.x + v1.y * v2.y
	 * + v1.z * v2.z
	 * 
	 * @param left
	 *            The LHS vector
	 * @param right
	 *            The RHS vector
	 * @return left dot right
	 */
	public static double dot(Vector2d left, Vector2d right) {
		return left.x * right.x + left.y * right.y;
	}

	/**
	 * Calculate the angle between two vectors, in radians
	 * 
	 * @param a
	 *            A vector
	 * @param b
	 *            The other vector
	 * @return the angle between the two vectors, in radians
	 */
	public static double angle(Vector2d a, Vector2d b) {
		double dls = dot(a, b) / (a.length() * b.length());
		if (dls < -1f)
			dls = -1f;
		else if (dls > 1.0f)
			dls = 1.0f;
		return (double) Math.acos(dls);
	}

	/**
	 * Add a vector to another vector and place the result in a destination
	 * vector.
	 * 
	 * @param left
	 *            The LHS vector
	 * @param right
	 *            The RHS vector
	 * @param dest
	 *            The destination vector, or null if a new vector is to be
	 *            created
	 * @return the sum of left and right in dest
	 */
	public static Vector2d add(Vector2d left, Vector2d right, Vector2d dest) {
		if (dest == null)
			return new Vector2d(left.x + right.x, left.y + right.y);
		else {
			dest.set(left.x + right.x, left.y + right.y);
			return dest;
		}
	}

	/**
	 * Subtract a vector from another vector and place the result in a
	 * destination vector.
	 * 
	 * @param left
	 *            The LHS vector
	 * @param right
	 *            The RHS vector
	 * @param dest
	 *            The destination vector, or null if a new vector is to be
	 *            created
	 * @return left minus right in dest
	 */
	public static Vector2d sub(Vector2d left, Vector2d right, Vector2d dest) {
		if (dest == null)
			return new Vector2d(left.x - right.x, left.y - right.y);
		else {
			dest.set(left.x - right.x, left.y - right.y);
			return dest;
		}
	}

	/**
	 * Store this vector in a FloatBuffer
	 * 
	 * @param buf
	 *            The buffer to store it in, at the current position
	 * @return this
	 */
	public Vectord store(DoubleBuffer buf) {
		buf.put(x);
		buf.put(y);
		return this;
	}

	/**
	 * Load this vector from a FloatBuffer
	 * 
	 * @param buf
	 *            The buffer to load it from, at the current position
	 * @return this
	 */
	public Vectord load(DoubleBuffer buf) {
		x = buf.get();
		y = buf.get();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lwjgl.vector.Vector#scale(float)
	 */
	public Vectord scale(double scale) {

		x *= scale;
		y *= scale;

		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(64);

		sb.append("Vector2d[");
		sb.append(x);
		sb.append(", ");
		sb.append(y);
		sb.append(']');
		return sb.toString();
	}

	/**
	 * @return x
	 */
	public final double getX() {
		return x;
	}

	/**
	 * @return y
	 */
	public final double getY() {
		return y;
	}

	/**
	 * Set X
	 * 
	 * @param x
	 */
	public final void setX(double x) {
		this.x = x;
	}

	/**
	 * Set Y
	 * 
	 * @param y
	 */
	public final void setY(double y) {
		this.y = y;
	}

}
