package net.guerra24.voxel.client.world;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import net.guerra24.voxel.client.world.entities.PositionComponent;
import net.guerra24.voxel.client.world.entities.VelocityComponent;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public class PhysicsSystem extends EntitySystem {
	private ImmutableArray<Entity> entities;

	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	
	private IWorld world;

	public PhysicsSystem(IWorld world) {
		this.world = world;
	}

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
	}

	@Override
	public void update(float deltaTime) {
		for (Entity entity : entities) {
			
			PositionComponent position = pm.get(entity);
			VelocityComponent velocity = vm.get(entity);
			
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
			int tempY = (int) tempy - 1;

			int bx = (int) tempX;
			int by = (int) tempY;
			int bz = (int) tempZ;

			int ya = world.getGlobalBlock(bx, by - 1, bz);
			
			if (ya != 0)
				velocity.y = 0;
			else
				velocity.y += -9.8f * deltaTime;
			
			position.position.x += velocity.x * deltaTime;
			position.position.y += velocity.y * deltaTime;
			position.position.z += velocity.z * deltaTime;
		}
	}
}