package net.luxvacuos.voxel.client.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.luxvacuos.voxel.client.resources.GameResources;

public abstract class World {

	protected Random seed;
	protected String name;
	protected Dimension activeDimension;

	private Map<Integer, Dimension> dimensions;

	public World(String name) {
		this.name = name;
		dimensions = new HashMap<>();
	}

	public abstract void init(GameResources gm);

	public void load(GameResources gm) {
	}

	public void addDimension(Dimension dim) {
		dimensions.put(dim.getDimensionID(), dim);
	}

	public Dimension getDimension(int id) {
		return dimensions.get(id);
	}

	public void setActiveDimension(Dimension dim) {
		this.activeDimension = dim;
	}

	public Dimension getActiveDimension() {
		return activeDimension;
	}

	public String getName() {
		return name;
	}

	public Random getSeed() {
		return seed;
	}

	public Map<Integer, Dimension> getDimensions() {
		return dimensions;
	}
}
