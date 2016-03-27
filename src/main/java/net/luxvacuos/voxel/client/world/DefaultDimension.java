package net.luxvacuos.voxel.client.world;

import java.util.Random;

import net.luxvacuos.voxel.client.resources.GameResources;

public class DefaultDimension extends Dimension {

	public DefaultDimension(String name, Random seed, int chunkDim, GameResources gm) {
		super(name, seed, chunkDim, gm);
	}

}
