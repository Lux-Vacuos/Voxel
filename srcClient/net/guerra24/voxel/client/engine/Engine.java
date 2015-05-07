package net.guerra24.voxel.client.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.guerra24.voxel.client.engine.entities.Entity;
import net.guerra24.voxel.client.engine.entities.types.Camera;
import net.guerra24.voxel.client.engine.entities.types.Light;
import net.guerra24.voxel.client.engine.entities.types.Player;
import net.guerra24.voxel.client.engine.menu.Button;
import net.guerra24.voxel.client.engine.menu.WorldSelectionScreen;
import net.guerra24.voxel.client.engine.render.MasterRenderer;
import net.guerra24.voxel.client.engine.render.shaders.types.WaterShader;
import net.guerra24.voxel.client.engine.render.textures.types.GuiTexture;
import net.guerra24.voxel.client.engine.render.types.GuiRenderer;
import net.guerra24.voxel.client.engine.render.types.WaterRenderer;
import net.guerra24.voxel.client.engine.resources.Loader;
import net.guerra24.voxel.client.engine.resources.GuiResources;
import net.guerra24.voxel.client.engine.resources.models.WaterTile;
import net.guerra24.voxel.client.engine.util.Logger;
import net.guerra24.voxel.client.engine.util.SystemInfo;
import net.guerra24.voxel.client.engine.util.WaterFrameBuffers;
import net.guerra24.voxel.client.engine.world.Blocks;
import net.guerra24.voxel.client.engine.world.World;
import net.guerra24.voxel.client.engine.world.chunks.Chunk;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Engine {

	public static List<Entity> allObjects = new ArrayList<Entity>();
	public static List<Entity> allEntities = new ArrayList<Entity>();
	public static List<Light> lights = new ArrayList<Light>();
	public static List<WaterTile> waters = new ArrayList<WaterTile>();

	public static List<GuiTexture> guis = new ArrayList<GuiTexture>();
	public static List<GuiTexture> guis2 = new ArrayList<GuiTexture>();
	public static List<GuiTexture> guis3 = new ArrayList<GuiTexture>();
	public static List<GuiTexture> guis4 = new ArrayList<GuiTexture>();
	public static List<GuiTexture> guis5 = new ArrayList<GuiTexture>();

	public static Random rand;
	public static Player player;
	public static Light sun;
	public static Light spot;
	public static Loader loader;
	public static Camera camera;
	public static GuiRenderer guiRenderer;
	public static boolean loop = true;

	public static State state = State.MAINMENU;
	public static boolean isLoading = false;
	private static int build = 3;

	public static void StartGame() {

		Logger.log("Loading");
		Logger.log("Voxel Game BUILD: " + build);
		DisplayManager.createDisplay();

		loader = new Loader();
		guiRenderer = new GuiRenderer(loader);
		GuiResources.loadingGui();
		guiRenderer.render(guis5);
		DisplayManager.updateDisplay();

		SystemInfo.chechOpenGl32();
		SystemInfo.printSystemInfo();
		Logger.log("Loading Resources");

		rand = new Random();
		camera = new Camera();

		MasterRenderer renderer = new MasterRenderer(loader);
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader,
				renderer.getProjectionMatrix());
		WaterFrameBuffers fbos = new WaterFrameBuffers();

		Blocks.createBlocks();
		GuiResources.loadGuiTexture();
		GuiResources.addGuiTextures();

		player = new Player(Blocks.cubeGlass, new Vector3f(-10, 68, -10), 0, 0,
				90, 1);
		sun = new Light(new Vector3f(0, 10000000000f, 0), new Vector3f(1f, 1f,
				1f));
		// spot = new Light(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0),
		// new Vector3f(1, 0.01f, 0.002f));
		// lights.add(spot);
		lights.add(sun);
		allObjects.add(player);
		// guis.add(gui2);
		allEntities.addAll(allObjects);

		while (loop) {
			switch (state) {
			case MAINMENU:
				guiRenderer.render(guis2);
				break;
			case WORLDSELECTION:
				WorldSelectionScreen.worldSelected();
				guiRenderer.render(guis3);
				break;
			case IN_PAUSE:
				guiRenderer.render(guis4);
				break;
			case GAME:
				camera.move();
				player.move();
				// fbos.bindReflectionFrameBuffer();
				// renderer.renderScene(allCubes, lights, camera);
				// fbos.unbindCurrentFrameBuffer();
				// spot.setPosition(player.getPosition());
				renderer.renderScene(allEntities, lights, camera);
				waterRenderer.render(waters, camera);
				//guiRenderer.renderNoPrepare(guis);
				break;
			}
			// System.out.println("X" + Mouse.getX() + "Y" + Mouse.getY());
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

	public enum State {
		GAME, MAINMENU, WORLDSELECTION, IN_PAUSE;
	}

	private static void switchStates() {
		if (state == State.MAINMENU && Button.isInButtonPlay()) {
			state = State.WORLDSELECTION;
		}

		if (state == State.MAINMENU && Button.isInButtonExit()) {
			loop = false;
		}
		if (state == State.WORLDSELECTION && Button.isInButtonBacK()) {
			state = State.MAINMENU;
		}

		if (state == State.IN_PAUSE && Button.backToMainMenu()) {
			World.saveGame();
			Chunk.cubes = new ArrayList<Entity>();
			WorldSelectionScreen.isPlaying = false;
			WorldSelectionScreen.isPrePlay = true;
			state = State.MAINMENU;
		}
		while (Keyboard.next()) {
			if (state == State.GAME && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				World.saveGame();
				camera.unlockMouse();
				state = State.IN_PAUSE;
			} else if (state == State.IN_PAUSE
					&& Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				camera.setMouse();
				state = State.GAME;
			}
		}
	}

	public static void main(String[] args) {
		StartGame();
	}
}
