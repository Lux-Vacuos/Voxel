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

import net.luxvacuos.lightengine.client.rendering.opengl.objects.Material;
import net.luxvacuos.lightengine.client.rendering.opengl.shaders.ShaderProgram;
import net.luxvacuos.lightengine.client.rendering.opengl.shaders.data.Attribute;
import net.luxvacuos.lightengine.client.rendering.opengl.shaders.data.UniformFloat;
import net.luxvacuos.lightengine.client.rendering.opengl.shaders.data.UniformMaterial;
import net.luxvacuos.lightengine.client.rendering.opengl.shaders.data.UniformMatrix;
import net.luxvacuos.lightengine.client.rendering.opengl.shaders.data.UniformVec3;
import net.luxvacuos.voxel.client.core.ClientVariables;

public class TessellatorShader extends ShaderProgram {

	private static TessellatorShader shader;

	public static TessellatorShader getShader() {
		if (shader == null)
			shader = new TessellatorShader();
		return shader;
	}

	private UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	private UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	private UniformVec3 cameraPos = new UniformVec3("cameraPos");
	private UniformFloat moveFactor = new UniformFloat("moveFactor");
	private UniformMaterial material = new UniformMaterial("material");

	private TessellatorShader() {
		super(ClientVariables.VERTEX_FILE_TESSELLATOR, ClientVariables.FRAGMENT_FILE_TESSELLATOR,
				ClientVariables.GEOMETRY_FILE_TESSELLATOR, new Attribute(0, "position"),
				new Attribute(1, "textureCoords"));
		super.storeUniforms(projectionMatrix, viewMatrix, moveFactor, cameraPos, material);
		super.validate();
	}

	/**
	 * Loads View Matrixd to the shader
	 * 
	 * @param camera Camera
	 */
	public void loadViewMatrix(Matrix4f cameraViewMatrix, Vector3f cameraPosition) {
		Matrix4f mat = new Matrix4f(cameraViewMatrix);
		mat.m30(0);
		mat.m31(0);
		mat.m32(0);
		viewMatrix.loadMatrix(mat);
		cameraPos.loadVec3(cameraPosition);
	}

	public void loadMaterial(Material mat) {
		this.material.loadMaterial(mat);
	}

	public void loadMoveFactor(float factor) {
		moveFactor.loadFloat(factor);
	}

	/**
	 * Loads Projection Matrixd to the shader
	 * 
	 * @param projection Projection Matrixd
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		projectionMatrix.loadMatrix(projection);
	}

}
