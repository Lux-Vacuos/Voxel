package net.guerra24.voxel.client.kernel;

import net.guerra24.voxel.client.kernel.render.types.WaterReflection;
import net.guerra24.voxel.client.kernel.util.Logger;
import net.guerra24.voxel.client.kernel.util.SystemInfo;
import net.guerra24.voxel.client.menu.MenuScreen;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.GuiResources;
import net.guerra24.voxel.client.world.Water;
import net.guerra24.voxel.client.world.World;
import net.guerra24.voxel.client.world.block.BlocksResources;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector4f;

public class Kernel {

	public static boolean debug = false;
	public static boolean isLoading = false;

	private static int build = 5;
	public static GameResources gameResources;
	public static GuiResources guiResources;
	public static World world;
	public static boolean error = false;

	public static void StartGame() {

		Logger.log("Loading");
		Logger.log("Voxel Game BUILD: " + build);
		DisplayManager.createDisplay();
		SystemInfo.printSystemInfo();

		gameResources = new GameResources();

		GuiResources.loadingGui();
		gameResources.guiRenderer.render(gameResources.guis5);
		DisplayManager.updateDisplay();

		gameResources.init();
		guiResources = new GuiResources();

		BlocksResources.createBlocks();
		gameResources.addRes();
		gameResources.music();

		world = new World();

		while (gameResources.gameStates.loop) {
			switch (gameResources.gameStates.state) {
			case MAINMENU:
				gameResources.guiRenderer.render(gameResources.guis2);
				break;
			case WORLDSELECTION:
				MenuScreen.worldSelected();
				gameResources.guiRenderer.render(gameResources.guis3);
				break;
			case MULTIPLAY_SCREEN:
				MenuScreen.multiScreen();
				gameResources.guiRenderer.render(gameResources.guis3);
				break;
			case IN_PAUSE:
				gameResources.guiRenderer.render(gameResources.guis4);
				break;
			case GAME:
				world.update();
				gameResources.camera.move();
				gameResources.player.move();
				gameResources.glEn();
				setReflection();
				gameResources.glDi();
				gameResources.renderer.renderScene(gameResources.allEntities,
						gameResources.lights, gameResources.camera,
						gameResources.plane);
				gameResources.renderer.renderSceneNoPrepare(
						gameResources.allObjects, gameResources.lights,
						gameResources.camera, gameResources.plane);
				gameResources.waterRenderer.render(gameResources.waters,
						gameResources.camera);
				gameResources.guiRenderer.renderNoPrepare(gameResources.guis);
				checkGameState();
				break;
			}
			if (debug) {
				debugMode();
			}
			if (!error) {
				gameResources.gameStates.switchStates();
				DisplayManager.updateDisplay();
			}
		}
		if (!error)
			disposeGame();
	}

	private static void checkGameState() {
		if (gameResources.camera.getPosition().y <= -1) {
			try {
				error = true;
				gameResources.gameStates.loop = false;
				throw new KernelException("Invalid player position");
			} catch (KernelException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setReflection() {
		gameResources.fbos1.bindReflectionFrameBuffer();
		WaterReflection.reflectionCam();
		gameResources.renderer.renderScene(gameResources.allEntities,
				gameResources.lights, gameResources.camera, new Vector4f(0, 1,
						0, -Water.water.getHeight()));
		WaterReflection.restoreCam();
		gameResources.fbos1.unbindCurrentFrameBuffer();
		gameResources.fbos.bindReflectionFrameBuffer();
		gameResources.renderer.renderScene(gameResources.allEntities,
				gameResources.lights, gameResources.camera, new Vector4f(0, -1,
						0, Water.water.getHeight()));
		gameResources.fbos.unbindCurrentFrameBuffer();
	}

	public static void debugMode() {
		System.out.println("X" + Mouse.getX() + "Y" + Mouse.getY());
		System.out.println(gameResources.camera.getPosition());
	}

	private static void disposeGame() {
		gameResources.guiRenderer.render(gameResources.guis5);
		DisplayManager.updateDisplay();
		Logger.log("Closing Game");
		gameResources.cleanUp();
		DisplayManager.closeDisplay();
	}

	public static void main(String[] args) {
		StartGame();
	}
}
