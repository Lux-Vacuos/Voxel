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

package net.luxvacuos.voxel.client.rendering.api.opengl;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.List;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.ecs.entities.CameraEntity;
import net.luxvacuos.voxel.client.ecs.entities.SunCamera;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.CubeMapTexture;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.ParticleTexture;
import net.luxvacuos.voxel.client.rendering.api.opengl.pipeline.MultiPass;
import net.luxvacuos.voxel.client.rendering.api.opengl.pipeline.PostProcess;
import net.luxvacuos.voxel.client.world.particles.Particle;
import net.luxvacuos.voxel.universal.core.IWorldSimulation;
import net.luxvacuos.voxel.universal.core.TaskManager;

public class Renderer {

	private static EntityRenderer entityRenderer;
	private static EntityShadowRenderer entityShadowRenderer;
	private static SkyboxRenderer skyboxRenderer;
	private static IDeferredPipeline deferredPipeline;
	private static IPostProcessPipeline postProcessPipeline;
	private static LightRenderer lightRenderer;
	private static EnvironmentRenderer environmentRenderer;
	private static ParticleRenderer particleRenderer;
	private static IrradianceCapture irradianceCapture;

	private static ShadowFBO shadowFBO;

	private static Frustum frustum;
	private static Window window;

	private static IRenderPass shadowPass, deferredPass, forwardPass, occlusionPass;

	private static float exposure = 1;

	public static void init(Window window) {
		Renderer.window = window;
		if (ClientVariables.shadowMapResolution > GLUtil.getTextureMaxSize())
			ClientVariables.shadowMapResolution = GLUtil.getTextureMaxSize();

		TaskManager.addTask(() -> frustum = new Frustum());
		TaskManager.addTask(() -> shadowFBO = new ShadowFBO(ClientVariables.shadowMapResolution,
				ClientVariables.shadowMapResolution));
		TaskManager.addTask(() -> entityRenderer = new EntityRenderer(window.getResourceLoader()));

		TaskManager.addTask(() -> entityShadowRenderer = new EntityShadowRenderer());
		TaskManager.addTask(() -> skyboxRenderer = new SkyboxRenderer(window.getResourceLoader()));
		TaskManager.addTask(() -> deferredPipeline = new MultiPass());
		TaskManager.addTask(() -> postProcessPipeline = new PostProcess(window));
		TaskManager.addTask(() -> particleRenderer = new ParticleRenderer(window.getResourceLoader()));
		lightRenderer = new LightRenderer();
		TaskManager.addTask(() -> irradianceCapture = new IrradianceCapture(window.getResourceLoader()));
		TaskManager.addTask(() -> environmentRenderer = new EnvironmentRenderer(
				new CubeMapTexture(window.getResourceLoader().createEmptyCubeMap(128, true), 128)));
	}

