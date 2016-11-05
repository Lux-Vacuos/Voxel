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

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.resources.DRay;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Rotation;

/**
 * Camera
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class Camera extends AbstractEntity {

	protected DRay dRay;
	protected boolean jump = false;
	protected double pitch, yaw, roll;

	public boolean isMoved = false;
	public float depth = 0;
	public Vector3d normal = new Vector3d();

	public Camera() {
		this.add(new Position());
		this.add(new Rotation());
	}

	public void render() {
	}

	@Override
	public void update(float delta) {
	}

	public void updateRay(Matrix4d projectionMatrix, int width, int height, Vector2d pos) {
		dRay = new DRay(projectionMatrix, Maths.createViewMatrix(this), pos, width, height);
	}

	public Vector3d getPosition() {
		return Components.POSITION.get(this).getPosition();
	}

	public void setPosition(Vector3d position) {
		Components.POSITION.get(this).set(position);
	}

	public double getPitch() {
		return pitch;
	}

	public double getYaw() {
		return yaw;
	}

	public double getRoll() {
		return roll;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	public void setYaw(double yaw) {
		this.yaw = yaw;
	}

	public void setRoll(double roll) {
		this.roll = roll;
	}

	public DRay getDRay() {
		return dRay;
	}

}