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
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.pipeline.MultiPass;
import net.luxvacuos.voxel.client.rendering.api.opengl.pipeline.SinglePass;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorBasicShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorShader;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.Dimension;
import net.luxvacuos.voxel.client.world.entities.Camera;

public class Renderer {

	private Matrix4d projectionMatrix, shadowProjectionMatrix;

	private EntityRenderer entityRenderer;
	private EntityShadowRenderer entityShadowRenderer;
	private ItemsDropRenderer itemsDropRenderer;
	private SkyboxRenderer skyboxRenderer;
	private RenderingPipeline renderingPipeline;
	private LightRenderer lightRenderer;

	private ShadowFBO shadowFBO;

	private Frustum frustum;

	private Window window;

	private FloatBuffer p;
	private FloatBuffer c;

	public Renderer(Window window) {
		this.window = window;
		p = BufferUtils.createFloatBuffer(1);
		c = BufferUtils.createFloatBuffer(3);
		projectionMatrix = createProjectionMatrix(window.getWidth(), window.getHeight(), ClientVariables.FOV,
				ClientVariables.NEAR_PLANE, ClientVariables.FAR_PLANE);
		shadowProjectionMatrix = Maths.orthographic(-ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, -ClientVariables.shadowMapDrawDistance,
				ClientVariables.shadowMapDrawDistance, false);
		if (ClientVariables.shadowMapResolution > GLUtil.getTextureMaxSize())
			ClientVariables.shadowMapResolution = GLUtil.getTextureMaxSize();
		frustum = new Frustum();
		shadowFBO = new ShadowFBO(ClientVariables.shadowMapResolution, ClientVariables.shadowMapResolution);
		entityRenderer = new EntityRenderer(projectionMatrix, shadowProjectionMatrix);
		entityShadowRenderer = new EntityShadowRenderer(shadowProjectionMatrix);
		skyboxRenderer = new SkyboxRenderer(window.getResourceLoader(), projectionMatrix);
		lightRenderer = new LightRenderer();
		itemsDropRenderer = new ItemsDropRenderer(projectionMatrix, shadowProjectionMatrix);
		if (ClientVariables.renderingPipeline.equals("SinglePass"))
			renderingPipeline = new SinglePass();
		else if (ClientVariables.renderingPipeline.equals("MultiPass"))
			renderingPipeline = new MultiPass();
	}

	public void update() {
		projectionMatrix = createProjectionMatrix(projectionMatrix, window.getWidth(), window.getHeight(),
				ClientVariables.FOV, ClientVariables.NEAR_PLANE, ClientVariables.FAR_PLANE);
	}

	public void render(Dimension dimension, Camera camera, Camera sunCamera,
			ClientWorldSimulation clientWorldSimulation, Vector3d lightPosition, Vector3d invertedLightPosition,
			float alpha) {
		frustum.calculateFrustum(shadowProjectionMatrix, sunCamera);
		if (ClientVariables.useShadows) {
			shadowFBO.begin();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
			glEnable(GL_DEPTH_TEST);
			glDisable(GL_BLEND);
			dimension.updateChunksShadow(sunCamera, shadowProjectionMatrix, frustum);
			itemsDropRenderer.getTess().drawShadow(sunCamera, shadowProjectionMatrix);
			entityShadowRenderer.renderEntity(dimension.getPhysicsEngine().getEntities(), sunCamera);
			shadowFBO.end();
		}
		frustum.calculateFrustum(projectionMatrix, camera);
		prepare();
		dimension.updateChunksOcclusion(window, camera, projectionMatrix, frustum);

		renderingPipeline.begin();
		prepare();
		skyboxRenderer.render(ClientVariables.RED, ClientVariables.GREEN, ClientVariables.BLUE, alpha, camera,
				projectionMatrix, clientWorldSimulation, lightPosition);
		dimension.updateChunksRender(camera, sunCamera, clientWorldSimulation, projectionMatrix, shadowProjectionMatrix,
				shadowFBO.getDepthTex(), shadowFBO.getShadowTex(), frustum, false);
		entityRenderer.renderEntity(dimension.getPhysicsEngine().getEntities(), camera, sunCamera, projectionMatrix,
				shadowFBO.getDepthTex());

		p.clear();
		glReadPixels(window.getWidth() / 2, window.getHeight() / 2, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, p);
		c.clear();
		glReadBuffer(GL_COLOR_ATTACHMENT2);
		glReadPixels(window.getWidth() / 2, window.getHeight() / 2, 1, 1, GL_RGB, GL_FLOAT, c);

		camera.depth = p.get(0);
		camera.normal.x = c.get(0);
		camera.normal.y = c.get(1);
		camera.normal.z = c.get(2);
		itemsDropRenderer.render(dimension.getPhysicsEngine().getEntities(), camera, sunCamera, clientWorldSimulation,
				projectionMatrix, shadowFBO.getDepthTex(), shadowFBO.getShadowTex());
		renderingPipeline.end();
		prepare();
		renderingPipeline.render(camera, projectionMatrix, lightPosition, invertedLightPosition, clientWorldSimulation,
				lightRenderer.getLights());

		dimension.updateChunksRender(camera, sunCamera, clientWorldSimulation, projectionMatrix, shadowProjectionMatrix,
				shadowFBO.getDepthTex(), shadowFBO.getShadowTex(), frustum, true);
		camera.render();

		ParticleMaster.getInstance().render(camera, projectionMatrix);
	}

	public void cleanUp() {
		shadowFBO.cleanUp();
		entityRenderer.cleanUp();
		entityShadowRenderer.cleanUp();
		TessellatorShader.getInstance().cleanUp();
		TessellatorBasicShader.getInstance().cleanUp();
		itemsDropRenderer.cleanUp();
		ParticleMaster.getInstance().cleanUp();
		renderingPipeline.dispose();
	}

	public Matrix4d getProjectionMatrix() {
		return projectionMatrix;
	}

	public Matrix4d getShadowProjectionMatrix() {
		return shadowProjectionMatrix;
	}

	public Frustum getFrustum() {
		return frustum;
	}

	public static void init() {
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void prepare() {
		prepare(ClientVariables.RED, ClientVariables.GREEN, ClientVariables.BLUE, 1);
	}

	public static void prepare(float r, float g, float b, float a) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		glClearColor(r, g, b, a);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
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
