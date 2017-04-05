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

import java.io.File;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Icon;
import net.luxvacuos.voxel.client.rendering.api.glfw.PixelBufferHandle;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowHandle;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NanoWindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.WM;
import net.luxvacuos.voxel.client.rendering.api.opengl.ParticleDomain;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.DefaultData;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.ShaderIncludes;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorBasicShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.TessellatorShader;
import net.luxvacuos.voxel.client.rendering.utils.BlockFaceAtlas;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.sound.LibraryLWJGLOpenAL;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystem;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystemConfig;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystemException;
import net.luxvacuos.voxel.client.sound.soundsystem.codecs.CodecJOgg;
import net.luxvacuos.voxel.client.util.LoggerSoundSystem;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.client.world.block.RenderBlock;
import net.luxvacuos.voxel.client.world.block.types.WaterBlock;
import net.luxvacuos.voxel.universal.core.AbstractInternalSubsystem;
import net.luxvacuos.voxel.universal.core.TaskManager;
import net.luxvacuos.voxel.universal.material.BlockMaterial;
import net.luxvacuos.voxel.universal.material.MaterialModder;
import net.luxvacuos.voxel.universal.world.block.Blocks;

/**
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class ClientInternalSubsystem extends AbstractInternalSubsystem {

	private static ClientInternalSubsystem instance = null;

	public static ClientInternalSubsystem getInstance() {
		if (instance == null)
			instance = new ClientInternalSubsystem();
		return instance;
	}

	private long gameWindowID;

	private SoundSystem soundSystem;

	private ClientInternalSubsystem() {
	}

	@Override
	public void preInit() {
		gameSettings = new ClientGameSettings();
		gameSettings.load(new File(ClientVariables.SETTINGS_PATH));
		gameSettings.read();

		Icon[] icons = new Icon[] { new Icon("icon32"), new Icon("icon64") };

		WindowHandle handle = WindowManager.generateHandle(ClientVariables.WIDTH, ClientVariables.HEIGHT, "Voxel");
		handle.canResize(false).isVisible(false).setIcon(icons).setCursor("arrow").useDebugContext(true);
		PixelBufferHandle pb = new PixelBufferHandle();
		pb.setSrgbCapable(1);
		handle.setPixelBuffer(pb);
		this.gameWindowID = WindowManager.createWindow(handle, ClientVariables.VSYNC);
		Window window = WindowManager.getWindow(this.gameWindowID);
		Mouse.setWindow(window);
		WM.setWM(new NanoWindowManager(window));

		ResourceLoader loader = window.getResourceLoader();
		loader.loadNVGFont("Roboto-Bold", "Roboto-Bold");
		loader.loadNVGFont("Roboto-Regular", "Roboto-Regular");
		loader.loadNVGFont("Poppins-Regular", "Poppins-Regular");
		loader.loadNVGFont("Poppins-Light", "Poppins-Light");
		loader.loadNVGFont("Poppins-Medium", "Poppins-Medium");
		loader.loadNVGFont("Poppins-Bold", "Poppins-Bold");
		loader.loadNVGFont("Poppins-SemiBold", "Poppins-SemiBold");
		loader.loadNVGFont("Entypo", "Entypo", 40);

		TaskManager.addTask(() -> ShaderIncludes.processIncludeFile("common.isl"));
		TaskManager.addTask(() -> ShaderIncludes.processIncludeFile("lighting.isl"));
		TaskManager.addTask(() -> ShaderIncludes.processIncludeFile("materials.isl"));
		TaskManager.addTask(
				() -> DefaultData.init(ClientInternalSubsystem.getInstance().getGameWindow().getResourceLoader()));
		TaskManager.addTask(() -> ParticleDomain.init());
	}

	@Override
	public void init() {
		if (!ClientVariables.WSL) {
			TaskManager.addTask(() -> {
				try {
					SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
					SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
				} catch (SoundSystemException e) {
					e.printStackTrace();
				}
			});
			SoundSystemConfig.setSoundFilesPackage("assets/" + ClientVariables.assets + "/sounds/");
			SoundSystemConfig.setLogger(new LoggerSoundSystem());
			TaskManager.addTask(() -> soundSystem = new SoundSystem());
		}
		TaskManager.addTask(() -> Renderer.init(getGameWindow()));
		TaskManager.addTask(() -> BlocksResources.init(getGameWindow().getResourceLoader()));
		TaskManager.addTask(() -> {
			MaterialModder matMod = new MaterialModder();
			Blocks.startRegister("voxel");
			BlockMaterial airMat = new BlockMaterial("air");
			airMat = (BlockMaterial) matMod.modify(airMat).canBeBroken(false).setBlocksMovement(false).setOpacity(0f)
					.setVisible(false).done();
			Blocks.register(new RenderBlock(airMat, new BlockFaceAtlas("air")));
			Blocks.register(new RenderBlock(new BlockMaterial("stone"), new BlockFaceAtlas("stone")));
			Blocks.register(
					new RenderBlock(new BlockMaterial("grass"), new BlockFaceAtlas("grass", "dirt", "grassSide")));
			Blocks.register(new RenderBlock(new BlockMaterial("dirt"), new BlockFaceAtlas("dirt")));
			Blocks.register(new RenderBlock(new BlockMaterial("sand"), new BlockFaceAtlas("sand")));
			Blocks.register(new RenderBlock(new BlockMaterial("cobblestone"), new BlockFaceAtlas("cobblestone")));
			Blocks.register(new RenderBlock(new BlockMaterial("wood"), new BlockFaceAtlas("wood")));
			Blocks.register(new RenderBlock(new BlockMaterial("leaves"), new BlockFaceAtlas("leaves")));
			BlockMaterial glassMat = new BlockMaterial("glass");
			glassMat = (BlockMaterial) matMod.modify(glassMat).setOpacity(0.0f).done();
			Blocks.register(new RenderBlock(glassMat, new BlockFaceAtlas("glass")));
			BlockMaterial waterMat = new BlockMaterial("water");
			waterMat = (BlockMaterial) matMod.modify(waterMat).canBeBroken(false).setBlocksMovement(false)
					.affectedByGravity(true).liquid().setOpacity(0.2f).done();
			Blocks.register(new WaterBlock(waterMat, new BlockFaceAtlas("water")));
			Blocks.finishRegister();
		});
	}

	@Override
	public void postInit() {
		TaskManager.addTask(() -> TessellatorShader.getShader());
		TaskManager.addTask(() -> TessellatorBasicShader.getShader());
	}

	/**
	 * Disposes all objects
	 * 
	 */
	@Override
	public void dispose() {
		gameSettings.save();
		if (!ClientVariables.WSL)
			soundSystem.cleanup();
		DefaultData.dispose();
		TessellatorShader.getShader().dispose();
		TessellatorBasicShader.getShader().dispose();
		Renderer.cleanUp();
		WM.getWM().dispose();
	}

	public SoundSystem getSoundSystem() {
		return soundSystem;
	}

	public Window getGameWindow() {
		return WindowManager.getWindow(this.gameWindowID);
	}

}