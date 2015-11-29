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

package net.guerra24.voxel.client.world.entities;

import net.guerra24.voxel.client.api.API;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.GuiResources;
import net.guerra24.voxel.client.resources.models.TexturedModel;
import net.guerra24.voxel.client.world.IWorld;
import net.guerra24.voxel.client.world.block.Block;
import net.guerra24.voxel.client.world.physics.AABB;
import net.guerra24.voxel.client.world.physics.CollisionType;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public class Player extends Entity implements IEntity {
	private final float JUMP_POWER = 4;
	private boolean isInWater = false;
	private float upwardsSpeed = 0;
	private AABB aabb;

	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		aabb = new AABB(position.x, position.y, position.z, 1, 2, 1);
	}

	@Override
	public void update(float delta, GameResources gm, GuiResources gi, IWorld world, API api) {
		aabb.update(getPosition());
		super.increasePosition(0, upwardsSpeed * delta, 0);
		if (isCollision(0, world) == CollisionType.FRONT) {
			super.increasePosition(0.1f, 0, 0);
		}
		CollisionType collision = isCollision(0, world);
		if (collision == CollisionType.TOP) {
			upwardsSpeed = 0;
			isInAir = false;
			isInWater = false;
		} else if (collision == CollisionType.WATER) {
			upwardsSpeed = -30f * delta;
			isInWater = true;
			isInAir = false;
		} else {
			upwardsSpeed += VoxelVariables.GRAVITY * delta;
			isInAir = true;
			isInWater = false;
		}
	}

	public void jump() {
		if (!isInAir && !isInWater) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		} else if (isInWater) {
			this.upwardsSpeed = 4;
		}
	}

	@Override
	public Entity getEntity() {
		return this;
	}

	private CollisionType isCollision(int direction, IWorld world) {

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
		b = world.getGlobalBlock(bx, by, bz);

		if (b != 0 && b != Block.Water.getId()) {
			Vector3f playerPosition = new Vector3f();
			playerPosition.x = v.x;
			playerPosition.y = v.y;
			playerPosition.z = v.z;
			aabb.update(playerPosition);

			AABB voxel = new AABB(bx, by, bz, 1f, 1f, 1f);

			if (!AABB.testAABB(aabb, voxel)) {
				collisionType = CollisionType.TOP;
			}
		} else if (b == Block.Water.getId()) {
			Vector3f playerPosition = new Vector3f();
			playerPosition.x = v.x;
			playerPosition.y = v.y;
			playerPosition.z = v.z;
			aabb.update(playerPosition);

			if (!AABB.testAABB(aabb, aabb)) {
				collisionType = CollisionType.WATER;
			}
		}

		return collisionType;
	}
}
