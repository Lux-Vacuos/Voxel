package net.guerra24.voxel.client.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.guerra24.voxel.client.engine.entities.Entity;
import net.guerra24.voxel.client.engine.entities.types.Camera;
import net.guerra24.voxel.client.engine.entities.types.Light;
import net.guerra24.voxel.client.engine.entities.types.Player;
import net.guerra24.voxel.client.engine.render.MasterRenderer;
import net.guerra24.voxel.client.engine.render.shaders.types.WaterShader;
import net.guerra24.voxel.client.engine.render.textures.types.GuiTexture;
import net.guerra24.voxel.client.engine.render.types.GuiRenderer;
import net.guerra24.voxel.client.engine.render.types.WaterRenderer;
import net.guerra24.voxel.client.engine.resources.Loader;
import net.guerra24.voxel.client.engine.resources.models.WaterTile;
import net.guerra24.voxel.client.engine.util.Logger;
import net.guerra24.voxel.client.engine.util.SystemInfo;
import net.guerra24.voxel.client.engine.util.WaterFrameBuffers;
import net.guerra24.voxel.client.engine.world.World;
import net.guerra24.voxel.client.engine.world.chunks.blocks.Blocks;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Engine {

	public static List<Entity> allCubes = new ArrayList<Entity>();
	public static List<Light> lights = new ArrayList<Light>();
	public static List<WaterTile> waters = new ArrayList<WaterTile>();

	public static Random rand;
	public static Player player;
	public static Light sun;
	public static Light spot;
	public static Loader loader;

	private static State state = State.MAINMENU;

	public static void StartGame() {

		Logger.log("Loading");

		DisplayManager.createDisplay();
		SystemInfo.chechOpenGl32();
		SystemInfo.printSystemInfo();
		Logger.log("Starting Rendering");

		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);

		rand = new Random();
		loader = new Loader();
		Camera camera = new Camera();
		MasterRenderer renderer = new MasterRenderer(loader);
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader,
				renderer.getProjectionMatrix());
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		List<GuiTexture> guis2 = new ArrayList<GuiTexture>();
		Blocks.createBlocks();

		GuiTexture gui = new GuiTexture(loader.loadTextureGui("HotBar"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		GuiTexture menu = new GuiTexture(loader.loadTextureGui("MainMenu"),
				new Vector2f(0.6f, -0.425f), new Vector2f(1.6f, 1.425f));
		//GuiTexture gui2 = new GuiTexture(fbos.getReflectionTexture(),
		//		new Vector2f(-0.5f, 0.5f), new Vector2f(0.5f, 0.5f));

		player = new Player(Blocks.cubeGlass, new Vector3f(-10, 68, -10), 0, 0,
				90, 1);
		sun = new Light(new Vector3f(0, 10000000000f, 0), new Vector3f(1f, 1f,
				1f));
		// spot = new Light(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0),
		// new Vector3f(1, 0.01f, 0.002f));

		Logger.log("Generating World with size: " + World.WORLD_SIZE);
		World.init();
		Logger.log("World Generation completed with size: " + World.WORLD_SIZE);

		// lights.add(spot);
		lights.add(sun);
		allCubes.add(player);
		//guis.add(gui2);
		guis.add(gui);
		guis2.add(menu);

		DisplayManager.splash.dispose();

		while (!Display.isCloseRequested()) {
			switch (state) {
			case MAINMENU:
				guiRenderer.render(guis2);
				break;
			case GAME:
				camera.move();
				player.move();
//				fbos.bindReflectionFrameBuffer();
//				renderer.renderScene(allCubes, lights, camera);
//				fbos.unbindCurrentFrameBuffer();
				// spot.setPosition(player.getPosition());
				renderer.renderScene(allCubes, lights, camera);
				waterRenderer.render(waters, camera);
				guiRenderer.renderNoPrepare(guis);
				break;
			}
			camera.setMouse();
			switchStates();
			DisplayManager.updateDisplay();
		}
		Logger.log("Closing Game");
		waterShader.cleanUp();
		fbos.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

	private enum State {
		GAME, MAINMENU;
	}

	private static void switchStates() {
		while (Keyboard.next()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				if (state == State.MAINMENU) {
					state = State.GAME;
				} else if (state == State.GAME) {
					state = State.MAINMENU;
				}

			}
		}
	}

	public static void main(String[] args) {
		StartGame();
	}
}
