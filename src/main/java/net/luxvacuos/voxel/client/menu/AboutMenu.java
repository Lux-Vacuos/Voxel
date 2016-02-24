/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Lux Vacuos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.luxvacuos.voxel.client.menu;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;

import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.universal.util.vector.Vector2f;

public class AboutMenu {

	private float xScale, yScale, globalY;
	private Button backButton;

	private int infinityLogo;
	private int voxelLogo;
	private int ashleyLogo;

	public AboutMenu(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		backButton = new Button(new Vector2f(533, 35), new Vector2f(215, 80), xScale, yScale);
		infinityLogo = gm.getLoader().loadNVGTexture("Infinity-Logo");
		voxelLogo = gm.getLoader().loadNVGTexture("Voxel-Logo");
		ashleyLogo = gm.getLoader().loadNVGTexture("Ashley-Logo");

	}

	public void render() {
		VectorsRendering.renderWindow("About", "Roboto-Bold", 20 * xScale, (20 + globalY) * yScale, 1240 * xScale,
				2200 * yScale);
		VectorsRendering.renderImage(440 * xScale, (60 + globalY) * yScale, 400 * xScale, 200 * yScale, voxelLogo, 1);
		VectorsRendering.renderImage(100 * xScale, (660 + globalY) * yScale, 400 * xScale, 200 * yScale, infinityLogo,
				1);
		VectorsRendering.renderImage(100 * xScale, (1070 + globalY) * yScale, 400 * xScale, 200 * yScale, ashleyLogo,
				1);
		VectorsRendering.renderText("Libraries", "Roboto-Regular", 100 * xScale, (600 + globalY) * yScale, 60 * yScale,
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
		VectorsRendering.renderText("This game makes use of the following libraries.", "Roboto-Regular", 100 * xScale,
				(680 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Infinity Game Engine - Infinity is license under the MIT License.",
				"Roboto-Regular", 100 * xScale, (880 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Light Weight Java Game Library 3 - LWJGL is license under the BSD License.",
				"Roboto-Regular", 100 * xScale, (1080 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Ashley - Ashley is license under the Apache2 License.", "Roboto-Regular",
				100 * xScale, (1280 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Sound System - Sound System is license under the Sound System License.",
				"Roboto-Regular", 100 * xScale, (1480 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Log4J - Log4j is license under the Apache2.", "Roboto-Regular", 100 * xScale,
				(1680 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Kryo - Kryo is license under the BSD License.", "Roboto-Regular", 100 * xScale,
				(1880 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("KryoNet - KryoNet is license under the BSD License.", "Roboto-Regular",
				100 * xScale, (2080 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("Version ", "Roboto-Bold", 100 * xScale, (300 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(
				VoxelVariables.version + " (" + VoxelVariables.state + "/Build " + VoxelVariables.build + ")"
						+ " Molten API " + VoxelVariables.apiVersion,
				"Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE, 1180 * xScale, (300 + globalY) * yScale,
				40 * yScale, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("OS ", "Roboto-Bold", 100 * xScale, (330 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.OS, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE, 1180 * xScale,
				(330 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("LWJGL Version ", "Roboto-Bold", 100 * xScale, (360 + globalY) * yScale,
				40 * yScale, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.LWJGLVer, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * xScale, (360 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("GLFW Version ", "Roboto-Bold", 100 * xScale, (390 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.GLFWVer, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * xScale, (390 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("OpenGL Version ", "Roboto-Bold", 100 * xScale, (420 + globalY) * yScale,
				40 * yScale, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.OpenGLVer, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * xScale, (420 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("Vulkan Version ", "Roboto-Bold", 100 * xScale, (450 + globalY) * yScale,
				40 * yScale, VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText(CoreInfo.VkVersion, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * xScale, (450 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("Vendor ", "Roboto-Bold", 100 * xScale, (480 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.Vendor, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * xScale, (480 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));

		VectorsRendering.renderText("Renderer ", "Roboto-Bold", 100 * xScale, (510 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.Renderer, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * xScale, (510 + globalY) * yScale, 40 * yScale,
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
