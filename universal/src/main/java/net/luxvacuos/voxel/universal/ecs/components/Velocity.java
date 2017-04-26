package net.luxvacuos.voxel.universal.ecs.components;

import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.igl.vector.Vector3d;

public class Velocity implements VoxelComponent {
	private Vector3d velocity;

	public Velocity() {
		velocity = new Vector3d();
	}

	public Velocity(double x, double y, double z) {
		velocity = new Vector3d(x, y, z);
	}

	public Velocity(Vector3d vec) {
		velocity = new Vector3d(vec);
	}

	public Vector3d getVelocity() {
		return velocity;
	}

	public double getX() {
		return this.velocity.x;
	}

	public double getY() {
		return this.velocity.y;
	}

	public double getZ() {
		return this.velocity.z;
	}

	public Velocity setX(double x) {
		this.velocity.x = x;

		return this;
	}

	public Velocity setY(double y) {
		this.velocity.y = y;

		return this;
	}

	public Velocity setZ(double z) {
		this.velocity.z = z;

		return this;
	}

	public Velocity set(double x, double y, double z) {
		this.velocity.x = x;
		this.velocity.y = y;
		this.velocity.z = z;

		return this;
	}

	public Velocity set(Vector3d vec) {
		this.velocity.x = vec.x;
		this.velocity.y = vec.y;
		this.velocity.z = vec.z;

		return this;
	}

	@Override
	public String toString() {
		return "[x:" + this.velocity.x + "]" + "[y:" + this.velocity.y + "]" + "[z:" + this.velocity.z + "]";
	}

	@Override
	public void load(TagCompound compound) throws NBTException {
		this.velocity.x = compound.getDouble("VelX");
		this.velocity.y = compound.getDouble("VelY");
		this.velocity.z = compound.getDouble("VelZ");

	}

	@Override
	public TagCompound save() {
		CompoundBuilder builder = new CompoundBuilder().start("VelocityComponent");

		builder.addDouble("VelX", this.velocity.x).addDouble("VelY", this.velocity.y).addDouble("VelZ",
				this.velocity.z);

		return builder.build();
	}
}
