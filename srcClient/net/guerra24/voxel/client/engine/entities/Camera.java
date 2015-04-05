package net.guerra24.voxel.client.engine.entities;

import java.nio.DoubleBuffer;

import net.guerra24.voxel.client.engine.DisplayManager;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(300, 70, 300);
	private float pitch;
	private float yaw;
	private float roll;// Optional!!!

	private float speed;

	private static int mouseSpeed = 2;
	private static final int maxLookUp = 90;
	private static final int maxLookDown = -90;

	public Camera() {
		this.speed = 0.5f;
	}

	public void move() {

		/*if (Keyboard.isKeyDown(Keyboard.KEY_W)) {

			position.z += -(float) Math.cos(Math.toRadians(yaw)) * speed;
			position.x += (float) Math.sin(Math.toRadians(yaw)) * speed;

		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z -= -(float) Math.cos(Math.toRadians(yaw)) * speed;
			position.x -= (float) Math.sin(Math.toRadians(yaw)) * speed;

		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {

			position.z += (float) Math.sin(Math.toRadians(yaw)) * speed;
			position.x += (float) Math.cos(Math.toRadians(yaw)) * speed;

		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {

			position.z -= (float) Math.sin(Math.toRadians(yaw)) * speed;
			position.x -= (float) Math.cos(Math.toRadians(yaw)) * speed;

		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {

			position.y += speed;

		} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

			position.y -= speed;

		}
	*/}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

}
