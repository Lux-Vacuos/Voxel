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

package net.luxvacuos.voxel.client.core.subsystems;

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;
import static org.lwjgl.assimp.Assimp.aiGetVersionMajor;
import static org.lwjgl.assimp.Assimp.aiGetVersionMinor;
import static org.lwjgl.assimp.Assimp.aiGetVersionRevision;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.states.SplashScreenState;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Icon;
import net.luxvacuos.voxel.client.rendering.api.glfw.PixelBufferHandle;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowHandle;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.IWindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NanoWindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.nanovg.themes.NanoTheme;
import net.luxvacuos.voxel.client.rendering.api.nanovg.themes.Theme;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleDomain;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.DefaultData;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.ShaderIncludes;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorBasicShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorShader;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.ui.Font;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.universal.core.ISubsystem;
import net.luxvacuos.voxel.universal.core.TaskManager;
import net.luxvacuos.voxel.universal.core.states.StateMachine;

public class GraphicalSubsystem implements ISubsystem {

	private static IWindowManager windowManager;
	private static Window window;

	private Font robotoRegular, robotoBold, poppinsRegular, poppinsLight, poppinsMedium, poppinsBold, poppinsSemiBold,
			entypo;

	@Override
	public void init() {
		REGISTRY.register("/Voxel/Display/width", ClientVariables.WIDTH);
		REGISTRY.register("/Voxel/Display/height", ClientVariables.HEIGHT);

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
		Theme.setTheme(new NanoTheme());
		setWindowManager(new NanoWindowManager(window));

		window.setVisible(true);
		window.updateDisplay((int) REGISTRY.getRegistryItem("/Voxel/Settings/Core/fps"));
		Timers.initDebugDisplay();
		ResourceLoader loader = window.getResourceLoader();
		robotoRegular = loader.loadNVGFont("Roboto-Regular", "Roboto-Regular");
		robotoBold = loader.loadNVGFont("Roboto-Bold", "Roboto-Bold");
		poppinsRegular = loader.loadNVGFont("Poppins-Regular", "Poppins-Regular");
		poppinsLight = loader.loadNVGFont("Poppins-Light", "Poppins-Light");
		poppinsMedium = loader.loadNVGFont("Poppins-Medium", "Poppins-Medium");
		poppinsBold = loader.loadNVGFont("Poppins-Bold", "Poppins-Bold");
		poppinsSemiBold = loader.loadNVGFont("Poppins-SemiBold", "Poppins-SemiBold");
		entypo = loader.loadNVGFont("Entypo", "Entypo", 40);
		TaskManager.addTask(() -> ShaderIncludes.processIncludeFile("common.isl"));
		TaskManager.addTask(() -> ShaderIncludes.processIncludeFile("lighting.isl"));
		TaskManager.addTask(() -> ShaderIncludes.processIncludeFile("materials.isl"));
		TaskManager.addTask(() -> DefaultData.init(loader));
		TaskManager.addTask(() -> ParticleDomain.init());
		TaskManager.addTask(() -> Renderer.init(window));
		TaskManager.addTask(() -> BlocksResources.init(loader));
		TaskManager.addTask(() -> TessellatorShader.getShader());
		TaskManager.addTask(() -> TessellatorBasicShader.getShader());
		StateMachine.registerState(new SplashScreenState());
		REGISTRY.register("/Voxel/System/lwjgl", Version.getVersion());
		REGISTRY.register("/Voxel/System/glfw", GLFW.glfwGetVersionString());
		REGISTRY.register("/Voxel/System/opengl", glGetString(GL_VERSION));
		REGISTRY.register("/Voxel/System/glsl", glGetString(GL_SHADING_LANGUAGE_VERSION));
		REGISTRY.register("/Voxel/System/vendor", glGetString(GL_VENDOR));
		REGISTRY.register("/Voxel/System/renderer", glGetString(GL_RENDERER));
		REGISTRY.register("/Voxel/System/assimp", aiGetVersionMajor() + "." + aiGetVersionMinor() + "." + aiGetVersionRevision());
		REGISTRY.register("/Voxel/System/vk", "Not Available");
	}

	@Override
	public void restart() {
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void dispose() {
		robotoRegular.dispose();
		robotoBold.dispose();
		poppinsRegular.dispose();
		poppinsLight.dispose();
		poppinsMedium.dispose();
		poppinsBold.dispose();
		poppinsSemiBold.dispose();
		entypo.dispose();
		DefaultData.dispose();
		TessellatorShader.getShader().dispose();
		TessellatorBasicShader.getShader().dispose();
		Renderer.cleanUp();
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
