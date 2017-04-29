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

package net.luxvacuos.voxel.client.ecs.entities;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.ecs.ClientComponents;
import net.luxvacuos.voxel.client.ecs.components.ProjectionMatrix;
import net.luxvacuos.voxel.client.ecs.components.ViewMatrix;
import net.luxvacuos.voxel.client.resources.CastRay;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.entities.PlayerEntity;

/**
 * Camera
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class CameraEntity extends PlayerEntity {

	protected CastRay castRay;

	protected CameraEntity(String name) {
		super(name);
		this.add(new ViewMatrix(new Matrix4d()));
		this.add(new ProjectionMatrix(new Matrix4d()));
	}

	protected CameraEntity(String name, String uuid) {
		super(name, uuid);
		this.add(new ViewMatrix(new Matrix4d()));
		this.add(new ProjectionMatrix(new Matrix4d()));
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
		return ClientComponents.PROJECTION_MATRIX.get(this).getProjectionMatrix();
	}

	public Matrix4d getViewMatrix() {
		return ClientComponents.VIEW_MATRIX.get(this).getViewMatrix();
	}

	public void setProjectionMatrix(Matrix4d projectionMatrix) {
		ClientComponents.PROJECTION_MATRIX.get(this).setProjectionMatrix(projectionMatrix);
	}

	public void setViewMatrix(Matrix4d viewMatrix) {
		ClientComponents.VIEW_MATRIX.get(this).setViewMatrix(viewMatrix);
	}

}