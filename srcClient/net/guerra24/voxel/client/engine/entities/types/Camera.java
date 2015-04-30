package net.guerra24.voxel.client.engine.entities.types;

import net.guerra24.voxel.client.engine.util.Logger;
import net.guerra24.voxel.client.engine.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 70, 0);
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

		float mouseDX = Mouse.getDX() * mouseSpeed * 0.16f;
		float mouseDY = Mouse.getDY() * mouseSpeed * 0.16f;
		if (yaw + mouseDX >= 360) {
			yaw = yaw + mouseDX - 360;
		} else if (yaw + mouseDX < 0) {
			yaw = 360 - yaw + mouseDX;
		} else {
			yaw += mouseDX;
		}
		if (pitch - mouseDY >= maxLookDown && pitch - mouseDY <= maxLookUp) {
			pitch += -mouseDY;
		} else if (pitch - mouseDY < maxLookDown) {
			pitch = maxLookDown;
		} else if (pitch - mouseDY > maxLookUp) {
			pitch = maxLookUp;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {

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
		if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
			Logger.log("Saving World");
			World.saveGame(World.worldPath);
			Logger.log("World saved");
		} else if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			Logger.log("Loading World");
			World.loadGame(World.worldPath);
			Logger.log("World loaded");
		}

	}

	public void setMouse() {
		if (Mouse.isButtonDown(0)) {
			Mouse.setGrabbed(true);
			Mouse.setCursorPosition(Display.getWidth() / 2,
					Display.getHeight() / 2);
		} else if (Mouse.isButtonDown(1)) {
			Mouse.setGrabbed(false);
		}
	}

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
