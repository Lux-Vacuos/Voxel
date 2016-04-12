package net.luxvacuos.voxel.client.world.entities.components;

import com.badlogic.ashley.core.Component;

public class LifeComponent implements Component {

	private float life;

	public LifeComponent() {
	}

	public LifeComponent(float life) {
		this.life = life;
	}

	public void setLife(float life) {
		this.life = life;
	}

	public float getLife() {
		return life;
	}

}
