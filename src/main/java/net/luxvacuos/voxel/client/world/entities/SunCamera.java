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

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.Ray;
import net.luxvacuos.voxel.client.util.Maths;

public class SunCamera extends Camera {

	private Vector2f center;

	public SunCamera(Matrix4f proj) {
		super(proj, new Vector3f(-1, -1, -1), new Vector3f(1, 1, 1));
		center = new Vector2f(2048, 2048);
	}

	public void updateShadowRay(GameResources gm, boolean inverted) {
		if (inverted)
			ray = new Ray(gm.getMasterShadowRenderer().getProjectionMatrix(),
					Maths.createViewMatrixPos(positionComponent.position,
							Maths.createViewMatrixRot(pitch + 180, yaw, roll, null)),
					center, 4096, 4096);
		else
			ray = new Ray(gm.getMasterShadowRenderer().getProjectionMatrix(), Maths.createViewMatrix(this), center,
					4096, 4096);
	}

}
