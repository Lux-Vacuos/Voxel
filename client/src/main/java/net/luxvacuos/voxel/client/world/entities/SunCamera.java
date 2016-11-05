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
import net.luxvacuos.voxel.client.resources.DRay;
import net.luxvacuos.voxel.client.util.Maths;

public class SunCamera extends Camera {

	private Vector2d center;
	private Matrix4d projectionMatrix;

	public SunCamera(Matrix4d projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
		center = new Vector2d(2048, 2048);
		dRay = new DRay(projectionMatrix, Maths.createViewMatrix(this), center, 0, 0);
	}

	public void updateShadowRay(boolean inverted) {
		if (inverted)
			dRay = new DRay(projectionMatrix, Maths.createViewMatrixPos(this.getPosition(),
					Maths.createViewMatrixRot(pitch + 180, yaw, roll, null)), center, 4096, 4096);
		else
			dRay = new DRay(projectionMatrix, Maths.createViewMatrix(this), center, 4096, 4096);
	}

}
