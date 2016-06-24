package net.luxvacuos.voxel.client.world;

import java.util.Random;

import net.luxvacuos.voxel.client.resources.GameResources;

public class ClientWorld extends World {

	public ClientWorld() {
		super("server");
	}

	@Override
	protected void localInit(GameResources gm) {
		addDimension(new ClientDimension(name, new Random(), 0, gm));
		setActiveDimension(getDimension(0));
	}

	@Override
	protected void load(GameResources gm) {
	}

	@Override
	protected void save() {
	}

	@Override
	public void dispose() {
	}

}
