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

package net.luxvacuos.voxel.client.rendering.api.opengl;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;

/**
 * Frustum Culling
 * 
 * @author Ron Sullivan (modified by Thomas Hourdel
 *         <thomas.hourdel@libertysurf.fr> (modified by Guerra24
 *         <pablo230699@hotmail.com>))
 * 
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
	private double[][] m_Frustum = new double[6][4];
	private Matrix4d clip_ = new Matrix4d();
	private DoubleBuffer clip_b;

	/**
	 * Normalize Frustum Plane
	 * 
	 * @param frustum
	 *            2D float frustum
	 * @param side
	 *            side
	 */
	public void normalizePlane(double[][] frustum, int side) {
		double magnitude = Math.sqrt(frustum[side][A] * frustum[side][A] + frustum[side][B] * frustum[side][B]
				+ frustum[side][C] * frustum[side][C]);

		frustum[side][A] /= magnitude;
		frustum[side][B] /= magnitude;
		frustum[side][C] /= magnitude;
		frustum[side][D] /= magnitude;
	}

	/**
	 * Updates the frustum view
	 * 
	 * @param projectionMatrix
	 *            Projection Matrixd
	 * @param camera
	 *            Camera
	 */
	public void calculateFrustum(Matrix4d projectionMatrix, Matrix4d viewMatrix) {
		double[] clip = new double[16];

		Matrix4d.mul(projectionMatrix, viewMatrix, clip_);
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
	public boolean cubeInFrustum(Vector3d center, double size) {
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
	public boolean cubeInFrustum(double x, double y, double z, double size) {
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
		clip_b = BufferUtils.createDoubleBuffer(16);
	}
}
