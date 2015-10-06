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

import io.github.guerra24.voxel.client.kernel.api.VAPI;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.DimensionalWorld;
import io.github.guerra24.voxel.client.kernel.world.physics.AABB;
import io.github.guerra24.voxel.client.kernel.world.physics.CollisionType;

public class Player extends Entity implements IEntity {
	private final float GRAVITY = -10;
	private final float JUMP_POWER = 4;

	private float upwardsSpeed = 0;
	private AABB aabb;

	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		aabb = new AABB(1, 2, 1);
	}

	@Override
	public void update(float delta, GameResources gm, GuiResources gi, DimensionalWorld world, VAPI api) {
		aabb.update(getPosition());
		super.increasePosition(0, upwardsSpeed * delta, 0);
		if (isCollision(0, world) == CollisionType.FRONT) {
			super.increasePosition(0.1f, 0, 0);
		}

		if (isCollision(0, world) == CollisionType.TOP) {
			upwardsSpeed = 0;
			isInAir = false;

		} else {
			upwardsSpeed += GRAVITY * delta;
			isInAir = true;
		}
	}

	public void jump() {
		if (!isInAir) {
			try {
				if (!checkAABB(getPosition(), new Vector3f(0, 128, 0))) {
					this.upwardsSpeed = JUMP_POWER;
					isInAir = true;
				} else {
					upwardsSpeed = 0;
					isInAir = false;
				}
			} finally {
			}
		}
	}

	private boolean checkAABB(Vector3f pos, Vector3f coll) {
		if (pos.y > coll.y)
			return true;
		return false;
	}

	@Override
	public Entity getEntity() {
		return this;
	}

	private CollisionType isCollision(int direction, DimensionalWorld world) {

		Vector3f v = this.getPosition();

		float tempx = (v.x);
		int tempX = (int) tempx;
		if (v.x < 0) {
			tempx = (v.x);
			tempX = (int) tempx - 1;
		}

		float tempz = (v.z);
		int tempZ = (int) tempz;
		if (v.z > 0) {
			tempz = (v.z);
			tempZ = (int) tempz + 1;
		}

		float tempy = (v.y);
		int tempY = (int) tempy - 2;

		int bx = (int) tempX;
		int by = (int) tempY;
		int bz = (int) tempZ;

		CollisionType collisionType = CollisionType.NONE;

		byte b = -99;
		int ground = 0;
		b = world.getGlobalBlock(world.getChunkDimension(), bx, by, bz);

		if (b != 0) {
			Vector3f playerPosition = new Vector3f();
			playerPosition.x = v.x;
			playerPosition.y = v.y;
			playerPosition.z = v.z;
			aabb.update(playerPosition);

			AABB voxel = new AABB(1f, 1f, 1f);
			Vector3f voxelPosition = new Vector3f();
			voxelPosition.x = bx - 1;
			voxelPosition.y = by;
			voxelPosition.z = bz;
			voxel.update(voxelPosition);

			if (!AABB.testAABB(aabb, voxel)) {
				collisionType = CollisionType.TOP;
				ground = (int) voxelPosition.y;
			}
		}

		return collisionType;
	}
}
