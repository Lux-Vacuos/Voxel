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

import java.io.File;

import net.luxvacuos.igl.CustomLog;
import net.luxvacuos.voxel.client.rendering.api.glfw.Icon;
import net.luxvacuos.voxel.client.rendering.api.glfw.PixelBufferHandle;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowHandle;
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.sound.LibraryLWJGLOpenAL;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystem;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystemConfig;
import net.luxvacuos.voxel.client.sound.soundsystem.SoundSystemException;
import net.luxvacuos.voxel.client.sound.soundsystem.codecs.CodecJOgg;
import net.luxvacuos.voxel.client.util.LoggerSoundSystem;
import net.luxvacuos.voxel.universal.core.AbstractInternalSubsystem;

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
		handle.canResize(false).isVisible(false).setIcon(icons).setCursor("normal");
		PixelBufferHandle pb = new PixelBufferHandle();
		pb.setDepthBits(32);
		pb.setRedBits(16);
		pb.setBlueBits(16);
		pb.setGreenBits(16);
		handle.setPixelBuffer(pb);
		this.gameWindowID = WindowManager.createWindow(handle, ClientVariables.VSYNC);
		Window window = WindowManager.getWindow(this.gameWindowID);

		ResourceLoader loader = window.getResourceLoader();
		loader.loadNVGFont("Roboto-Bold", "Roboto-Bold");
		loader.loadNVGFont("Roboto-Regular", "Roboto-Regular");
		loader.loadNVGFont("Entypo", "Entypo", 40);
	}

	@Override
	public void init() {
		CustomLog.getInstance();
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
		} catch (SoundSystemException e) {
			e.printStackTrace();
		}
		SoundSystemConfig.setSoundFilesPackage("assets/sounds/");
		SoundSystemConfig.setLogger(new LoggerSoundSystem());
		soundSystem = new SoundSystem();
	}

	@Override
	public void postInit() {
	}

	/**
	 * Disposes all objects
	 * 
	 */
	@Override
	public void dispose() {
		gameSettings.save();
		soundSystem.cleanup();
	}

	public SoundSystem getSoundSystem() {
		return soundSystem;
	}

	public Window getGameWindow() {
		return WindowManager.getWindow(this.gameWindowID);
	}

}