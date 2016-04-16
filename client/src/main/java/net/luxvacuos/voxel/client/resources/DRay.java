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

package net.luxvacuos.voxel.client.resources;

import com.badlogic.gdx.math.collision.Ray;

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;

public class DRay {
	public Vector3f origin;
	public Vector3f direction;
	public Vector3f inv_direction;
	public Ray ray;

	public DRay(Matrix4f proj, Matrix4f view, Vector2f mouse, int width, int height) {
		Vector3f v = new Vector3f();
		v.x = (((2.0f * mouse.x) / width) - 1) / proj.m00;
		v.y = -(((2.0f * mouse.y) / height) - 1) / proj.m11;
		v.z = 1.0f;

		Matrix4f invert_view = Matrix4f.invert(view, null);

		Vector3f ray_direction = new Vector3f();
		ray_direction.x = v.x * invert_view.m00 + v.y * invert_view.m10 + v.z * invert_view.m20;
		ray_direction.y = v.x * invert_view.m01 + v.y * invert_view.m11 + v.z * invert_view.m21;
		ray_direction.z = v.x * invert_view.m02 + v.y * invert_view.m12 + v.z * invert_view.m22;

		Vector3f ray_origin = new Vector3f(invert_view.m30, invert_view.m31, invert_view.m32);
		this.origin = new Vector3f(ray_origin.x, ray_origin.y, ray_origin.z);
		this.direction = new Vector3f(ray_direction.x, ray_direction.y, ray_direction.z);
		this.inv_direction = new Vector3f(-ray_direction.x, -ray_direction.y, -ray_direction.z);
		ray = new Ray(ray_origin.getAsVec3(), inv_direction.getAsVec3());
	}

	public DRay(Vector3f origin, Vector3f direction) {
		this.direction = direction;
		this.origin = origin;
		this.inv_direction = new Vector3f(-direction.x, -direction.y, -direction.z);
		ray = new Ray(origin.getAsVec3(), inv_direction.getAsVec3());
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
