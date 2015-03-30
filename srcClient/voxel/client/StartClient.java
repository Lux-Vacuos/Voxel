package voxel.client;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import voxel.client.engine.DisplayManager;
import voxel.client.engine.entities.Entity;
import voxel.client.engine.render.Renderer;
import voxel.client.engine.render.shaders.StaticShader;
import voxel.client.engine.render.textures.ModelTexture;
import voxel.client.engine.resources.Loader;
import voxel.client.engine.resources.models.RawModel;
import voxel.client.engine.resources.models.TexturedModel;
import voxel.client.engine.util.Logger;
import voxel.client.engine.util.SystemInfo;

public class StartClient {

	public static void StartGame() {

	}

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		SystemInfo.chechOpenGl32();
		SystemInfo.printSystemInfo();
		Logger.log("Starting Rendering");

		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);

		float[] vertices = { -0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f, 0.5f, -0.5f,
				0f, 0.5f, 0.5f, 0f };
		int[] indices = { 0, 1, 3, 3, 1, 2 };
		float[] textureCoords = { 0, 0, 0, 1, 1, 1, 1, 0 };

		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("Grass"));
		TexturedModel textureModel = new TexturedModel(model, texture);

		Entity entity = new Entity(textureModel, new Vector3f(0, 0, -1), 0, 0,
				0, 1);

		while (!Display.isCloseRequested()) {
			entity.increasePosition(0, 0, -0.01f);
			entity.increaseRotation(0, 1f, 0);
			renderer.prepare();
			shader.start();
			renderer.render(entity, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		Logger.log("Closing Game");
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
