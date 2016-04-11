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

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.Camera;

/**
 * Skybox Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class SkyboxShader extends ShaderProgram {
	/**
	 * Skybox Shader Data
	 */
	private int loc_projectionMatrix;
	private int loc_transformationMatrix;
	private int loc_viewMatrix;
	private int loc_time;
	private int loc_lightPosition;
	private int loc_fogColour;

	public SkyboxShader() throws Exception {
		super(VoxelVariables.VERTEX_FILE_SKYBOX, VoxelVariables.FRAGMENT_FILE_SKYBOX);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
		loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
		loc_viewMatrix = super.getUniformLocation("viewMatrix");
		loc_fogColour = super.getUniformLocation("fogColour");
		loc_time = super.getUniformLocation("time");
		loc_lightPosition = super.getUniformLocation("lightPosition");
	}

	/**
	 * Loads a Projection Matrix
	 * 
	 * @param matrix
	 *            Projection Matrix
	 */
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(loc_projectionMatrix, matrix);
	}

	/**
	 * Loads View Matrix
	 * 
	 * @param camera
	 *            Camera
	 * @param delta
	 *            Delta
	 */
	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix(camera);
		super.loadMatrix(loc_viewMatrix, matrix);
	}

	public void loadTransformationMatrix(Matrix4f mat) {
		super.loadMatrix(loc_transformationMatrix, mat);
	}

	public void loadLightPosition(Vector3f pos) {
		super.loadVector(loc_lightPosition, pos);
	}

	/**
	 * Loads Fog Color
	 * 
	 * @param r
	 *            Red Value
	 * @param g
	 *            Green Value
	 * @param b
	 *            Blue Value
	 */
	public void loadFog(float r, float g, float b) {
		super.loadVector(loc_fogColour, new Vector3f(r, g, b));
	}

	public void loadTime(float time) {
		super.loadFloat(loc_time, time);
	}

}
