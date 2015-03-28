package voxel.server.core.engine.model;

public class Position {
	/*
	 * float x = WorldManager.getMobManager().getPlayer().getX(); float y =
	 * WorldManager.getMobManager().getPlayer().getY(); float z =
	 * WorldManager.getMobManager().getPlayer().getZ();
	 */
	float x;
	float y;
	float z;

	public Position(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setPosition(Position value) {
		this.x = value.getX();
		this.y = value.getY();
		this.z = value.getZ();
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}
}
