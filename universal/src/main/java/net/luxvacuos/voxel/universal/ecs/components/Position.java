package net.luxvacuos.voxel.universal.ecs.components;

import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.igl.vector.Vector3d;

public class Position implements VoxelComponent {

	private Vector3d position;

	public Position() {
		position = new Vector3d();
	}

	public Position(double x, double y, double z) {
		position = new Vector3d(x, y, z);
	}

	public Position(Vector3d vec) {
		position = new Vector3d(vec);
	}

	public Vector3d getPosition() {
		return position;
	}

	public double getX() {
		return this.position.x;
	}

	public double getY() {
		return this.position.y;
	}

	public double getZ() {
		return this.position.z;
	}

	public Position setX(double x) {
		this.position.x = x;
		return this;
	}

	public Position setY(double y) {
		this.position.y = y;
		return this;
	}

	public Position setZ(double z) {
		this.position.z = z;
		return this;
	}

	public Position set(double x, double y, double z) {
		position.set(x, y, z);

		return this;
	}

	public Position set(Vector3d vec) {
		position.set(vec);

		return this;
	}

	@Override
	public String toString() {
		return "[x:" + this.position.x + "]" + "[y:" + this.position.y + "]" + "[z:" + this.position.z + "]";
	}

	@Override
	public void load(TagCompound compound) throws NBTException {
		this.position.x = compound.getDouble("PosX");
		this.position.y = compound.getDouble("PosY");
		this.position.z = compound.getDouble("PosZ");

	}

	@Override
	public TagCompound save() {
		CompoundBuilder builder = new CompoundBuilder().start("PositionComponent");

		builder.addDouble("PosX", this.position.x).addDouble("PosY", this.position.y).addDouble("PosZ",
				this.position.z);

		return builder.build();
	}
}
