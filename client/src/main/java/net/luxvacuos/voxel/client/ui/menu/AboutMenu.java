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
		backButton = new Button(new Vector2f(533, 35), new Vector2f(215, 80));
		infinityLogo = gm.getLoader().loadNVGTexture("Infinity-Logo");
		voxelLogo = gm.getLoader().loadNVGTexture("Voxel-Logo");
		ashleyLogo = gm.getLoader().loadNVGTexture("Ashley-Logo");

	}

	public void render() {
		VectorsRendering.renderWindow("About", "Roboto-Bold", 20 * VoxelVariables.XSCALE,
				(20 + globalY) * VoxelVariables.YSCALE, 1240 * VoxelVariables.XSCALE, 2200 * VoxelVariables.YSCALE);
		VectorsRendering.renderImage(440 * VoxelVariables.XSCALE, (60 + globalY) * VoxelVariables.YSCALE,
				400 * VoxelVariables.XSCALE, 200 * VoxelVariables.YSCALE, voxelLogo, 1);
		VectorsRendering.renderImage(100 * VoxelVariables.XSCALE, (660 + globalY) * VoxelVariables.YSCALE,
				400 * VoxelVariables.XSCALE, 200 * VoxelVariables.YSCALE, infinityLogo, 1);
		VectorsRendering.renderImage(100 * VoxelVariables.XSCALE, (1070 + globalY) * VoxelVariables.YSCALE,
				400 * VoxelVariables.XSCALE, 200 * VoxelVariables.YSCALE, ashleyLogo, 1);
		VectorsRendering.renderText("Libraries", "Roboto-Regular", 100 * VoxelVariables.XSCALE,
				(600 + globalY) * VoxelVariables.YSCALE, 60 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
		VectorsRendering.renderText("This game makes use of the following libraries.", "Roboto-Regular",
				100 * VoxelVariables.XSCALE, (680 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Infinity Game Engine - Infinity is licensed under the MIT License.",
				"Roboto-Regular", 100 * VoxelVariables.XSCALE, (880 + globalY) * VoxelVariables.YSCALE,
				40 * VoxelVariables.YSCALE, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Light Weight Java Game Library 3 - LWJGL is licensed under the BSD License.",
				"Roboto-Regular", 100 * VoxelVariables.XSCALE, (1080 + globalY) * VoxelVariables.YSCALE,
				40 * VoxelVariables.YSCALE, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Ashley - Ashley is licensed under the Apache2 License.", "Roboto-Regular",
				100 * VoxelVariables.XSCALE, (1280 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Sound System - Sound System is licensed under the Sound System License.",
				"Roboto-Regular", 100 * VoxelVariables.XSCALE, (1480 + globalY) * VoxelVariables.YSCALE,
				40 * VoxelVariables.YSCALE, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Log4J - Log4j is licensed under the Apache2.", "Roboto-Regular",
				100 * VoxelVariables.XSCALE, (1680 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Kryo - Kryo is licensed under the BSD License.", "Roboto-Regular",
				100 * VoxelVariables.XSCALE, (1880 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("KryoNet - KryoNet is licensed under the BSD License.", "Roboto-Regular",
				100 * VoxelVariables.XSCALE, (2080 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("Version ", "Roboto-Bold", 100 * VoxelVariables.XSCALE,
				(300 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(
				" (" + VoxelVariables.version + ")" + " Molten API" + " (" + MoltenAPI.apiVersion + "/"
						+ MoltenAPI.build + ")",
				"Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE, 1180 * VoxelVariables.XSCALE,
				(300 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("OS ", "Roboto-Bold", 100 * VoxelVariables.XSCALE,
				(330 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.OS, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * VoxelVariables.XSCALE, (330 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("LWJGL Version ", "Roboto-Bold", 100 * VoxelVariables.XSCALE,
				(360 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.LWJGLVer, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * VoxelVariables.XSCALE, (360 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("GLFW Version ", "Roboto-Bold", 100 * VoxelVariables.XSCALE,
				(390 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.GLFWVer, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * VoxelVariables.XSCALE, (390 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("OpenGL Version ", "Roboto-Bold", 100 * VoxelVariables.XSCALE,
				(420 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.OpenGLVer, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * VoxelVariables.XSCALE, (420 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("Vulkan Version ", "Roboto-Bold", 100 * VoxelVariables.XSCALE,
				(450 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText(CoreInfo.VkVersion, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * VoxelVariables.XSCALE, (450 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("Vendor ", "Roboto-Bold", 100 * VoxelVariables.XSCALE,
				(480 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.Vendor, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * VoxelVariables.XSCALE, (480 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("Renderer ", "Roboto-Bold", 100 * VoxelVariables.XSCALE,
				(510 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.Renderer, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * VoxelVariables.XSCALE, (510 + globalY) * VoxelVariables.YSCALE, 40 * VoxelVariables.YSCALE,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
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
