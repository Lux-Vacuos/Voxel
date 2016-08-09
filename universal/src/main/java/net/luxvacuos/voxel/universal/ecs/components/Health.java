package net.luxvacuos.voxel.universal.ecs.components;

import com.badlogic.ashley.core.Component;

public class Health implements Component {
	
	private float health;
	
	public Health() {
		this.health = 10;
	}
	
	public Health(float health) {
		this.health = health;
	}
	
	public float get() {
		return this.health;
	}
	
	public Health set(float health) {
		this.health = health;
		
		return this;
	}
	
	public Health take(float amount) {
		this.health -= amount;
		
		return this;
	}
	
	public Health give(float amount) {
		this.health += amount;
		
		return this;
	}

}
