package net.luxvacuos.voxel.client.world.entities;

import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.world.entities.components.RendereableComponent;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Rotation;
import net.luxvacuos.voxel.universal.ecs.components.Scale;

public class Test extends AbstractEntity {

	public Test(Vector3d pos) {
		add(new Position(pos));
		add(new Rotation());
		add(new RendereableComponent());
		add(new Scale());
		getComponent(RendereableComponent.class).model = EntityResources.getTest();
	}

}
