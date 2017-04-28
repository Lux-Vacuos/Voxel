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

import java.util.List;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.ecs.entities.CameraEntity;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Light;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.Attribute;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformBoolean;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformFloat;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformInteger;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformMatrix;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformSampler;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformVec2;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformVec3;

/**
 * Post Processing Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class DeferredShadingShader extends ShaderProgram {

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

	private UniformVec3 pointLightsPosition[];
	private UniformVec3 pointLightsColor[];
	private UniformInteger totalPointLights = new UniformInteger("totalPointLights");

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
	private UniformBoolean useChromaticAberration = new UniformBoolean("useChromaticAberration");
	private UniformBoolean useLensFlares = new UniformBoolean("useLensFlares");
	private UniformBoolean useShadows = new UniformBoolean("useShadows");

	private UniformSampler gDiffuse = new UniformSampler("gDiffuse");
	private UniformSampler gPosition = new UniformSampler("gPosition");
	private UniformSampler gNormal = new UniformSampler("gNormal");
	private UniformSampler gDepth = new UniformSampler("gDepth");
	private UniformSampler gPBR = new UniformSampler("gPBR");
	private UniformSampler gMask = new UniformSampler("gMask");
	private UniformSampler composite0 = new UniformSampler("composite0");
	private UniformSampler composite1 = new UniformSampler("composite1");
	private UniformSampler composite2 = new UniformSampler("composite2");
	private UniformSampler composite3 = new UniformSampler("composite3");

	private UniformMatrix projectionLightMatrix[];
	private UniformMatrix viewLightMatrix = new UniformMatrix("viewLightMatrix");
	private UniformMatrix biasMatrix = new UniformMatrix("biasMatrix");
	private UniformSampler shadowMap[];

	private boolean loadedShadowMatrix = false;

	private static float tTime = 0;
	private static Matrix4d iPM, iVM;

	public DeferredShadingShader(String type) {
		super("deferred/V_" + type + ".glsl", "deferred/F_" + type + ".glsl", new Attribute(0, "position"));
		pointLightsPosition = new UniformVec3[256];
		for (int x = 0; x < 256; x++) {
			pointLightsPosition[x] = new UniformVec3("pointLightsPosition[" + x + "]");
		}
		pointLightsColor = new UniformVec3[256];
		for (int x = 0; x < 256; x++) {
			pointLightsColor[x] = new UniformVec3("pointLightsColor[" + x + "]");
		}
		super.storeUniformArray(pointLightsPosition);
		super.storeUniformArray(pointLightsColor);
		projectionLightMatrix = new UniformMatrix[4];
		for (int x = 0; x < 4; x++) {
			projectionLightMatrix[x] = new UniformMatrix("projectionLightMatrix[" + x + "]");
		}
		super.storeUniformArray(projectionLightMatrix);
		shadowMap = new UniformSampler[4];
		for (int x = 0; x < 4; x++) {
			shadowMap[x] = new UniformSampler("shadowMap[" + x + "]");
		}
		super.storeUniformArray(shadowMap);
		super.storeAllUniformLocations(projectionMatrix, viewMatrix, inverseProjectionMatrix, inverseViewMatrix,
				previousViewMatrix, cameraPosition, previousCameraPosition, lightPosition, invertedLightPosition,
				skyColor, resolution, sunPositionInScreen, exposure, time, camUnderWaterOffset, shadowDrawDistance,
				camUnderWater, useFXAA, useDOF, useMotionBlur, useReflections, useVolumetricLight, useAmbientOcclusion,
				gDiffuse, gPosition, gNormal, gDepth, gPBR, gMask, composite0, composite1, composite2, totalPointLights,
				useChromaticAberration, composite3, useLensFlares, biasMatrix, viewLightMatrix, useShadows);
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
		gPBR.loadTexUnit(4);
		gMask.loadTexUnit(5);
		composite0.loadTexUnit(6);
		composite1.loadTexUnit(7);
		composite2.loadTexUnit(8);
		composite3.loadTexUnit(9);
		shadowMap[0].loadTexUnit(10);
		shadowMap[1].loadTexUnit(11);
		shadowMap[2].loadTexUnit(12);
		shadowMap[3].loadTexUnit(13);
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

	public void loadSkyColor(Vector3d color) {
		skyColor.loadVec3(color);
	}

	public void loadLightPosition(Vector3d pos, Vector3d invertPos) {
		lightPosition.loadVec3(pos);
		invertedLightPosition.loadVec3(invertPos);
	}

	public void loadSunPosition(Vector2d pos) {
		sunPositionInScreen.loadVec2(pos);
	}

	public void loadTime(float time) {
		this.time.loadFloat(time);
	}

	public void loadPointLightsPos(List<Light> lights) {
		for (int x = 0; x < 256; x++) {
			if (x < lights.size()) {
				pointLightsPosition[x].loadVec3(lights.get(x).getPosition());
			}
		}
		for (int x = 0; x < 256; x++) {
			if (x < lights.size()) {
				pointLightsColor[x].loadVec3(lights.get(x).getColor());
			}
		}
		totalPointLights.loadInteger(lights.size());
	}

	public void loadBiasMatrix(Matrix4d[] shadowProjectionMatrix) {
		if (!loadedShadowMatrix) {
			Matrix4d biasMatrix = new Matrix4d();
			biasMatrix.m00 = 0.5f;
			biasMatrix.m11 = 0.5f;
			biasMatrix.m22 = 0.5f;
			biasMatrix.m30 = 0.5f;
			biasMatrix.m31 = 0.5f;
			biasMatrix.m32 = 0.5f;
			this.biasMatrix.loadMatrix(biasMatrix);
			for (int x = 0; x < 4; x++) {
				this.projectionLightMatrix[x].loadMatrix(shadowProjectionMatrix[x]);
			}
			loadedShadowMatrix = true;
		}
	}

	public void loadLightMatrix(Matrix4d sunCameraViewMatrix) {
		viewLightMatrix.loadMatrix(sunCameraViewMatrix);
	}

	/**
	 * Load Display Resolution
	 * 
	 * @param res
	 *            Resolution as Vector2d
	 */
	public void loadResolution(Vector2d res) {
		resolution.loadVec2(res);
	}

	public void loadSettings(boolean useDOF, boolean useFXAA, boolean useMotionBlur, boolean useVolumetricLight,
			boolean useReflections, boolean useAmbientOcclusion, int shadowDrawDistance, boolean useChromaticAberration,
			boolean useLensFlares, boolean useShadows) {
		this.useDOF.loadBoolean(useDOF);
		this.useFXAA.loadBoolean(useFXAA);
		this.useMotionBlur.loadBoolean(useMotionBlur);
		this.useVolumetricLight.loadBoolean(useVolumetricLight);
		this.useReflections.loadBoolean(useReflections);
		this.useAmbientOcclusion.loadBoolean(useAmbientOcclusion);
		this.shadowDrawDistance.loadInteger(shadowDrawDistance);
		this.useChromaticAberration.loadBoolean(useChromaticAberration);
		this.useLensFlares.loadBoolean(useLensFlares);
		this.useShadows.loadBoolean(useShadows);
	}

	public void loadMotionBlurData(CameraEntity camera, Matrix4d previousViewMatrix, Vector3d previousCameraPosition) {
		this.projectionMatrix.loadMatrix(camera.getProjectionMatrix());
		this.inverseProjectionMatrix.loadMatrix(Matrix4d.invert(camera.getProjectionMatrix(), iPM));
		this.inverseViewMatrix.loadMatrix(Matrix4d.invert(camera.getViewMatrix(), iVM));
		this.previousViewMatrix.loadMatrix(previousViewMatrix);
		this.cameraPosition.loadVec3(camera.getPosition());
		this.previousCameraPosition.loadVec3(previousCameraPosition);
	}

	/**
	 * Loads View Matrixd to the shader
	 * 
	 * @param camera
	 *            Camera
	 */
	public void loadviewMatrix(CameraEntity camera) {
		this.viewMatrix.loadMatrix(camera.getViewMatrix());
	}

}
