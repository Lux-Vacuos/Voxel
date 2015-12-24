package net.guerra24.voxel.client.world;

public enum Biomes {
	OCEAN(1f), HILLS(1.2f), MOUNTAIN_CANYON(8f);

	private final float multiplier;

	private Biomes(float multiplier) {
		this.multiplier = multiplier;
	}

	public float getMultiplier() {
		return multiplier;
	}

}
