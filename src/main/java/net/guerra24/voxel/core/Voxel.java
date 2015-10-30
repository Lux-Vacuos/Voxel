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

package net.guerra24.voxel.core;

import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

import org.lwjgl.Sys;

import net.guerra24.voxel.api.VAPI;
import net.guerra24.voxel.bootstrap.Bootstrap;
import net.guerra24.voxel.graphics.opengl.Display;
import net.guerra24.voxel.input.Keyboard;
import net.guerra24.voxel.resources.GameResources;
import net.guerra24.voxel.resources.GuiResources;
import net.guerra24.voxel.util.Logger;
import net.guerra24.voxel.world.InfinityWorld;
import net.guerra24.voxel.world.WorldsHandler;
import net.guerra24.voxel.world.block.BlocksResources;

/**
 * The Kernel, Game Engine Core
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Kernel
 */
public class Voxel {

	public static float renderCalls = 0;
	public static float renderCallsPerFrame = 0;
	public static float totalRenderCalls = 0;
	public static int errorTime = 0;

	/**
	 * Game Threads
	 */
	public static WorldThread1 worldThread2;

	/**
	 * Game Data
	 */
	private GameResources gameResources;
	private GuiResources guiResources;
	private WorldsHandler worldsHandler;
	private Display display;
	private VAPI api;

	/**
	 * Constructor of the Kernel, Initializes the Game and starts the loop
	 * 
	 * @param errorTest
	 *            If running JUnit Test
	 */
	public Voxel() {
		mainLoop();
	}

	public void preInit() {
		display = new Display();
		display.initDsiplay(VoxelVariables.WIDTH, VoxelVariables.HEIGHT);
		display.startUp();
		Logger.log("Loading");
		Logger.log("Voxel Version: " + VoxelVariables.version);
		Logger.log("Build: " + VoxelVariables.build);
		Logger.log("Running on: " + Bootstrap.getPlatform());
		Logger.log("LWJGL Version: " + Sys.getVersion());
		Logger.log("OpenGL Version: " + glGetString(GL_VERSION));
		Logger.log("Vendor: " + glGetString(GL_VENDOR));
		Logger.log("Renderer: " + glGetString(GL_RENDERER));
		if (Bootstrap.getPlatform() == Bootstrap.Platform.MACOSX)
			VoxelVariables.runningOnMac = true;
		gameResources = new GameResources();
		api = new VAPI();
		api.preInit();
	}

	private void init() {
		gameResources.init();
		guiResources = new GuiResources(gameResources);
		BlocksResources.createBlocks(gameResources.getLoader());
		gameResources.addRes();
		gameResources.music();
		worldsHandler = new WorldsHandler();
		InfinityWorld world = new InfinityWorld();
		worldsHandler.registerWorld(world.getCodeName(), world);
		worldsHandler.setActiveWorld("Infinity");
		Logger.log("Initializing Threads");
		worldThread2 = new WorldThread1();
		worldThread2.setName("Voxel World 1");
		worldThread2.setWorldHandler(worldsHandler);
		worldThread2.setGameResources(gameResources);
		worldThread2.setKernel(this);
		worldThread2.setGuiResources(guiResources);
		worldThread2.start();
		api.init();
	}

	private void postInit() {
		api.postInit();
		glfwShowWindow(Display.getWindow());
		gameResources.getSoundSystem().play("menu1");
	}

	public void mainLoop() {
		preInit();
		init();
		postInit();
		float delta = 0;
		while (gameResources.getGameStates().loop) {
			if (Display.timeCountRender > 1f) {
				Display.fps = Display.fpsCount;
				Display.fpsCount = 0;
				Display.ups = Display.upsCount;
				Display.upsCount = 0;
				Display.timeCountRender -= 1f;
			}
			delta = Display.getDeltaRender();
			render(gameResources, delta);
			totalRenderCalls += renderCalls;
			renderCallsPerFrame = renderCalls;
			renderCalls = 0;
		}
		dispose();
	}

	public void render(GameResources gm, float delta) {
		Display.fpsCount++;
		switch (gm.getGameStates().state) {
		case MAINMENU:
			gm.getFrustum().calculateFrustum(gm);
			gm.getRenderer().prepare();
			gm.getRenderer().renderEntity(gm.mainMenuModels, gm);
			gm.getGuiRenderer().renderGui(gm.guis2);
			display.updateDisplay(30, gm);
			break;
		case IN_PAUSE:
			gm.getRenderer().prepare();
			worldsHandler.getActiveWorld().updateChunksRender(gm);
			gm.getRenderer().renderEntity(gm.getPhysics().getMobManager().getMobs(), gm);
			gm.getSkyboxRenderer().render(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, delta, gm);
			gm.getParticleController().render(gm);
			gm.getGuiRenderer().renderGui(gm.guis4);
			display.updateDisplay(VoxelVariables.FPS, gm);
			break;
		case GAME:
			gm.getCamera().update(delta, gameResources, guiResources, worldsHandler.getActiveWorld(), api);
			gm.getPhysics().getMobManager().getPlayer().update(delta, gm, guiResources, worldsHandler.getActiveWorld(),
					api);
			gm.getFrustum().calculateFrustum(gm);

			worldsHandler.getActiveWorld().lighting();

			gm.getWaterFBO().begin(128, 128);
			gm.getCamera().invertPitch();
			gm.getRenderer().prepare();
			gm.getSkyboxRenderer().render(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, delta, gm);
			gm.getWaterFBO().end();
			gm.getCamera().invertPitch();

			gm.getPostProcessing().getPost_fbo().begin(Display.getWidth(), Display.getHeight());
			gm.getRenderer().prepare();
			worldsHandler.getActiveWorld().updateChunksRender(gm);
			gm.getSkyboxRenderer().render(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, delta, gm);
			gm.getRenderer().renderEntity(gm.getPhysics().getMobManager().getMobs(), gm);
			gm.getParticleController().render(gm);
			gm.getPostProcessing().getPost_fbo().end();

			gm.getRenderer().prepare();
			gm.getPostProcessing().render(gm);
			gm.getGuiRenderer().renderGui(gm.guis);
			display.updateDisplay(VoxelVariables.FPS, gm);
			break;
		case LOADING_WORLD:
			gm.getRenderer().prepare();
			gm.getGuiRenderer().renderGui(gm.guis3);
			display.updateDisplay(30, gm);
			break;
		}
	}

	public void update(GameResources gm, GuiResources gi, WorldsHandler world, float delta) {
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
			worldsHandler.getActiveWorld().updateChunksGeneration(gm, api);
			gm.getPhysics().getMobManager().update(delta, gm, gi, world.getActiveWorld(), api);
			gm.getParticleController().update(delta, gm, gi, world.getActiveWorld());
			gm.getRenderer().getWaterRenderer().update(delta);
			gm.getSkyboxRenderer().update(delta);
			gm.getParticleController().update(delta, gm, gi, world.getActiveWorld());
			break;
		case LOADING_WORLD:
			break;
		}
		gm.getGameStates().switchStates(gm, world, api, display);
	}

	public void dispose() {
		Logger.log("Closing Game");
		gameResources.cleanUp();
		api.dispose();
		Bootstrap.config.dispose();
		display.closeDisplay();
	}

	public GameResources getGameResources() {
		return gameResources;
	}

	public GuiResources getGuiResources() {
		return guiResources;
	}

	public VAPI getApi() {
		return api;
	}

}
