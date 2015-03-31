package voxel.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import voxel.client.engine.DisplayManager;
import voxel.client.engine.entities.Camera;
import voxel.client.engine.entities.Entity;
import voxel.client.engine.render.Renderer;
import voxel.client.engine.render.shaders.StaticShader;
import voxel.client.engine.render.textures.ModelTexture;
import voxel.client.engine.resources.Loader;
import voxel.client.engine.resources.OBJLoader;
import voxel.client.engine.resources.models.RawModel;
import voxel.client.engine.resources.models.TexturedModel;
import voxel.client.engine.util.Logger;
import voxel.client.engine.util.SystemInfo;

public class StartClient {

	public static final int CHUNK_SIZE = 16;

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

		RawModel model = OBJLoader.loadObjModel("Block", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("Grass"));
		TexturedModel cubeModel = new TexturedModel(model, texture);

		Camera camera = new Camera();
		List<Entity> allCubes = new ArrayList<Entity>();

		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++)
				for (int z = 0; z < CHUNK_SIZE; z++) {
					allCubes.add(new Entity(cubeModel, new Vector3f(x,y,z), 0f, 0f, 0f, 1f));
				}
		}

		while (!Display.isCloseRequested()) {
			camera.move();
			renderer.prepare();
			shader.start();
			shader.loadviewMatrix(camera);
			for(Entity cube : allCubes) {
				renderer.render(cube, shader);
			}
			shader.stop();
			DisplayManager.updateDisplay();
		}
		Logger.log("Closing Game");
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
