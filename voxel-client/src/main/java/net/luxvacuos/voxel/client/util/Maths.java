package net.luxvacuos.voxel.client.util;

import org.joml.Rayf;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Maths {

	private final static Vector3 v2 = new Vector3();

	public static boolean intersectRayBounds(Rayf ray, BoundingBox box, Vector3 intersection) {
		Vector3 origin = new Vector3(ray.oX, ray.oY, ray.oZ);
		Vector3 direction = new Vector3(ray.dX, ray.dY, ray.dZ);
		if (box.contains(origin)) {
			if (intersection != null)
				intersection.set(origin);
			return true;
		}
		double lowest = 0, t;
		boolean hit = false;

		// min x
		if (origin.x <= box.min.x && direction.x > 0) {
			t = (box.min.x - origin.x) / direction.x;
			if (t >= 0) {
				v2.set(direction).scl(t).add(origin);
				if (v2.y >= box.min.y && v2.y <= box.max.y && v2.z >= box.min.z && v2.z <= box.max.z
						&& (!hit || t < lowest)) {
					hit = true;
					lowest = t;
				}
			}
		}
		// max x
		if (origin.x >= box.max.x && direction.x < 0) {
			t = (box.max.x - origin.x) / direction.x;
			if (t >= 0) {
				v2.set(direction).scl(t).add(origin);
				if (v2.y >= box.min.y && v2.y <= box.max.y && v2.z >= box.min.z && v2.z <= box.max.z
						&& (!hit || t < lowest)) {
					hit = true;
					lowest = t;
				}
			}
		}
		// min y
		if (origin.y <= box.min.y && direction.y > 0) {
			t = (box.min.y - origin.y) / direction.y;
			if (t >= 0) {
				v2.set(direction).scl(t).add(origin);
				if (v2.x >= box.min.x && v2.x <= box.max.x && v2.z >= box.min.z && v2.z <= box.max.z
						&& (!hit || t < lowest)) {
					hit = true;
					lowest = t;
				}
			}
		}
		// max y
		if (origin.y >= box.max.y && direction.y < 0) {
			t = (box.max.y - origin.y) / direction.y;
			if (t >= 0) {
				v2.set(direction).scl(t).add(origin);
				if (v2.x >= box.min.x && v2.x <= box.max.x && v2.z >= box.min.z && v2.z <= box.max.z
						&& (!hit || t < lowest)) {
					hit = true;
					lowest = t;
				}
			}
		}
		// min z
		if (origin.z <= box.min.z && direction.z > 0) {
			t = (box.min.z - origin.z) / direction.z;
			if (t >= 0) {
				v2.set(direction).scl(t).add(origin);
				if (v2.x >= box.min.x && v2.x <= box.max.x && v2.y >= box.min.y && v2.y <= box.max.y
						&& (!hit || t < lowest)) {
					hit = true;
					lowest = t;
				}
			}
		}
		// max y
		if (origin.z >= box.max.z && direction.z < 0) {
			t = (box.max.z - origin.z) / direction.z;
			if (t >= 0) {
				v2.set(direction).scl(t).add(origin);
				if (v2.x >= box.min.x && v2.x <= box.max.x && v2.y >= box.min.y && v2.y <= box.max.y
						&& (!hit || t < lowest)) {
					hit = true;
					lowest = t;
				}
			}
		}
		if (hit && intersection != null) {
			intersection.set(direction).scl(lowest).add(origin);
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
}
