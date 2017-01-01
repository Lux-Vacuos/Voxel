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

package net.luxvacuos.voxel.client.rendering.api.opengl.shaders;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.Attribute;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformFloat;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformMatrix;

public class ParticleShader extends ShaderProgram {

	private UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	private UniformFloat numberOfRows = new UniformFloat("numberOfRows");

	public ParticleShader() {
		super(ClientVariables.VERTEX_FILE_PARTICLE, ClientVariables.FRAGMENT_FILE_PARTICLE,
				new Attribute(0, "position"), new Attribute(1, "modelViewMatrix"), new Attribute(5, "texOffsets"),
				new Attribute(6, "blendFactor"));
		super.storeAllUniformLocations(projectionMatrix);
	}

	public void loadNumberOfRows(float rows) {
		numberOfRows.loadFloat(rows);
	}

	public void loadProjectionMatrix(Matrix4d projectionMatrix) {
		this.projectionMatrix.loadMatrix(projectionMatrix);
	}

}
