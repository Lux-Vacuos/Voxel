/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

package net.luxvacuos.voxel.universal.world.dimension;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.AABB;
import net.luxvacuos.voxel.universal.ecs.components.Health;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Velocity;
import net.luxvacuos.voxel.universal.world.entities.AbstractEntity;

public class PhysicsSystem extends EntitySystem {
	private ImmutableArray<Entity> entities;

	private List<BoundingBox> boxes = new ArrayList<>();
	private IDimension dim;
	private Vector3 normalTMP = new Vector3();
	private double depthTMP;
	private int faceTMP;

	public PhysicsSystem(IDimension dim) {
		this.dim = dim;
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(Position.class, Velocity.class, AABB.class).get());
	}

	@Override
	public void update(float delta) {
		for (Entity entity : entities) {
			Position pos = Components.POSITION.get(entity);
			Velocity velocity = Components.VELOCITY.get(entity);
			AABB aabb = Components.AABB.get(entity);
			Health health = Components.HEALTH.get(entity);

			if (entity instanceof AbstractEntity)
				((AbstractEntity) entity).update(delta);

			velocity.setX(velocity.getX() * 0.7f - velocity.getX() * 0.0001f);
			velocity.setY(velocity.getY() - 9.8f * delta);
			velocity.setZ(velocity.getZ() * 0.7f - velocity.getZ() * 0.0001f);

			aabb.update(pos.getPosition());
			if (dim != null)
				boxes = dim.getGlobalBoundingBox(aabb.getBoundingBox());

			if (aabb.isEnabled())
				for (BoundingBox boundingBox : boxes) {
					normalTMP.set(0, 0, 0);
					if (AABBIntersect(aabb.getBoundingBox().min, aabb.getBoundingBox().max, boundingBox.min,
							boundingBox.max)) {
						depthTMP /= 4f;
						if (normalTMP.x > 0 && velocity.getX() > 0) {
							velocity.setX(0);
							pos.setX(pos.getX() - depthTMP);
						}
						if (normalTMP.x < 0 && velocity.getX() < 0) {
							velocity.setX(0);
							pos.setX(pos.getX() + depthTMP);
						}

						if (normalTMP.y > 0 && velocity.getY() > 0)
							velocity.setY(0);
						if (normalTMP.y < 0 && velocity.getY() < 0) {
							if (health != null && velocity.getY() < -10f) {
								health.take((float) (velocity.getY() * 0.4f));
							}
							velocity.setY(0);
							pos.setY(pos.getY() + depthTMP);
						}

						if (normalTMP.z > 0 && velocity.getZ() > 0) {
							velocity.setZ(0);
							pos.setZ(pos.getZ() - depthTMP);
						}
						if (normalTMP.z < 0 && velocity.getZ() < 0) {
							velocity.setZ(0);
							pos.setZ(pos.getZ() + depthTMP);
						}
					}
				}
			pos.setX(pos.getX() + velocity.getX() * delta);
			pos.setY(pos.getY() + velocity.getY() * delta);
			pos.setZ(pos.getZ() + velocity.getZ() * delta);

			if (entity instanceof AbstractEntity)
				((AbstractEntity) entity).afterUpdate(delta);

		}
	}

	private boolean AABBIntersect(final Vector3 mina, final Vector3 maxa, final Vector3 minb, final Vector3 maxb) {
		final Vector3 faces[] = { new Vector3(-1, 0, 0), new Vector3(1, 0, 0), new Vector3(0, -1, 0),
				new Vector3(0, 1, 0), new Vector3(0, 0, -1), new Vector3(0, 0, 1) };
		double distances[] = { (maxb.x - mina.x), (maxa.x - minb.x), (maxb.y - mina.y), (maxa.y - minb.y),
				(maxb.z - mina.z), (maxa.z - minb.z) };
		for (int i = 0; i < 6; i++) {
			if (distances[i] < 0.0f)
				return false;
			if ((i == 0) || (distances[i] < depthTMP)) {
				faceTMP = i;
				normalTMP = faces[i];
				depthTMP = distances[i];
			}

		}
		return true;

	}

	public List<BoundingBox> getBoxes() {
		return boxes;
	}

	public void addBox(BoundingBox box) {
		boxes.add(box);
	}

}