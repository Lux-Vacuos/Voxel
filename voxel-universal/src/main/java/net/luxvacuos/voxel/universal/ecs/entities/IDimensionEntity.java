package net.luxvacuos.voxel.universal.ecs.entities;

import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public interface IDimensionEntity {
	
	public void updateDim(float delta, IDimension dim);

}
