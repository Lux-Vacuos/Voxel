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
import net.luxvacuos.voxel.client.world.block.BlockBase;
import net.luxvacuos.voxel.client.world.entities.AbstractEntity;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;
import net.luxvacuos.voxel.client.world.entities.components.ArmourComponent;
import net.luxvacuos.voxel.client.world.entities.components.CollisionComponent;
import net.luxvacuos.voxel.client.world.entities.components.DropComponent;
import net.luxvacuos.voxel.client.world.entities.components.LifeComponent;
import net.luxvacuos.voxel.client.world.entities.components.VelocityComponent;
import net.luxvacuos.voxel.client.world.items.ItemDrop;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.Position;

public class PhysicsSystem extends EntitySystem {
	private ImmutableArray<Entity> entities;

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
	private double depthTMP;
	private int faceTMP;

	public PhysicsSystem(Dimension dim) {
		this.dim = dim;
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(
				Family.all(Position.class, VelocityComponent.class, CollisionComponent.class).get());
	}

	public void processItems(GameResources gm) {
		for (Entity entity : entities) {
			Position position = Components.POSITION.get(entity);
			if (entity instanceof ItemDrop) {
				tmp.set(0, 0, 0);
				VelocityComponent velocity = vm.get(entity);
				PlayerCamera cam = (PlayerCamera) gm.getCamera();
				if (Vector3f
						.sub(position.getPosition(), gm.getCamera().getPosition(), tmp)
						.lengthSquared() < 2) {
					A: for (int x = 0; x < cam.getInventory().getSizeX(); x++) {
						for (int y = 0; y < cam.getInventory().getSizeY(); y++) {
							if (cam.getInventory().getItems()[x][y].getBlock().getId() == ((ItemDrop) entity).getBlock()
									.getId()) {
								cam.getInventory().getItems()[x][y].setBlock(((ItemDrop) entity).getBlock());
								cam.getInventory().getItems()[x][y]
										.setTotal(cam.getInventory().getItems()[x][y].getTotal() + 1);
								break A;
							} else if (cam.getInventory().getItems()[x][y].getBlock().getId() == 0) {
								cam.getInventory().getItems()[x][y].setBlock(((ItemDrop) entity).getBlock());
								cam.getInventory().getItems()[x][y].setTotal(1);
								break A;
							}
						}
					}
					getEngine().removeEntity(entity);
				}
				if (Vector3f
						.sub(position.getPosition(), gm.getCamera().getPosition(), tmp)
						.lengthSquared() < 6) {
					Vector3f.add(Vector3f.sub(gm.getCamera().getPosition(), position.getPosition(), null), velocity.velocity,
							velocity.velocity);
				}
			}
		}
	}

	public void processEntities(GameResources gm) {
		for (Entity entity : entities) {
			if(entity instanceof PlayerCamera) continue;
			if (entity instanceof AbstractEntity) {

				LifeComponent life = lm.get(entity);
				DropComponent drop = dm.get(entity);
				CollisionComponent collison = cm.get(entity);
				Position pos = Components.POSITION.get(entity);
				VelocityComponent velocity = vm.get(entity);

				if (life != null) {
					if (Maths.intersectRayBounds(((PlayerCamera) gm.getCamera()).getDRay().getRay(),
							collison.boundingBox, tmp1) && ((PlayerCamera) gm.getCamera()).isHit()) {
						life.life -= 1;

						Vector3f.add(Vector3f.sub(pos.getPosition(), gm.getCamera().getPosition(), null),
								velocity.velocity, velocity.velocity);

						if (life.life <= 0) {
							if (drop != null) {
								drop.drop(getEngine(), pos.getPosition());
							}
							getEngine().removeEntity(entity);
						}
						break;
					}
				}
			}
		}
	}

	@Override
	public void update(float deltaTime) {
		for (Entity entity : entities) {
			Position pos = Components.POSITION.get(entity);
			VelocityComponent velocity = vm.get(entity);
			CollisionComponent collison = cm.get(entity);
			LifeComponent life = lm.get(entity);
			ArmourComponent armour = am.get(entity);

			velocity.velocity.x *= 0.7f - velocity.velocity.x * 0.0001f;
			velocity.velocity.z *= 0.7f - velocity.velocity.z * 0.0001f;
			velocity.velocity.y += -9.8f * deltaTime;

			collison.update(pos.getPosition());
			boxes = dim.getGlobalBoundingBox(collison.boundingBox);

			double tempx = (velocity.velocity.x);
			int tempX = (int) tempx;
			if (velocity.velocity.x < 0) {
				tempx = (velocity.velocity.x);
				tempX = (int) tempx - 1;
			}

			double tempz = (velocity.velocity.z);
			int tempZ = (int) tempz;
			if (velocity.velocity.z > 0) {
				tempz = (velocity.velocity.z);
				tempZ = (int) tempz + 1;
			}

			double tempy = (velocity.velocity.y);
			int tempY = (int) tempy - 1;

			int bx = (int) tempX;
			int by = (int) tempY;
			int bz = (int) tempZ - 1;
			if (collison.enabled)
				for (BoundingBox boundingBox : boxes) {
					normalTMP.set(0, 0, 0);
					if (AABBIntersect(collison.boundingBox.min, collison.boundingBox.max, boundingBox.min,
							boundingBox.max)) {
						depthTMP /= 4f;
						if (normalTMP.x > 0 && velocity.velocity.x > 0) {
							velocity.velocity.x = 0;
							pos.setX(pos.getX() - depthTMP);
						}
						if (normalTMP.x < 0 && velocity.velocity.x < 0) {
							velocity.velocity.x = 0;
							pos.setX(pos.getX() + depthTMP);
						}

						if (normalTMP.y > 0 && velocity.velocity.y > 0)
							velocity.velocity.y = 0;
						if (normalTMP.y < 0 && velocity.velocity.y < 0) {
							if (life != null && velocity.velocity.y < -10f) {
								life.life += velocity.velocity.y * 0.4f + 1 * armour.armour.getProtection();
							}
							velocity.velocity.y = 0;
							pos.setY(pos.getY() + depthTMP);
						}

						if (normalTMP.z > 0 && velocity.velocity.z > 0) {
							velocity.velocity.z = 0;
							pos.setZ(pos.getZ() - depthTMP);
						}
						if (normalTMP.z < 0 && velocity.velocity.z < 0) {
							velocity.velocity.z = 0;
							pos.setZ(pos.getZ() + depthTMP);
						}
					}
				}
			if (life != null) {
				BlockBase b = dim.getGlobalBlock(bx, by, bz);
				if (b == Block.Lava)
					life.life -= 0.2f;
				if (!b.isTransparent() && b.getId() != Block.NULL.getId())
					life.life -= 0.5f;
			}

			pos.setX(pos.getX() + velocity.velocity.x * deltaTime);
			pos.setX(pos.getY() + velocity.velocity.y * deltaTime);
			pos.setX(pos.getZ() + velocity.velocity.z * deltaTime);

			if (entity instanceof AbstractEntity)
				((AbstractEntity) entity).update(deltaTime);

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

}