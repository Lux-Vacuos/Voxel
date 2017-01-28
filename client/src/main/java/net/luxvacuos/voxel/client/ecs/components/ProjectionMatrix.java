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

package net.luxvacuos.voxel.client.ecs.components;

import com.hackhalo2.nbt.exceptions.NBTException;
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.voxel.universal.ecs.components.VoxelComponent;

public class ProjectionMatrix implements VoxelComponent {

	private Matrix4d projectionMatrix;

	public ProjectionMatrix(Matrix4d projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}
	
	public Matrix4d getProjectionMatrix() {
		return this.projectionMatrix;
	}
	
	public void setProjectionMatrix(Matrix4d projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}

	@Override
	public void load(TagCompound compound) throws NBTException {
		// TODO Auto-generated method stub

	}

	@Override
	public TagCompound save() {
		// TODO Auto-generated method stub
		return null;
	}

}
