package net.luxvacuos.voxel.client.world.entities;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.universal.resources.UniversalResources;

public class Ghost extends GameEntity {

	public Ghost(Vector3f position) {
		super(UniversalResources.player, position, new Vector3f(-1, -1, -1), new Vector3f(1, 1, 1), 0, 0, 0, 0, 0, 0,
				1);
	}

	@Override
	public void update(float delta) {
	}

}
