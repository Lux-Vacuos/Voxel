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
import net.luxvacuos.voxel.client.world.physics.CollisionComponent;
import net.luxvacuos.voxel.client.world.physics.PositionComponent;
import net.luxvacuos.voxel.client.world.physics.VelocityComponent;

public class GameEntity extends Entity {

	private TexturedModel model;
	private PositionComponent positionComponent;
	private VelocityComponent velocityComponent;
	private CollisionComponent collisionComponent;
	private float rotX, rotY, rotZ;
	private float scale;
	private int visibility;

	public GameEntity(TexturedModel model, Vector3f position, Vector3f aabbMin, Vector3f aabbMax, float rotX,
			float rotY, float rotZ, float scale) {
		velocityComponent = new VelocityComponent();
		positionComponent = new PositionComponent();
		positionComponent.position = new Vector3f(position);
		collisionComponent = new CollisionComponent();
		this.add(positionComponent);
		this.add(velocityComponent);
		this.add(collisionComponent);
		this.model = model;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		collisionComponent.min = aabbMin.getAsVec3();
		collisionComponent.max = aabbMax.getAsVec3();
		collisionComponent.boundingBox.set(collisionComponent.min, collisionComponent.max);
	}

	public GameEntity(TexturedModel model, Vector3f position, Vector3f aabbMin, Vector3f aabbMax, float vx, float vy,
			float vz, float rotX, float rotY, float rotZ, float scale) {
		velocityComponent = new VelocityComponent();
		positionComponent = new PositionComponent();
		collisionComponent = new CollisionComponent();
		positionComponent.position = new Vector3f(position);
		velocityComponent.velocity.x = vx;
		velocityComponent.velocity.y = vy;
		velocityComponent.velocity.z = vz;
		this.add(positionComponent);
		this.add(velocityComponent);
		this.add(collisionComponent);
		this.model = model;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		collisionComponent.min = aabbMin.getAsVec3();
		collisionComponent.max = aabbMax.getAsVec3();
		collisionComponent.boundingBox.set(collisionComponent.min, collisionComponent.max);
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.positionComponent.position.x += dx;
		this.positionComponent.position.y += dy;
		this.positionComponent.position.z += dz;
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
		return positionComponent.position;
	}

	public void setPosition(Vector3f position) {
		this.positionComponent.position = position;
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
