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
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.lightengine.client.rendering.api.opengl.shaders.ShaderProgram;
import net.luxvacuos.lightengine.client.rendering.api.opengl.shaders.data.Attribute;
import net.luxvacuos.lightengine.client.rendering.api.opengl.shaders.data.UniformMatrix;
import net.luxvacuos.lightengine.client.rendering.api.opengl.shaders.data.UniformVec3;
import net.luxvacuos.voxel.client.core.ClientVariables;

public class TessellatorBasicShader extends ShaderProgram {

	private static TessellatorBasicShader shader;

	public static TessellatorBasicShader getShader() {
		if (shader == null)
			shader = new TessellatorBasicShader();
		return shader;
	}

	private UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	private UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");

	private UniformVec3 cameraPos = new UniformVec3("cameraPos");

	private TessellatorBasicShader() {
		super(ClientVariables.VERTEX_FILE_TESSELLATOR_BASIC, ClientVariables.FRAGMENT_FILE_TESSELLATOR_BASIC,
				new Attribute(0, "position"), new Attribute(1, "textureCoords"));
		super.storeAllUniformLocations(projectionMatrix, viewMatrix, cameraPos);
	}

	/**
	 * Loads View Matrixd to the shader
	 * 
	 * @param camera
	 *            Camera
	 */
	public void loadViewMatrix(Matrix4d cameraViewMatrix, Vector3d cameraPosition) {
		Matrix4d mat = new Matrix4d(cameraViewMatrix);
		mat.m30 = 0;
		mat.m31 = 0;
		mat.m32 = 0;
		viewMatrix.loadMatrix(mat);
		cameraPos.loadVec3(cameraPosition);
	}

	/**
	 * Loads Projection Matrixd to the shader
	 * 
	 * @param projection
	 *            Projection Matrixd
	 */
	public void loadProjectionMatrix(Matrix4d projection) {
		projectionMatrix.loadMatrix(projection);
	}

}
