package voxel.client.engine.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import voxel.client.engine.entities.Camera;
import voxel.client.engine.entities.Entity;
import voxel.client.engine.render.shaders.StaticShader;
import voxel.client.engine.resources.models.TexturedModel;

public class MasterRenderer {

	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);

	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();

	public void render(Camera camera) {
		renderer.prepare();
		shader.start();
		shader.loadviewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear();
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null) {
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

}
