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
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.ChunkLoader;
import net.luxvacuos.voxel.universal.ecs.components.Rotation;

public class CubeMapCamera extends CameraEntity {

	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	private static final float FOV = 90;
	private static final float ASPECT_RATIO = 1f;

	public CubeMapCamera(Vector3d position) {
		Components.POSITION.get(this).set(position);
		this.add(new ChunkLoader(2));
		createProjectionMatrix();
	}

	public void switchToFace(int faceIndex) {
		Rotation rotation = Components.ROTATION.get(this);
		switch (faceIndex) {
		case 0:
			rotation.setX(0);
			rotation.setY(90);
			break;
		case 1:
			rotation.setX(0);
			rotation.setY(-90);
			break;
		case 2:
			rotation.setX(90);
			rotation.setY(180);
			break;
		case 3:
			rotation.setX(-90);
			rotation.setY(180);
			break;
		case 4:
			rotation.setX(0);
			rotation.setY(180);
			break;
		case 5:
			rotation.setX(0);
			rotation.setY(0);
			break;
		}
		rotation.setZ(180);
		updateViewMatrix();
	}

	private void createProjectionMatrix() {
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / ASPECT_RATIO;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		Matrix4d projectionMatrix = new Matrix4d();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		ClientComponents.PROJECTION_MATRIX.get(this).setProjectionMatrix(projectionMatrix);
	}

	private void updateViewMatrix() {
		ClientComponents.VIEW_MATRIX.get(this).setViewMatrix(Maths.createViewMatrix(this));
	}

}
