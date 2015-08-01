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

package io.github.guerra24.voxel.client.kernel.util;

import io.github.guerra24.voxel.client.kernel.world.entities.Camera;

import java.util.Random;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Maths {

	private static final Vector3f forward = new Vector3f();
	private static final Vector3f side = new Vector3f();
	private static final Vector3f up = new Vector3f();
	private static final Vector3f eye = new Vector3f();

	public static Matrix4f createTransformationMatrix(Vector2f translation,
			Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}

	public static Matrix4f createTransformationMatrix(Vector3f translation,
			float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0),
				matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0),
				matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1),
				matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}

	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()),
				new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(
				0, 1, 0), viewMatrix, viewMatrix);
		Vector3f cameraPost = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPost.x, -cameraPost.y,
				-cameraPost.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}

	public static Matrix4f lookAt(float eyeX, float eyeY, float eyeZ,
			float centerX, float centerY, float centerZ, float upX, float upY,
			float upZ) {
		forward.set(centerX - eyeX, centerY - eyeY, centerZ - eyeZ);
		forward.normalise();

		up.set(upX, upY, upZ);

		Vector3f.cross(forward, up, side);
		side.normalise();

		Vector3f.cross(side, forward, up);
		up.normalise();

		Matrix4f matrix = new Matrix4f();
		matrix.m00 = side.x;
		matrix.m01 = side.y;
		matrix.m02 = side.z;

		matrix.m10 = up.x;
		matrix.m11 = up.y;
		matrix.m12 = up.z;

		matrix.m20 = -forward.x;
		matrix.m21 = -forward.y;
		matrix.m22 = -forward.z;

		matrix.transpose();

		eye.set(-eyeX, -eyeY, -eyeZ);
		matrix.translate(eye);

		return matrix;
	}

	public static float clamp(double d) {
		return (float) Math.max(0, Math.min(128, d));
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}
}
