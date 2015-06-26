/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

public class Kernel {

	public static boolean debug = false;
	public static boolean isLoading = false;

	private static int build = 26;
	private static double version = 1.0;
	public static GameResources gameResources;
	public static GuiResources guiResources;
	public static boolean error = false, postPro = false;
	public static World world;
	public static Console thread1;

	public static void run() {

		thread1 = new Console();
		thread1.start();

		while (!thread1.isReady) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		startGame();
	}

	private static void startGame() {
		init();
		while (gameResources.gameStates.loop) {
			loop();
		}
		disposeGame();
	}

	private static void init() {

		Logger.log(Thread.currentThread(), "Loading");
		Logger.log(Thread.currentThread(), "Voxel Game Version: " + version);
		Logger.log(Thread.currentThread(), "Build: " + build);
		DisplayManager.createDisplay();
		SystemInfo.printSystemInfo();

		gameResources = new GameResources();

		GuiResources.loadingGui();
		gameResources.guiRenderer.render(gameResources.guis5);
		DisplayManager.updateDisplay(30);

		gameResources.init();
		guiResources = new GuiResources();

		BlocksResources.createBlocks();
		gameResources.addRes();
		gameResources.music();
		world = new World();

	}

	private static void loop() {
		switch (gameResources.gameStates.state) {
		case MAINMENU:
			gameResources.guiRenderer.render(gameResources.guis2);
			gameResources.renderer.renderSceneNoPrepare(
					gameResources.allObjects, gameResources.lights,
					gameResources.camera, gameResources.plane);

			DisplayManager.updateDisplay(30);
			break;
		case WORLDSELECTION:
			MenuScreen.worldSelected();
			gameResources.guiRenderer.render(gameResources.guis3);
			DisplayManager.updateDisplay(30);
			break;
		case IN_PAUSE:
			gameResources.guiRenderer.render(gameResources.guis4);
			DisplayManager.updateDisplay(30);
			break;
		case GAME:
			world.update(gameResources.camera);
			// gameResources.mouse.update();
			gameResources.camera.move();
			gameResources.player.move();
			gameResources.glEn();
			gameResources.waterRenderer.setReflection();
			gameResources.glDi();
			gameResources.renderer.renderScene(gameResources.cubes,
					gameResources.lights, gameResources.camera,
					gameResources.plane);
			gameResources.renderer.renderSceneNoPrepare(
					gameResources.allObjects, gameResources.lights,
					gameResources.camera, gameResources.plane);
			gameResources.waterRenderer.render(gameResources.waters,
					gameResources.camera);
			gameResources.guiRenderer.renderNoPrepare(gameResources.guis);
			DisplayManager.updateDisplay(60);
			break;
		}
		gameResources.gameStates.switchStates();
	}

	private static void disposeGame() {
		gameResources.guiRenderer.render(gameResources.guis5);
		DisplayManager.updateDisplay(30);
		Logger.log(Thread.currentThread(), "Closing Game");
		gameResources.cleanUp();
		thread1.close();
		DisplayManager.closeDisplay();
	}

	public static void main(String[] args) {
		if (postPro) {
			System.setProperty("org.lwjgl.librarypath",
					new File("natives").getAbsolutePath());
		}
		run();
	}
}
