package net.luxvacuos.voxel.client.world;

import java.util.Random;

import net.luxvacuos.voxel.client.resources.GameResources;

public class DefaultWorld extends World {

	public DefaultWorld(String name) {
		super(name);
	}

	@Override
	public void init(GameResources gm) {
		super.seed = new Random();
		addDimension(new DefaultDimension(name, seed, 0, gm));
		setActiveDimension(getDimension(0));
	}

}
