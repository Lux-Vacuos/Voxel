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

import io.github.guerra24.voxel.client.kernel.DisplayManager;
import io.github.guerra24.voxel.client.resources.models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Player extends Entity {
	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 12;
	private static final float TERRAIN_HEIGHT = 71;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;

	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move() {
		checkInputs();
		super.increaseRotation(0,
				currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math
				.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math
				.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0,
				upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);

		if (super.getPosition().y < TERRAIN_HEIGHT) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = TERRAIN_HEIGHT;
		}
	}

	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	private void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			this.currentSpeed = -RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			this.currentSpeed = RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			this.currentTurnSpeed = TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			jump();
		}
	}
}
