package net.luxvacuos.voxel.universal.world.utils;

import com.badlogic.gdx.math.Vector3;

public final class BlockNode {
	
	private final int x, y, z;
	
	public BlockNode(int x, int y, int z) {
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
	
	public Vector3 asVector3() {
		return new Vector3(x, y, z);
	}

}
