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

package net.luxvacuos.voxel.client.rendering.api.opengl.shaders;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Material;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.Attribute;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformBoolean;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformFloat;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformMaterial;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformMatrix;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformSampler;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformVec3;

public class TessellatorShader extends ShaderProgram {

	private static TessellatorShader shader;

	public static TessellatorShader getShader() {
		if (shader == null)
			shader = new TessellatorShader();
		return shader;
	}

	private UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	private UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	private UniformMatrix biasMatrix = new UniformMatrix("biasMatrix");
	private UniformMatrix projectionLightMatrix = new UniformMatrix("projectionLightMatrix");
	private UniformMatrix viewLightMatrix = new UniformMatrix("viewLightMatrix");
	private UniformVec3 cameraPos = new UniformVec3("cameraPos");
	private UniformFloat moveFactor = new UniformFloat("moveFactor");
	private UniformSampler depth = new UniformSampler("depth");
	private UniformBoolean useShadows = new UniformBoolean("useShadows");
	private UniformMaterial material = new UniformMaterial("material");

	private TessellatorShader() {
		super(ClientVariables.VERTEX_FILE_TESSELLATOR, ClientVariables.FRAGMENT_FILE_TESSELLATOR,
				new Attribute(0, "position"), new Attribute(1, "textureCoords"), new Attribute(2, "normal"),
				new Attribute(3, "tangent"));
		super.storeAllUniformLocations(projectionMatrix, viewMatrix, biasMatrix, projectionLightMatrix, viewLightMatrix,
				moveFactor, depth, useShadows, cameraPos, material);
		conectTextureUnits();
	}

	private void conectTextureUnits() {
		super.start();
		depth.loadTexUnit(5);
		super.stop();
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

	public void loadBiasMatrix(Matrix4d shadowProjectionMatrix) {
		Matrix4d biasMatrix = new Matrix4d();
		biasMatrix.m00 = 0.5f;
		biasMatrix.m11 = 0.5f;
		biasMatrix.m22 = 0.5f;
		biasMatrix.m30 = 0.5f;
		biasMatrix.m31 = 0.5f;
		biasMatrix.m32 = 0.5f;
		this.biasMatrix.loadMatrix(biasMatrix);
		projectionLightMatrix.loadMatrix(shadowProjectionMatrix);
	}

	public void loadMaterial(Material mat) {
		this.material.loadMaterial(mat);
	}

	public void loadLightMatrix(Matrix4d sunCameraViewMatrix) {
		viewLightMatrix.loadMatrix(sunCameraViewMatrix);
	}

	public void loadMoveFactor(float factor) {
		moveFactor.loadFloat(factor);
	}

	public void loadSettings(boolean useShadows) {
		this.useShadows.loadBoolean(useShadows);
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
