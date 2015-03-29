package voxel.client;

import org.lwjgl.opengl.Display;

import voxel.client.engine.DisplayManager;
import voxel.client.engine.Renderer;
import voxel.client.engine.resources.Loader;
import voxel.client.engine.resources.RawModel;
import voxel.client.engine.util.Logger;

public class StartClient {

	public static void StartGame() {
		DisplayManager.createDisplay();

		Loader loader = new Loader();
		Renderer renderer = new Renderer();

		float[] vertices = { -0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f, 0.5f, -0.5f,
				0f, 0.5f, 0.5f, 0f };
		int[] indices = { 0, 1, 3, 3, 1, 2 };

		RawModel model = loader.loadToVAO(vertices, indices);

		while (!Display.isCloseRequested()) {
			renderer.prepare();
			renderer.render(model);
			DisplayManager.updateDisplay();
		}
		Logger.log("Closing Game");
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

	public static void main(String[] args) {
		DisplayManager.createDisplay();

		Loader loader = new Loader();
		Renderer renderer = new Renderer();

		float[] vertices = { -0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f, 0.5f, -0.5f,
				0f, 0.5f, 0.5f, 0f };
		int[] indices = { 0, 1, 3, 3, 1, 2 };

		RawModel model = loader.loadToVAO(vertices, indices);

		while (!Display.isCloseRequested()) {
			renderer.prepare();
			renderer.render(model);
			DisplayManager.updateDisplay();
		}
		Logger.log("Closing Game");
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
