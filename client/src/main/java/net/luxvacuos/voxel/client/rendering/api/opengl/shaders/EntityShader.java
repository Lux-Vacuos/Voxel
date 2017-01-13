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
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Material;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.Attribute;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformBoolean;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformMaterial;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformMatrix;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformSampler;
import net.luxvacuos.voxel.client.world.entities.Camera;

/**
 * Entity Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class EntityShader extends ShaderProgram {

	private UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	private UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	private UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	private UniformMatrix biasMatrix = new UniformMatrix("biasMatrix");
	private UniformMatrix projectionLightMatrix = new UniformMatrix("projectionLightMatrix");
	private UniformMatrix viewLightMatrix = new UniformMatrix("viewLightMatrix");
	private UniformSampler shadowMap = new UniformSampler("shadowMap");
	private UniformBoolean useShadows = new UniformBoolean("useShadows");
	private UniformMaterial material = new UniformMaterial("material");

	public EntityShader() {
		super(ClientVariables.VERTEX_FILE_ENTITY, ClientVariables.FRAGMENT_FILE_ENTITY, new Attribute(0, "position"),
				new Attribute(1, "textureCoords"), new Attribute(2, "normals"), new Attribute(3, "tangent"));
		super.storeAllUniformLocations(transformationMatrix, projectionMatrix, viewMatrix, biasMatrix,
				projectionLightMatrix, viewLightMatrix, shadowMap, useShadows, material);
		connectTextureUnits();
	}

	/**
	 * Loads Textures ID
	 * 
	 */
	private void connectTextureUnits() {
		super.start();
		shadowMap.loadTexUnit(8);
		super.stop();
	}

	public void useShadows(boolean value) {
		useShadows.loadBoolean(value);
	}

	/**
	 * Loads Transformation Matrixd to the shader
	 * 
	 * @param matrix
	 *            Transformation Matrixd
	 */
	public void loadTransformationMatrix(Matrix4d matrix) {
		transformationMatrix.loadMatrix(matrix);
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

	public void loadLightMatrix(Camera sunCamera) {
		viewLightMatrix.loadMatrix(sunCamera.getViewMatrix());
	}

	public void loadMaterial(Material mat) {
		this.material.loadMaterial(mat);
	}

	/**
	 * Loads View Matrixd to the shader
	 * 
	 * @param camera
	 *            Camera
	 */
	public void loadviewMatrix(Camera camera) {
		viewMatrix.loadMatrix(camera.getViewMatrix());
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