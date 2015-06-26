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

package io.github.guerra24.voxel.client.world.entities.types;

import io.github.guerra24.voxel.client.kernel.DisplayManager;
import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.kernel.util.AbstractFilesPath;
import io.github.guerra24.voxel.client.kernel.util.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Camera {

	private Vector3f position = new Vector3f(40, 80, 40);
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
			if (position.y < 128) {
				position.y += DisplayManager.getFrameTimeSeconds() * speed
						* multiplierMovement;
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			if (position.y > 0) {
				position.y -= DisplayManager.getFrameTimeSeconds() * speed
						* multiplierMovement;
			}
		}
		if (Mouse.isButtonDown(2)) {
			loadCameraPos();
		}
	}

	public void setMouse() {
		Mouse.setGrabbed(true);
		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
	}

	public void unlockMouse() {
		Mouse.setGrabbed(false);
	}

	public void saveCameraPos() {
		String json = Kernel.gameResources.gson
				.toJson(Kernel.gameResources.camera);

		FileWriter writer;
		try {
			writer = new FileWriter(AbstractFilesPath.camPath);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			Logger.error(Thread.currentThread(), "Failed to Save Camera pos");
		}
	}

	public void loadCameraPos() {

		try {
			BufferedReader camera = new BufferedReader(new FileReader(
					AbstractFilesPath.camPath));
			JsonParser parser = new JsonParser();
			JsonObject jobject = parser.parse(camera).getAsJsonObject();

			Camera cse = Kernel.gameResources.gson.fromJson(jobject,
					Camera.class);
			Kernel.gameResources.camera = cse;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Logger.error(Thread.currentThread(), "Failed to load Save Game");
		}
	}

	public void invertPitch() {
		this.pitch = -pitch;
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
}