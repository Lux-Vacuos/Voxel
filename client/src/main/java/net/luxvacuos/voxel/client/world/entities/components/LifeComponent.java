package net.luxvacuos.voxel.client.world.entities.components;

import com.badlogic.ashley.core.Component;

public class LifeComponent implements Component {

	public float life;

	public LifeComponent() {
	}

	public LifeComponent(float life) {
		this.life = life;
	}

}
