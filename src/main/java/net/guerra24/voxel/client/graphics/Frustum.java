/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
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

package net.guerra24.voxel.client.graphics;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.client.world.entities.Camera;
import net.guerra24.voxel.universal.util.vector.Matrix4f;
import net.guerra24.voxel.universal.util.vector.Vector3f;

/**
 * Frustum Culling
 * 
 * @author Ron Sullivan (modified by Thomas Hourdel(modified by Guerra24))
 *         <thomas.hourdel@libertysurf.fr>
 * @category Rendering
 */
public class Frustum {

	/**
	 * Frustum Culling Data
	 */
	private final int RIGHT = 0;
	private final int LEFT = 1;
	private final int BOTTOM = 2;
	private final int TOP = 3;
	private final int BACK = 4;
	private final int FRONT = 5;
	private final int A = 0;
	private final int B = 1;
	private final int C = 2;
	private final int D = 3;
	private float[][] m_Frustum = new float[6][4];
	private Matrix4f clip_ = new Matrix4f();
	private FloatBuffer clip_b;

	/**
	 * Normalize Frustum Plane
	 * 
	 * @param frustum
	 *            2D float frustum
	 * @param side
	 *            side
	 */
	public void normalizePlane(float[][] frustum, int side) {
		float magnitude = (float) Math.sqrt(frustum[side][A] * frustum[side][A] + frustum[side][B] * frustum[side][B]
				+ frustum[side][C] * frustum[side][C]);

		frustum[side][A] /= magnitude;
		frustum[side][B] /= magnitude;
		frustum[side][C] /= magnitude;
		frustum[side][D] /= magnitude;
	}

	/**
	 * Update Frustum
	 * 
	 * @param gm
	 *            GameResources
	 */
	public void calculateFrustum(Matrix4f projectionMatrix, Camera camera) {
		float[] clip = new float[16];

		Matrix4f.mul(projectionMatrix, Maths.createViewMatrix(camera), clip_);
		clip_b.rewind();
		clip_.store(clip_b);
		clip_b.rewind();
		clip_b.get(clip);

		m_Frustum[RIGHT][A] = clip[3] - clip[0];
		m_Frustum[RIGHT][B] = clip[7] - clip[4];
		m_Frustum[RIGHT][C] = clip[11] - clip[8];
		m_Frustum[RIGHT][D] = clip[15] - clip[12];
		normalizePlane(m_Frustum, RIGHT);
		m_Frustum[LEFT][A] = clip[3] + clip[0];
		m_Frustum[LEFT][B] = clip[7] + clip[4];
		m_Frustum[LEFT][C] = clip[11] + clip[8];
		m_Frustum[LEFT][D] = clip[15] + clip[12];
		normalizePlane(m_Frustum, LEFT);
		m_Frustum[BOTTOM][A] = clip[3] + clip[1];
		m_Frustum[BOTTOM][B] = clip[7] + clip[5];
		m_Frustum[BOTTOM][C] = clip[11] + clip[9];
		m_Frustum[BOTTOM][D] = clip[15] + clip[13];
		normalizePlane(m_Frustum, BOTTOM);
		m_Frustum[TOP][A] = clip[3] - clip[1];
		m_Frustum[TOP][B] = clip[7] - clip[5];
		m_Frustum[TOP][C] = clip[11] - clip[9];
		m_Frustum[TOP][D] = clip[15] - clip[13];
		normalizePlane(m_Frustum, TOP);
		m_Frustum[BACK][A] = clip[3] - clip[2];
		m_Frustum[BACK][B] = clip[7] - clip[6];
		m_Frustum[BACK][C] = clip[11] - clip[10];
		m_Frustum[BACK][D] = clip[15] - clip[14];
		normalizePlane(m_Frustum, BACK);
		m_Frustum[FRONT][A] = clip[3] + clip[2];
		m_Frustum[FRONT][B] = clip[7] + clip[6];
		m_Frustum[FRONT][C] = clip[11] + clip[10];
		m_Frustum[FRONT][D] = clip[15] + clip[14];

		normalizePlane(m_Frustum, FRONT);
	}

