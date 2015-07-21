/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.guerra24.voxel.client.world.entities;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_TRANSFORM_BIT;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import io.github.guerra24.voxel.client.kernel.DisplayManager;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 80, 0);
	private float pitch;
	private float yaw;
	private float speed;
	private float multiplierMouse = 24;
	private float multiplierMovement = 16;

	private static int mouseSpeed = 2;
	private static final int maxLookUp = 90;
	private static final int maxLookDown = -90;
	public boolean isMoved = false;

	public Camera() {
		this.speed = 0.5f;
	}

	public void move() {
		isMoved = false;
		float mouseDX = Mouse.getDX() * DisplayManager.getFrameTimeSeconds()
				* mouseSpeed * 0.16f * multiplierMouse;
		float mouseDY = Mouse.getDY() * DisplayManager.getFrameTimeSeconds()
				* mouseSpeed * 0.16f * multiplierMouse;
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
			position.z += -(float) Math.cos(Math.toRadians(yaw))
					* DisplayManager.getFrameTimeSeconds() * speed
					* multiplierMovement;
			position.x += (float) Math.sin(Math.toRadians(yaw))
					* DisplayManager.getFrameTimeSeconds() * speed
					* multiplierMovement;
			isMoved = true;

		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z -= -(float) Math.cos(Math.toRadians(yaw))
					* DisplayManager.getFrameTimeSeconds() * speed
					* multiplierMovement;
			position.x -= (float) Math.sin(Math.toRadians(yaw))
					* DisplayManager.getFrameTimeSeconds() * speed
					* multiplierMovement;
			isMoved = true;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.z += (float) Math.sin(Math.toRadians(yaw))
					* DisplayManager.getFrameTimeSeconds() * speed
					* multiplierMovement;
			position.x += (float) Math.cos(Math.toRadians(yaw))
					* DisplayManager.getFrameTimeSeconds() * speed
					* multiplierMovement;
			isMoved = true;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.z -= (float) Math.sin(Math.toRadians(yaw))
					* DisplayManager.getFrameTimeSeconds() * speed
					* multiplierMovement;
			position.x -= (float) Math.cos(Math.toRadians(yaw))
					* DisplayManager.getFrameTimeSeconds() * speed
					* multiplierMovement;
			isMoved = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			if (position.y < 144) {
				position.y += DisplayManager.getFrameTimeSeconds() * speed
						* multiplierMovement;
				// Kernel.gameResources.player.jump();
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			if (position.y > -16) {
				position.y -= DisplayManager.getFrameTimeSeconds() * speed
						* multiplierMovement;
			}
		}
		applyTranslations();
	}

	public void applyTranslations() {
		glPushAttrib(GL_TRANSFORM_BIT);
		glMatrixMode(GL_MODELVIEW);
		glRotatef(getPitch(), 1, 0, 0);
		glRotatef(getYaw(), 0, 1, 0);
		glRotatef(0, 0, 0, 1);
		glTranslatef(-getPosition().x, -getPosition().y, -getPosition().z);
		glPopAttrib();
	}

	public void setMouse() {
		Mouse.setGrabbed(true);
		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
	}

	public void unlockMouse() {
		Mouse.setGrabbed(false);
	}

	public void invertPitch() {
		this.pitch = -pitch;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}
}