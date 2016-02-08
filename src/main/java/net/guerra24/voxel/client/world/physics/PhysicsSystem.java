/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
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

package net.guerra24.voxel.client.world.physics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;

import net.guerra24.voxel.client.world.IWorld;
import net.guerra24.voxel.client.world.block.Block;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public class PhysicsSystem extends EntitySystem {
	private ImmutableArray<Entity> entities;

	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);

	private IWorld world;

	public PhysicsSystem(IWorld world) {
		this.world = world;
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(
				Family.all(PositionComponent.class, VelocityComponent.class, CollisionComponent.class).get());
	}

	@Override
	public void update(float deltaTime) {
		for (Entity entity : entities) {

			PositionComponent position = pm.get(entity);
			VelocityComponent velocity = vm.get(entity);
			CollisionComponent collison = cm.get(entity);

			Vector3f positionV = position.position;
			Vector3f velocityV = velocity.velocity;

			float tempx = (positionV.x);
			int tempX = (int) tempx;
			if (positionV.x < 0) {
				tempx = (positionV.x);
				tempX = (int) tempx - 1;
			}

			float tempz = (positionV.z);
			int tempZ = (int) tempz;
			if (positionV.z > 0) {
				tempz = (positionV.z);
				tempZ = (int) tempz + 1;
			}

			float tempy = (positionV.y);
			int tempY = (int) tempy;

			int bx = (int) tempX;
			int by = (int) tempY;
			int bz = (int) tempZ;

			int ya = world.getGlobalBlock(bx, by + 1, bz);
			int yb = world.getGlobalBlock(bx, by - 1, bz);
			int xa = world.getGlobalBlock(bx + 1, by, bz);
			int xb = world.getGlobalBlock(bx - 1, by, bz);
			int za = world.getGlobalBlock(bx, by, bz + 1);
			int zb = world.getGlobalBlock(bx, by, bz - 1);

			velocityV.y += -9.8f * deltaTime;

			velocityV.x *= 0.4f - velocityV.x * 0.01f;
			velocityV.z *= 0.4f - velocityV.z * 0.01f;

			collison.boundingBox.set(new Vector3(positionV.x, positionV.y, positionV.z),
					new Vector3(positionV.x + 1f, positionV.y + 1f, positionV.z + 1f));

			if (velocityV.y < 0)
				if (yb != Block.Air.getId() && yb != Block.Water.getId())
					if (entity.getComponent(CollisionComponent.class).boundingBox
							.intersects(Block.getBlock((byte) yb).getBoundingBox(new Vector3f(bx, by, bz))))
						velocityV.y = 0;

			if (velocityV.y > 0)
				if (ya != Block.Air.getId() && ya != Block.Water.getId())
					if (entity.getComponent(CollisionComponent.class).boundingBox
							.intersects(Block.getBlock((byte) ya).getBoundingBox(new Vector3f(bx, by, bz))))
						velocityV.y = 0;

			if (velocityV.x < 0)
				if (xb != Block.Air.getId() && xb != Block.Water.getId())
					if (entity.getComponent(CollisionComponent.class).boundingBox
							.intersects(Block.getBlock((byte) xb).getBoundingBox(new Vector3f(bx, by, bz))))
						velocityV.x = 0;

			if (velocityV.x > 0)
				if (xa != Block.Air.getId() && xa != Block.Water.getId())
					if (entity.getComponent(CollisionComponent.class).boundingBox
							.intersects(Block.getBlock((byte) xa).getBoundingBox(new Vector3f(bx, by, bz))))
						velocityV.x = 0;

			if (velocityV.z < 0)
				if (zb != Block.Air.getId() && zb != Block.Water.getId())
					if (entity.getComponent(CollisionComponent.class).boundingBox
							.intersects(Block.getBlock((byte) zb).getBoundingBox(new Vector3f(bx, by, bz))))
						velocityV.z = 0;

			if (velocityV.z > 0)
				if (za != Block.Air.getId() && za != Block.Water.getId())
					if (entity.getComponent(CollisionComponent.class).boundingBox
							.intersects(Block.getBlock((byte) za).getBoundingBox(new Vector3f(bx, by, bz - 1))))
						velocityV.z = 0;

			position.position.x += velocityV.x * deltaTime;
			position.position.y += velocityV.y * deltaTime;
			position.position.z += velocityV.z * deltaTime;
		}
	}

}