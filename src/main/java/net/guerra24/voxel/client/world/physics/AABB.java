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

package net.guerra24.voxel.client.world.physics;

import net.guerra24.voxel.universal.util.vector.Vector3f;

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
