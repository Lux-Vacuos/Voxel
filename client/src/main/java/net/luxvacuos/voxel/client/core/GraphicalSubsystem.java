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
import static org.lwjgl.glfw.GLFW.glfwInit;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Icon;
import net.luxvacuos.voxel.client.rendering.api.glfw.PixelBufferHandle;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowHandle;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.IWindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NanoWindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.universal.core.ISubsystem;

public class GraphicalSubsystem implements ISubsystem {

	private static IWindowManager windowManager;
	private static Window window;

	@Override
	public void init() {
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		Icon[] icons = new Icon[] { new Icon("icon32"), new Icon("icon64") };

		WindowHandle handle = WindowManager.generateHandle((int) REGISTRY.getRegistryItem("/Voxel/Display/width"),
				(int) REGISTRY.getRegistryItem("/Voxel/Display/height"), "Voxel");
		handle.canResize(false).isVisible(false).setIcon(icons).setCursor("arrow").useDebugContext(true);
		PixelBufferHandle pb = new PixelBufferHandle();
		pb.setSrgbCapable(1);
		handle.setPixelBuffer(pb);
		long gameWindowID = WindowManager.createWindow(handle,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/vsync"));
		window = WindowManager.getWindow(gameWindowID);
		Mouse.setWindow(window);
		setWindowManager( new NanoWindowManager(window));

		window.setVisible(true);
		window.updateDisplay((int) REGISTRY.getRegistryItem("/Voxel/Settings/Core/fps"));
		Timers.initDebugDisplay();
		ResourceLoader loader = window.getResourceLoader();
		loader.loadNVGFont("Roboto-Bold", "Roboto-Bold");
		loader.loadNVGFont("Roboto-Regular", "Roboto-Regular");
		loader.loadNVGFont("Poppins-Regular", "Poppins-Regular");
		loader.loadNVGFont("Poppins-Light", "Poppins-Light");
		loader.loadNVGFont("Poppins-Medium", "Poppins-Medium");
		loader.loadNVGFont("Poppins-Bold", "Poppins-Bold");
		loader.loadNVGFont("Poppins-SemiBold", "Poppins-SemiBold");
		loader.loadNVGFont("Entypo", "Entypo", 40);
	}

	@Override
	public void restart() {
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void dispose() {
		windowManager.dispose();
		WindowManager.closeAllDisplays();
	}

	public static void setWindowManager(IWindowManager iwm) {
		if (windowManager != null)
			windowManager.dispose();
		windowManager = iwm;
		Logger.log("Window Manager: " + iwm.getClass().getSimpleName());
	}

	public static IWindowManager getWindowManager() {
		return windowManager;
	}

	public static Window getMainWindow() {
		return window;
	}

}
