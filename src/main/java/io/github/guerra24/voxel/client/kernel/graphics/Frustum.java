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

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;

public class Frustum {
	public float[][] m_Frustum = new float[6][4];
	private static Frustum frustum = new Frustum();
	private FloatBuffer _proj = BufferUtils.createFloatBuffer(16);
	private FloatBuffer _modl = BufferUtils.createFloatBuffer(16);
	private FloatBuffer _clip = BufferUtils.createFloatBuffer(16);
	float[] proj = new float[16];
	float[] modl = new float[16];
	float[] clip = new float[16];

	public static void updateFrustum() {
		frustum.calculateFrustum();
	}

	public static Frustum getFrustum() {
		return frustum;
	}

	private void normalizePlane(float[][] frustum, int side) {
		float magnitude = (float) Math.sqrt(frustum[side][0] * frustum[side][0]
				+ frustum[side][1] * frustum[side][1] + frustum[side][2]
				* frustum[side][2]);

		frustum[side][0] /= magnitude;
		frustum[side][1] /= magnitude;
		frustum[side][2] /= magnitude;
		frustum[side][3] /= magnitude;
	}

	private void calculateFrustum() {
		this._proj.clear();
		this._modl.clear();
		this._clip.clear();

		glGetFloat(2983, this._proj);
		glGetFloat(2982, this._modl);

		this._proj.flip().limit(16);
		this._proj.get(this.proj);
		this._modl.flip().limit(16);
		this._modl.get(this.modl);

		for (int i = 0; i < 4; i++) {
			for (int n = 0; n < 4; n++) {
				this.clip[i * 4 + n] = (this.modl[i * 4] * this.proj[n]
						+ this.modl[((i + 1) * 4) - 3] * this.proj[4 + n]
						+ this.modl[((i + 1) * 4) - 2] * this.proj[8 + n] + this.modl[((i + 1) * 4) - 1]
						* this.proj[12 + n]);
			}
		}

		for (int j = 0; j < 6; j++) {
			int tt = j >> 1;
			for (int m = 0; m < 4; m++) {
				this.m_Frustum[j][m] = (this.clip[(((1 + m) * 4)) - 1] - this.clip[m
						* 4 + tt]);
			}
			normalizePlane(this.m_Frustum, j);
		}
	}

	public boolean pointInFrustum(float x, float y, float z) {
		for (int i = 0; i < 6; i++) {
			if (this.m_Frustum[i][0] * x + this.m_Frustum[i][1] * y
					+ this.m_Frustum[i][2] * z + this.m_Frustum[i][3] <= 0.0F) {
				return false;
			}
		}
		return true;
	}

	public boolean sphereInFrustum(float x, float y, float z, float radius) {
		for (int i = 0; i < 6; i++) {
			if (this.m_Frustum[i][0] * x + this.m_Frustum[i][1] * y
					+ this.m_Frustum[i][2] * z + this.m_Frustum[i][3] <= -radius) {
				return false;
			}
		}
		return true;
	}

	public boolean cubeFullyInFrustum(float x1, float y1, float z1, float x2,
			float y2, float z2) {
		for (int i = 0; i < 6; i++) {
			if (this.m_Frustum[i][0] * x1 + this.m_Frustum[i][1] * y1
					+ this.m_Frustum[i][2] * z1 + this.m_Frustum[i][3] <= 0.0F) {
				return false;
			}
			if (this.m_Frustum[i][0] * x2 + this.m_Frustum[i][1] * y1
					+ this.m_Frustum[i][2] * z1 + this.m_Frustum[i][3] <= 0.0F) {
				return false;
			}
			if (this.m_Frustum[i][0] * x1 + this.m_Frustum[i][1] * y2
					+ this.m_Frustum[i][2] * z1 + this.m_Frustum[i][3] <= 0.0F) {
				return false;
			}
			if (this.m_Frustum[i][0] * x2 + this.m_Frustum[i][1] * y2
					+ this.m_Frustum[i][2] * z1 + this.m_Frustum[i][3] <= 0.0F) {
				return false;
			}
			if (this.m_Frustum[i][0] * x1 + this.m_Frustum[i][1] * y1
					+ this.m_Frustum[i][2] * z2 + this.m_Frustum[i][3] <= 0.0F) {
				return false;
			}
			if (this.m_Frustum[i][0] * x2 + this.m_Frustum[i][1] * y1
					+ this.m_Frustum[i][2] * z2 + this.m_Frustum[i][3] <= 0.0F) {
				return false;
			}
			if (this.m_Frustum[i][0] * x1 + this.m_Frustum[i][1] * y2
					+ this.m_Frustum[i][2] * z2 + this.m_Frustum[i][3] <= 0.0F) {
				return false;
			}
			if (this.m_Frustum[i][0] * x2 + this.m_Frustum[i][1] * y2
					+ this.m_Frustum[i][2] * z2 + this.m_Frustum[i][3] <= 0.0F) {
				return false;
			}
		}
		return true;
	}

	public boolean cubeInFrustum(float x1, float y1, float z1, float x2,
			float y2, float z2) {
		for (int i = 0; i < 6; i++) {
			if ((this.m_Frustum[i][0] * x1 + this.m_Frustum[i][1] * y1
					+ this.m_Frustum[i][2] * z1 + this.m_Frustum[i][3] <= 0.0F)
					&& (this.m_Frustum[i][0] * x2 + this.m_Frustum[i][1] * y1
							+ this.m_Frustum[i][2] * z1 + this.m_Frustum[i][3] <= 0.0F)
					&& (this.m_Frustum[i][0] * x1 + this.m_Frustum[i][1] * y2
							+ this.m_Frustum[i][2] * z1 + this.m_Frustum[i][3] <= 0.0F)
					&& (this.m_Frustum[i][0] * x2 + this.m_Frustum[i][1] * y2
							+ this.m_Frustum[i][2] * z1 + this.m_Frustum[i][3] <= 0.0F)
					&& (this.m_Frustum[i][0] * x1 + this.m_Frustum[i][1] * y1
							+ this.m_Frustum[i][2] * z2 + this.m_Frustum[i][3] <= 0.0F)
					&& (this.m_Frustum[i][0] * x2 + this.m_Frustum[i][1] * y1
							+ this.m_Frustum[i][2] * z2 + this.m_Frustum[i][3] <= 0.0F)
					&& (this.m_Frustum[i][0] * x1 + this.m_Frustum[i][1] * y2
							+ this.m_Frustum[i][2] * z2 + this.m_Frustum[i][3] <= 0.0F)
					&& (this.m_Frustum[i][0] * x2 + this.m_Frustum[i][1] * y2
							+ this.m_Frustum[i][2] * z2 + this.m_Frustum[i][3] <= 0.0F)) {
				return false;
			}
		}
		return true;
	}
}
