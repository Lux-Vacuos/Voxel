package io.github.guerra24.voxel.client.kernel;

import io.github.guerra24.voxel.client.kernel.console.Console;
import io.github.guerra24.voxel.client.kernel.util.Logger;
import io.github.guerra24.voxel.client.kernel.util.SystemInfo;
import io.github.guerra24.voxel.client.menu.MenuScreen;
import io.github.guerra24.voxel.client.resources.GameResources;
import io.github.guerra24.voxel.client.resources.GuiResources;
import io.github.guerra24.voxel.client.world.World;
import io.github.guerra24.voxel.client.world.block.BlocksResources;

import java.io.File;

import org.lwjgl.input.Mouse;

public class Kernel extends Thread {

	public static boolean debug = false;
	public static boolean isLoading = false;

	private static int build = 17;
	private static double version = 1.0;
	public static GameResources gameResources;
	public static GuiResources guiResources;
	public static boolean error = false, postPro = false;
	public static World world;
	public static Kernel thread0;
	public static Console thread1;

	public void run() {

		thread1 = new Console();
		thread1.start();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Logger.log(currentThread(), "Loading");
		Logger.log(currentThread(), "Voxel Game Version: " + version);
		Logger.log(currentThread(), "Build: " + build);
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
				// world.test();
				world.update();
				// gameResources.mouse.update();
				gameResources.camera.move();
				gameResources.player.move();
				gameResources.glEn();
				gameResources.waterRenderer.setReflection();
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

	public static void standaloneRender() {
		gameResources.camera.move();
		gameResources.player.move();
		gameResources.glEn();
		gameResources.waterRenderer.setReflection();
		gameResources.glDi();
		gameResources.renderer
				.renderScene(gameResources.allEntities, gameResources.lights,
						gameResources.camera, gameResources.plane);
		gameResources.renderer
				.renderSceneNoPrepare(gameResources.allObjects,
						gameResources.lights, gameResources.camera,
						gameResources.plane);
		gameResources.waterRenderer.render(gameResources.waters,
				gameResources.camera);
		gameResources.guiRenderer.renderNoPrepare(gameResources.guis);
		DisplayManager.updateDisplay();
	}

	public static void debugMode() {
		System.out.println("X" + Mouse.getX() + "Y" + Mouse.getY());
		System.out.println(gameResources.camera.getPosition());
	}

	private static void disposeGame() {
		gameResources.guiRenderer.render(gameResources.guis5);
		DisplayManager.updateDisplay();
		Logger.log(currentThread(), "Closing Game");
		gameResources.cleanUp();
		thread1.close();
		DisplayManager.closeDisplay();
	}

	public static void main(String[] args) {
		if (postPro) {
			System.setProperty("org.lwjgl.librarypath",
					new File("natives").getAbsolutePath());
		}
		thread0 = new Kernel();
		thread0.start();
	}
}
