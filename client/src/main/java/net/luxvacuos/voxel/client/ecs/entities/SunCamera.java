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
import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.voxel.client.ecs.ClientComponents;
import net.luxvacuos.voxel.client.resources.DRay;
import net.luxvacuos.voxel.client.util.Maths;

public class SunCamera extends CameraEntity {

	private Vector2d center;

	private Matrix4d[] projectionArray;

	public SunCamera(Matrix4d[] projectionArray) {
		this.projectionArray = projectionArray;
		center = new Vector2d(2048, 2048);
		dRay = new DRay(this.getProjectionMatrix(), Maths.createViewMatrix(this), center, 0, 0);
		ClientComponents.VIEW_MATRIX.get(this).setViewMatrix(Maths.createViewMatrix(this));
		ClientComponents.PROJECTION_MATRIX.get(this).setProjectionMatrix(projectionArray[0]);
	}

	public void updateShadowRay(boolean inverted) {

		if (inverted)
			dRay = new DRay(this.getProjectionMatrix(), Maths.createViewMatrixPos(this.getPosition(), Maths
					.createViewMatrixRot(getRotation().getX() + 180, getRotation().getY(), getRotation().getZ(), null)),
					center, 4096, 4096);
		else
			dRay = new DRay(this.getProjectionMatrix(), Maths.createViewMatrix(this), center, 4096, 4096);
		ClientComponents.VIEW_MATRIX.get(this).setViewMatrix(Maths.createViewMatrix(this));
	}

	public void switchProjectionMatrix(int id) {
		ClientComponents.PROJECTION_MATRIX.get(this).setProjectionMatrix(this.projectionArray[id]);
	}

	public Matrix4d[] getProjectionArray() {
		return projectionArray;
	}

	public DRay getDRay() {
		return dRay;
	}

}
