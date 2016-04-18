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

package net.luxvacuos.voxel.client.world.entities;

import com.badlogic.ashley.core.Entity;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.resources.models.TexturedModel;
import net.luxvacuos.voxel.client.world.entities.components.CollisionComponent;
import net.luxvacuos.voxel.client.world.entities.components.PositionComponent;
import net.luxvacuos.voxel.client.world.entities.components.VelocityComponent;

public class GameEntity extends Entity {

	private TexturedModel model;
	private float rotX, rotY, rotZ;
	private float scale;
	@Deprecated
	private int visibility;

	public GameEntity(TexturedModel model, Vector3f position, Vector3f aabbMin, Vector3f aabbMax, float rotX,
			float rotY, float rotZ, float scale) {
		this.add(new CollisionComponent());
		this.add(new VelocityComponent());
		this.add(new PositionComponent());
		this.getComponent(PositionComponent.class).position = new Vector3f(position);
		this.model = model;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.getComponent(CollisionComponent.class).min = aabbMin.getAsVec3();
		this.getComponent(CollisionComponent.class).max = aabbMax.getAsVec3();
		this.getComponent(CollisionComponent.class).boundingBox.set(this.getComponent(CollisionComponent.class).min,
				this.getComponent(CollisionComponent.class).max);
		init();
	}

	public GameEntity(TexturedModel model, Vector3f position, Vector3f aabbMin, Vector3f aabbMax, float vx, float vy,
			float vz, float rotX, float rotY, float rotZ, float scale) {
		this.add(new CollisionComponent());
		this.add(new VelocityComponent());
		this.add(new PositionComponent());
		this.getComponent(PositionComponent.class).position = new Vector3f(position);
		this.getComponent(VelocityComponent.class).velocity.x = vx;
		this.getComponent(VelocityComponent.class).velocity.y = vy;
		this.getComponent(VelocityComponent.class).velocity.z = vz;
		this.model = model;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		setAABB(aabbMin, aabbMax);
		init();
	}

	public GameEntity(Vector3f position, Vector3f aabbMin, Vector3f aabbMax, float rotX, float rotY, float rotZ,
			float scale) {
		this.add(new CollisionComponent());
		this.add(new VelocityComponent());
		this.add(new PositionComponent());
		this.getComponent(PositionComponent.class).position = new Vector3f(position);
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		setAABB(aabbMin, aabbMax);
		init();
	}

	public GameEntity(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.add(new CollisionComponent());
		this.add(new VelocityComponent());
		this.add(new PositionComponent());
		this.getComponent(PositionComponent.class).position = new Vector3f(position);
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		init();
	}

	public void init() {
	}

	public void update(float delta) {
	}

	public void setAABB(Vector3f aabbMin, Vector3f aabbMax) {
		this.getComponent(CollisionComponent.class).min = aabbMin.getAsVec3();
		this.getComponent(CollisionComponent.class).max = aabbMax.getAsVec3();
		this.getComponent(CollisionComponent.class).boundingBox.set(this.getComponent(CollisionComponent.class).min,
				this.getComponent(CollisionComponent.class).max);
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.getComponent(PositionComponent.class).position.x += dx;
		this.getComponent(PositionComponent.class).position.y += dy;
		this.getComponent(PositionComponent.class).position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return getComponent(PositionComponent.class).position;
	}

	public void setPosition(Vector3f position) {
		this.getComponent(PositionComponent.class).position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

}
