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
import net.guerra24.voxel.client.world.entities.CollisionComponent;
import net.guerra24.voxel.client.world.entities.PositionComponent;
import net.guerra24.voxel.client.world.entities.VelocityComponent;
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

			Vector3f v = position.position;

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

			int ya = world.getGlobalBlock(bx, by + 1, bz);
			int yb = world.getGlobalBlock(bx, by - 1, bz);
			int xa = world.getGlobalBlock(bx + 1, by, bz);
			int xb = world.getGlobalBlock(bx - 1, by, bz);
			int za = world.getGlobalBlock(bx, by, bz + 1);
			int zb = world.getGlobalBlock(bx, by, bz - 1);

			velocity.y += -9.8f * deltaTime;
			if (ya == Block.Water.getId())
				if (velocity.y < 2)
					velocity.y = -2;
				else if (velocity.y > 2)
					velocity.y = 2;

			collison.boundingBox.set(new Vector3(position.position.x, position.position.y, position.position.z),
					new Vector3(position.position.x + 1f, position.position.y + 0.1f, position.position.z + 1f));

			if (velocity.y < 0)
				if (yb != Block.Air.getId() && yb != Block.Water.getId())
					if (entity.getComponent(CollisionComponent.class).boundingBox
							.intersects(Block.getBlock((byte) yb).getBoundingBox(new Vector3f(bx, by, bz))))
						velocity.y = 0;

			if (velocity.y > 0)
				if (ya != Block.Air.getId() && ya != Block.Water.getId())
					if (entity.getComponent(CollisionComponent.class).boundingBox
							.intersects(Block.getBlock((byte) ya).getBoundingBox(new Vector3f(bx, by, bz))))
						velocity.y = 0;

			if (velocity.x < 0)
				if (xb != Block.Air.getId() && xb != Block.Water.getId())
					if (entity.getComponent(CollisionComponent.class).boundingBox
							.intersects(Block.getBlock((byte) xb).getBoundingBox(new Vector3f(bx, by, bz))))
						velocity.x = 0;

			if (velocity.x > 0)
				if (xa != Block.Air.getId() && xa != Block.Water.getId())
					if (entity.getComponent(CollisionComponent.class).boundingBox
							.intersects(Block.getBlock((byte) xa).getBoundingBox(new Vector3f(bx, by, bz))))
						velocity.x = 0;

			if (velocity.z < 0)
				if (zb != Block.Air.getId() && zb != Block.Water.getId())
					if (entity.getComponent(CollisionComponent.class).boundingBox
							.intersects(Block.getBlock((byte) zb).getBoundingBox(new Vector3f(bx, by, bz))))
						velocity.z = 0;

			if (velocity.z > 0)
				if (za != Block.Air.getId() && za != Block.Water.getId())
					if (entity.getComponent(CollisionComponent.class).boundingBox
							.intersects(Block.getBlock((byte) za).getBoundingBox(new Vector3f(bx, by, bz - 1))))
						velocity.z = 0;

			position.position.x += velocity.x * deltaTime;
			position.position.y += velocity.y * deltaTime;
			position.position.z += velocity.z * deltaTime;
		}
	}
}