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
import io.github.guerra24.voxel.client.kernel.graphics.opengl.DisplayManager;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class Player extends Entity {
	private static final float GRAVITY = -10;
	private static final float JUMP_POWER = 4;

	private float upwardsSpeed = 0;

	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move() {
		super.increasePosition(0,
				upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		try {
			//if (Kernel.world.getBlock((int) (super.getPosition().x - 0.5f),
			//		(int) super.getPosition().y - 1,
			//		(int) (super.getPosition().z + 0.5f)) == 0) {
				upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
				isInAir = true;
			//} else {
				upwardsSpeed = 0;
				isInAir = false;
			//}
		} finally {
		}
	}

	public void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
}
