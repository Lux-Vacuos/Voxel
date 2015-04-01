package voxel.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import voxel.client.engine.DisplayManager;
import voxel.client.engine.entities.Camera;
import voxel.client.engine.entities.Entity;
import voxel.client.engine.entities.Player;
import voxel.client.engine.render.MasterRenderer;
import voxel.client.engine.util.Logger;
import voxel.client.engine.util.SystemInfo;
import voxel.client.engine.world.CreateChunk;
import voxel.client.engine.world.blocks.Blocks;

public class StartClient {

	public static List<Entity> allCubes = new ArrayList<Entity>();

	public static Random rand;

	public static void StartGame() {

		DisplayManager.createDisplay();
		SystemInfo.chechOpenGl32();
		SystemInfo.printSystemInfo();
		Logger.log("Starting Rendering");

		Blocks.createBlocks();

		Camera camera = new Camera();

		rand = new Random();
		CreateChunk.createChunks();

		MasterRenderer renderer = new MasterRenderer();

		Player player = new Player(Blocks.cubeGrass, new Vector3f(0, 16, 0), 0,
				0, 90, 1);
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move();
			renderer.processEntity(player);

			for (Entity cube : allCubes) {
				renderer.processEntity(cube);
			}
			renderer.render(camera);
			DisplayManager.updateDisplay();
		}
		Logger.log("Closing Game");
		renderer.cleanUp();
		Blocks.loader.cleanUp();
		DisplayManager.closeDisplay();
	}

	public static void main(String[] args) {
		StartGame();
	}
}
