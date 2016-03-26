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
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.IWorld;
import net.luxvacuos.voxel.client.world.items.ItemDropBase;

public class PhysicsSystem extends EntitySystem {
	private ImmutableArray<Entity> entities;

	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);

	private IWorld world;
	private List<BoundingBox> boxes;
	private Vector3f tmp = new Vector3f();
	private Vector3 normalTMP = new Vector3();
	private float depthTMP;
	private int faceTMP;

	public PhysicsSystem(IWorld world) {
		this.world = world;
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(
				Family.all(PositionComponent.class, VelocityComponent.class, CollisionComponent.class).get());
	}

	public void processItems(GameResources gm) {
		for (Entity entity : entities) {
			if (entity instanceof ItemDropBase) {
				if (Vector3f
						.sub(entity.getComponent(PositionComponent.class).position, gm.getCamera().getPosition(), tmp)
						.lengthSquared() < 2) {
					getEngine().removeEntity(entity);
				}
			}
		}
	}

	@Override
	public void update(float deltaTime) {
		for (Entity entity : entities) {

			PositionComponent position = pm.get(entity);
			VelocityComponent velocity = vm.get(entity);
			CollisionComponent collison = cm.get(entity);

			velocity.velocity.y += -9.8f * deltaTime;

			velocity.velocity.x *= 0.4f - velocity.velocity.x * 0.01f;
			velocity.velocity.z *= 0.4f - velocity.velocity.z * 0.01f;

			collison.update(position.position);
			boxes = world.getGlobalBoundingBox(collison.boundingBox);

			for (BoundingBox boundingBox : boxes) {
				normalTMP.set(0, 0, 0);
				if (AABBIntersect(collison.boundingBox.min, collison.boundingBox.max, boundingBox.min, boundingBox.max,
						depthTMP, faceTMP)) {
					if (normalTMP.x > 0 && velocity.velocity.x > 0)
						velocity.velocity.x = 0;
					if (normalTMP.x < 0 && velocity.velocity.x < 0)
						velocity.velocity.x = 0;

					if (normalTMP.y > 0 && velocity.velocity.y > 0)
						velocity.velocity.y = 0;
					if (normalTMP.y < 0 && velocity.velocity.y < 0)
						velocity.velocity.y = 0;

					if (normalTMP.z > 0 && velocity.velocity.z > 0)
						velocity.velocity.z = 0;
					if (normalTMP.z < 0 && velocity.velocity.z < 0)
						velocity.velocity.z = 0;
				}
			}

			position.position.x += velocity.velocity.x * deltaTime;
			position.position.y += velocity.velocity.y * deltaTime;
			position.position.z += velocity.velocity.z * deltaTime;

		}
	}

	private boolean AABBIntersect(final Vector3 mina, final Vector3 maxa, final Vector3 minb, final Vector3 maxb,
			float depthColl, int faceColl) {
		final Vector3 faces[] = { new Vector3(-1, 0, 0), new Vector3(1, 0, 0), new Vector3(0, -1, 0),
				new Vector3(0, 1, 0), new Vector3(0, 0, -1), new Vector3(0, 0, 1) };
		float distances[] = { (maxb.x - mina.x), (maxa.x - minb.x), (maxb.y - mina.y), (maxa.y - minb.y),
				(maxb.z - mina.z), (maxa.z - minb.z) };
		for (int i = 0; i < 6; i++) {
			if (distances[i] < 0.0f)
				return false;
			if ((i == 0) || (distances[i] < depthColl)) {
				faceColl = i;
				normalTMP = faces[i];
				depthColl = distances[i];
			}

		}
		return true;

	}

}