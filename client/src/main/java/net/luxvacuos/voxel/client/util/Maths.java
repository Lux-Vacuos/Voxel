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

package net.luxvacuos.voxel.client.util;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.igl.vector.Vector4f;
import net.luxvacuos.voxel.client.world.chunks.Chunk;
import net.luxvacuos.voxel.client.world.chunks.ChunkNode;
import net.luxvacuos.voxel.client.world.entities.Camera;

/**
 * Maths
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Util
 */
public class Maths {

	private final static Vector3 v2 = new Vector3();

	/**
	 * Create a Transformation Matrix 2D
	 * 
	 * @param translation
	 *            Position
	 * @param scale
	 *            Scale
	 * @return Transformation Matrix 2D
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
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, double rx, double ry, double rz,
			double scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate(Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate(Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate(Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}

	/**
	 * Create a View Matrix applying position and rotation
	 * 
	 * @param camera
	 *            Camera
	 * @return View Matrix
	 */
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		createViewMatrixRot(camera.getPitch(), camera.getYaw(), camera.getRoll(), viewMatrix);
		createViewMatrixPos(camera.getPosition(), viewMatrix);
		return viewMatrix;
	}

	/**
	 * Create a View Matrix applying position
	 * 
	 * @param camera
	 *            Camera
	 * @param viewMatrix
	 *            View Matrix Pass an already created matrix or null for
	 *            creating a new one
	 * @return The composed matrix
	 */
	public static Matrix4f createViewMatrixPos(Vector3f pos, Matrix4f viewMatrix) {
		if (viewMatrix == null)
			viewMatrix = new Matrix4f();
		Vector3f cameraPost = pos;
		Vector3f negativeCameraPos = new Vector3f(-cameraPost.x, -cameraPost.y, -cameraPost.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}

	/**
	 * Create a View Matrix applying rotation
	 * 
	 * @param camera
	 *            Camera
	 * @param viewMatrix
	 *            View Matrix Pass an already created matrix or null for
	 *            creating a new one
	 * @return The composed matrix
	 */
	public static Matrix4f createViewMatrixRot(double pitch, double yaw, double roll, Matrix4f viewMatrix) {
		if (viewMatrix == null)
			viewMatrix = new Matrix4f();
		Matrix4f.rotate(Math.toRadians(pitch), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate(Math.toRadians(yaw), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate(Math.toRadians(roll), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
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
	public static Matrix4f orthographic(float left, float right, float bottom, float top, float zNear, float zFar,
			boolean zZeroToOne) {
		Matrix4f dest = new Matrix4f();
		// calculate right matrix elements
		float rm00 = 2.0f / (right - left);
		float rm11 = 2.0f / (top - bottom);
		float rm22 = (zZeroToOne ? 1.0f : 2.0f) / (zNear - zFar);
		float rm30 = (left + right) / (left - right);
		float rm31 = (top + bottom) / (bottom - top);
		float rm32 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);

		// perform optimized multiplication
		// compute the last column first, because other columns do not depend on
		// it
		dest.m30 = dest.m00 * rm30 + dest.m10 * rm31 + dest.m20 * rm32 + dest.m30;
		dest.m31 = dest.m01 * rm30 + dest.m11 * rm31 + dest.m21 * rm32 + dest.m31;
		dest.m32 = dest.m02 * rm30 + dest.m12 * rm31 + dest.m22 * rm32 + dest.m32;
		dest.m33 = dest.m03 * rm30 + dest.m13 * rm31 + dest.m23 * rm32 + dest.m33;
		dest.m00 = dest.m00 * rm00;
		dest.m01 = dest.m01 * rm00;
		dest.m02 = dest.m02 * rm00;
		dest.m03 = dest.m03 * rm00;
		dest.m10 = dest.m10 * rm11;
		dest.m11 = dest.m11 * rm11;
		dest.m12 = dest.m12 * rm11;
		dest.m13 = dest.m13 * rm11;
		dest.m20 = dest.m20 * rm22;
		dest.m21 = dest.m21 * rm22;
		dest.m22 = dest.m22 * rm22;
		dest.m23 = dest.m23 * rm22;

		return dest;

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
	 * 
	 * Clamp a value
	 * 
	 * @param d
	 *            Value
	 * @return Clamped Value
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public static float clamp(double d, double min, double max) {
		return (float) Math.max(min, Math.min(max, d));
	}

	/**
	 * Gets a Random int from Range
	 * 
	 * @param min
	 *            Min Value
	 * @param max
	 *            Max Value
	 * @return Random Int
	 */
	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static Vector2f convertTo2F(Vector3f pos, Matrix4f projection, Matrix4f viewMatrix, int width, int height) {
		return Matrix4f.Project(pos, projection, viewMatrix, new Vector4f(0, 0, width, height));
	}

	public static float randFloat() {
		Random rand = new Random();
		float randomNum = (rand.nextFloat() - 0.5f) / 16;
		return randomNum;
	}

	public static boolean getRandomBoolean(float chanceOfTrue) {
		return new Random().nextInt(100) < chanceOfTrue;
	}

	public static void sortLowToHigh(List<Chunk> list) {
		for (int i = 1; i < list.size(); i++) {
			Chunk item = list.get(i);
			if (item.getDistance() < list.get(i - 1).getDistance()) {
				sortLowToHigh(list, i);
			}
		}
	}

	private static void sortLowToHigh(List<Chunk> list, int i) {
		Chunk item = list.get(i);
		int attemptPos = i - 1;
		while (attemptPos != 0 && list.get(attemptPos - 1).getDistance() > item.getDistance()) {
			attemptPos--;
		}
		list.remove(i);
		list.add(attemptPos, item);
	}

	public static void sortLowToHighN(List<ChunkNode> list) {
		for (int i = 1; i < list.size(); i++) {
			ChunkNode item = list.get(i);
			if (item.distance < list.get(i - 1).distance) {
				sortLowToHighN(list, i);
			}
		}
	}

	private static void sortLowToHighN(List<ChunkNode> list, int i) {
		ChunkNode item = list.get(i);
		int attemptPos = i - 1;
		while (attemptPos != 0 && list.get(attemptPos - 1).distance > item.distance) {
			attemptPos--;
		}
		list.remove(i);
		list.add(attemptPos, item);
	}

	public static void sortHighToLow(List<Chunk> list) {
		for (int i = 1; i < list.size(); i++) {
			Chunk item = list.get(i);
			if (item.getDistance() > list.get(i - 1).getDistance()) {
				sortUpHighToLow(list, i);
			}
		}
	}

	private static void sortUpHighToLow(List<Chunk> list, int i) {
		Chunk item = list.get(i);
		int attemptPos = i - 1;
		while (attemptPos != 0 && list.get(attemptPos - 1).getDistance() < item.getDistance()) {
			attemptPos--;
		}
		list.remove(i);
		list.add(attemptPos, item);
	}

	public static boolean intersectRayBounds(Ray ray, BoundingBox box, Vector3 intersection) {
		if (box.contains(ray.origin)) {
			if (intersection != null)
				intersection.set(ray.origin);
			return true;
		}
		double lowest = 0, t;
		boolean hit = false;

		// min x
		if (ray.origin.x <= box.min.x && ray.direction.x > 0) {
			t = (box.min.x - ray.origin.x) / ray.direction.x;
			if (t >= 0) {
				v2.set(ray.direction).scl(t).add(ray.origin);
				if (v2.y >= box.min.y && v2.y <= box.max.y && v2.z >= box.min.z && v2.z <= box.max.z
						&& (!hit || t < lowest)) {
					hit = true;
					lowest = t;
				}
			}
		}
		// max x
		if (ray.origin.x >= box.max.x && ray.direction.x < 0) {
			t = (box.max.x - ray.origin.x) / ray.direction.x;
			if (t >= 0) {
				v2.set(ray.direction).scl(t).add(ray.origin);
				if (v2.y >= box.min.y && v2.y <= box.max.y && v2.z >= box.min.z && v2.z <= box.max.z
						&& (!hit || t < lowest)) {
					hit = true;
					lowest = t;
				}
			}
		}
		// min y
		if (ray.origin.y <= box.min.y && ray.direction.y > 0) {
			t = (box.min.y - ray.origin.y) / ray.direction.y;
			if (t >= 0) {
				v2.set(ray.direction).scl(t).add(ray.origin);
				if (v2.x >= box.min.x && v2.x <= box.max.x && v2.z >= box.min.z && v2.z <= box.max.z
						&& (!hit || t < lowest)) {
					hit = true;
					lowest = t;
				}
			}
		}
		// max y
		if (ray.origin.y >= box.max.y && ray.direction.y < 0) {
			t = (box.max.y - ray.origin.y) / ray.direction.y;
			if (t >= 0) {
				v2.set(ray.direction).scl(t).add(ray.origin);
				if (v2.x >= box.min.x && v2.x <= box.max.x && v2.z >= box.min.z && v2.z <= box.max.z
						&& (!hit || t < lowest)) {
					hit = true;
					lowest = t;
				}
			}
		}
		// min z
		if (ray.origin.z <= box.min.z && ray.direction.z > 0) {
			t = (box.min.z - ray.origin.z) / ray.direction.z;
			if (t >= 0) {
				v2.set(ray.direction).scl(t).add(ray.origin);
				if (v2.x >= box.min.x && v2.x <= box.max.x && v2.y >= box.min.y && v2.y <= box.max.y
						&& (!hit || t < lowest)) {
					hit = true;
					lowest = t;
				}
			}
		}
		// max y
		if (ray.origin.z >= box.max.z && ray.direction.z < 0) {
			t = (box.max.z - ray.origin.z) / ray.direction.z;
			if (t >= 0) {
				v2.set(ray.direction).scl(t).add(ray.origin);
				if (v2.x >= box.min.x && v2.x <= box.max.x && v2.y >= box.min.y && v2.y <= box.max.y
						&& (!hit || t < lowest)) {
					hit = true;
					lowest = t;
				}
			}
		}
		if (hit && intersection != null) {
			intersection.set(ray.direction).scl(lowest).add(ray.origin);
			if (intersection.x < box.min.x) {
				intersection.x = box.min.x;
			} else if (intersection.x > box.max.x) {
				intersection.x = box.max.x;
			}
			if (intersection.y < box.min.y) {
				intersection.y = box.min.y;
			} else if (intersection.y > box.max.y) {
				intersection.y = box.max.y;
			}
			if (intersection.z < box.min.z) {
				intersection.z = box.min.z;
			} else if (intersection.z > box.max.z) {
				intersection.z = box.max.z;
			}
		}
		return hit;
	}

	public static float dti(double val) {
		return (float) Math.abs(val - Math.round(val));
	}

}
