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

package io.github.guerra24.voxel.client.kernel.core;

import static org.lwjgl.opengl.GL11.glLoadIdentity;
import io.github.guerra24.voxel.client.kernel.api.API;
import io.github.guerra24.voxel.client.kernel.bootstrap.Bootstrap;
import io.github.guerra24.voxel.client.kernel.graphics.Frustum;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.DisplayManager;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.SystemInfo;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.util.Logger;
import io.github.guerra24.voxel.client.kernel.world.World;
import io.github.guerra24.voxel.client.kernel.world.block.BlocksResources;

/**
 * The Kernel, Game Engine Core
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.1 Build-52
 * @since 0.0.1 Build-1
 * @category Kernel
 */
public class Kernel implements IKernel {

	/**
	 * Contains the Game Resources, all the textures, models and other type of
	 * data
	 */
	public static GameResources gameResources;
	/**
	 * Contains the GUI/UI Resources
	 */
	public static GuiResources guiResources;
	/**
	 * Contains and Handles the Game World
	 */
	public static World world;
	/**
	 * Render calls
	 */
	public static float renderCalls = 0;
	/**
	 * Render Calls Per Frame
	 */
	public static float renderCallsPerFrame = 0;
	/**
	 * Total Render Calls
	 */
	public static float totalRenderCalls = 0;
	/**
	 * Error Check timing
	 */
	public static int errorTime = 0;
	/**
	 * Update Thread
	 */
	public static UpdateThread update;
	/**
	 * Error Test
	 */
	public boolean errorTest;

	/**
	 * Modding API
	 */
	public static API api;

	/**
	 * Constructor of the Kernel, Initializes the Game and starts the loop
	 * 
	 * @param errorTest
	 *            If running JUnit Test
	 */
	public Kernel(boolean errorTest) {
		this.errorTest = errorTest;
		mainLoop();
	}

	@Override
	public void init() {

		Logger.log(Thread.currentThread(), "Loading");
		Logger.log(Thread.currentThread(), "Voxel Game Version: "
				+ KernelConstants.version);
		Logger.log(Thread.currentThread(), "Build: " + KernelConstants.build);
		Logger.log(Thread.currentThread(),
				"Running on: " + Bootstrap.getPlatform());
		DisplayManager.createDisplay();
		SystemInfo.printSystemInfo();
		if (KernelConstants.advancedOpenGL)
			Logger.log(Thread.currentThread(), "Using Advanced Rendering");

		gameResources = new GameResources();

		GuiResources.loadingGui();
		gameResources.guiRenderer.render(gameResources.guis5);
		DisplayManager.updateDisplay(30);
		api = new API();
		api.preInit();

		gameResources.init();
		guiResources = new GuiResources();

		BlocksResources.createBlocks();
		gameResources.addRes();
		gameResources.music();
		world = new World();
		update = new UpdateThread();
		update.setName("Voxel World");
		update.start();
		api.init();
		// byte[] user = Launcher.user.getBytes(Charset.forName("UTF-8"));
		// Logger.log(Thread.currentThread(), "User: " + Launcher.user +
		// "UUID: "
		// + UUID.nameUUIDFromBytes(user));
		api.postInit();
	}

	@Override
	public void mainLoop() {
		init();
		while (gameResources.gameStates.loop) {
			render();
			update();
			error();
			totalRenderCalls += renderCalls;
			renderCallsPerFrame = renderCalls;
			renderCalls = 0;
		}
		dispose();
	}

	@Override
	public void render() {
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
			gameResources.renderer.renderWorld(gameResources.cubes,
					gameResources.lights, gameResources.camera);
			gameResources.waterRenderer.render(gameResources.waters,
					gameResources.camera);
			gameResources.renderer.renderEntity(gameResources.allObjects,
					gameResources.lights, gameResources.camera);
			gameResources.guiRenderer.renderNoPrepare(gameResources.guis);
			glLoadIdentity();
			DisplayManager.updateDisplay(KernelConstants.FPS);
			break;
		case LOADING_WORLD:
			gameResources.guiRenderer.render(gameResources.guis3);
			DisplayManager.updateDisplay(30);
			break;
		}
	}

	@Override
	public void update() {
		switch (gameResources.gameStates.state) {
		case MAINMENU:
			break;
		case IN_PAUSE:
			break;
		case GAME:
			gameResources.player.move();
			gameResources.camera.move();
			Frustum.updateFrustum();
			break;
		case LOADING_WORLD:
			break;
		}
	}

	@Override
	public void error() {
		errorTime++;
		if (errorTime % 100 == 0) {
			if (renderCallsPerFrame > 50000) {
				Logger.warn(Thread.currentThread(), "Render Overflow");
			}
			errorTime = 0;
		}
	}

	@Override
	public void dispose() {
		gameResources.guiRenderer.render(gameResources.guis5);
		DisplayManager.updateDisplay(30);
		Logger.log(Thread.currentThread(), "Closing Game");
		gameResources.cleanUp();
		if (!errorTest)
			Bootstrap.thread1.close();
		DisplayManager.closeDisplay();
	}

}
