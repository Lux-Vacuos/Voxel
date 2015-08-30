/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
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

package io.github.guerra24.voxel.client.kernel.graphics;

import io.github.guerra24.voxel.client.kernel.core.Kernel;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

/**
 * Frustum Culling
 * 
 * @author Ron Sullivan (modified by Thomas Hourdel(modified by Guerra24))
 *         <thomas.hourdel@libertysurf.fr>
 */
public class Frustum {

	public static final int RIGHT = 0;
	public static final int LEFT = 1;
	public static final int BOTTOM = 2;
	public static final int TOP = 3;
	public static final int BACK = 4;
	public static final int FRONT = 5;
	public static final int A = 0;
	public static final int B = 1;
	public static final int C = 2;
	public static final int D = 3;

	float[][] m_Frustum = new float[6][4];

	/** FloatBuffer to get ModelView matrix. **/
	FloatBuffer modl_b;

	/** FloatBuffer to get Projection matrix. **/
	FloatBuffer proj_b;

	public void normalizePlane(float[][] frustum, int side) {
		float magnitude = (float) Math.sqrt(frustum[side][A] * frustum[side][A]
				+ frustum[side][B] * frustum[side][B] + frustum[side][C]
				* frustum[side][C]);

		frustum[side][A] /= magnitude;
		frustum[side][B] /= magnitude;
		frustum[side][C] /= magnitude;
		frustum[side][D] /= magnitude;
	}

