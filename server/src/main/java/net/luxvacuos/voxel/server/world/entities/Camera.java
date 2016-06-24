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

package net.luxvacuos.voxel.server.world.entities;

import com.badlogic.ashley.core.Entity;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.server.world.entities.components.CollisionComponent;
import net.luxvacuos.voxel.server.world.entities.components.PositionComponent;
import net.luxvacuos.voxel.server.world.entities.components.VelocityComponent;

/**
 * Camera
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class Camera extends Entity {

	protected boolean jump = false;

	public boolean isMoved = false;

	public Camera(Vector3f aabbMin, Vector3f aabbMax) {
		this.add(new VelocityComponent());
		this.add(new PositionComponent());
		this.add(new CollisionComponent());
		this.getComponent(CollisionComponent.class).min = aabbMin.getAsVec3();
		this.getComponent(CollisionComponent.class).max = aabbMax.getAsVec3();
		this.getComponent(CollisionComponent.class).boundingBox.set(this.getComponent(CollisionComponent.class).min,
				this.getComponent(CollisionComponent.class).max);
	}

	public Vector3f getPosition() {
		return this.getComponent(PositionComponent.class).position;
	}

	public void setPosition(Vector3f position) {
		this.getComponent(PositionComponent.class).position = position;
	}

}