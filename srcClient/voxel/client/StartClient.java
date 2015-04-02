package voxel.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import voxel.client.engine.DisplayManager;
import voxel.client.engine.entities.Camera;
import voxel.client.engine.entities.Entity;
import voxel.client.engine.entities.Player;
import voxel.client.engine.render.MasterRenderer;
import voxel.client.engine.render.gui.GuiRenderer;
import voxel.client.engine.render.gui.GuiTexture;
import voxel.client.engine.resources.Loader;
import voxel.client.engine.util.Logger;
import voxel.client.engine.util.SystemInfo;
import voxel.client.engine.world.Chunk;
import voxel.client.engine.world.blocks.Blocks;

public class StartClient {

	public static List<Entity> allCubes = new ArrayList<Entity>();

	public static Random rand;

	public static void StartGame() {

		DisplayManager.createDisplay();
		SystemInfo.chechOpenGl32();
		SystemInfo.printSystemInfo();
		Logger.log("Starting Rendering");

		Loader loader = new Loader();
		Blocks.createBlocks();
		Camera camera = new Camera();
		rand = new Random();
		Chunk.create();

		MasterRenderer renderer = new MasterRenderer();
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("HotBar"),
				new Vector2f(0.6f, -0.427f), new Vector2f(1.6f, 1.427f));
		guis.add(gui);

		GuiRenderer guiRenderer = new GuiRenderer(loader);

		Player player = new Player(Blocks.cubeGrass, new Vector3f(16, 16, 0),
				0, 0, 90, 1);
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move();
			renderer.processEntity(player);

			for (Entity cube : allCubes) {
				renderer.processEntity(cube);
			}
			renderer.render(camera);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		Logger.log("Closing Game");
		guiRenderer.cleanUp();
		renderer.cleanUp();
		Blocks.loader.cleanUp();
		DisplayManager.closeDisplay();
	}

	public static void main(String[] args) {
		StartGame();
	}
}
