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

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.resources.Ray;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.physics.CollisionComponent;
import net.luxvacuos.voxel.client.world.physics.PositionComponent;
import net.luxvacuos.voxel.client.world.physics.VelocityComponent;

/**
 * Camera
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class Camera extends Entity {

	protected float pitch;
	protected float yaw;
	protected float roll;
	protected Ray ray;
	protected boolean jump = false;

	protected VelocityComponent velocityComponent;
	protected PositionComponent positionComponent;
	protected CollisionComponent collisionComponent;

	public boolean isMoved = false;
	public float depth = 0;

	public Camera(Matrix4f proj, Vector3f aabbMin, Vector3f aabbMax) {
		velocityComponent = new VelocityComponent();
		positionComponent = new PositionComponent();
		collisionComponent = new CollisionComponent();
		this.add(velocityComponent);
		this.add(positionComponent);
		this.add(collisionComponent);
		ray = new Ray(proj, Maths.createViewMatrix(this), new Vector2f(), 0, 0);
		collisionComponent.min = aabbMin.getAsVec3();
		collisionComponent.max = aabbMax.getAsVec3();
		collisionComponent.boundingBox.set(collisionComponent.min, collisionComponent.max);
	}

	public void updateRay(Matrix4f projectionMatrix, int width, int height, Vector2f pos) {
		ray = new Ray(projectionMatrix, Maths.createViewMatrix(this), pos, width, height);
	}

	public Vector3f getPosition() {
		return positionComponent.position;
	}

	public void setPosition(Vector3f position) {
		this.positionComponent.position = position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public Ray getRay() {
		return ray;
	}

	public CollisionComponent getCollisionComponent() {
		return collisionComponent;
	}

}