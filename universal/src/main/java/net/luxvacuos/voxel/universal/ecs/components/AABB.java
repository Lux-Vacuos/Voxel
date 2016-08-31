package net.luxvacuos.voxel.universal.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.igl.vector.Vector3f;

public class AABB implements Component {

	private BoundingBox bb = new BoundingBox();

	private Vector3 min, max;
	private boolean enabled = true;

	public AABB() {
		this(new Vector3(), new Vector3());
	}

	public AABB(Vector3 min, Vector3 max) {
		this.min = min;
		this.max = max;
	}

	public AABB(Vector3f min, Vector3f max) {
		this.min = min.getAsVec3();
		this.max = max.getAsVec3();
	}

	public AABB set(Vector3 min, Vector3 max) {
		this.min = min;
		this.max = max;

		return this;
	}

	public AABB set(Vector3f min, Vector3f max) {
		this.min = min.getAsVec3();
		this.max = max.getAsVec3();

		return this;
	}

	public AABB setMin(Vector3 min) {
		this.min = min;

		return this;
	}

	public AABB setMin(Vector3f min) {
		this.min = min.getAsVec3();

		return this;
	}

	public AABB setMin(double x, double y, double z) {
		this.min = new Vector3(x, y, z);

		return this;
	}

	public Vector3f getMin() {
		return new Vector3f(this.min.x, this.min.y, this.min.z);
	}

	public AABB setMax(Vector3 max) {
		this.max = max;

		return this;
	}

	public AABB setMax(Vector3f max) {
		this.max = max.getAsVec3();

		return this;
	}

	public AABB setMax(double x, double y, double z) {
		this.max = new Vector3(x, y, z);

		return this;
	}

	public Vector3f getMax() {
		return new Vector3f(this.max.x, this.max.y, this.max.z);
	}

	public AABB setEnabled(boolean flag) {
		this.enabled = flag;
		return this;
	}

	public AABB toggleEnabled() {
		this.enabled = !this.enabled;

		return this;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public BoundingBox getBoundingBox() {
		return this.bb;
	}

	public AABB setBoundingBox(BoundingBox bb) {
		this.bb = bb;

		return this;
	}

	public AABB setBoundingBox(Vector3f min, Vector3f max) {
		this.bb = new BoundingBox(min.getAsVec3(), max.getAsVec3());

		return this;
	}

	public AABB setBoundingBox(Vector3 min, Vector3 max) {
		this.bb = new BoundingBox(min, max);

		return this;
	}

	public void update(Vector3f position) {
		this.bb.set(new Vector3(position.x + min.x, position.y + min.y, position.z + min.z),
				new Vector3(position.x + max.x, position.y + max.y, position.z + max.z));
	}
}
