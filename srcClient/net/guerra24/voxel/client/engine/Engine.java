package net.guerra24.voxel.client.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.guerra24.voxel.client.engine.display.DisplayManager;
import net.guerra24.voxel.client.engine.entities.Entity;
import net.guerra24.voxel.client.engine.entities.types.Camera;
import net.guerra24.voxel.client.engine.entities.types.Light;
import net.guerra24.voxel.client.engine.entities.types.Player;
import net.guerra24.voxel.client.engine.render.MasterRenderer;
import net.guerra24.voxel.client.engine.render.gui.GuiRenderer;
import net.guerra24.voxel.client.engine.render.gui.textures.GuiTexture;
import net.guerra24.voxel.client.engine.render.shaders.types.WaterShader;
import net.guerra24.voxel.client.engine.render.water.WaterRenderer;
import net.guerra24.voxel.client.engine.render.water.WaterTile;
import net.guerra24.voxel.client.engine.resources.Loader;
import net.guerra24.voxel.client.engine.util.Logger;
import net.guerra24.voxel.client.engine.util.SystemInfo;
import net.guerra24.voxel.client.engine.world.World;
import net.guerra24.voxel.client.engine.world.chunks.blocks.Blocks;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Engine {

	public static List<Entity> allCubes = new ArrayList<Entity>();
	public static List<Light> lights = new ArrayList<Light>();

	public static Random rand;
	public static Player player;
	public static Light sun;
	public static Light spot;

	public static void StartGame() {

		// CREATING WINDOW

		DisplayManager.createDisplay();
		SystemInfo.chechOpenGl32();
		SystemInfo.printSystemInfo();
		Logger.log("Starting Rendering");

		Mouse.setGrabbed(true);
		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);

		// SETTING UP CONSTRUCTORS AND VARIABLES

		rand = new Random();
		Loader loader = new Loader();
		Camera camera = new Camera();
		MasterRenderer renderer = new MasterRenderer(loader);
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader,
				renderer.getProjectionMatrix());
		List<WaterTile> waters = new ArrayList<WaterTile>();
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		Blocks.createBlocks();
		/*
		 * texture.getTexture().setHasTransparency(true); Enables transparency.
		 */

		// SETTING UP VARIABLES

		GuiTexture gui = new GuiTexture(loader.loadTexture("HotBar"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));

		player = new Player(Blocks.cubeGlass, new Vector3f(-10, 68, -10), 0, 0,
				90, 1);
		sun = new Light(new Vector3f(0, 10000000000f, 0), new Vector3f(1f, 1f,
				1f));
		// spot = new Light(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0),
		// new Vector3f(1, 0.01f, 0.002f));

		// WORLD GENERATION

		Logger.log("Generating World with size: " + World.WORLD_SIZE);
		World.init();
		Logger.log("World Generation completed with size: " + World.WORLD_SIZE);

		// PUT RESOURCES IN LISTS

		// lights.add(spot);
		lights.add(sun);
		allCubes.add(player);
		waters.add(new WaterTile(60, 60, 50));
		guis.add(gui);

		// GAME LOOP
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move();
			// spot.setPosition(player.getPosition());
			renderer.renderScene(allCubes, lights, camera);
			waterRenderer.render(waters, camera);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		Logger.log("Closing Game");
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		Blocks.loader.cleanUp();
		DisplayManager.closeDisplay();
	}

	public static void main(String[] args) {
		StartGame();
	}
}
