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

package net.luxvacuos.voxel.client.ui.menu;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_RIGHT;

import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.universal.api.MoltenAPI;

public class AboutMenu {

	private float globalY;
	private Button backButton;

	private int infinityLogo;
	private int voxelLogo;
	private int ashleyLogo;

	public AboutMenu(GameResources gm) throws Exception {
		backButton = new Button(new Vector2f(GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 100, 40),
				new Vector2f(200, 40));
		infinityLogo = gm.getLoader().loadNVGTexture("Infinity-Logo");
		voxelLogo = gm.getLoader().loadNVGTexture("Voxel-Logo");
		ashleyLogo = gm.getLoader().loadNVGTexture("Ashley-Logo");

	}

	public void render() {
		VectorsRendering.renderWindow("About", "Roboto-Bold", 20, (20 + globalY),
				GameResources.getInstance().getDisplay().getDisplayWidth() - 40, 2200);
		VectorsRendering.renderImage(GameResources.getInstance().getDisplay().getDisplayWidth() / 2f - 200, (60 + globalY), 400, 200, voxelLogo, 1);
		VectorsRendering.renderImage(60, (660 + globalY), 400, 200, infinityLogo, 1);
		VectorsRendering.renderImage(60, (1070 + globalY), 400, 200, ashleyLogo, 1);

		VectorsRendering.renderText("Libraries", "Roboto-Bold", 60, (600 + globalY), 60,
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
		VectorsRendering.renderText("This game makes use of the following libraries.", "Roboto-Regular", 60,
				(680 + globalY), 30, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Infinity Game Engine - Infinity is licensed under the MIT License.",
				"Roboto-Regular", 60, (880 + globalY), 30,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Light Weight Java Game Library 3 - LWJGL is licensed under the BSD License.",
				"Roboto-Regular", 60, (1080 + globalY), 30,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Ashley - Ashley is licensed under the Apache2 License.", "Roboto-Regular", 60,
				(1280 + globalY), 30, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Sound System - Sound System is licensed under the Sound System License.",
				"Roboto-Regular", 60, (1480 + globalY), 30,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Log4J - Log4j is licensed under the Apache2.", "Roboto-Regular", 60,
				(1680 + globalY), 30, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Kryo - Kryo is licensed under the BSD License.", "Roboto-Regular", 60,
				(1880 + globalY), 30, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("KryoNet - KryoNet is licensed under the BSD License.", "Roboto-Regular", 60,
				(2080 + globalY), 30, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		
		
		VectorsRendering.renderText("Version ", "Roboto-Bold", 60, (300 + globalY), 30,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(
				" (" + VoxelVariables.version + ")" + " Molten API" + " (" + MoltenAPI.apiVersion + "/"
						+ MoltenAPI.build + ")",
				"Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE, GameResources.getInstance().getDisplay().getDisplayWidth() - 60, (300 + globalY), 30,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("OS ", "Roboto-Bold", 60, (330 + globalY), 30,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.OS, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE, GameResources.getInstance().getDisplay().getDisplayWidth() - 60,
				(330 + globalY), 30, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("LWJGL Version ", "Roboto-Bold", 60, (360 + globalY), 30,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.LWJGLVer, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE, GameResources.getInstance().getDisplay().getDisplayWidth() - 60,
				(360 + globalY), 30, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("GLFW Version ", "Roboto-Bold", 60, (390 + globalY), 30,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.GLFWVer, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE, GameResources.getInstance().getDisplay().getDisplayWidth() - 60,
				(390 + globalY), 30, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("OpenGL Version ", "Roboto-Bold", 60, (420 + globalY), 30,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.OpenGLVer, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE, GameResources.getInstance().getDisplay().getDisplayWidth() - 60,
				(420 + globalY), 30, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("Vulkan Version ", "Roboto-Bold", 60, (450 + globalY), 30,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText(CoreInfo.VkVersion, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE, GameResources.getInstance().getDisplay().getDisplayWidth() - 60,
				(450 + globalY), 30, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("Vendor ", "Roboto-Bold", 60, (480 + globalY), 30,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.Vendor, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE, GameResources.getInstance().getDisplay().getDisplayWidth() - 60,
				(480 + globalY), 30, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("Renderer ", "Roboto-Bold", 60, (510 + globalY), 30,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.Renderer, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE, GameResources.getInstance().getDisplay().getDisplayWidth() - 60,
				(510 + globalY), 30, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		backButton.render("Back");
	}

	public void update() {
		globalY += Mouse.getDWheel() * 20f;
		if (globalY > 0)
			globalY = 0;
		else if (globalY < -1520)
			globalY = -1520;
	}

	public Button getBackButton() {
		return backButton;
	}

}
