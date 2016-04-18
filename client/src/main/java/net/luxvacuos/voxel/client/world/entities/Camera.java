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
import net.luxvacuos.voxel.client.resources.DRay;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.components.CollisionComponent;
import net.luxvacuos.voxel.client.world.entities.components.PositionComponent;
import net.luxvacuos.voxel.client.world.entities.components.VelocityComponent;

/**
 * Camera
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class Camera extends Entity {

	protected float pitch;
	protected float yaw;
	protected float roll;
	protected DRay dRay;
	protected boolean jump = false;

	public boolean isMoved = false;
	public float depth = 0;

	public Camera(Matrix4f proj, Vector3f aabbMin, Vector3f aabbMax) {
		this.add(new VelocityComponent());
		this.add(new PositionComponent());
		this.add(new CollisionComponent());
		dRay = new DRay(proj, Maths.createViewMatrix(this), new Vector2f(), 0, 0);
		this.getComponent(CollisionComponent.class).min = aabbMin.getAsVec3();
		this.getComponent(CollisionComponent.class).max = aabbMax.getAsVec3();
		this.getComponent(CollisionComponent.class).boundingBox.set(this.getComponent(CollisionComponent.class).min,
				this.getComponent(CollisionComponent.class).max);
	}

	public void updateRay(Matrix4f projectionMatrix, int width, int height, Vector2f pos) {
		dRay = new DRay(projectionMatrix, Maths.createViewMatrix(this), pos, width, height);
	}

	public Vector3f getPosition() {
		return this.getComponent(PositionComponent.class).position;
	}

	public void setPosition(Vector3f position) {
		this.getComponent(PositionComponent.class).position = position;
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

	public DRay getDRay() {
		return dRay;
	}

}