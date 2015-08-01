/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.guerra24.voxel.client.kernel.graphics.shaders;

import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;
import io.github.guerra24.voxel.client.kernel.world.entities.Light;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class EntityShader extends ShaderProgram {

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_attenuations[];
	private int location_viewMatrix;
	private int location_skyColour;
	private int location_directLightDirection;

	public EntityShader() {
		super(KernelConstants.VERTEX_FILE_ENTITY,
				KernelConstants.FRAGMENT_FILE_ENTITY);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super
				.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super
				.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_skyColour = super.getUniformLocation("skyColour");
		location_directLightDirection = super
				.getUniformLocation("directLightDirection");

		location_lightPosition = new int[KernelConstants.MAX_LIGHTS];
		location_lightColour = new int[KernelConstants.MAX_LIGHTS];
		location_attenuations = new int[KernelConstants.MAX_LIGHTS];
		for (int i = 0; i < KernelConstants.MAX_LIGHTS; i++) {
			location_lightPosition[i] = super
					.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour["
					+ i + "]");
			location_attenuations[i] = super.getUniformLocation("attenuations["
					+ i + "]");
		}
	}

	public void loadSkyColour(float r, float g, float b) {
		super.loadVector(location_skyColour, new Vector3f(r, g, b));
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	public void loadDirectLightDirection(Vector3f direction) {
		super.loadVector(location_directLightDirection, direction);
	}

	public void loadLights(List<Light> lights) {
		for (int i = 0; i < KernelConstants.MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector(location_lightPosition[i], lights.get(i)
						.getPosition());
				super.loadVector(location_lightColour[i], lights.get(i)
						.getColour());
				super.loadVector(location_attenuations[i], lights.get(i)
						.getAttenuation());
			}
		}
	}

	public void loadviewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
}