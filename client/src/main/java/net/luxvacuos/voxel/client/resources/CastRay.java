/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

package net.luxvacuos.voxel.client.resources;

import com.badlogic.gdx.math.collision.Ray;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.igl.vector.Vector3d;

public class CastRay {
	public Vector3d origin;
	public Vector3d direction;
	public Vector3d invDirection;
	public Ray ray;

	public CastRay(Matrix4d proj, Matrix4d view, Vector2d mouse, int width, int height) {
		Vector3d v = new Vector3d();
		v.x = (((2.0f * mouse.x) / width) - 1) / proj.m00;
		v.y = -(((2.0f * mouse.y) / height) - 1) / proj.m11;
		v.z = 1.0f;

		Matrix4d invertView = Matrix4d.invert(view, null);

		Vector3d rayDirection = new Vector3d();
		rayDirection.x = v.x * invertView.m00 + v.y * invertView.m10 + v.z * invertView.m20;
		rayDirection.y = v.x * invertView.m01 + v.y * invertView.m11 + v.z * invertView.m21;
		rayDirection.z = v.x * invertView.m02 + v.y * invertView.m12 + v.z * invertView.m22;

		Vector3d ray_origin = new Vector3d(invertView.m30, invertView.m31, invertView.m32);
		this.origin = new Vector3d(ray_origin.x, ray_origin.y, ray_origin.z);
		this.direction = new Vector3d(rayDirection.x, rayDirection.y, rayDirection.z);
		this.invDirection = new Vector3d(-rayDirection.x, -rayDirection.y, -rayDirection.z);
		ray = new Ray(ray_origin.getAsVec3(), invDirection.getAsVec3());
	}

	public void update(Matrix4d proj, Matrix4d view, Vector2d mouse, int width, int height) {
		Vector3d v = new Vector3d();
		v.x = (((2.0f * mouse.x) / width) - 1) / proj.m00;
		v.y = -(((2.0f * mouse.y) / height) - 1) / proj.m11;
		v.z = 1.0f;

		Matrix4d invertView = Matrix4d.invert(view, null);

		Vector3d rayDirection = new Vector3d();
		rayDirection.x = v.x * invertView.m00 + v.y * invertView.m10 + v.z * invertView.m20;
		rayDirection.y = v.x * invertView.m01 + v.y * invertView.m11 + v.z * invertView.m21;
		rayDirection.z = v.x * invertView.m02 + v.y * invertView.m12 + v.z * invertView.m22;

		Vector3d rayOrigin = new Vector3d(invertView.m30, invertView.m31, invertView.m32);
		this.origin = new Vector3d(rayOrigin.x, rayOrigin.y, rayOrigin.z);
		this.direction = new Vector3d(rayDirection.x, rayDirection.y, rayDirection.z);
		this.invDirection = new Vector3d(-rayDirection.x, -rayDirection.y, -rayDirection.z);
		ray.set(rayOrigin.getAsVec3(), invDirection.getAsVec3());
	}

	public CastRay(Vector3d origin, Vector3d direction) {
		this.direction = direction;
		this.origin = origin;
		this.invDirection = new Vector3d(-direction.x, -direction.y, -direction.z);
		ray = new Ray(origin.getAsVec3(), invDirection.getAsVec3());
	}

	public Ray getRay() {
		return ray;
	}

	@Override
	public String toString() {
		return "origin: " + origin.x + ", " + origin.y + ", " + origin.z + ";\n direction: " + direction.x + ", "
				+ direction.y + ", " + direction.z + "";
	}
}
