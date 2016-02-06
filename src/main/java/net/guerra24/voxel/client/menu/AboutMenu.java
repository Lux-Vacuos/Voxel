package net.guerra24.voxel.client.menu;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;

import net.guerra24.voxel.client.core.CoreInfo;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.graphics.VectorsRendering;
import net.guerra24.voxel.client.input.Mouse;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.universal.util.vector.Vector2f;

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
		VectorsRendering.renderText("Ashley - Ashley is license under the Apache2 License.",
				"Roboto-Regular", 100 * xScale, (1280 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Sound System - Sound System is license under the Sound System License.",
				"Roboto-Regular", 100 * xScale, (1480 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));		
		VectorsRendering.renderText("Log4J - Log4j is license under the Apache2.",
				"Roboto-Regular", 100 * xScale, (1680 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Kryo - Kryo is license under the BSD License.",
				"Roboto-Regular", 100 * xScale, (1880 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("KryoNet - KryoNet is license under the BSD License.",
				"Roboto-Regular", 100 * xScale, (2080 + globalY) * yScale, 40 * yScale,
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
		VectorsRendering.renderText(CoreInfo.platform.toString(), "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * xScale, (330 + globalY) * yScale, 40 * yScale,
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
		VectorsRendering.renderText("Vendor ", "Roboto-Bold", 100 * xScale, (450 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.Vendor, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * xScale, (450 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText("Renderer ", "Roboto-Bold", 100 * xScale, (480 + globalY) * yScale, 40 * yScale,
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 160, VectorsRendering.colorB));
		VectorsRendering.renderText(CoreInfo.Renderer, "Roboto-Regular", NVG_ALIGN_RIGHT | NVG_ALIGN_MIDDLE,
				1180 * xScale, (480 + globalY) * yScale, 40 * yScale,
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
