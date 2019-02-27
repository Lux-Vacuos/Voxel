package net.luxvacuos.voxel.universal.world.utils;

import com.badlogic.gdx.math.Vector3;

public final class BlockNode {

	private int x, y, z;

	public BlockNode(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public Vector3 asVector3() {
		return new Vector3(x, y, z);
	}

}
