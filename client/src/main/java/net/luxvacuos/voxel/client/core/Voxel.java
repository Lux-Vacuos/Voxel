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

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.bootstrap.Bootstrap;
import net.luxvacuos.voxel.client.bootstrap.Bootstrap.Platform;
import net.luxvacuos.voxel.client.core.GlobalStates.GameState;
import net.luxvacuos.voxel.client.core.GlobalStates.InternalState;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.opengl.MasterRenderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.CrashScreen;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.universal.api.ModInitialization;
import net.luxvacuos.voxel.universal.api.MoltenAPI;
import net.luxvacuos.voxel.universal.core.UVoxel;

/**
 * The Kernel, Game Engine Core
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Kernel
 */
public class Voxel extends UVoxel {

	private ModInitialization api;
	private boolean disposed = false;
	private boolean loaded = false;

	public Voxel() {
		super.prefix = Bootstrap.getPrefix();
		super.client = true;
		mainLoop();
	}

	public void preInit() throws Exception {
		try {
			Manifest manifest = new Manifest(getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"));
			Attributes attr = manifest.getMainAttributes();
			String t = attr.getValue("Specification-Version");
			if (t != null)
				VoxelVariables.version = t;
		} catch (IOException E) {
			E.printStackTrace();
		}
		Logger.log("Starting Client");
		gameResources = GameResources.getInstance();
		getGameResources().preInit();
		getGameResources().getDisplay().setVisible();
		MasterRenderer.prepare(0, 0, 0, 1);
		getGameResources().getDisplay().updateDisplay(VoxelVariables.FPS);
		Logger.log("Voxel Version: " + VoxelVariables.version);
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
		if (Bootstrap.getPlatform() == Bootstrap.Platform.MACOSX) {
			VoxelVariables.runningOnMac = true;
		}
		api = new ModInitialization(this);
		api.preInit();
	}

	public void init() throws Exception {
		getGameResources().init(this);
		BlocksResources.createBlocks(getGameResources().getLoader());
		getGameResources().loadResources();
		Logger.log("Initializing Threads");
		api.init();
	}

	public void postInit() throws Exception {
		api.postInit();
		Mouse.setHidden(true);
		Timers.initDebugDisplay();
		getGameResources().postInit();
		getGameResources().getGlobalStates().setState(GameState.SPLASH_SCREEN);
	}

	private void mainLoop() {
		try {
			preInit();
			init();
			postInit();
			getGameResources().getGlobalStates().setInternalState(InternalState.RUNNIG);
			float delta = 0;
			float accumulator = 0f;
			float interval = 1f / VoxelVariables.UPS;
			float alpha = 0;

			loaded = true;
			while (getGameResources().getGlobalStates().getInternalState().equals(InternalState.RUNNIG)) {
				Timers.startCPUTimer();
				if (getGameResources().getDisplay().getTimeCount() > 1f) {
					CoreInfo.ups = CoreInfo.upsCount;
					CoreInfo.upsCount = 0;
					getGameResources().getDisplay().setTimeCount(getGameResources().getDisplay().getTimeCount() - 1);
				}
				delta = getGameResources().getDisplay().getDelta();
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
				getGameResources().getDisplay().updateDisplay(VoxelVariables.FPS);
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
		getGameResources().getGlobalStates().doRender(this, delta);
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
		getGameResources().getGlobalStates().doUpdate(this, delta);
	}

	private void handleError(Throwable e) {
		try {
			if (!disposed && loaded)
				try {
					getGameResources().getWorldsHandler().getActiveWorld().dispose();
					dispose();
				} catch (Exception e1) {
				}
			else
				getGameResources().getDisplay().closeDisplay();
		} catch (NullPointerException t) {
			e.printStackTrace();
		}
		if (!Bootstrap.getPlatform().equals(Platform.MACOSX))
			CrashScreen.run(e);
		else
			System.exit(-1);
	}

	public void dispose() throws Exception {
		disposed = true;
		Logger.log("Cleaning Resources");
		getGameResources().cleanUp();
		api.dispose();
		getGameResources().getDisplay().closeDisplay();
	}

	@Override
	public GameResources getGameResources() {
		return ((GameResources) gameResources);
	}

	public ModInitialization getApi() {
		return api;
	}

}
