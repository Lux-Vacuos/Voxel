package voxel.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import voxel.client.engine.DisplayManager;
import voxel.client.engine.entities.Camera;
import voxel.client.engine.entities.Entity;
import voxel.client.engine.render.MasterRenderer;
import voxel.client.engine.render.textures.ModelTexture;
import voxel.client.engine.resources.Loader;
import voxel.client.engine.resources.OBJLoader;
import voxel.client.engine.resources.models.RawModel;
import voxel.client.engine.resources.models.TexturedModel;
import voxel.client.engine.util.Logger;
import voxel.client.engine.util.SystemInfo;
import voxel.client.engine.world.CreateChunk;

public class StartClient {

	public static TexturedModel cubeModel;
	public static List<Entity> allCubes = new ArrayList<Entity>();

	public static void StartGame() {
	}

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		SystemInfo.chechOpenGl32();
		SystemInfo.printSystemInfo();
		Logger.log("Starting Rendering");

		Loader loader = new Loader();

		RawModel model = OBJLoader.loadObjModel("Block", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("Grass"));
		cubeModel = new TexturedModel(model, texture);

		Camera camera = new Camera();
		CreateChunk.createChunks();

		MasterRenderer renderer = new MasterRenderer();
		while (!Display.isCloseRequested()) {
			camera.move();
			for (Entity cube : allCubes) {
				renderer.processEntity(cube);
			}
			renderer.render(camera);
			DisplayManager.updateDisplay();
		}
		Logger.log("Closing Game");
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
