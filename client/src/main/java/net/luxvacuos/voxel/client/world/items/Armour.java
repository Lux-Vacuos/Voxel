package net.luxvacuos.voxel.client.world.items;

public abstract class Armour {

	protected float protection;
	protected float durability;
	protected float weight;
	protected float warmth;
	protected float impediment;
	// TODO: Enchanting

	public float getDurability() {
		return durability;
	}

	public float getImpediment() {
		return impediment;
	}

	public float getProtection() {
		return protection;
	}

	public float getWarmth() {
		return warmth;
	}

	public float getWeight() {
		return weight;
	}

}
