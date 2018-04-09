/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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

package net.luxvacuos.voxel.client.ui.windows;

import static net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem.LANG;
import static net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem.REGISTRY;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_RIGHT;

import net.luxvacuos.lightengine.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.lightengine.client.ui.Alignment;
import net.luxvacuos.lightengine.client.ui.ComponentWindow;
import net.luxvacuos.lightengine.client.ui.Image;
import net.luxvacuos.lightengine.client.ui.Text;
import net.luxvacuos.lightengine.universal.util.registry.Key;
import net.luxvacuos.lightengine.universal.util.registry.KeyCache;

public class AboutWindow extends ComponentWindow {

	public AboutWindow(int x, int y, int w, int h) {
		super(x, y, w, h, LANG.getRegistryItem("voxel.aboutwindow.name"));
	}

	@Override
	public void initApp() {
		super.setMinWidth(600);
		super.setMinHeight(560);
		super.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);
		Image voxelLogo = new Image(0, -40, 400, 200,
				GraphicalSubsystem.getMainWindow().getResourceLoader().loadNVGTexture("Voxel-Logo"));
		voxelLogo.setAlignment(Alignment.BOTTOM);
		voxelLogo.setWindowAlignment(Alignment.TOP);

		Text versionL = new Text(LANG.getRegistryItem("voxel.aboutwindow.txtversion"), 30, -260);
		versionL.setWindowAlignment(Alignment.LEFT_TOP);
		Text versionR = new Text((String) REGISTRY.getRegistryItem(KeyCache.getKey("/Light Engine/version")), -30,
				-260);
		versionR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		versionR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text osL = new Text(LANG.getRegistryItem("voxel.aboutwindow.txtos"), 30, -290);
		osL.setWindowAlignment(Alignment.LEFT_TOP);
		Text osR = new Text((String) REGISTRY.getRegistryItem(new Key("/Light Engine/System/os")), -30, -290);
		osR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		osR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text lwjglL = new Text(LANG.getRegistryItem("voxel.aboutwindow.txtlwjgl"), 30, -320);
		lwjglL.setWindowAlignment(Alignment.LEFT_TOP);
		Text lwjglR = new Text((String) REGISTRY.getRegistryItem(new Key("/Light Engine/System/lwjgl")), -30, -320);
		lwjglR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		lwjglR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text glfwL = new Text(LANG.getRegistryItem("voxel.aboutwindow.txtglfw"), 30, -350);
		glfwL.setWindowAlignment(Alignment.LEFT_TOP);
		Text glfwR = new Text((String) REGISTRY.getRegistryItem(new Key("/Light Engine/System/glfw")), -30, -350);
		glfwR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		glfwR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text openglL = new Text(LANG.getRegistryItem("voxel.aboutwindow.txtogl"), 30, -380);
		openglL.setWindowAlignment(Alignment.LEFT_TOP);
		Text openglR = new Text((String) REGISTRY.getRegistryItem(new Key("/Light Engine/System/opengl")), -30, -380);
		openglR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		openglR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text glslL = new Text(LANG.getRegistryItem("voxel.aboutwindow.txtglsl"), 30, -410);
		glslL.setWindowAlignment(Alignment.LEFT_TOP);
		Text glslR = new Text((String) REGISTRY.getRegistryItem(new Key("/Light Engine/System/glsl")), -30, -410);
		glslR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		glslR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text vkL = new Text(LANG.getRegistryItem("voxel.aboutwindow.txtvk"), 30, -440);
		vkL.setWindowAlignment(Alignment.LEFT_TOP);
		Text vkR = new Text((String) REGISTRY.getRegistryItem(new Key("/Light Engine/System/vk")), -30, -440);
		vkR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		vkR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text vendorL = new Text(LANG.getRegistryItem("voxel.aboutwindow.txtvendor"), 30, -470);
		vendorL.setWindowAlignment(Alignment.LEFT_TOP);
		Text vendorR = new Text((String) REGISTRY.getRegistryItem(new Key("/Light Engine/System/vendor")), -30, -470);
		vendorR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		vendorR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text rendererL = new Text(LANG.getRegistryItem("voxel.aboutwindow.txtrenderer"), 30, -500);
		rendererL.setWindowAlignment(Alignment.LEFT_TOP);
		Text rendererR = new Text((String) REGISTRY.getRegistryItem(new Key("/Light Engine/System/renderer")), -30,
				-500);
		rendererR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		rendererR.setWindowAlignment(Alignment.RIGHT_TOP);

		super.addComponent(voxelLogo);
		super.addComponent(versionL);
		super.addComponent(versionR);
		super.addComponent(osL);
		super.addComponent(osR);
		super.addComponent(lwjglL);
		super.addComponent(lwjglR);
		super.addComponent(glfwL);
		super.addComponent(glfwR);
		super.addComponent(openglL);
		super.addComponent(openglR);
		super.addComponent(glslL);
		super.addComponent(glslR);
		super.addComponent(vkL);
		super.addComponent(vkR);
		super.addComponent(vendorL);
		super.addComponent(vendorR);
		super.addComponent(rendererL);
		super.addComponent(rendererR);

		super.initApp();
	}

}
