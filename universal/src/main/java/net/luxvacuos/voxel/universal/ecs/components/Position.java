package net.luxvacuos.voxel.universal.ecs.components;

import com.badlogic.ashley.core.Component;

import net.luxvacuos.igl.vector.Vector3f;

public class Position implements Component {

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
	
	public Position(Vector3f vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public Vector3f getPosition() {
		return new Vector3f(this.x, this.y, this.z);
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
	
	public Position set(Vector3f vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		
		return this;
	}
	
	@Override
	public String toString() {
		return "[x:" + this.x + "]" + "[y:" + this.y + "]" + "[z:" + this.z + "]";
	}
}
