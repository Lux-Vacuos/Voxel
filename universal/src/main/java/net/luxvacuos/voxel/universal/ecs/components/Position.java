package net.luxvacuos.voxel.universal.ecs.components;

import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.igl.vector.Vector3d;

public class Position implements VoxelComponent {

	private double x, y, z;
	
	public Position() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Position(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Position(Vector3d vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public Vector3d getPosition() {
		return new Vector3d(this.x, this.y, this.z);
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getZ() {
		return this.z;
	}
	
	public Position setX(double x) {
		this.x = x;
		
		return this;
	}
	
	public Position setY(double y) {
		this.y = y;
		
		return this;
	}
	
	public Position setZ(double z) {
		this.z = z;
		
		return this;
	}
	
	public Position set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}
	
	public Position set(Vector3d vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		
		return this;
	}
	
	@Override
	public String toString() {
		return "[x:" + this.x + "]" + "[y:" + this.y + "]" + "[z:" + this.z + "]";
	}

	@Override
	public void load(TagCompound compound) throws NBTException {
		this.x = compound.getDouble("PosX");
		this.y = compound.getDouble("PosY");
		this.z = compound.getDouble("PosZ");
		
	}

	@Override
	public TagCompound save() {
		CompoundBuilder builder = new CompoundBuilder().start("PositionComponent");
		
		builder.addDouble("PosX", this.x).addDouble("PosY", this.y).addDouble("PosZ", this.z);
		
		return builder.build();
	}
}
