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
