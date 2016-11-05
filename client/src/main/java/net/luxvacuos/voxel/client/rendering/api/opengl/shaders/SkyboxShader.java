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
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.Attribute;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformFloat;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformMatrix;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformVec3;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.Camera;

/**
 * Skybox Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class SkyboxShader extends ShaderProgram {

	private static Matrix4d camM;

	private UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	private UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	private UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	private UniformFloat time = new UniformFloat("time");
	private UniformVec3 fogColour = new UniformVec3("fogColour");
	private UniformVec3 lightPosition = new UniformVec3("lightPosition");

	public SkyboxShader() {
		super(ClientVariables.VERTEX_FILE_SKYBOX, ClientVariables.FRAGMENT_FILE_SKYBOX, new Attribute(0, "position"),
				new Attribute(2, "normal"));
		super.storeAllUniformLocations(projectionMatrix, transformationMatrix, viewMatrix, time, fogColour,
				lightPosition);
	}

	/**
	 * Loads a Projection Matrixd
	 * 
	 * @param matrix
	 *            Projection Matrixd
	 */
	public void loadProjectionMatrix(Matrix4d matrix) {
		projectionMatrix.loadMatrix(matrix);
	}

	/**
	 * Loads View Matrixd
	 * 
	 * @param camera
	 *            Camera
	 * @param delta
	 *            Delta
	 */
	public void loadViewMatrix(Camera camera) {
		viewMatrix.loadMatrix(Maths.createViewMatrixRot(camera.getPitch(), camera.getYaw(), camera.getRoll(), camM));
	}

	public void loadTransformationMatrix(Matrix4d mat) {
		transformationMatrix.loadMatrix(mat);
	}

	public void loadLightPosition(Vector3d pos) {
		lightPosition.loadVec3(pos);
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
		fogColour.loadVec3(new Vector3d(r, g, b));
	}

	public void loadTime(float time) {
		this.time.loadFloat(time);
	}

}