	public void calculateFrustum(Camera camera) {
		float[] proj = new float[16];
		float[] modl = new float[16];
		float[] clip = new float[16];

		proj_b.rewind();
		Kernel.gameResources.renderer.getProjectionMatrix().store(proj_b);
		proj_b.rewind();
		proj_b.get(proj);
		modl_b.rewind();
		Maths.createViewMatrix(camera).store(modl_b);
		modl_b.rewind();
		modl_b.get(modl);

		clip[0] = modl[0] * proj[0] + modl[1] * proj[4] + modl[2] * proj[8]
				+ modl[3] * proj[12];
		clip[1] = modl[0] * proj[1] + modl[1] * proj[5] + modl[2] * proj[9]
				+ modl[3] * proj[13];
		clip[2] = modl[0] * proj[2] + modl[1] * proj[6] + modl[2] * proj[10]
				+ modl[3] * proj[14];
		clip[3] = modl[0] * proj[3] + modl[1] * proj[7] + modl[2] * proj[11]
				+ modl[3] * proj[15];

		clip[4] = modl[4] * proj[0] + modl[5] * proj[4] + modl[6] * proj[8]
				+ modl[7] * proj[12];
		clip[5] = modl[4] * proj[1] + modl[5] * proj[5] + modl[6] * proj[9]
				+ modl[7] * proj[13];
		clip[6] = modl[4] * proj[2] + modl[5] * proj[6] + modl[6] * proj[10]
				+ modl[7] * proj[14];
		clip[7] = modl[4] * proj[3] + modl[5] * proj[7] + modl[6] * proj[11]
				+ modl[7] * proj[15];

		clip[8] = modl[8] * proj[0] + modl[9] * proj[4] + modl[10] * proj[8]
				+ modl[11] * proj[12];
		clip[9] = modl[8] * proj[1] + modl[9] * proj[5] + modl[10] * proj[9]
				+ modl[11] * proj[13];
		clip[10] = modl[8] * proj[2] + modl[9] * proj[6] + modl[10] * proj[10]
				+ modl[11] * proj[14];
		clip[11] = modl[8] * proj[3] + modl[9] * proj[7] + modl[10] * proj[11]
				+ modl[11] * proj[15];

		clip[12] = modl[12] * proj[0] + modl[13] * proj[4] + modl[14] * proj[8]
				+ modl[15] * proj[12];
		clip[13] = modl[12] * proj[1] + modl[13] * proj[5] + modl[14] * proj[9]
				+ modl[15] * proj[13];
		clip[14] = modl[12] * proj[2] + modl[13] * proj[6] + modl[14]
				* proj[10] + modl[15] * proj[14];
		clip[15] = modl[12] * proj[3] + modl[13] * proj[7] + modl[14]
				* proj[11] + modl[15] * proj[15];
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

	public boolean pointInFrustum(float x, float y, float z) {
		for (int i = 0; i < 6; i++) {
			if (m_Frustum[i][A] * x + m_Frustum[i][B] * y + m_Frustum[i][C] * z
					+ m_Frustum[i][D] <= 0) {
				return false;
			}
		}
		return true;
	}

	public boolean sphereInFrustum(float x, float y, float z, float radius) {
		for (int i = 0; i < 6; i++) {
			if (m_Frustum[i][A] * x + m_Frustum[i][B] * y + m_Frustum[i][C] * z
					+ m_Frustum[i][D] <= -radius) {
				return false;
			}
		}

		return true;
	}

	public boolean cubeInFrustum(Vector3f center, float size) {
		return cubeInFrustum(center.x, center.y, center.z, size);
	}

	public boolean cubeInFrustum(float x, float y, float z, float size) {
		for (int i = 0; i < 6; i++) {
			if (m_Frustum[i][A] * (x - size) + m_Frustum[i][B] * (y - size)
					+ m_Frustum[i][C] * (z - size) + m_Frustum[i][D] > 0)
				continue;
			if (m_Frustum[i][A] * (x + size) + m_Frustum[i][B] * (y - size)
					+ m_Frustum[i][C] * (z - size) + m_Frustum[i][D] > 0)
				continue;
			if (m_Frustum[i][A] * (x - size) + m_Frustum[i][B] * (y + size)
					+ m_Frustum[i][C] * (z - size) + m_Frustum[i][D] > 0)
				continue;
			if (m_Frustum[i][A] * (x + size) + m_Frustum[i][B] * (y + size)
					+ m_Frustum[i][C] * (z - size) + m_Frustum[i][D] > 0)
				continue;
			if (m_Frustum[i][A] * (x - size) + m_Frustum[i][B] * (y - size)
					+ m_Frustum[i][C] * (z + size) + m_Frustum[i][D] > 0)
				continue;
			if (m_Frustum[i][A] * (x + size) + m_Frustum[i][B] * (y - size)
					+ m_Frustum[i][C] * (z + size) + m_Frustum[i][D] > 0)
				continue;
			if (m_Frustum[i][A] * (x - size) + m_Frustum[i][B] * (y + size)
					+ m_Frustum[i][C] * (z + size) + m_Frustum[i][D] > 0)
				continue;
			if (m_Frustum[i][A] * (x + size) + m_Frustum[i][B] * (y + size)
					+ m_Frustum[i][C] * (z + size) + m_Frustum[i][D] > 0)
				continue;
			return false;
		}

		return true;
	}

	public boolean cubeInFrustum(float x1, float y1, float z1, float x2,
			float y2, float z2) {
		for (int i = 0; i < 6; i++) {
			if ((this.m_Frustum[i][A] * x1 + this.m_Frustum[i][B] * y1
					+ this.m_Frustum[i][C] * z1 + this.m_Frustum[i][D] <= 0.0F)
					&& (this.m_Frustum[i][A] * x2 + this.m_Frustum[i][B] * y1
							+ this.m_Frustum[i][C] * z1 + this.m_Frustum[i][D] <= 0.0F)
					&& (this.m_Frustum[i][A] * x1 + this.m_Frustum[i][B] * y2
							+ this.m_Frustum[i][C] * z1 + this.m_Frustum[i][D] <= 0.0F)
					&& (this.m_Frustum[i][A] * x2 + this.m_Frustum[i][B] * y2
							+ this.m_Frustum[i][C] * z1 + this.m_Frustum[i][D] <= 0.0F)
					&& (this.m_Frustum[i][A] * x1 + this.m_Frustum[i][B] * y1
							+ this.m_Frustum[i][C] * z2 + this.m_Frustum[i][D] <= 0.0F)
					&& (this.m_Frustum[i][A] * x2 + this.m_Frustum[i][B] * y1
							+ this.m_Frustum[i][C] * z2 + this.m_Frustum[i][D] <= 0.0F)
					&& (this.m_Frustum[i][A] * x1 + this.m_Frustum[i][B] * y2
							+ this.m_Frustum[i][C] * z2 + this.m_Frustum[i][D] <= 0.0F)
					&& (this.m_Frustum[i][A] * x2 + this.m_Frustum[i][B] * y2
							+ this.m_Frustum[i][C] * z2 + this.m_Frustum[i][D] <= 0.0F)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Frustum constructor, creates FloatBuffer
	 */
	public Frustum() {
		modl_b = BufferUtils.createFloatBuffer(16);
		proj_b = BufferUtils.createFloatBuffer(16);
	}
}
