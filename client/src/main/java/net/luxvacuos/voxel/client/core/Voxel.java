/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;
import static org.lwjgl.assimp.Assimp.aiGetVersionMajor;
import static org.lwjgl.assimp.Assimp.aiGetVersionMinor;
import static org.lwjgl.assimp.Assimp.aiGetVersionRevision;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.api.MoltenAPI;
import net.luxvacuos.voxel.client.core.states.CrashState;
import net.luxvacuos.voxel.client.core.states.MPWorldState;
import net.luxvacuos.voxel.client.core.states.MainMenuState;
import net.luxvacuos.voxel.client.core.states.SPWorldState;
import net.luxvacuos.voxel.client.core.states.SplashScreenState;
import net.luxvacuos.voxel.client.core.states.StateNames;
import net.luxvacuos.voxel.client.core.states.TestState;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.universal.api.ModsHandler;
import net.luxvacuos.voxel.universal.bootstrap.AbstractBootstrap;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.EngineType;
import net.luxvacuos.voxel.universal.core.TaskManager;
import net.luxvacuos.voxel.universal.core.states.StateMachine;
import net.luxvacuos.voxel.universal.util.PerRunLog;

/**
 * Voxel's Heart, the main object where the loop is stored.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Kernel
 */
public class Voxel extends AbstractVoxel {

	GLFWErrorCallback errorfun = GLFWErrorCallback.createPrint(System.err);

	/**
	 * Create the instance and set some variables
	 */
	public Voxel(AbstractBootstrap bootstrap) {
		super(bootstrap);
		GLFW.glfwSetErrorCallback(this.errorfun);
		super.engineType = EngineType.CLIENT;
		init();
	}

	/**
	 * 
	 * PreInit. Initializes basic stuff.
	 * 
	 * @throws Exception
	 *             Throws Exception in case of error
	 */
	@Override
	public void init() {
		PerRunLog.setBootstrap(bootstrap);
		Logger.init();
		Logger.log("Starting Client");

		super.addSubsystem(new ClientCoreSubsystem());
		super.addSubsystem(new GraphicalSubsystem());

		ClientVariables.initRuntimeVariables(bootstrap);

		super.initSubsystems();

		try {
			Manifest manifest = new Manifest(getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"));
			Attributes attr = manifest.getMainAttributes();
			String t = attr.getValue("Specification-Version");
			if (t != null)
				ClientVariables.version = t;
		} catch (IOException E) {
			E.printStackTrace();
		}

		internalSubsystem = ClientInternalSubsystem.getInstance();
		internalSubsystem.preInit();

		StateMachine.registerState(new SplashScreenState());

		CoreInfo.platform = bootstrap.getPlatform();
		CoreInfo.LWJGLVer = Version.getVersion();
		CoreInfo.GLFWVer = GLFW.glfwGetVersionString();
		CoreInfo.OpenGLVer = glGetString(GL_VERSION);
		CoreInfo.GLSLVersion = glGetString(GL_SHADING_LANGUAGE_VERSION);
		CoreInfo.Vendor = glGetString(GL_VENDOR);
		CoreInfo.Renderer = glGetString(GL_RENDERER);
		Logger.log("Voxel Client Version: " + ClientVariables.version);
		Logger.log("Running on: " + CoreInfo.platform);
		Logger.log("LWJGL Version: " + CoreInfo.LWJGLVer);
		Logger.log("GLFW Version: " + CoreInfo.GLFWVer);
		Logger.log("OpenGL Version: " + CoreInfo.OpenGLVer);
		Logger.log("GLSL Version: " + CoreInfo.GLSLVersion);
		Logger.log("Assimp: " + aiGetVersionMajor() + "." + aiGetVersionMinor() + "." + aiGetVersionRevision());
		Logger.log("Vendor: " + CoreInfo.Vendor);
		Logger.log("Renderer: " + CoreInfo.Renderer);
		modsHandler = new ModsHandler(this);
		modsHandler.setMoltenAPI(new MoltenAPI());
		modsHandler.preInit();

		internalSubsystem.init();
		TaskManager.addTask(() -> StateMachine.registerState(new MainMenuState()));
		TaskManager.addTask(() -> StateMachine.registerState(new SPWorldState()));
		TaskManager.addTask(() -> StateMachine.registerState(new MPWorldState()));
		if (ClientVariables.TEST_MODE)
			TaskManager.addTask(() -> StateMachine.registerState(new TestState()));
		modsHandler.init();
		modsHandler.postInit();
		internalSubsystem.postInit();
		StateMachine.setCurrentState(StateNames.SPLASH_SCREEN);
		try {
			StateMachine.run();
			update();
			dispose();
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			handleError(t);
		}
	}

	@Override
	public void restart() {
		throw new UnsupportedOperationException("Not Implemented");
	}

	/**
	 * Main Loop
	 */
	@Override
	public void update() {
		float delta = 0;
		float accumulator = 0f;
		float interval = 1f / (int) REGISTRY.getRegistryItem("/Voxel/Settings/Core/ups");
		float alpha = 0;
		int fps = (int) REGISTRY.getRegistryItem("/Voxel/Settings/Core/fps");
		Window window = GraphicalSubsystem.getMainWindow();
		while (StateMachine.isRunning() && !(window.isCloseRequested())) {
			Timers.startCPUTimer();
			TaskManager.update();
			if (window.getTimeCount() > 1f) {
				CoreInfo.ups = CoreInfo.upsCount;
				CoreInfo.upsCount = 0;
				window.setTimeCount(window.getTimeCount() - 1);
			}
			delta = window.getDelta();
			accumulator += delta;
			while (accumulator >= interval) {
				WindowManager.update();
				CoreInfo.upsCount++;
				StateMachine.update(this, interval);
				accumulator -= interval;
			}
			alpha = accumulator / interval;
			Timers.stopCPUTimer();
			Timers.startGPUTimer();
			StateMachine.render(this, alpha);
			window.updateDisplay(fps);
			Timers.stopGPUTimer();
			Timers.update();
		}
	}

	/**
	 * Handle all errors
	 * 
	 * @param e
	 *            Throwable
	 */
	@Override
	public void handleError(Throwable e) {
		CrashState.t = e;
		StateMachine.registerState(new CrashState());
		if (!StateMachine.isRunning())
			StateMachine.run();
		StateMachine.setCurrentState(StateNames.CRASH);
		Mouse.setGrabbed(false);
		update();
		dispose();
		GLFW.glfwTerminate();
	}

	/**
	 * Dispose method, all loaded stuff is cleaned
	 * 
	 */
	@Override
	public void dispose() {
		super.dispose();
		internalSubsystem.dispose();
		modsHandler.dispose();
		GLFW.glfwSetErrorCallback(null).free();
		StateMachine.dispose();
		GLFW.glfwTerminate();
	}

}
