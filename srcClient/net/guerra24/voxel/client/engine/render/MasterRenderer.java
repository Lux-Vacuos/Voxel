package net.guerra24.voxel.client.engine.render;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.guerra24.voxel.client.engine.entities.Entity;
import net.guerra24.voxel.client.engine.entities.types.Camera;
import net.guerra24.voxel.client.engine.entities.types.Light;
import net.guerra24.voxel.client.engine.render.entity.EntityRenderer;
import net.guerra24.voxel.client.engine.render.shaders.types.EntityShader;
import net.guerra24.voxel.client.engine.render.textures.skybox.SkyboxRenderer;
import net.guerra24.voxel.client.engine.resources.Loader;
import net.guerra24.voxel.client.engine.resources.models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

public class MasterRenderer {

	private static final float FOV = 90f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;

	private static final float RED = 0.375f;
	private static final float GREEN = 0.555f;
	private static final float BLUE = 0.655f;

	private static Matrix4f projectionMatrix;

	private EntityShader shader = new EntityShader();
	private EntityRenderer renderer;

	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();

	private SkyboxRenderer skyboxRenderer;

	public MasterRenderer(Loader loader) {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
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
            Camera camera) {
        for (Entity entity : entities) {
            processEntity(entity);
        }
        render(lights, camera);
    }

	public void render(List<Light> lights, Camera camera) {
		prepare();
		shader.start();
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadviewMatrix(camera);
		renderer.render(entities);
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

	public void prepare() {
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