	public static void render(ImmutableArray<Entity> entities, Map<ParticleTexture, List<Particle>> particles,
			CameraEntity camera, CameraEntity sunCamera, IWorldSimulation worldSimulation, Vector3d lightPosition,
			Vector3d invertedLightPosition, float alpha) {

		resetState();

		environmentRenderer.renderEnvironmentMap(camera.getPosition(), skyboxRenderer, worldSimulation, lightPosition,
				window);
		irradianceCapture.render(window, environmentRenderer.getCubeMapTexture().getID());

		if (ClientVariables.useShadows) {
			SunCamera sCam = (SunCamera) sunCamera;

			sCam.switchProjectionMatrix(0);
			frustum.calculateFrustum(sunCamera);

			shadowFBO.begin();
			shadowFBO.changeTexture(0);
			clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			if (shadowPass != null)
				shadowPass.render(camera, sunCamera, frustum, shadowFBO);
			entityShadowRenderer.renderEntity(entities, sunCamera);
			shadowFBO.end();

			sCam.switchProjectionMatrix(1);
			frustum.calculateFrustum(sunCamera);

			shadowFBO.begin();
			shadowFBO.changeTexture(1);
			clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			if (shadowPass != null)
				shadowPass.render(camera, sunCamera, frustum, shadowFBO);
			entityShadowRenderer.renderEntity(entities, sunCamera);
			shadowFBO.end();

			sCam.switchProjectionMatrix(2);
			frustum.calculateFrustum(sunCamera);

			shadowFBO.begin();
			shadowFBO.changeTexture(2);
			clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			if (shadowPass != null)
				shadowPass.render(camera, sunCamera, frustum, shadowFBO);
			entityShadowRenderer.renderEntity(entities, sunCamera);
			shadowFBO.end();

			sCam.switchProjectionMatrix(3);
			frustum.calculateFrustum(sunCamera);

			shadowFBO.begin();
			shadowFBO.changeTexture(3);
			clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			if (shadowPass != null)
				shadowPass.render(camera, sunCamera, frustum, shadowFBO);
			entityShadowRenderer.renderEntity(entities, sunCamera);
			shadowFBO.end();
		}

		frustum.calculateFrustum(camera);
		clearBuffer(GL_DEPTH_BUFFER_BIT);
		if (occlusionPass != null)
			occlusionPass.render(camera, sunCamera, frustum, shadowFBO);

		deferredPipeline.begin();

		clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		skyboxRenderer.render(ClientVariables.RED, ClientVariables.GREEN, ClientVariables.BLUE, camera, worldSimulation,
				lightPosition, 1, false);
		if (deferredPass != null)
			deferredPass.render(camera, sunCamera, frustum, shadowFBO);
		entityRenderer.renderEntity(entities, camera, sunCamera, shadowFBO);

		deferredPipeline.end();

		deferredPipeline.preRender(camera, lightPosition, invertedLightPosition, worldSimulation,
				lightRenderer.getLights(), irradianceCapture.getCubeMapTexture(), exposure);

		postProcessPipeline.begin();

		clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		deferredPipeline.render(postProcessPipeline.getFBO());
		if (forwardPass != null)
			forwardPass.render(camera, sunCamera, frustum, shadowFBO);
		particleRenderer.render(particles, camera);
		postProcessPipeline.end();

		postProcessPipeline.preRender(window.getNVGID(), camera);
	}

	public static void cleanUp() {
		if (environmentRenderer != null)
			environmentRenderer.cleanUp();
		if (shadowFBO != null)
			shadowFBO.cleanUp();
		if (entityRenderer != null)
			entityRenderer.cleanUp();
		if (entityShadowRenderer != null)
			entityShadowRenderer.cleanUp();
		if (deferredPipeline != null)
			deferredPipeline.dispose();
		if (postProcessPipeline != null)
			postProcessPipeline.dispose();
		if (particleRenderer != null)
			particleRenderer.cleanUp();
		if (irradianceCapture != null)
			irradianceCapture.dispose();
	}

	public static int getResultTexture() {
		return postProcessPipeline.getResultTexture();
	}

	public static void setShadowPass(IRenderPass shadowPass) {
		Renderer.shadowPass = shadowPass;
	}

	public static void setDeferredPass(IRenderPass deferredPass) {
		Renderer.deferredPass = deferredPass;
	}

	public static void setForwardPass(IRenderPass forwardPass) {
		Renderer.forwardPass = forwardPass;
	}

	public static void setOcclusionPass(IRenderPass occlusionPass) {
		Renderer.occlusionPass = occlusionPass;
	}

	public static Frustum getFrustum() {
		return frustum;
	}

	public static LightRenderer getLightRenderer() {
		return lightRenderer;
	}

	public static void init() {
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void resetState() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void clearColors(float r, float g, float b, float a) {
		glClearColor(r, g, b, a);
	}

	public static void clearBuffer(int values) {
		glClear(values);
	}

	public static Matrix4d createProjectionMatrix(int width, int height, float fov, float nearPlane, float farPlane) {
		return createProjectionMatrix(new Matrix4d(), width, height, fov, nearPlane, farPlane);
	}

	public static Matrix4d createProjectionMatrix(Matrix4d proj, int width, int height, float fov, float nearPlane,
			float farPlane) {
		float aspectRatio = (float) width / (float) height;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustrum_length = farPlane - nearPlane;

		proj.setIdentity();
		proj.m00 = x_scale;
		proj.m11 = y_scale;
		proj.m22 = -((farPlane + nearPlane) / frustrum_length);
		proj.m23 = -1;
		proj.m32 = -((2 * nearPlane * farPlane) / frustrum_length);
		proj.m33 = 0;
		return proj;
	}

}
