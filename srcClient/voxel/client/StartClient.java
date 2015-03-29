package voxel.client;

import org.lwjgl.opengl.Display;

import voxel.client.engine.DisplayManager;
import voxel.client.engine.render.Renderer;
import voxel.client.engine.render.shaders.StaticShader;
import voxel.client.engine.resources.Loader;
import voxel.client.engine.resources.RawModel;
import voxel.client.engine.util.Logger;
import voxel.client.engine.util.SystemInfo;

public class StartClient {

	public static void StartGame() {

	}

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		SystemInfo.chechOpenGl32();
		SystemInfo.printSystemInfo();

		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();

		float[] vertices = { -0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f, 0.5f, -0.5f,
				0f, 0.5f, 0.5f, 0f };
		int[] indices = { 0, 1, 3, 3, 1, 2 };

		RawModel model = loader.loadToVAO(vertices, indices);

		while (!Display.isCloseRequested()) {
			renderer.prepare();
			shader.start();
			renderer.render(model);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		Logger.log("Closing Game");
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
