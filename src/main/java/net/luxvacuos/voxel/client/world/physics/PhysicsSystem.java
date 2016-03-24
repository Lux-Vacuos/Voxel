/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.world.physics;

import java.util.List;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.world.IWorld;
import net.luxvacuos.voxel.client.world.block.Block;

public class PhysicsSystem extends EntitySystem {
	private ImmutableArray<Entity> entities;

	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);

	private IWorld world;
	private List<BoundingBox> boxes;
	private Vector3f tmp = new Vector3f();;

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
// NEW COLLISION SYSTEM, PRE-PRE-PRE ALPHA
	/*
	@Override
	public void update(float deltaTime) {
		for (Entity entity : entities) {

			PositionComponent position = pm.get(entity);
			VelocityComponent velocity = vm.get(entity);
			CollisionComponent collison = cm.get(entity);

			velocity.velocity.y += -1.8f * deltaTime;

			velocity.velocity.x *= 0.4f - velocity.velocity.x * 0.01f;
			velocity.velocity.z *= 0.4f - velocity.velocity.z * 0.01f;

			collison.boundingBox.set(
					new Vector3(position.position.x - 0.2f, position.position.y - 1f, position.position.z - 0.2f),
					new Vector3(position.position.x + 0.2f, position.position.y + 0.2f, position.position.z + 0.2f));

			boxes = world.getGlobalBoundingBox(collison.boundingBox);

			for (BoundingBox boundingBox : boxes) {
				if (boundingBox.intersects(collison.boundingBox)) {
					tmp.set(minimumTranslation(collison.boundingBox, boundingBox));
					if (tmp.x != 0) {
						float pos = 0;
						if (velocity.velocity.x > 0)
							pos -= 0.01f;
						else if (velocity.velocity.x < 0)
							pos += 0.01f;
						position.position.x += pos;
						velocity.velocity.x = 0;
					}
					if (tmp.y != 0) {
						float pos = 0;
						if (velocity.velocity.y > 0)
							pos -= 0.01f;
						else if (velocity.velocity.y < 0)
							pos += 0.01f;
						position.position.y += pos;
						velocity.velocity.y = 0;
					}
					if (tmp.z != 0) {
						float pos = 0;
						if (velocity.velocity.z > 0)
							pos -= 0.01f;
						else if (velocity.velocity.z < 0)
							pos += 0.01f;
						position.position.z += pos;
						velocity.velocity.z = 0;
					}
				}
			}

			position.position.x += velocity.velocity.x * deltaTime;
			position.position.y += velocity.velocity.y * deltaTime;
			position.position.z += velocity.velocity.z * deltaTime;

		}
	}

	public Vector3 minimumTranslation(BoundingBox own, BoundingBox other) {
		Vector3 amin = own.min;
		Vector3 amax = own.max;
		Vector3 bmin = other.min;
		Vector3 bmax = other.max;

		Vector3 mtd = new Vector3();

		float left = (bmin.x - amax.x);
		float right = (bmax.x - amin.x);
		float top = (bmin.y - amax.y);
		float bottom = (bmax.y - amin.y);
		float front = (bmin.z - amax.z);
		float back = (bmax.z - amin.z);

		// box intersect. work out the mtd on both x and y axes.
		if (Math.abs(left) < right)
			mtd.x = left;
		else
			mtd.x = right;

		if (Math.abs(top) < bottom)
			mtd.y = top;
		else
			mtd.y = bottom;

		if (Math.abs(front) < back)
			mtd.z = front;
		else
			mtd.z = back;

		// 0 the axis with the largest mtd value.
		if (Math.abs(mtd.x) < Math.abs(mtd.y))
			mtd.y = 0;
		else
			mtd.x = 0;
		return mtd;
	}
	*/
}