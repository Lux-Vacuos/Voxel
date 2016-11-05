package net.luxvacuos.voxel.universal.ecs.components;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.igl.vector.Vector3d;

public class AABB implements VoxelComponent {

	private BoundingBox bb = new BoundingBox();

	private Vector3 min, max;
	private boolean enabled = true;

	public AABB() {
		this(new Vector3(), new Vector3());
	}

	public AABB(Vector3 min, Vector3 max) {
		this.min = min;
		this.max = max;
		
		this.bb = new BoundingBox(this.min, this.max);
	}

	public AABB(Vector3d min, Vector3d max) {
		this(min.getAsVec3(), max.getAsVec3());
	}

	public AABB set(Vector3 min, Vector3 max) {
		this.min = min;
		this.max = max;

		return this;
	}

	public AABB set(Vector3d min, Vector3d max) {
		this.min = min.getAsVec3();
		this.max = max.getAsVec3();

		return this;
	}

	public AABB setMin(Vector3 min) {
		this.min = min;

		return this;
	}

	public AABB setMin(Vector3d min) {
		this.min = min.getAsVec3();

		return this;
	}

	public AABB setMin(double x, double y, double z) {
		this.min = new Vector3(x, y, z);

		return this;
	}

	public Vector3d getMin() {
		return new Vector3d(this.min.x, this.min.y, this.min.z);
	}

	public AABB setMax(Vector3 max) {
		this.max = max;

		return this;
	}

	public AABB setMax(Vector3d max) {
		this.max = max.getAsVec3();

		return this;
	}

	public AABB setMax(double x, double y, double z) {
		this.max = new Vector3(x, y, z);

		return this;
	}

	public Vector3d getMax() {
		return new Vector3d(this.max.x, this.max.y, this.max.z);
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

	public AABB setBoundingBox(Vector3d min, Vector3d max) {
		return this.setBoundingBox(min.getAsVec3(), max.getAsVec3());
	}

	public AABB setBoundingBox(Vector3 min, Vector3 max) {
		this.bb = new BoundingBox(min, max);

		return this;
	}

	public void update(Vector3d position) {
		this.bb.set(new Vector3(position.x + this.min.x, position.y + this.min.y, position.z + this.min.z),
				new Vector3(position.x + this.max.x, position.y + this.max.y, position.z + this.max.z));
	}

	@Override
	public void load(TagCompound compound) throws NBTException {
		double minX, minY, minZ, maxX, maxY, maxZ;
		
		minX = compound.getDouble("MinX");
		minY = compound.getDouble("MinY");
		minZ = compound.getDouble("MinZ");
		maxX = compound.getDouble("MaxX");
		maxY = compound.getDouble("MaxY");
		maxZ = compound.getDouble("MaxZ");
		
		this.min = new Vector3(minX, minY, minZ);
		this.max = new Vector3(maxX, maxY, maxZ);
		
		this.bb.set(this.min, this.max);
		
	}

	@Override
	public TagCompound save() {
		CompoundBuilder builder = new CompoundBuilder().start("AABBComponent");
		
		builder.addDouble("MinX", this.min.x).addDouble("MinY", this.min.y).addDouble("MinZ", this.min.z);
		builder.addDouble("MaxX", this.max.x).addDouble("MaxY", this.max.y).addDouble("MaxZ", this.max.z);
		
		return builder.build();
	}
}
