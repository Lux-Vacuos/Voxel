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

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.CubeMapTexture;
import net.luxvacuos.voxel.client.rendering.api.opengl.pipeline.MultiPass;
import net.luxvacuos.voxel.client.rendering.api.opengl.pipeline.SinglePass;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorBasicShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorShader;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.world.dimension.IDimension;

public class Renderer {

	private EntityRenderer entityRenderer;
	private EntityShadowRenderer entityShadowRenderer;
	private SkyboxRenderer skyboxRenderer;
	private RenderingPipeline renderingPipeline;
	private LightRenderer lightRenderer;
	private EnvironmentRenderer environmentRenderer;

	private ShadowFBO shadowFBO;

	private Frustum frustum;
	private Window window;

	public Renderer(Window window, Camera camera, Camera sunCamera) {
		this.window = window;
		if (ClientVariables.shadowMapResolution > GLUtil.getTextureMaxSize())
			ClientVariables.shadowMapResolution = GLUtil.getTextureMaxSize();
		frustum = new Frustum();
		shadowFBO = new ShadowFBO(ClientVariables.shadowMapResolution, ClientVariables.shadowMapResolution);
		entityRenderer = new EntityRenderer(camera.getProjectionMatrix(), sunCamera.getProjectionMatrix());
		entityShadowRenderer = new EntityShadowRenderer(sunCamera.getProjectionMatrix());
		skyboxRenderer = new SkyboxRenderer(window.getResourceLoader(), camera.getProjectionMatrix());
		lightRenderer = new LightRenderer();
		if (ClientVariables.renderingPipeline.equals("SinglePass"))
			renderingPipeline = new SinglePass();
		else if (ClientVariables.renderingPipeline.equals("MultiPass"))
			renderingPipeline = new MultiPass();
		environmentRenderer = new EnvironmentRenderer(
				new CubeMapTexture(window.getResourceLoader().createEmptyCubeMap(128, true), 128));
	}

	public void render(IDimension iDimension, ImmutableArray<Entity> entities, Camera camera, Camera sunCamera,
			ClientWorldSimulation clientWorldSimulation, Vector3d lightPosition, Vector3d invertedLightPosition,
			float alpha) {

		resetState();

		environmentRenderer.renderEnvironmentMap(camera.getPosition(), skyboxRenderer, clientWorldSimulation,
				lightPosition, window);

		frustum.calculateFrustum(sunCamera.getProjectionMatrix(), sunCamera.getViewMatrix());
		if (ClientVariables.useShadows) {
			shadowFBO.begin();
			clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			// dimension.renderShadow(sunCamera,
			// sunCamera.getProjectionMatrix(), frustum);
			entityShadowRenderer.renderEntity(entities, sunCamera);
			shadowFBO.end();
		}
		frustum.calculateFrustum(camera.getProjectionMatrix(), camera.getViewMatrix());
		clearBuffer(GL_DEPTH_BUFFER_BIT);
		// dimension.renderOcclusion(window, camera,
		// camera.getProjectionMatrix(), frustum);

		renderingPipeline.begin();
		clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		skyboxRenderer.render(ClientVariables.RED, ClientVariables.GREEN, ClientVariables.BLUE, camera,
				clientWorldSimulation, lightPosition, 1, false);
		// dimension.render(camera, sunCamera, clientWorldSimulation,
		// camera.getProjectionMatrix(),
		// sunCamera.getProjectionMatrix(), shadowFBO.getShadowDepth(),
		// shadowFBO.getShadowData(), frustum, false);

		entityRenderer.renderEntity(entities, camera, sunCamera, shadowFBO.getShadowDepth());
		renderingPipeline.end();
		clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		renderingPipeline.render(camera, lightPosition, invertedLightPosition, clientWorldSimulation,
				lightRenderer.getLights(), environmentRenderer.getCubeMapTexture());

		// dimension.render(camera, sunCamera, clientWorldSimulation,
		// camera.getProjectionMatrix(),
		// sunCamera.getProjectionMatrix(), shadowFBO.getShadowDepth(),
		// shadowFBO.getShadowData(), frustum, true);

		ParticleMaster.getInstance().render(camera);
	}

	public void cleanUp() {
		environmentRenderer.cleanUp();
		shadowFBO.cleanUp();
		entityRenderer.cleanUp();
		entityShadowRenderer.cleanUp();
		TessellatorShader.getInstance().cleanUp();
		TessellatorBasicShader.getInstance().cleanUp();
		ParticleMaster.getInstance().cleanUp();
		renderingPipeline.dispose();
	}

	public Frustum getFrustum() {
		return frustum;
	}

	public LightRenderer getLightRenderer() {
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
