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

package net.luxvacuos.voxel.client.ui.menus;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_RIGHT;

import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.ui.nextui.Alignment;
import net.luxvacuos.voxel.client.ui.nextui.Image;
import net.luxvacuos.voxel.client.ui.nextui.RootComponent;
import net.luxvacuos.voxel.client.ui.nextui.Text;

public class AboutMenu extends RootComponent {

	public AboutMenu(float x, float y, float w, float h) {
		super(x, y, w, h, "About");
	}

	@Override
	public void initApp(Window window) {
		super.setAlwaysOnTop(true);
		super.setResizable(false);
		super.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);

		Image voxelLogo = new Image(0, -40, 400, 200,
				ClientInternalSubsystem.getInstance().getGameWindow().getResourceLoader().loadNVGTexture("Voxel-Logo"));
		voxelLogo.setAlignment(Alignment.BOTTOM);
		voxelLogo.setWindowAlignment(Alignment.TOP);

		Text versionL = new Text("Version", 30, -260);
		versionL.setFont("Roboto-Bold");
		versionL.setWindowAlignment(Alignment.LEFT_TOP);
		Text versionR = new Text(" (" + ClientVariables.version + ")", -30, -260);
		versionR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		versionR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text osL = new Text("Operative System", 30, -290);
		osL.setFont("Roboto-Bold");
		osL.setWindowAlignment(Alignment.LEFT_TOP);
		Text osR = new Text(CoreInfo.OS, -30, -290);
		osR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		osR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text lwjglL = new Text("LWJGL Version", 30, -320);
		lwjglL.setFont("Roboto-Bold");
		lwjglL.setWindowAlignment(Alignment.LEFT_TOP);
		Text lwjglR = new Text(CoreInfo.LWJGLVer, -30, -320);
		lwjglR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		lwjglR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text glfwL = new Text("GLFW Version", 30, -350);
		glfwL.setFont("Roboto-Bold");
		glfwL.setWindowAlignment(Alignment.LEFT_TOP);
		Text glfwR = new Text(CoreInfo.GLFWVer, -30, -350);
		glfwR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		glfwR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text openglL = new Text("OpenGL Version", 30, -380);
		openglL.setFont("Roboto-Bold");
		openglL.setWindowAlignment(Alignment.LEFT_TOP);
		Text openglR = new Text(CoreInfo.OpenGLVer, -30, -380);
		openglR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		openglR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text glslL = new Text("GLSL Version", 30, -410);
		glslL.setFont("Roboto-Bold");
		glslL.setWindowAlignment(Alignment.LEFT_TOP);
		Text glslR = new Text(CoreInfo.GLSLVersion, -30, -410);
		glslR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		glslR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text vkL = new Text("Vulkan Version", 30, -440);
		vkL.setFont("Roboto-Bold");
		vkL.setWindowAlignment(Alignment.LEFT_TOP);
		Text vkR = new Text(CoreInfo.VkVersion, -30, -440);
		vkR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		vkR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text vendorL = new Text("Vendor", 30, -470);
		vendorL.setFont("Roboto-Bold");
		vendorL.setWindowAlignment(Alignment.LEFT_TOP);
		Text vendorR = new Text(CoreInfo.Vendor, -30, -470);
		vendorR.setAlign(NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE);
		vendorR.setWindowAlignment(Alignment.RIGHT_TOP);

		Text rendererL = new Text("Renderer", 30, -500);
		rendererL.setFont("Roboto-Bold");
		rendererL.setWindowAlignment(Alignment.LEFT_TOP);
		Text rendererR = new Text(CoreInfo.Renderer, -30, -500);
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

		super.initApp(window);
	}

}
