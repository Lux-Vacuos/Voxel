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


package io.github.guerra24.voxel.client.kernel.world.physics;

import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;

public class AABB {
	public Vector3f center;
	public float r[];

	public AABB(final float x, final float y, final float z) {
		center = new Vector3f();
		r = new float[3];
		r[0] = x;
		r[1] = y;
		r[2] = z;
	}

	public void update(final Vector3f position) {
		center.x = position.x;
		center.y = position.y;
		center.z = position.z;
	}

	public static boolean testAABB(final AABB box1, final AABB box2) {
		if ((box1.center.x > box2.center.x) && (box1.center.x + box1.r[0] < box2.center.x + box2.r[0]))
			return true;
		if ((box1.center.y > box2.center.y) && (box1.center.y + box1.r[1] < box2.center.y + box2.r[1]))
			return true;
		if ((box1.center.z > box2.center.z) && (box1.center.z + box1.r[2] < box2.center.z + box2.r[2]))
			return true;
		return false;
	}

}
