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

import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.World;
import io.github.guerra24.voxel.client.kernel.world.physics.AABB;
import io.github.guerra24.voxel.client.kernel.world.physics.CollisionType;

public class Player extends Entity implements IEntity {
	private static final float GRAVITY = -10;
	private static final float JUMP_POWER = 4;

	private float upwardsSpeed = 0;
	private AABB aabb;

	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		aabb = new AABB(1, 2, 1);
	}

	@Override
	public void update(float delta, GameResources gm, GuiResources gi, World world) {
		aabb.update(getPosition());
		super.increasePosition(0, upwardsSpeed * delta, 0);
		if (isCollision(0, world) == CollisionType.NONE) {
			upwardsSpeed += GRAVITY * delta;
			isInAir = true;
		} else {
			upwardsSpeed = 0;
			isInAir = false;
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

	private CollisionType isCollision(int dir, World world) {// This Works >>>

		Vector3f v = this.getPosition();

		float tempx = (v.x) / 1 * 1;
		int tempX = (int) tempx;

		float tempz = (v.z) / 1 * 1;
		int tempZ = (int) tempz;

		float tempy = (v.y) / 1 * 1;
		int tempY = (int) tempy - 1;

		int bx = (int) tempX;
		int by = (int) tempY;
		int bz = (int) tempZ;

		CollisionType collisionType = CollisionType.NONE;

		byte b = -99;
		b = world.getGlobalBlock(world.dim, bx, by, bz);

		if (b != 0) {
			Vector3f playerPosition = new Vector3f();
			playerPosition.x = v.x;
			playerPosition.y = v.y;
			playerPosition.z = v.z;
			aabb.update(playerPosition);

			AABB voxel = new AABB(1f, 1f, 1f);
			Vector3f voxelPosition = new Vector3f();
			voxelPosition.x = bx;
			voxelPosition.y = by;
			voxelPosition.z = bz;
			voxel.update(voxelPosition);

			if (!AABB.testAABB(aabb, voxel)) {
				collisionType = CollisionType.TOP;
				upwardsSpeed = 0;
			}
		}

		return collisionType;
	}
}
