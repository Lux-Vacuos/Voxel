package net.luxvacuos.voxel.client.world.entities;

import net.luxvacuos.voxel.client.world.entities.components.RendereableComponent;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Rotation;
import net.luxvacuos.voxel.universal.ecs.components.Scale;

public class Plane extends AbstractEntity {

	public Plane() {
		add(new Position());
		add(new Rotation());
		add(new RendereableComponent());
		add(new Scale());
		getComponent(RendereableComponent.class).model = EntityResources.getPlane();
	}

}
