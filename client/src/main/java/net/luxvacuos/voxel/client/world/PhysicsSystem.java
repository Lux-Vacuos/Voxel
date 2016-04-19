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

package net.luxvacuos.voxel.client.world;

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
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.block.Block;
import net.luxvacuos.voxel.client.world.entities.GameEntity;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.client.world.entities.components.ArmourComponent;
import net.luxvacuos.voxel.client.world.entities.components.CollisionComponent;
import net.luxvacuos.voxel.client.world.entities.components.DropComponent;
import net.luxvacuos.voxel.client.world.entities.components.LifeComponent;
import net.luxvacuos.voxel.client.world.entities.components.PositionComponent;
import net.luxvacuos.voxel.client.world.entities.components.VelocityComponent;
import net.luxvacuos.voxel.client.world.items.ItemDrop;

public class PhysicsSystem extends EntitySystem {
	private ImmutableArray<Entity> entities;

	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);
	private ComponentMapper<LifeComponent> lm = ComponentMapper.getFor(LifeComponent.class);
	private ComponentMapper<DropComponent> dm = ComponentMapper.getFor(DropComponent.class);
	private ComponentMapper<ArmourComponent> am = ComponentMapper.getFor(ArmourComponent.class);

	private Dimension dim;
	private List<BoundingBox> boxes;
	private Vector3f tmp = new Vector3f();
	private Vector3 normalTMP = new Vector3();
	private Vector3 tmp1 = new Vector3();
	private float depthTMP;
	private int faceTMP;

	public PhysicsSystem(Dimension dim) {
		this.dim = dim;
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(
				Family.all(PositionComponent.class, VelocityComponent.class, CollisionComponent.class).get());
	}

	public void processItems(GameResources gm) {
		for (Entity entity : entities) {
			if (entity instanceof ItemDrop) {
				tmp.set(0, 0, 0);
				if (Vector3f
						.sub(entity.getComponent(PositionComponent.class).position, gm.getCamera().getPosition(), tmp)
						.lengthSquared() < 2) {
					A: for (int x = 0; x < ((PlayerCamera) gm.getCamera()).getInventory().getSizeX(); x++) {
						for (int y = 0; y < ((PlayerCamera) gm.getCamera()).getInventory().getSizeY(); y++) {
							if (((PlayerCamera) gm.getCamera()).getInventory().getItems()[x][y].getBlock()
									.getId() == ((ItemDrop) entity).getBlock().getId()) {
								((PlayerCamera) gm.getCamera()).getInventory().getItems()[x][y]
										.setBlock(((ItemDrop) entity).getBlock());
								((PlayerCamera) gm.getCamera()).getInventory().getItems()[x][y].setTotal(
										((PlayerCamera) gm.getCamera()).getInventory().getItems()[x][y].getTotal() + 1);
								break A;
							} else if (((PlayerCamera) gm.getCamera()).getInventory().getItems()[x][y].getBlock()
									.getId() == 0) {
								((PlayerCamera) gm.getCamera()).getInventory().getItems()[x][y]
										.setBlock(((ItemDrop) entity).getBlock());
								((PlayerCamera) gm.getCamera()).getInventory().getItems()[x][y].setTotal(1);
								break A;
							}
						}
					}
					getEngine().removeEntity(entity);
				}
			}
		}
	}

	public void processEntities(GameResources gm) {
		for (Entity entity : entities) {
			if (entity instanceof GameEntity) {

				LifeComponent life = lm.get(entity);
				DropComponent drop = dm.get(entity);
				CollisionComponent collison = cm.get(entity);
				PositionComponent position = pm.get(entity);

				if (life != null) {
					if (Maths.intersectRayBounds(((PlayerCamera) gm.getCamera()).getDRay().getRay(),
							collison.boundingBox, tmp1) && ((PlayerCamera) gm.getCamera()).isHit())
						life.life -= 1;
					if (life.life <= 0) {
						if (drop != null) {
							drop.drop(getEngine(), position.position);
						}
						getEngine().removeEntity(entity);
					}
					break;
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
			LifeComponent life = lm.get(entity);
			ArmourComponent armour = am.get(entity);

			velocity.velocity.x *= 0.4f - velocity.velocity.x * 0.002f;
			velocity.velocity.z *= 0.4f - velocity.velocity.z * 0.002f;
			velocity.velocity.y += -9.8f * deltaTime;

			collison.update(position.position);
			boxes = dim.getGlobalBoundingBox(collison.boundingBox);

			float tempx = (velocity.velocity.x);
			int tempX = (int) tempx;
			if (velocity.velocity.x < 0) {
				tempx = (velocity.velocity.x);
				tempX = (int) tempx - 1;
			}

			float tempz = (velocity.velocity.z);
			int tempZ = (int) tempz;
			if (velocity.velocity.z > 0) {
				tempz = (velocity.velocity.z);
				tempZ = (int) tempz + 1;
			}

			float tempy = (velocity.velocity.y);
			int tempY = (int) tempy - 1;

			int bx = (int) tempX;
			int by = (int) tempY;
			int bz = (int) tempZ - 1;

			for (BoundingBox boundingBox : boxes) {
				normalTMP.set(0, 0, 0);
				if (AABBIntersect(collison.boundingBox.min, collison.boundingBox.max, boundingBox.min,
						boundingBox.max)) {
					if (normalTMP.x > 0 && velocity.velocity.x > 0)
						velocity.velocity.x = 0;
					if (normalTMP.x < 0 && velocity.velocity.x < 0)
						velocity.velocity.x = 0;

					if (normalTMP.y > 0 && velocity.velocity.y > 0)
						velocity.velocity.y = 0;
					if (normalTMP.y < 0 && velocity.velocity.y < 0) {
						if (life != null && velocity.velocity.y < -10f) {
							life.life += velocity.velocity.y * 0.2f + 1 * armour.armour.getProtection();
							System.out.println(life.life);
						}
						velocity.velocity.y = 0;
						depthTMP /= 4f;
						position.position.y += depthTMP;
					}

					if (normalTMP.z > 0 && velocity.velocity.z > 0)
						velocity.velocity.z = 0;
					if (normalTMP.z < 0 && velocity.velocity.z < 0)
						velocity.velocity.z = 0;
				}
			}
			if (life != null) {
				byte b = dim.getGlobalBlock(bx, by, bz);
				if (b == Block.Lava.getId())
					life.life -= 0.2f;
				if (!Block.getBlock(b).isTransparent())
					life.life -= 0.5f;
			}

			position.position.x += velocity.velocity.x * deltaTime;
			position.position.y += velocity.velocity.y * deltaTime;
			position.position.z += velocity.velocity.z * deltaTime;

			if (entity instanceof GameEntity)
				((GameEntity) entity).update(deltaTime);

		}
	}

	public void doSpawn(GameResources gm) {
		/*
		 * Vector3f tmp = new Vector3f(gm.getCamera().getPosition());
		 * Vector3f.add(tmp, new Vector3f(Maths.randInt(1, 10), Maths.randInt(1,
		 * 10), Maths.randInt(1, 10)), tmp); if (Maths.getRandomBoolean(80)) {
		 * getEngine().addEntity(new Ghost(new Vector3f(tmp))); }
		 */
	}

	private boolean AABBIntersect(final Vector3 mina, final Vector3 maxa, final Vector3 minb, final Vector3 maxb) {
		final Vector3 faces[] = { new Vector3(-1, 0, 0), new Vector3(1, 0, 0), new Vector3(0, -1, 0),
				new Vector3(0, 1, 0), new Vector3(0, 0, -1), new Vector3(0, 0, 1) };
		float distances[] = { (maxb.x - mina.x), (maxa.x - minb.x), (maxb.y - mina.y), (maxa.y - minb.y),
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

}