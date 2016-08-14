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
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.RunningSide;

/**
 * Voxel's Heart, the main object where the loop is stored.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Kernel
 */
public class Voxel extends AbstractVoxel {

	/**
	 * Mod Initialization instance
	 */
	private ModInitialization api;
	/**
	 * Disposed boolean
	 */
	private boolean disposed = false;
	/**
	 * Loaded boolean
	 */
	private boolean loaded = false;

	/**
	 * Create the instance and set some variables
	 */
	public Voxel() {
		// Path prefix
		super.prefix = Bootstrap.getPrefix();
		// Call Mainloop
		loop();
	}

	/**
	 * 
	 * PreInit. Initializes basic stuff.
	 * 
	 * @throws Exception
	 *             Throws Exception in case of error
	 */
	@Override
	public void preInit() throws Exception {

		// Find version from Manifest file

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

		// Create the GameResources instance

		gameResources = GameResources.getInstance();
		// Do preInit on Game Resources
		getGameResources().preInit();
		// Set Window visible
		getGameResources().getDisplay().setVisible();
		// Clear Screen to black
		MasterRenderer.prepare(0, 0, 0, 1);
		// Update Screen buffers
		getGameResources().getDisplay().updateDisplay(VoxelVariables.FPS);
		// Print info
		Logger.log("Voxel Version: " + VoxelVariables.version);
		Logger.log("Molten API Version: " + MoltenAPI.apiVersion);
		Logger.log("Build: " + MoltenAPI.build);
		Logger.log("Running on: " + Bootstrap.getPlatform());
		Logger.log("LWJGL Version: " + Version.getVersion());
		Logger.log("GLFW Version: " + GLFW.glfwGetVersionString());
		Logger.log("OpenGL Version: " + glGetString(GL_VERSION));
		Logger.log("Vendor: " + glGetString(GL_VENDOR));
		Logger.log("Renderer: " + glGetString(GL_RENDERER));
		// Set the info to objects
		CoreInfo.platform = Bootstrap.getPlatform();
		CoreInfo.LWJGLVer = Version.getVersion();
		CoreInfo.GLFWVer = GLFW.glfwGetVersionString();
		CoreInfo.OpenGLVer = glGetString(GL_VERSION);
		CoreInfo.Vendor = glGetString(GL_VENDOR);
		CoreInfo.Renderer = glGetString(GL_RENDERER);
		// Check for OS X
		if (Bootstrap.getPlatform() == Bootstrap.Platform.MACOSX) {
			VoxelVariables.runningOnMac = true;
		}
		// Create ModInitialization instance
		api = new ModInitialization(this);
		// Do Mod PreInit
		api.preInit();
	}

	/**
	 * Init. Start loading assets.
	 * 
	 * @throws Exception
	 *             Throws Exception in case of error
	 */
	@Override
	public void init() throws Exception {
		// Do Init on Game Resources
		getGameResources().init(this);
		// Load Block assets
		BlocksResources.createBlocks(getGameResources().getLoader());
		// Load extra assets
		getGameResources().loadResources();
		// Do Mod Init
		api.init();
	}

	/**
	 * 
	 * PostInit. Set the states and do final stuff
	 * 
	 * @throws Exception
	 *             Throws Exception in case of error
	 */
	@Override
	public void postInit() throws Exception {
		// Do Mod PostInit
		api.postInit();
		// Set the Mouse hidden
		Mouse.setHidden(true);
		// Initialize debug data
		Timers.initDebugDisplay();
		// Do PostInit on Game Resources
		getGameResources().postInit();
		// Set the state to splash screen
		getGameResources().getGlobalStates().setState(GameState.SPLASH_SCREEN);
	}

	/**
	 * Main Loop
	 */
	@Override
	public void loop() {
		// Big try catch to handle all possible exceptions.
		try {
			// Do the init process
			preInit();
			init();
			postInit();
			// Set Internal State to Running
			getGameResources().getGlobalStates().setInternalState(InternalState.RUNNIG);
			// Initialize time variables
			float delta = 0;
			float accumulator = 0f;
			float interval = 1f / VoxelVariables.UPS;
			float alpha = 0;
			// Set loaded
			loaded = true;
			while (getGameResources().getGlobalStates().getInternalState().equals(InternalState.RUNNIG)) {
				// Start CPU timer
				Timers.startCPUTimer();
				// Update UPS
				if (getGameResources().getDisplay().getTimeCount() > 1f) {
					CoreInfo.ups = CoreInfo.upsCount;
					CoreInfo.upsCount = 0;
					getGameResources().getDisplay().setTimeCount(getGameResources().getDisplay().getTimeCount() - 1);
				}
				// Get last frame delta
				delta = getGameResources().getDisplay().getDelta();
				// Add delta to accumulator
				accumulator += delta;
				// Fixed Update while
				while (accumulator >= interval) {
					// Call update
					update(interval);
					// Subtract interval to accumulator
					accumulator -= interval;
				}
				// Get alpha
				alpha = accumulator / interval;
				// Stop CPU timer
				Timers.stopCPUTimer();
				// Start GPU timer
				Timers.startGPUTimer();
				// Call render
				render(alpha);
				// Stop GPU timer
				Timers.stopGPUTimer();
				// Update Timers
				Timers.update();
				// Update Screen buffers
				getGameResources().getDisplay().updateDisplay(VoxelVariables.FPS);
			}
			// Dispose all resources
			dispose();
		} catch (Throwable t) {
			handleError(t);
		}
	}

	/**
	 * Render method
	 * 
	 * @param alpha
	 *            Alpha for update
	 */
	public void render(float alpha) {
		getGameResources().getGlobalStates().doRender(this, alpha);
	}

	/**
	 * 
	 * Update method
	 * 
	 * @param delta
	 *            Delta for update
	 */
	@Override
	public void update(float delta) {
		CoreInfo.upsCount++;
		getGameResources().getGlobalStates().doUpdate(this, delta);
	}

	/**
	 * Handle all errors
	 * 
	 * @param e
	 *            Throwable
	 */
	@Override
	public void handleError(Throwable e) {
		// Try to dispose world and assets
		try {
			if (!disposed && loaded)
				try {
					getGameResources().getWorldsHandler().getActiveWorld().dispose();
					dispose();
				} catch (Exception e1) {
				}
			else
				// try to close display
				getGameResources().getDisplay().closeDisplay();
		} catch (NullPointerException t) {
			e.printStackTrace();
		}
		// If running on MacOSX don't show crash screen
		if (!Bootstrap.getPlatform().equals(Platform.MACOSX))
			CrashScreen.run(e);
		else
			System.exit(-1);
	}

	/**
	 * Dispose method, all loaded stuff is cleaned
	 * 
	 */
	@Override
	public void dispose() {
		Logger.log("Cleaning Resources");
		// Clean loaded assets
		this.gameResources.dispose();
		// Clean mods
		api.dispose();
		// Close Window
		getGameResources().getDisplay().closeDisplay();
		// Set dispose and loaded
		disposed = true;
		loaded = false;
	}

	@Override
	public GameResources getGameResources() {
		return ((GameResources) gameResources);
	}

	@Override
	public RunningSide getSide() {
		return RunningSide.CLIENT;
	}

}
