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

package io.github.guerra24.voxel.client.kernel.world.entities;

import io.github.guerra24.voxel.client.kernel.core.Kernel;
import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.DisplayManager;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

/**
 * Camera
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.2 Build-57
 * @since 0.0.1 Build-5
 */
public class Camera {

	private Vector3f position = new Vector3f(0, 80, 0);
	private float pitch;
	private float yaw;
	private float speed;
	private float multiplierMouse = 24;
	private float multiplierMovement = 24;
	private byte block = 2;

	private static int mouseSpeed = 2;
	private static final int maxLookUp = 90;
	private static final int maxLookDown = -90;
	public boolean isMoved = false;

	public Camera() {
		this.speed = 0.2f;
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
			// position.y += DisplayManager.getFrameTimeSeconds() * speed
			// * multiplierMovement;
			Kernel.gameResources.player.jump();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			// position.y -= DisplayManager.getFrameTimeSeconds() * speed
			// * multiplierMovement;
			speed = 0.1f;
		} else {
			speed = 0.2f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			speed = 1;
		} else {
			speed = 0.2f;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_T))
			System.out.println(Kernel.renderCallsPerFrame);
		if (Keyboard.isKeyDown(Keyboard.KEY_Y))
			System.out.println(position);
	}

	public void updatePicker() {
		if (Keyboard.isKeyDown(Keyboard.KEY_1))
			block = 1;
		else if (Keyboard.isKeyDown(Keyboard.KEY_2))
			block = 2;
		else if (Keyboard.isKeyDown(Keyboard.KEY_3))
			block = 3;
		else if (Keyboard.isKeyDown(Keyboard.KEY_4))
			block = 4;
		else if (Keyboard.isKeyDown(Keyboard.KEY_5))
			block = 5;
		else if (Keyboard.isKeyDown(Keyboard.KEY_6))
			block = 6;
		else if (Keyboard.isKeyDown(Keyboard.KEY_7))
			block = 8;
		else if (Keyboard.isKeyDown(Keyboard.KEY_8))
			block = 9;
		if (Mouse.isButtonDown(0)) {
			Kernel.gameResources.mouse.update();
			Kernel.world
					.setGlobalBlock(
							Kernel.world.dim,
							(int) (Kernel.gameResources.mouse.getCurrentRay().x + position.x),
							(int) (Kernel.gameResources.mouse.getCurrentRay().y + position.y),
							(int) (Kernel.gameResources.mouse.getCurrentRay().z + position.z),
							(byte) 0);
		} else if (Mouse.isButtonDown(1)) {
			Kernel.gameResources.mouse.update();
			Kernel.world
					.setGlobalBlock(
							Kernel.world.dim,
							(int) (Kernel.gameResources.mouse.getCurrentRay().x + position.x),
							(int) (Kernel.gameResources.mouse.getCurrentRay().y + position.y),
							(int) (Kernel.gameResources.mouse.getCurrentRay().z + position.z),
							block);
		}
	}

	public void updateDebug() {
		if (Keyboard.isKeyDown(Keyboard.KEY_F3)
				&& Keyboard.isKeyDown(Keyboard.KEY_R))
			for (int zr = -KernelConstants.genRadius; zr <= KernelConstants.genRadius; zr++) {
				int zz = Kernel.world.getzPlayChunk() + zr;
				for (int xr = -KernelConstants.genRadius; xr <= KernelConstants.genRadius; xr++) {
					int xx = Kernel.world.getxPlayChunk() + xr;
					if (zr * zr + xr * xr <= KernelConstants.genRadius
							* KernelConstants.genRadius) {
						if (Kernel.world.hasChunk(Kernel.world.dim, xx, zz)) {
							Kernel.world.getChunk(Kernel.world.dim, xx, zz)
									.clear();
						}
					}
				}
			}
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