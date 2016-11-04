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

import java.util.List;

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Light;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.Attribute;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformBoolean;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformFloat;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformInteger;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformMatrix;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformSampler;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformVec2;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformVec3;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.Camera;

/**
 * Post Processing Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class DeferredShadingShader extends ShaderProgram {

	private UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	private UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	private UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	private UniformMatrix inverseProjectionMatrix = new UniformMatrix("inverseProjectionMatrix");
	private UniformMatrix inverseViewMatrix = new UniformMatrix("inverseViewMatrix");
	private UniformMatrix previousViewMatrix = new UniformMatrix("previousViewMatrix");

	private UniformVec3 cameraPosition = new UniformVec3("cameraPosition");
	private UniformVec3 previousCameraPosition = new UniformVec3("previousCameraPosition");
	private UniformVec3 lightPosition = new UniformVec3("lightPosition");
	private UniformVec3 invertedLightPosition = new UniformVec3("invertedLightPosition");
	private UniformVec3 skyColor = new UniformVec3("skyColor");
	private UniformVec3 pointLightsPos[];

	private UniformVec2 resolution = new UniformVec2("resolution");
	private UniformVec2 sunPositionInScreen = new UniformVec2("sunPositionInScreen");

	private UniformFloat exposure = new UniformFloat("exposure");
	private UniformFloat time = new UniformFloat("time");
	private UniformFloat camUnderWaterOffset = new UniformFloat("camUnderWaterOffset");

	private UniformInteger shadowDrawDistance = new UniformInteger("shadowDrawDistance");

	private UniformBoolean camUnderWater = new UniformBoolean("camUnderWater");
	private UniformBoolean useFXAA = new UniformBoolean("useFXAA");
	private UniformBoolean useDOF = new UniformBoolean("useDOF");
	private UniformBoolean useMotionBlur = new UniformBoolean("useMotionBlur");
	private UniformBoolean useReflections = new UniformBoolean("useReflections");
	private UniformBoolean useVolumetricLight = new UniformBoolean("useVolumetricLight");
	private UniformBoolean useAmbientOcclusion = new UniformBoolean("useAmbientOcclusion");

	private UniformSampler gDiffuse = new UniformSampler("gDiffuse");
	private UniformSampler gPosition = new UniformSampler("gPosition");
	private UniformSampler gNormal = new UniformSampler("gNormal");
	private UniformSampler gDepth = new UniformSampler("gDepth");
	private UniformSampler gData0 = new UniformSampler("gData0");
	private UniformSampler gData1 = new UniformSampler("gData1");
	private UniformSampler composite0 = new UniformSampler("composite0");
	private UniformSampler composite1 = new UniformSampler("composite1");

	private static float tTime = 0;

	private static Matrix4f iPM, iVM;

	public DeferredShadingShader(String type) {
		super("IP_V_" + type + ".glsl", "IP_F_" + type + ".glsl", new Attribute(0, "position"));
		pointLightsPos = new UniformVec3[256];
		for (int x = 0; x < 256; x++) {
			pointLightsPos[x] = new UniformVec3("pointLightsPos[" + x + "]");
		}
		super.storeUniformArray(pointLightsPos);
		super.storeAllUniformLocations(transformationMatrix, projectionMatrix, viewMatrix, inverseProjectionMatrix,
				inverseViewMatrix, previousViewMatrix, cameraPosition, previousCameraPosition, lightPosition,
				invertedLightPosition, skyColor, resolution, sunPositionInScreen, exposure, time, camUnderWaterOffset,
				shadowDrawDistance, camUnderWater, useFXAA, useDOF, useMotionBlur, useReflections, useVolumetricLight,
				useAmbientOcclusion, gDiffuse, gPosition, gNormal, gDepth, gData0, gData1, composite0, composite1);
		connectTextureUnits();
	}

	/**
	 * Loads Textures ID
	 * 
	 */
	private void connectTextureUnits() {
		super.start();
		gDiffuse.loadTexUnit(0);
		gPosition.loadTexUnit(1);
		gNormal.loadTexUnit(2);
		gDepth.loadTexUnit(3);
		gData0.loadTexUnit(4);
		gData1.loadTexUnit(5);
		composite0.loadTexUnit(6);
		composite1.loadTexUnit(7);
		super.stop();
	}

	public void loadUnderWater(boolean value) {
		camUnderWater.loadBoolean(value);
		camUnderWaterOffset.loadFloat(tTime += 0.1f);
		tTime %= 10;
	}

	public void loadExposure(float exposure) {
		this.exposure.loadFloat(exposure);
	}

	public void loadSkyColor(Vector3f color) {
		skyColor.loadVec3(color);
	}

	public void loadLightPosition(Vector3f pos, Vector3f invertPos) {
		lightPosition.loadVec3(pos);
		invertedLightPosition.loadVec3(invertPos);
	}

	public void loadSunPosition(Vector2f pos) {
		sunPositionInScreen.loadVec2(pos);
	}

	public void loadTime(float time) {
		this.time.loadFloat(time);
	}

	public void loadPointLightsPos(List<Light> lights) {
		// TODO: Add color support
		for (int x = 0; x < 256; x++) {
			if (x < lights.size()) {
				pointLightsPos[x].loadVec3(lights.get(x).getPosition());
			} else {
				pointLightsPos[x].loadVec3(new Vector3f(0, -100, 0));
			}
		}
	}

	/**
	 * Load Display Resolution
	 * 
	 * @param res
	 *            Resolution as Vector2f
	 */
	public void loadResolution(Vector2f res) {
		resolution.loadVec2(res);
	}

	public void loadSettings(boolean useDOF, boolean useFXAA, boolean useMotionBlur, boolean useVolumetricLight,
			boolean useReflections, boolean useAmbientOcclusion, int shadowDrawDistance) {
		this.useDOF.loadBoolean(useDOF);
		this.useFXAA.loadBoolean(useFXAA);
		this.useMotionBlur.loadBoolean(useMotionBlur);
		this.useVolumetricLight.loadBoolean(useVolumetricLight);
		this.useReflections.loadBoolean(useReflections);
		this.useAmbientOcclusion.loadBoolean(useAmbientOcclusion);
		this.shadowDrawDistance.loadInteger(shadowDrawDistance);
	}

	public void loadMotionBlurData(Matrix4f projectionMatrix, Camera camera, Matrix4f previousViewMatrix,
			Vector3f previousCameraPosition) {
		this.projectionMatrix.loadMatrix(projectionMatrix);
		this.inverseProjectionMatrix.loadMatrix(Matrix4f.invert(projectionMatrix, iPM));
		this.inverseViewMatrix.loadMatrix(Matrix4f.invert(Maths.createViewMatrix(camera), iVM));
		this.previousViewMatrix.loadMatrix(previousViewMatrix);
		this.cameraPosition.loadVec3(camera.getPosition());
		this.previousCameraPosition.loadVec3(previousCameraPosition);
	}

	/**
	 * Loads View Matrix to the shader
	 * 
	 * @param camera
	 *            Camera
	 */
	public void loadviewMatrix(Camera camera) {
		this.viewMatrix.loadMatrix(Maths.createViewMatrix(camera));
	}

	/**
	 * Loads Transformation Matrix to the shader
	 * 
	 * @param matrix
	 *            Transformation Matrix
	 */
	public void loadTransformation(Matrix4f matrix) {
		transformationMatrix.loadMatrix(matrix);
	}

}
