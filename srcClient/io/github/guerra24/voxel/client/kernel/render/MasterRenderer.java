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

package io.github.guerra24.voxel.client.kernel.render;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glDisable;
import io.github.guerra24.voxel.client.kernel.render.shaders.types.EntityShader;
import io.github.guerra24.voxel.client.kernel.render.types.EntityRenderer;
import io.github.guerra24.voxel.client.kernel.render.types.SkyboxRenderer;
import io.github.guerra24.voxel.client.resources.Loader;
import io.github.guerra24.voxel.client.resources.models.TexturedModel;
import io.github.guerra24.voxel.client.world.entities.Entity;
import io.github.guerra24.voxel.client.world.entities.types.Camera;
import io.github.guerra24.voxel.client.world.entities.types.Light;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

public class MasterRenderer {

	private static final float FOV = 90f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;

	public static final float RED = 0.375f;
	public static final float GREEN = 0.555f;
	public static final float BLUE = 0.655f;

	private static Matrix4f projectionMatrix;
	private EntityShader shader = new EntityShader();
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private SkyboxRenderer skyboxRenderer;

	public float BRIGHT = 0.0f;

	public static EntityRenderer entityRenderer;

	public MasterRenderer(Loader loader) {
		enableCulling();
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(shader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
	}

	public static void enableCulling() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}

	public static void disableCulling() {
		glDisable(GL_CULL_FACE);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public void renderScene(List<Entity> entities, List<Light> lights,
			Camera camera, Vector4f clipPlane) {
		for (Entity entity : entities) {
			processEntity(entity);
		}
		render(lights, camera, clipPlane);
	}

	public void renderSceneNoPrepare(List<Entity> entities, List<Light> lights,
			Camera camera, Vector4f clipPlane) {
		for (Entity entity : entities) {
			processEntity(entity);
		}
		renderNoPrepare(lights, camera, clipPlane);
	}

	public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
		prepare();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadviewMatrix(camera);
		shader.loadBrightValue(BRIGHT);
		entityRenderer.render(entities);
		shader.stop();
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		entities.clear();
	}

	public void renderNoPrepare(List<Light> lights, Camera camera,
			Vector4f clipPlane) {
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadviewMatrix(camera);
		entityRenderer.render(entities);
		shader.stop();
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		entities.clear();
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	public static void prepare() {
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(RED, GREEN, BLUE, 1);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth()
				/ (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustrum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustrum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustrum_length);
		projectionMatrix.m33 = 0;
	}

}
