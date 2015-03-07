package net.voxel.core.world.blocks;

public abstract class Blocks {
	
	public static Blocks Void = new BlocksVoid();	
	private byte id;
	private boolean isActive;
	
	public Blocks(byte id) {
		this.id = id;
	}
	public abstract byte getId();
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}