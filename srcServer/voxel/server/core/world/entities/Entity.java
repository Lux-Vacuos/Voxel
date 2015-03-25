package voxel.server.core.world.entities;

import org.lwjgl.util.vector.Vector3f;

public class Entity {

	private Camera camera;
	private Vector3f pos, rot;
	private int id;

	public Entity(float x, float y, float z, int id) {
		this(x, y, z, 0, 0, 0, id);
	}

	public Entity(float x, float y, float z, float rx, float ry, float rz, int id) {
		this(new Vector3f(x, y, z), new Vector3f(rx, ry, rz), id);
	}

	public Entity(Camera camera, int id) {
		this(camera, new Vector3f(camera.getX(), camera.getY(), camera.getZ()), new Vector3f(camera.getPitch(), camera.getYaw(), camera.getRoll()), id);
	}

	public Entity(Camera camera, Vector3f pos, Vector3f rot, int id) {
		this(pos, rot, id);
		this.camera = camera;
	}

	public Entity(Vector3f pos, Vector3f rot, int id) {
		this.pos = pos;
		this.rot = rot;
		this.id = id;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public int getID() {
		return id;
	}

	public Vector3f getPos() {
		return pos;
	}

	public Vector3f getRot() {
		return rot;
	}

	public void setPos(float x, float y, float z) {
		setPos(new Vector3f(x, y, z));
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public void setZ(float z) {
		this.pos.z = z;
	}

	public void setY(float y) {
		this.pos.y = y;
	}

	public void setX(float x) {
		this.pos.x = x;
	}

	public float getX() {
		return pos.x;
	}

	public float getY() {
		return pos.y;
	}

	public float getZ() {
		return pos.z;
	}

	public void setRot(float rx, float ry, float rz) {
		setRot(new Vector3f(rx, ry, rz));
	}

	public void setRot(Vector3f rot) {
		this.rot = rot;
	}

	public float getPitch() {
		return rot.x;
	}

	public float getYaw() {
		return rot.y;
	}

	public float getRoll() {
		return rot.z;
	}

	public void setYaw(float yaw) {
		this.rot.y = yaw;
	}

	public void setPitch(float pitch) {
		this.rot.x = pitch;
	}

	public void setRoll(float roll) {
		this.rot.z = roll;
	}
}