	/**
	 * Calculate a point in Frustum
	 * 
	 * @param x
	 *            X Position
	 * @param y
	 *            Y Position
	 * @param z
	 *            Z Position
	 * @return true if in Frustum
	 */
	public boolean pointInFrustum(float x, float y, float z) {
		for (int i = 0; i < 6; i++) {
			if (m_Frustum[i][A] * x + m_Frustum[i][B] * y + m_Frustum[i][C] * z + m_Frustum[i][D] <= 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Calculate a sphere in Frustum
	 * 
	 * @param x
	 *            X Position
	 * @param y
	 *            Y Position
	 * @param z
	 *            Z Position
	 * @param radius
	 *            Sphere Radius
	 * @return true if in Frustum
	 */
	public boolean sphereInFrustum(float x, float y, float z, float radius) {
		for (int i = 0; i < 6; i++) {
			if (m_Frustum[i][A] * x + m_Frustum[i][B] * y + m_Frustum[i][C] * z + m_Frustum[i][D] <= -radius) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Calculate a cube in Frustum
	 * 
	 * @param center
	 *            Center of the Cube
	 * @param size
	 *            Size of the cube
	 * @return true if in Frustum
	 */
	public boolean cubeInFrustum(Vector3f center, float size) {
		return cubeInFrustum(center.x, center.y, center.z, size);
	}

	/**
	 * Calculate a cube in Frustum
	 * 
	 * @param x
	 *            X Position
	 * @param y
	 *            Y Position
	 * @param z
	 *            Z Position
	 * @param size
	 *            Size of the cube
	 * @return true if in Frustum
	 */
	public boolean cubeInFrustum(float x, float y, float z, float size) {
		for (int i = 0; i < 6; i++) {
			if (m_Frustum[i][A] * (x - size) + m_Frustum[i][B] * (y - size) + m_Frustum[i][C] * (z - size)
					+ m_Frustum[i][D] > 0)
				continue;
			if (m_Frustum[i][A] * (x + size) + m_Frustum[i][B] * (y - size) + m_Frustum[i][C] * (z - size)
					+ m_Frustum[i][D] > 0)
				continue;
			if (m_Frustum[i][A] * (x - size) + m_Frustum[i][B] * (y + size) + m_Frustum[i][C] * (z - size)
					+ m_Frustum[i][D] > 0)
				continue;
			if (m_Frustum[i][A] * (x + size) + m_Frustum[i][B] * (y + size) + m_Frustum[i][C] * (z - size)
					+ m_Frustum[i][D] > 0)
				continue;
			if (m_Frustum[i][A] * (x - size) + m_Frustum[i][B] * (y - size) + m_Frustum[i][C] * (z + size)
					+ m_Frustum[i][D] > 0)
				continue;
			if (m_Frustum[i][A] * (x + size) + m_Frustum[i][B] * (y - size) + m_Frustum[i][C] * (z + size)
					+ m_Frustum[i][D] > 0)
				continue;
			if (m_Frustum[i][A] * (x - size) + m_Frustum[i][B] * (y + size) + m_Frustum[i][C] * (z + size)
					+ m_Frustum[i][D] > 0)
				continue;
			if (m_Frustum[i][A] * (x + size) + m_Frustum[i][B] * (y + size) + m_Frustum[i][C] * (z + size)
					+ m_Frustum[i][D] > 0)
				continue;
			return false;
		}

		return true;
	}

	/**
	 * Calculate a cube in Frustum
	 * 
	 * @param x1
	 *            X1 Position
	 * @param y1
	 *            Y1 Postion
	 * @param z1
	 *            Z1 Position
	 * @param x2
	 *            X2 Position
	 * @param y2
	 *            Y2 Position
	 * @param z2
	 *            Z2 Position
	 * @return true if in Frustum
	 */
	public boolean cubeInFrustum(float x1, float y1, float z1, float x2, float y2, float z2) {
		for (int i = 0; i < 6; i++) {
			if ((this.m_Frustum[i][A] * x1 + this.m_Frustum[i][B] * y1 + this.m_Frustum[i][C] * z1
					+ this.m_Frustum[i][D] <= 0.0F)
					&& (this.m_Frustum[i][A] * x2 + this.m_Frustum[i][B] * y1 + this.m_Frustum[i][C] * z1
							+ this.m_Frustum[i][D] <= 0.0F)
					&& (this.m_Frustum[i][A] * x1 + this.m_Frustum[i][B] * y2 + this.m_Frustum[i][C] * z1
							+ this.m_Frustum[i][D] <= 0.0F)
					&& (this.m_Frustum[i][A] * x2 + this.m_Frustum[i][B] * y2 + this.m_Frustum[i][C] * z1
							+ this.m_Frustum[i][D] <= 0.0F)
					&& (this.m_Frustum[i][A] * x1 + this.m_Frustum[i][B] * y1 + this.m_Frustum[i][C] * z2
							+ this.m_Frustum[i][D] <= 0.0F)
					&& (this.m_Frustum[i][A] * x2 + this.m_Frustum[i][B] * y1 + this.m_Frustum[i][C] * z2
							+ this.m_Frustum[i][D] <= 0.0F)
					&& (this.m_Frustum[i][A] * x1 + this.m_Frustum[i][B] * y2 + this.m_Frustum[i][C] * z2
							+ this.m_Frustum[i][D] <= 0.0F)
					&& (this.m_Frustum[i][A] * x2 + this.m_Frustum[i][B] * y2 + this.m_Frustum[i][C] * z2
							+ this.m_Frustum[i][D] <= 0.0F)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Frustum constructor
	 * 
	 */
	public Frustum() {
		clip_b = BufferUtils.createFloatBuffer(16);
	}
}
