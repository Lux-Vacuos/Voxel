/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.core;

import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.bootstrap.Bootstrap;
import net.luxvacuos.voxel.client.core.GlobalStates.InternalState;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.CrashScreen;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.universal.api.ModInitialization;
import net.luxvacuos.voxel.universal.api.MoltenAPI;

/**
 * The Kernel, Game Engine Core
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Kernel
 */
public class Voxel {

	/**
	 * Game Data
	 */
	private GameResources gameResources;
	private ModInitialization api;
	private boolean disposed = false;
	private boolean loaded = false;

	/**
	 * Constructor of the Voxel Kernel, Initializes the Game and starts the loop
	 */
	public Voxel() {
		mainLoop();
	}

	public Voxel(String test) {
		Logger.log("Running voxel in test mode");
	}

	/**
	 * PreInit phase, initialize the display and runs the API PreInit
	 * 
	 */
	public void preInit() throws Throwable {
		Logger.log("Starting Client");
		gameResources = GameResources.instance();
		gameResources.preInit();
		Logger.log("Voxel Version: " + VoxelVariables.version);
		Logger.log("Build: " + VoxelVariables.build);
		Logger.log("Molten API Version: " + MoltenAPI.apiVersion);
		Logger.log("Build: " + MoltenAPI.build);
		Logger.log("Running on: " + Bootstrap.getPlatform());
		Logger.log("LWJGL Version: " + Version.getVersion());
		Logger.log("GLFW Version: " + GLFW.glfwGetVersionString());
		Logger.log("OpenGL Version: " + glGetString(GL_VERSION));
		Logger.log("Vendor: " + glGetString(GL_VENDOR));
		Logger.log("Renderer: " + glGetString(GL_RENDERER));
		CoreInfo.platform = Bootstrap.getPlatform();
		CoreInfo.LWJGLVer = Version.getVersion();
		CoreInfo.GLFWVer = GLFW.glfwGetVersionString();
		CoreInfo.OpenGLVer = glGetString(GL_VERSION);
		CoreInfo.Vendor = glGetString(GL_VENDOR);
		CoreInfo.Renderer = glGetString(GL_RENDERER);
		gameResources.getDisplay().setVisible();

		if (Bootstrap.getPlatform() == Bootstrap.Platform.MACOSX) {
			VoxelVariables.runningOnMac = true;
		}
		api = new ModInitialization(gameResources);
		api.preInit();
	}

	/**
	 * Init phase, initialize the game data (models,textures,music,etc) and runs
	 * the API Init
	 */
	public void init() throws Throwable {
		gameResources.init(this);
		BlocksResources.createBlocks(gameResources.getLoader());
		gameResources.loadResources();
		Logger.log("Initializing Threads");
		gameResources.getRenderer().prepare();
		api.init();
	}

	/**
	 * PostInit phase, starts music and runs the API PostInit
	 */
	public void postInit() throws Throwable {
		api.postInit();
		Mouse.setHidden(true);
		Timers.initDebugDisplay();
	}

	/**
	 * Voxel Main Loop
	 * 
	 */
	private void mainLoop() {
		try {
			preInit();
			init();
			postInit();
			gameResources.getGlobalStates().setInternalState(InternalState.RUNNIG);
			loaded = true;
			float delta = 0;
			float accumulator = 0f;
			float interval = 1f / VoxelVariables.UPS;
			float alpha = 0;

			while (gameResources.getGlobalStates().getInternalState() == InternalState.RUNNIG) {
				Timers.startCPUTimer();
				if (gameResources.getDisplay().getTimeCount() > 1f) {
					CoreInfo.ups = CoreInfo.upsCount;
					CoreInfo.upsCount = 0;
					gameResources.getDisplay().setTimeCount(gameResources.getDisplay().getTimeCount() - 1);
				}
				delta = gameResources.getDisplay().getDelta();
				accumulator += delta;
				while (accumulator >= interval) {
					update(interval);
					accumulator -= interval;
				}
				alpha = accumulator / interval;
				Timers.stopCPUTimer();
				Timers.startGPUTimer();
				render(alpha);
				Timers.stopGPUTimer();
				Timers.update();
				gameResources.getDisplay().updateDisplay(VoxelVariables.FPS);
			}
			dispose();
		} catch (Throwable t) {
			handleError(t);
		}
	}

	/**
	 * Handles all render calls
	 * 
	 * @param delta
	 *            Delta value from Render Thread
	 */
	private void render(float delta) {
		gameResources.getGlobalStates().doRender(this, delta);
	}

	/**
	 * Handles all update calls
	 * 
	 * @param delta
	 *            Delta value from Update Thread
	 * @throws Exception
	 */
	private void update(float delta) throws Exception {
		CoreInfo.upsCount++;
		gameResources.getGlobalStates().doUpdate(this, delta);
	}

	private void handleError(Throwable e) {
		if (!disposed && loaded)
			try {
				dispose();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		CrashScreen.run(e);
	}

	public void dispose() throws Exception {
		disposed = true;
		Logger.log("Cleaning Resources");
		gameResources.cleanUp();
		api.dispose();
		gameResources.getDisplay().closeDisplay();
	}

	public GameResources getGameResources() {
		return gameResources;
	}

	public ModInitialization getApi() {
		return api;
	}

}
