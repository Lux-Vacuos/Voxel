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

package net.luxvacuos.voxel.client.resources.models;

import net.luxvacuos.igl.vector.Vector3f;

public class AABB {

	public Vector3f[] corners;

	public AABB(float x, float y, float z, float width, float height, float length) {
		this.corners = new Vector3f[] { new Vector3f(x, y, z), new Vector3f(x + width, y + height, z + length) };
	}

	public AABB() {
		this(0, 0, 0, 1, 1, 1);
	}

	public void update(Vector3f pos) {
		this.corners[0] = pos;
	}

	public static boolean testAABB(AABB a, AABB b) {
		if (a.corners[1].x < b.corners[0].x || a.corners[0].x > b.corners[1].x)
			return false;
		if (a.corners[1].y < b.corners[0].y || a.corners[0].y > b.corners[1].y)
			return false;
		if (a.corners[1].z < b.corners[0].z || a.corners[0].z > b.corners[1].z)
			return false;
		return true;
	}
}
