package net.luxvacuos.voxel.client.api;

import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.Dimension;
import net.luxvacuos.voxel.universal.api.APIMethod;

public class GetActiveDimension implements APIMethod<Dimension> {

	@Override
	public Dimension run(Object... objects) {
		return GameResources.getInstance().getWorldsHandler().getActiveWorld().getActiveDimension();
	}

}
