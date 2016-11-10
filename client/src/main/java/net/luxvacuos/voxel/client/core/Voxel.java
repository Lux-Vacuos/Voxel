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

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.api.MoltenAPI;
import net.luxvacuos.voxel.client.core.states.AboutState;
import net.luxvacuos.voxel.client.core.states.MainMenuState;
import net.luxvacuos.voxel.client.core.states.OptionsState;
import net.luxvacuos.voxel.client.core.states.SPCreateWorld;
import net.luxvacuos.voxel.client.core.states.SPLoadingState;
import net.luxvacuos.voxel.client.core.states.SPPauseState;
import net.luxvacuos.voxel.client.core.states.SPSelectionState;
import net.luxvacuos.voxel.client.core.states.SPState;
import net.luxvacuos.voxel.client.core.states.SplashScreenState;
import net.luxvacuos.voxel.client.core.states.StateNames;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.CrashScreen;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.universal.api.ModsHandler;
import net.luxvacuos.voxel.universal.bootstrap.AbstractBootstrap;
import net.luxvacuos.voxel.universal.bootstrap.Platform;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.EngineType;
import net.luxvacuos.voxel.universal.core.states.StateMachine;
import net.luxvacuos.voxel.universal.util.PerRunLog;

/**
 * Voxel's Heart, the main object where the loop is stored.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Kernel
 */
public class Voxel extends AbstractVoxel {
	/**
	 * Disposed boolean
	 */
	private boolean disposed = false;
	/**
	 * Loaded boolean
	 */
	private boolean loaded = false;

	GLFWErrorCallback errorfun = GLFWErrorCallback.createPrint(System.err);

	/**
	 * Create the instance and set some variables
	 */
	public Voxel(AbstractBootstrap bootstrap) {
		super(bootstrap);
		// Set the GLFW Error Callback
		GLFW.glfwSetErrorCallback(this.errorfun);
		// Set client
		super.engineType = EngineType.CLIENT;
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
		PerRunLog.setBootstrap(bootstrap);
		Logger.init();

		// Find version from Manifest file
		try {
			Manifest manifest = new Manifest(getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"));
			Attributes attr = manifest.getMainAttributes();
			String t = attr.getValue("Specification-Version");
			if (t != null)
				ClientVariables.version = t;
		} catch (IOException E) {
			E.printStackTrace();
		}
		Logger.log("Starting Client");

		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		ClientVariables.SETTINGS_PATH = bootstrap.getPrefix() + "/config/settings.conf";
		ClientVariables.WORLD_PATH = bootstrap.getPrefix() + "/world/";

		// Create the GameResources instance
		gameResources = GameResources.getInstance();
		// Do preInit on Game Resources
		getGameResources().preInit();
		// Set Window visible
		getGameResources().getGameWindow().setVisible(true);
		// Clear Screen
		Renderer.prepare(1, 1, 1, 1);
		// Update Screen buffers
		getGameResources().getGameWindow().updateDisplay(ClientVariables.FPS);
		// Set the info to objects
		CoreInfo.platform = bootstrap.getPlatform();
		CoreInfo.LWJGLVer = Version.getVersion();
		CoreInfo.GLFWVer = GLFW.glfwGetVersionString();
		CoreInfo.OpenGLVer = glGetString(GL_VERSION);
		CoreInfo.Vendor = glGetString(GL_VENDOR);
		CoreInfo.Renderer = glGetString(GL_RENDERER);
		// Print info
		Logger.log("Voxel Client Version: " + ClientVariables.version);
		Logger.log("Running on: " + CoreInfo.platform);
		Logger.log("LWJGL Version: " + CoreInfo.LWJGLVer);
		Logger.log("GLFW Version: " + CoreInfo.GLFWVer);
		Logger.log("OpenGL Version: " + CoreInfo.OpenGLVer);
		Logger.log("Vendor: " + CoreInfo.Vendor);
		Logger.log("Renderer: " + CoreInfo.Renderer);
		// Check for OS X
		if (bootstrap.getPlatform().equals(Platform.MACOSX)) {
			ClientVariables.runningOnMac = true;
		}
		// Create ModsHandler instance
		modsHandler = new ModsHandler(this);
		modsHandler.setMoltenAPI(new MoltenAPI());
		// Do Mod PreInit
		modsHandler.preInit();
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
		BlocksResources.createBlocks(getGameResources().getResourceLoader());
		// Load extra assets
		getGameResources().loadResources();
		// Load the States into the StateMachine
		StateMachine.registerState(new AboutState());
		StateMachine.registerState(new MainMenuState());
		StateMachine.registerState(new OptionsState());
		StateMachine.registerState(new SPCreateWorld());
		StateMachine.registerState(new SplashScreenState());
		StateMachine.registerState(new SPLoadingState());
		StateMachine.registerState(new SPPauseState());
		StateMachine.registerState(new SPSelectionState());
		StateMachine.registerState(new SPState());
		// Do Mod Init
		modsHandler.init();
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
		// Save settings
		GameResources.getInstance().getGameSettings().save();
		// Do Mod PostInit
		modsHandler.postInit();
		// Set the Mouse hidden
		Mouse.setHidden(true);
		// Initialize debug data
		Timers.initDebugDisplay();
		// Do PostInit on Game Resources
		getGameResources().postInit();
		// Set the state to splash screen
		StateMachine.setCurrentState(StateNames.SPLASH_SCREEN);
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
			StateMachine.run();
			// Initialize time variables
			float delta = 0;
			float accumulator = 0f;
			float interval = 1f / ClientVariables.UPS;
			float alpha = 0;
			// Set loaded
			loaded = true;
			// Get a local game window reference
			Window window = getGameResources().getGameWindow();
			while (StateMachine.isRunning() && !(window.isCloseRequested())) {
				// Start CPU timer
				Timers.startCPUTimer();
				// Update UPS
				if (window.getTimeCount() > 1f) {
					CoreInfo.ups = CoreInfo.upsCount;
					CoreInfo.upsCount = 0;
					window.setTimeCount(window.getTimeCount() - 1);
				}
				// Get last frame delta
				delta = window.getDelta();
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
				window.updateDisplay(ClientVariables.FPS);
			}
			// Dispose all resources
			dispose();
			GLFW.glfwTerminate();
		} catch (Throwable t) {
			t.printStackTrace(System.err);
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
		StateMachine.render(this, alpha);
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
		StateMachine.update(this, delta);
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
					dispose();
				} catch (Exception e1) {
				}
			else
				// try to close display
				WindowManager.closeAllDisplays();
		} catch (NullPointerException t) {
			e.printStackTrace();
		} finally {
			GLFW.glfwTerminate();
		}

		// If running on MacOSX don't show crash screen
		if (!bootstrap.getPlatform().equals(Platform.MACOSX))
			CrashScreen.run(e);
		else {
			e.printStackTrace(System.err); // Instead dump the error into the
											// Error Stream
			System.exit(-1);
		}

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
		modsHandler.dispose();
		// Close Window
		WindowManager.closeAllDisplays();
		// Free the Error Callback
		GLFW.glfwSetErrorCallback(null).free();

		StateMachine.dispose();
		// Set dispose and loaded
		disposed = true;
		loaded = false;
	}

	@Override
	public GameResources getGameResources() {
		return ((GameResources) gameResources);
	}

}
