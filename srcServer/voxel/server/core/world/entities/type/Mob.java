package voxel.server.core.world.entities.type;

import voxel.client.core.engine.GameObject;
import voxel.server.core.world.entities.Camera;
import voxel.server.core.world.entities.Entity;

public class Mob extends Entity implements GameObject {

	private int mobID;
	private boolean isDead;

	public Mob(Camera camera, int id, int mobID) {
		this(camera, camera.getX(), camera.getY(), camera.getZ(), 0, 0, 0, id,
				mobID);
	}

	public Mob(Camera camera, float x, float y, float z, float rx, float ry,
			float rz, int id, int mobID) {
		super(camera, id);
		this.mobID = mobID;
	}

	@Override
	public void update() {
	}

	public void move() {
		getCamera().updateMouse();
		getCamera().updateKeyboard(32, 0.2f);
		setPos(getCamera().getX(), getCamera().getY(), getCamera().getZ());
		setRot(getCamera().getPitch(), getCamera().getYaw(), getCamera()
				.getRoll());
	}

	@Override
	public void render() {
	}

	@Override
	public void dispose() {
	}

	public int getID() {
		return mobID;
	}

	public void set(int mobID) {
		this.mobID = mobID;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
}
