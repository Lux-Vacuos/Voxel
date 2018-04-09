/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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

package net.luxvacuos.voxel.client.rendering.shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import net.luxvacuos.lightengine.client.rendering.shaders.ShaderProgram;
import net.luxvacuos.lightengine.client.rendering.shaders.data.Attribute;
import net.luxvacuos.lightengine.client.rendering.shaders.data.UniformMatrix;
import net.luxvacuos.lightengine.client.rendering.shaders.data.UniformVec3;
import net.luxvacuos.voxel.client.core.ClientVariables;

public class BlockOutlineShader extends ShaderProgram {

	private UniformVec3 color = new UniformVec3("color");
	private UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	private UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	private UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");

	public BlockOutlineShader() {
		super(ClientVariables.VERTEX_FILE_BLOCK_OUTLINE, ClientVariables.FRAGMENT_FILE_BLOCK_OUTLINE,
				new Attribute(0, "position"));
		super.storeAllUniformLocations(color, viewMatrix, projectionMatrix, transformationMatrix);
	}

	public void loadColor(Vector3f color) {
		this.color.loadVec3(color);
	}

	public void loadViewMatrix(Matrix4f cameraViewMatrix) {
		viewMatrix.loadMatrix(cameraViewMatrix);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		projectionMatrix.loadMatrix(projection);
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		transformationMatrix.loadMatrix(matrix);
	}

}
