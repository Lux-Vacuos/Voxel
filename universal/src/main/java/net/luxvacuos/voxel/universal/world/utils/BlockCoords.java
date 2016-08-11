package net.luxvacuos.voxel.universal.world.utils;

public final class BlockCoords {
	
	private final int x, y, z;
	
	public BlockCoords(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}

}
