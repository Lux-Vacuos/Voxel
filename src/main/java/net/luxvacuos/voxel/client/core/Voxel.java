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
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.universal.api.ModInitialization;
import net.luxvacuos.voxel.universal.api.MoltenAPI;
import net.luxvacuos.voxel.universal.api.VersionException;

/**
 * The Kernel, Game Engine Core
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Kernel
 */
public class Voxel {

	/**
	 * Game Threads
	 */
	/**
	 * Game Data
	 */
	private GameResources gameResources;
	private ModInitialization api;

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
	public void preInit() {
		Logger.log("Starting Client");
		gameResources = GameResources.instance();
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
		try {
			api.preInit();
		} catch (VersionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Init phase, initialize the game data (models,textures,music,etc) and runs
	 * the API Init
	 */
	public void init() {
		gameResources.init(this);
		BlocksResources.createBlocks(gameResources.getLoader());
		gameResources.loadResources();
		Logger.log("Initializing Threads");
		gameResources.getRenderer().prepare();

		try {
			api.init();
		} catch (VersionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * PostInit phase, starts music and runs the API PostInit
	 */
	public void postInit() {
		try {
			api.postInit();
		} catch (VersionException e) {
			e.printStackTrace();
		}
		Mouse.setHidden(true);
		Timers.initDebugDisplay();
	}

	/**
	 * Voxel Main Loop
	 * 
	 */
	private void mainLoop() {
		preInit();
		init();
		postInit();
		gameResources.getGlobalStates().setInternalState(InternalState.RUNNIG);
		float delta = 0;
		float accumulator = 0f;
		float interval = 1f / VoxelVariables.UPS;
		float alpha = 0;
		try {
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
		} catch (Exception e) {
			Logger.log("FATAL ERROR - STOPPING");
			e.printStackTrace();
			gameResources.getGlobalStates().setInternalState(InternalState.INTERNAL_ERROR);
		}
		dispose();
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
	 */
	private void update(float delta) {
		CoreInfo.upsCount++;
		gameResources.getGlobalStates().doUpdate(this, delta);
	}

	/**
	 * Disposes all game data
	 */
	public void dispose() {
		Logger.log("Closing Game");
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
