/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.resources.DRay;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.Player;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Rotation;
import net.luxvacuos.voxel.universal.ecs.entities.AbstractEntity;

/**
 * Camera
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public abstract class Camera extends AbstractEntity {

	protected Matrix4d projectionMatrix, viewMatrix;
	protected DRay dRay;

	public Camera() {
		this.add(new Position());
		this.add(new Rotation());
		this.add(new Player());
	}

	public Vector3d getPosition() {
		return Components.POSITION.get(this).getPosition();
	}

	public void setPosition(Vector3d position) {
		Components.POSITION.get(this).set(position);
	}

	public Vector3d getRotation() {
		return Components.ROTATION.get(this).getRotation();
	}

	public void setRotation(Vector3d rotation) {
		Components.ROTATION.get(this).set(rotation);
	}

	public Matrix4d getProjectionMatrix() {
		return projectionMatrix;
	}

	public Matrix4d getViewMatrix() {
		return viewMatrix;
	}

	public void setProjectionMatrix(Matrix4d projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}

	public void setViewMatrix(Matrix4d viewMatrix) {
		this.viewMatrix = viewMatrix;
	}

}