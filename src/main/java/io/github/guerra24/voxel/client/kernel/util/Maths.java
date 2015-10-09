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

import io.github.guerra24.voxel.client.kernel.util.vector.Matrix4f;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector2f;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glReadPixels;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

/**
 * Maths
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Util
 */
public class Maths {

	private static final int SIZE_FLOAT = 4;
	private static final int SIZE_BYTE = 1;

	/**
	 * Create a Transformation Matrix 2D
	 * 
	 * @param translation
	 *            Position
	 * @param scale
	 *            Scale
	 * @return Transformation Matrix 2D
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}

	/**
	 * Create a Transformation Matrix 3D
	 * 
	 * @param translation
	 *            Position
	 * @param rx
	 *            Rotation X
	 * @param ry
	 *            Rotation Y
	 * @param rz
	 *            Rotation Z
	 * @param scale
	 *            Scale
	 * @return Transformation Matrix 3D
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}

	/**
	 * Create a View Matrix
	 * 
	 * @param camera
	 *            Camera
	 * @return View Matrix
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		Vector3f cameraPost = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPost.x, -cameraPost.y, -cameraPost.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}

	/**
	 * Create orthographic matrix
	 * 
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @param near
	 * @param far
	 * @return Matrix4f
	 */
	public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f matrix = new Matrix4f();
		matrix.m00 = 2.0f / (right - left);
		matrix.m11 = 2.0f / (top - bottom);
		matrix.m22 = 2.0f / (near - far);

		matrix.m03 = (left + right) / (left - right);
		matrix.m13 = (bottom + top) / (bottom - top);
		matrix.m23 = (far + near) / (far - near);

		matrix.m33 = 1.0f;

		return matrix;
	}

	/**
	 * Clamp the value to Generation Terrain
	 * 
	 * @param d
	 *            Value
	 * @return Clamped Value
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static float clamp(double d) {
		return (float) Math.max(0, Math.min(128, d));
	}

	/**
	 * Gets a Random int from Range
	 * 
	 * @param min
	 *            Min Value
	 * @param max
	 *            Max Value
	 * @return Random Int
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	/**
	 * Gets a Random float from Range
	 * 
	 * @param min
	 *            Min Value
	 * @param max
	 *            Max Value
	 * @return Random Int
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static float randFloat() {
		Random rand = new Random();
		float randomNum = (rand.nextFloat() - 0.5f) / 16;
		return randomNum;
	}

}
