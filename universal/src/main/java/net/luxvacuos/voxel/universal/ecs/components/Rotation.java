package net.luxvacuos.voxel.universal.ecs.components;

import com.hackhalo2.nbt.CompoundBuilder;
import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.igl.vector.Vector3d;

public class Rotation implements VoxelComponent {
	
	private double x, y, z;
	
	public Rotation() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Rotation(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Rotation(Vector3d vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public Vector3d getRotation() {
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
	
	public Rotation setX(double x) {
		this.x = x;
		
		return this;
	}
	
	public Rotation setY(double y) {
		this.y = y;
		
		return this;
	}
	
	public Rotation setZ(double z) {
		this.z = z;
		
		return this;
	}
	
	public Rotation set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}
	
	public Rotation set(Vector3d vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		
		return this;
	}
	
	@Override
	public void load(TagCompound compound) throws NBTException {
		this.x = compound.getDouble("RotX");
		this.y = compound.getDouble("RotY");
		this.z = compound.getDouble("RotZ");
		
	}

	@Override
	public TagCompound save() {
		CompoundBuilder builder = new CompoundBuilder().start("RotationComponent");
		
		builder.addDouble("RotX", this.x).addDouble("RotY", this.y).addDouble("RotZ", this.z);
		
		return builder.build();
	}

}
