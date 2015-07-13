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
import io.github.guerra24.voxel.client.menu.ConfigGUI;
import io.github.guerra24.voxel.client.resources.GameResources;
import io.github.guerra24.voxel.client.resources.GuiResources;
import io.github.guerra24.voxel.client.world.World;
import io.github.guerra24.voxel.client.world.block.BlocksResources;

import java.io.File;

public class Kernel {

	private static Platform platform;
	public static GameResources gameResources;
	public static GuiResources guiResources;
	public static World world;
	public static Console thread1;
	public static ConfigGUI config;

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
		Thread.currentThread().setName("Voxel");
		config = new ConfigGUI();
		while (!config.ready) {
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
		Logger.log(Thread.currentThread(), "Voxel Game Version: "
				+ KernelConstants.version);
		Logger.log(Thread.currentThread(), "Build: " + KernelConstants.build);
		Logger.log(Thread.currentThread(), "Running on: " + getPlatform());
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
			DisplayManager.updateDisplay(30);
			break;
		case IN_PAUSE:
			gameResources.guiRenderer.render(gameResources.guis4);
			DisplayManager.updateDisplay(30);
			break;
		case GAME:
			gameResources.camera.move();
			world.update(gameResources.camera);
			// world.test();
			// gameResources.player.move();
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
			DisplayManager.updateDisplay(KernelConstants.FPS);
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

	public static Platform getPlatform() {
		if (platform == null) {
			final String OS = System.getProperty("os.name").toLowerCase();
			final String ARCH = System.getProperty("os.arch").toLowerCase();

			boolean isWindows = OS.contains("windows");
			boolean isLinux = OS.contains("linux");
			boolean isMac = OS.contains("mac");
			boolean is64Bit = ARCH.equals("amd64") || ARCH.equals("x86_64");

			platform = Platform.UNKNOWN;

			if (isWindows)
				platform = is64Bit ? Platform.WINDOWS_64 : Platform.WINDOWS_32;
			if (isLinux)
				platform = is64Bit ? Platform.LINUX_64 : Platform.LINUX_32;
			if (isMac)
				platform = Platform.MACOSX;
		}

		return platform;
	}

	public enum Platform {
		WINDOWS_32, WINDOWS_64, MACOSX, LINUX_32, LINUX_64, UNKNOWN
	}

	public static void main(String[] args) {
		if (KernelConstants.postPro) {
			System.setProperty("org.lwjgl.librarypath",
					new File("natives").getAbsolutePath());
		}
		run();
	}
}
