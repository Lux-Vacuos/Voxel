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

import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.util.vector.Matrix4f;

/**
 * Water Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class WaterShader extends ShaderProgram {
	/**
	 * Water Shader Data
	 */
	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_moveFactor;
	private int loc_projectionLightMatrix;
	private int loc_viewLightMatrix;
	private int loc_biasMatrix;
	private int loc_useShadows;

	/**
	 * Constructor
	 * 
	 */
	public WaterShader() {
		super(VoxelVariables.VERTEX_FILE_WATER, VoxelVariables.FRAGMENT_FILE_WATER);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		location_moveFactor = getUniformLocation("moveFactor");
		loc_projectionLightMatrix = super.getUniformLocation("projectionLightMatrix");
		loc_viewLightMatrix = super.getUniformLocation("viewLightMatrix");
		loc_useShadows = super.getUniformLocation("useShadows");
	}

	/**
	 * Loads the Move Factor
	 * 
	 * @param factor
	 *            Factor
	 */
	public void loadMoveFactor(float factor) {
		super.loadFloat(location_moveFactor, factor);
	}

	public void loadBiasMatrix(GameResources gm) {
		Matrix4f biasMatrix = new Matrix4f();
		biasMatrix.m00 = 0.5f;
		biasMatrix.m01 = 0;
		biasMatrix.m02 = 0;
		biasMatrix.m03 = 0;
		biasMatrix.m10 = 0;
		biasMatrix.m11 = 0.5f;
		biasMatrix.m12 = 0;
		biasMatrix.m13 = 0;
		biasMatrix.m20 = 0;
		biasMatrix.m21 = 0;
		biasMatrix.m22 = 0.5f;
		biasMatrix.m23 = 0;
		biasMatrix.m30 = 0.5f;
		biasMatrix.m31 = 0.5f;
		biasMatrix.m32 = 0.5f;
		biasMatrix.m33 = 1f;
		super.loadMatrix(loc_biasMatrix, biasMatrix);
		super.loadMatrix(loc_projectionLightMatrix, gm.getMasterShadowRenderer().getProjectionMatrix());
	}

	public void loadLightMatrix(GameResources gm) {
		super.loadBoolean(loc_useShadows, VoxelVariables.useShadows);
		super.loadMatrix(loc_viewLightMatrix, Maths.createViewMatrix(gm.getSun_Camera()));
	}

	/**
	 * Loads the Projection Matrix
	 * 
	 * @param projection
	 *            Projection Matrix
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}

	/**
	 * Load View Matrix
	 * 
	 * @param camera
	 *            Camera
	 */
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

	/**
	 * Load Model Matrix
	 * 
	 * @param modelMatrix
	 *            Model Matrix
	 */
	public void loadModelMatrix(Matrix4f modelMatrix) {
		super.loadMatrix(location_modelMatrix, modelMatrix);
	}

}