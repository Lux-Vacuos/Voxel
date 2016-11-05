package net.luxvacuos.voxel.universal.ecs.components;

import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.igl.vector.Vector3d;

public class Velocity implements VoxelComponent {
private double x, y, z;
	
	public Velocity() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Velocity(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Velocity(Vector3d vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public Vector3d getVelocity() {
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
	
	public Velocity setX(double x) {
		this.x = x;
		
		return this;
	}
	
	public Velocity setY(double y) {
		this.y = y;
		
		return this;
	}
	
	public Velocity setZ(double z) {
		this.z = z;
		
		return this;
	}
	
	public Velocity set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}
	
	public Velocity set(Vector3d vec) {
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
		this.x = compound.getDouble("VelX");
		this.y = compound.getDouble("VelY");
		this.z = compound.getDouble("VelZ");
		
	}

	@Override
	public TagCompound save() {
		CompoundBuilder builder = new CompoundBuilder().start("VelocityComponent");
		
		builder.addDouble("VelX", this.x).addDouble("VelY", this.y).addDouble("VelZ", this.z);
		
		return builder.build();
	}
}
