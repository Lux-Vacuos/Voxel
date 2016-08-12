package net.luxvacuos.voxel.universal.ecs.components;

import com.badlogic.ashley.core.Component;

public class Scale implements Component {
	
	private float scale;
	
	public Scale() {
		this.scale = 1;
	}
	
	public Scale(float scale) {
		this.scale = scale;
	}
	
	public float getScale() {
		return this.scale;
	}
	
	public Scale setScale(float scale) {
		this.scale = scale;
		
		return this;
	}

}
