package net.guerra24.voxel.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.guerra24.voxel.client.engine.DisplayManager;
import net.guerra24.voxel.client.engine.entities.Entity;
import net.guerra24.voxel.client.engine.entities.types.Camera;
import net.guerra24.voxel.client.engine.entities.types.Light;
import net.guerra24.voxel.client.engine.entities.types.Player;
import net.guerra24.voxel.client.engine.render.MasterRenderer;
import net.guerra24.voxel.client.engine.render.gui.GuiRenderer;
import net.guerra24.voxel.client.engine.render.gui.textures.GuiTexture;
import net.guerra24.voxel.client.engine.resources.Loader;
import net.guerra24.voxel.client.engine.util.Logger;
import net.guerra24.voxel.client.engine.util.SystemInfo;
import net.guerra24.voxel.client.engine.world.World;
import net.guerra24.voxel.client.engine.world.chunks.blocks.Blocks;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class StartClient {

	public static List<Entity> allCubes = new ArrayList<Entity>();

	public static Random rand;
	public static Light light = new Light(new Vector3f(300, 64, 300),
			new Vector3f(1, 1, 1));

	public static Player player;

	public static void StartGame() {

		DisplayManager.createDisplay();
		SystemInfo.chechOpenGl32();
		SystemInfo.printSystemInfo();
		Logger.log("Starting Rendering");

		Mouse.setGrabbed(true);
		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);

		rand = new Random();
		Loader loader = new Loader();
		Camera camera = new Camera();
		Blocks.createBlocks();
		MasterRenderer renderer = new MasterRenderer(loader);
		GuiRenderer guiRenderer = new GuiRenderer(loader);

		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("HotBar"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));

		guis.add(gui);

		/*
		 * texture.getTexture().setHasTransparency(true); Enables transparency.
		 */

		player = new Player(Blocks.cubeGrass, new Vector3f(300, 64, 300), 0, 0,
				90, 1);
		Logger.log("Generating World with size: " + World.WORLD_SIZE);
		World.init();
		Logger.log("World Generation completed with size: " + World.WORLD_SIZE);

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
