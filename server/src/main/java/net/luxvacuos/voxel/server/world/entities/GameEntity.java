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
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.AABB;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Velocity;

public class GameEntity extends Entity {

	private float rotX, rotY, rotZ;
	private float scale;

	public GameEntity(Vector3f position, Vector3f aabbMin, Vector3f aabbMax, float rotX, float rotY, float rotZ,
			float scale) {
		this.add(new AABB());
		this.add(new Velocity());
		this.add(new Position(position));
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		setAABB(aabbMin, aabbMax);
		init();
	}

	public GameEntity(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.add(new AABB());
		this.add(new Velocity());
		this.add(new Position(position));
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
		Components.AABB.get(this).set(aabbMin, aabbMax).setBoundingBox(aabbMin, aabbMax);
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
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

}
