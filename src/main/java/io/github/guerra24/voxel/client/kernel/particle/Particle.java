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


package io.github.guerra24.voxel.client.kernel.particle;

import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.DimensionalWorld;
import io.github.guerra24.voxel.client.kernel.world.entities.Entity;
import io.github.guerra24.voxel.client.kernel.world.physics.AABB;
import io.github.guerra24.voxel.client.kernel.world.physics.CollisionType;

public class Particle extends Entity {

	private final float GRAVITY = -0.001f;
	private float lifeTime;
	private float friction = 0.01f;
	private float xVel = 0;
	private float yVel = 0;
	private float zVel = 0;

	public Particle(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float lifeTime,
			float scale, float xVel, float zVel) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.xVel = xVel;
		this.zVel = zVel;
		this.lifeTime = lifeTime;
	}

	public void update(float delta, GameResources gm, GuiResources gi, DimensionalWorld world) {
		Vector3f normal = new Vector3f(0, 1, 0);
		Vector3f dir = Vector3f.sub(
				new Vector3f(gm.getCamera().getPosition().x, gm.getCamera().getPosition().y,
						gm.getCamera().getPosition().z),
				new Vector3f(super.getPosition().x, super.getPosition().y, super.getPosition().z), null);
		dir = (Vector3f) dir.normalise();
		float angle = (float) Math.toDegrees(Math.acos(Vector3f.dot(normal, dir)));
		Vector3f rotationAxis = Vector3f.cross(normal, dir, null);
		rotationAxis = (Vector3f) rotationAxis.normalise();
		super.setRotX(angle * rotationAxis.x);
		super.setRotY(angle * rotationAxis.y);
		super.setRotZ(angle * rotationAxis.z);
		updatePysics(delta, world);
		lifeTime--;
	}

	private void updatePysics(float delta, DimensionalWorld world) {
		if (xVel > 0)
			xVel -= friction * delta;
		else if (xVel < 0)
			xVel += friction * delta;
		if (zVel > 0)
			zVel -= friction * delta;
		else if (zVel < 0)
			zVel += friction * delta;

		if (isCollision(0, world) == CollisionType.TOP)
			yVel = 0;
		else
			yVel += GRAVITY * delta;
		super.increasePosition(xVel, yVel, zVel);
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
		int tempY = (int) tempy;

		int bx = (int) tempX;
		int by = (int) tempY;
		int bz = (int) tempZ;

		CollisionType collisionType = CollisionType.NONE;

		byte b = -99;
		b = world.getGlobalBlock(world.getChunkDimension(), bx, by, bz);

		if (b != 0) {
			Vector3f playerPosition = new Vector3f();
			playerPosition.x = v.x;
			playerPosition.y = v.y;
			playerPosition.z = v.z;
			this.update(playerPosition);

			AABB voxel = new AABB(0.5f, 0.5f, 0.5f);
			Vector3f voxelPosition = new Vector3f();
			voxelPosition.x = bx - 1;
			voxelPosition.y = by;
			voxelPosition.z = bz;
			voxel.update(voxelPosition);

			if (!AABB.testAABB(this, voxel)) {
				collisionType = CollisionType.TOP;
			}
		}

		return collisionType;
	}

	public float getLifeTime() {
		return lifeTime;
	}

}
