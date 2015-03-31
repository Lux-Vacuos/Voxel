package voxel.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

	public static TexturedModel cubeGrass;
	public static TexturedModel cubeStone;
	public static TexturedModel cubeSand;
	public static List<Entity> allCubes = new ArrayList<Entity>();

	public static Random rand;

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
		ModelTexture texture1 = new ModelTexture(
				loader.loadTexture("Cobblestone"));
		ModelTexture texture2 = new ModelTexture(loader.loadTexture("Sand"));
		cubeGrass = new TexturedModel(model, texture);
		cubeStone = new TexturedModel(model, texture1);
		cubeSand = new TexturedModel(model, texture2);

		Camera camera = new Camera();

		rand = new Random();
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
