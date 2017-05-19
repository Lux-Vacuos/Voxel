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

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.bootstrap.Bootstrap;
import net.luxvacuos.voxel.client.core.states.CrashState;
import net.luxvacuos.voxel.client.core.states.MPWorldState;
import net.luxvacuos.voxel.client.core.states.MainMenuState;
import net.luxvacuos.voxel.client.core.states.SPWorldState;
import net.luxvacuos.voxel.client.core.states.StateNames;
import net.luxvacuos.voxel.client.core.states.TestState;
import net.luxvacuos.voxel.client.core.subsystems.ClientCoreSubsystem;
import net.luxvacuos.voxel.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.voxel.client.core.subsystems.SoundSubsystem;
import net.luxvacuos.voxel.client.core.subsystems.WorldSubsystem;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;
import net.luxvacuos.voxel.universal.core.CoreSubsystem;
import net.luxvacuos.voxel.universal.core.EngineType;
import net.luxvacuos.voxel.universal.core.TaskManager;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

public class Voxel extends AbstractVoxel {

	public Voxel() {
		GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		super.engineType = EngineType.CLIENT;
		init();
	}

	/**
	 * Init function
	 */
	@Override
	public void init() {
		Logger.init();
		Logger.log("Starting Client");

		super.addSubsystem(new ClientCoreSubsystem());
		super.addSubsystem(new GraphicalSubsystem());
		if (!ClientVariables.WSL)
			super.addSubsystem(new SoundSubsystem());
		super.addSubsystem(new WorldSubsystem());

		super.initSubsystems();
		
		Logger.log("Voxel Client Version: " + ClientVariables.version);
		Logger.log("Running on: " + Bootstrap.getPlatform());
		Logger.log("LWJGL Version: " + REGISTRY.getRegistryItem("/Voxel/System/lwjgl"));
		Logger.log("GLFW Version: " + REGISTRY.getRegistryItem("/Voxel/System/glfw"));
		Logger.log("OpenGL Version: " + REGISTRY.getRegistryItem("/Voxel/System/opengl"));
		Logger.log("GLSL Version: " + REGISTRY.getRegistryItem("/Voxel/System/glsl"));
		Logger.log("Assimp: " + REGISTRY.getRegistryItem("/Voxel/System/assimp"));
		Logger.log("Vendor: " + REGISTRY.getRegistryItem("/Voxel/System/vendor"));
		Logger.log("Renderer: " + REGISTRY.getRegistryItem("/Voxel/System/renderer"));

		TaskManager.addTask(() -> StateMachine.registerState(new MainMenuState()));
		TaskManager.addTask(() -> StateMachine.registerState(new SPWorldState()));
		TaskManager.addTask(() -> StateMachine.registerState(new MPWorldState()));
		if (ClientVariables.TEST_MODE)
			TaskManager.addTask(() -> StateMachine.registerState(new TestState()));
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
				CoreSubsystem.ups = CoreSubsystem.upsCount;
				CoreSubsystem.upsCount = 0;
				window.setTimeCount(window.getTimeCount() - 1);
			}
			delta = window.getDelta();
			accumulator += delta;
			while (accumulator >= interval) {
				WindowManager.update();
				StateMachine.update(this, interval);
				CoreSubsystem.upsCount++;
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

	@Override
	public void dispose() {
		super.dispose();
		StateMachine.dispose();
		GLFW.glfwTerminate();
	}

}
