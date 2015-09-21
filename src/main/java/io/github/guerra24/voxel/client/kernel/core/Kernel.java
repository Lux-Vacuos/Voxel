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

import io.github.guerra24.voxel.client.kernel.api.API;
import io.github.guerra24.voxel.client.kernel.bootstrap.Bootstrap;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.Display;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.SystemInfo;
import io.github.guerra24.voxel.client.kernel.input.Keyboard;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.GuiResources;
import io.github.guerra24.voxel.client.kernel.util.Logger;
import io.github.guerra24.voxel.client.kernel.world.World;
import io.github.guerra24.voxel.client.kernel.world.block.BlocksResources;

/**
 * The Kernel, Game Engine Core
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Kernel
 */
public class Kernel implements IKernel {

	/**
	 * Contains the Game Resources, all the textures, models and other type of
	 * data
	 */
	private GameResources gameResources;

	/**
	 * Contains the GUI/UI Resources
	 */
	private GuiResources guiResources;
	/**
	 * Contains and Handles the Game World
	 */
	private World world;
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
	 * World Thread
	 */
	private static WorldThread worldThread;
	/**
	 * Update Thread
	 */
	private static UpdateThread update;

	private Display display;
	/**
	 * Error Test
	 */
	public boolean errorTest;

	/**
	 * Modding API
	 */
	private API api;

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
		Logger.log(Thread.currentThread(), "Voxel Game Version: " + KernelConstants.version);
		Logger.log(Thread.currentThread(), "Build: " + KernelConstants.build);
		Logger.log(Thread.currentThread(), "Running on: " + Bootstrap.getPlatform());
		display = new Display();
		display.initDsiplay();
		display.startUp();
		SystemInfo.printSystemInfo();

		gameResources = new GameResources();
		api = new API();
		api.preInit();
		gameResources.init();
		guiResources = new GuiResources(gameResources);
		BlocksResources.createBlocks(gameResources.getLoader());
		gameResources.addRes();
		gameResources.music();
		world = new World();
		worldThread = new WorldThread();
		worldThread.setName("Voxel World");
		worldThread.setApi(api);
		worldThread.setWorld(world);
		worldThread.setGm(gameResources);
		worldThread.start();
		update = new UpdateThread();
		update.setName("Voxel Update");
		update.setApi(api);
		update.setWorld(world);
		update.setGm(gameResources);
		update.start();
		api.init();
		// byte[] user = Launcher.user.getBytes(Charset.forName("UTF-8"));
		// Logger.log(Thread.currentThread(), "User: " + Launcher.user +
		// "UUID: "
		// + UUID.nameUUIDFromBytes(user));
		api.postInit();
		KernelConstants.loaded = true;
		gameResources.getSoundSystem().play("menu1");
	}

	@Override
	public void mainLoop() {
		init();
		float delta = 0;
		float accumulator = 0f;
		float interval = 1f / 30;
		float alpha = 0;
		while (gameResources.getGameStates().loop) {
			if (Display.timeCount > 1f) {
				Logger.log(Thread.currentThread(), "RCPS: " + Kernel.renderCallsPerFrame);
				Logger.log(Thread.currentThread(), "FPS: " + Display.fps);
				Logger.log(Thread.currentThread(), "UPS: " + Display.ups);
				Display.fps = Display.fpsCount;
				Display.fpsCount = 0;
				Display.ups = Display.upsCount;
				Display.upsCount = 0;
				Display.timeCount -= 1f;
			}
			delta = Display.getDelta();
			accumulator += delta;
			while (accumulator >= interval) {
				update(gameResources, guiResources, interval);
				accumulator -= interval;
			}

			alpha = accumulator / interval;
			render(gameResources, delta);
			error();
			totalRenderCalls += renderCalls;
			renderCallsPerFrame = renderCalls;
			renderCalls = 0;
		}
		dispose();
	}

	@Override
	public void render(GameResources gm, float delta) {
		Display.fpsCount++;
		switch (gm.getGameStates().state) {
		case MAINMENU:
			gm.getFrustum().calculateFrustum(gm);
			gm.getRenderer().prepare();
			gm.getRenderer().renderEntity(gm.mainMenuModels, gm.mainMenuLights, gm);
			gm.getGuiRenderer().renderGui(gm.guis2);
			display.updateDisplay(30, gm);
			break;
		case IN_PAUSE:
			gm.getFrustum().calculateFrustum(gm);
			gm.getRenderer().prepare();
			world.updateChunksRender(gm);
			gm.getRenderer().renderEntity(gm.getPhysics().getMobManager().getMobs(), gm.lights, gm);
			gm.getSkyboxRenderer().render(KernelConstants.RED, KernelConstants.GREEN, KernelConstants.BLUE, delta, gm);
			gm.getGuiRenderer().renderGui(gm.guis4);
			display.updateDisplay(KernelConstants.FPS, gm);
			break;
		case GAME:
			gm.getFrustum().calculateFrustum(gm);
			gm.getRenderer().prepare();
			world.updateChunksRender(gm);
			gm.getRenderer().renderEntity(gm.getPhysics().getMobManager().getMobs(), gm.lights, gm);
			gm.getCamera().updatePicker();
			gm.getSkyboxRenderer().render(KernelConstants.RED, KernelConstants.GREEN, KernelConstants.BLUE, delta, gm);
			gm.getGuiRenderer().renderGui(gm.guis);
			display.updateDisplay(KernelConstants.FPS, gm);
			break;
		case LOADING_WORLD:
			gm.getRenderer().prepare();
			gm.getGuiRenderer().renderGui(gm.guis3);
			display.updateDisplay(60, gm);
			break;
		}
	}

	@Override
	public void update(GameResources gm, GuiResources gi, float delta) {
		Display.upsCount++;
		switch (gm.getGameStates().state) {
		case MAINMENU:
			if (Keyboard.isKeyDown(Keyboard.KEY_O))
				Bootstrap.config.setVisible(true);
			gm.mainMenuModels.get(0).getEntity().increaseRotation(0, 0.1f, 0);
			break;
		case IN_PAUSE:
			if (Keyboard.isKeyDown(Keyboard.KEY_O))
				Bootstrap.config.setVisible(true);
			break;
		case GAME:
			gm.getPhysics().getMobManager().update(delta, gm, gi);
			gm.getWaterRenderer().update(delta);
			gm.getSkyboxRenderer().update(delta);
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
		Logger.log(Thread.currentThread(), "Closing Game");
		gameResources.cleanUp();
		api.dispose();
		if (!errorTest) {
			Bootstrap.config.dispose();
		}
		display.closeDisplay();
	}

	public GameResources getGameResources() {
		return gameResources;
	}

	public GuiResources getGuiResources() {
		return guiResources;
	}

	public World getWorld() {
		return world;
	}

	public API getApi() {
		return api;
	}

}
