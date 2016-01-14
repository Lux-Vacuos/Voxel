/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
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

package net.guerra24.voxel.client.menu;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.graphics.MenuRendering;
import net.guerra24.voxel.client.graphics.opengl.Display;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.world.IWorld;

public class GameSP {
	private float xScale, yScale;

	public GameSP(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
	}

	public void render(GameResources gm, IWorld world) {
		if (VoxelVariables.debug) {
			MenuRendering.renderText(
					"Voxel " + VoxelVariables.version + " (" + VoxelVariables.state + "-Build " + VoxelVariables.build
							+ ")",
					"Roboto-Bold", 5 * xScale, 12 * yScale, 20,
					MenuRendering.rgba(160, 160, 160, 200, MenuRendering.colorA),
					MenuRendering.rgba(255, 255, 255, 255, MenuRendering.colorB));
			MenuRendering.renderText("FPS: " + Display.fps + " UPS: " + Display.ups, "Roboto-Bold", 5 * xScale,
					32 * yScale, 20, MenuRendering.rgba(160, 160, 160, 200, MenuRendering.colorA),
					MenuRendering.rgba(255, 255, 255, 255, MenuRendering.colorB));
			MenuRendering.renderText(
					"Loaded Chunks: " + world.getLoadedChunks() + " Rendered Chunks: " + world.getRenderedChunks(),
					"Roboto-Bold", 5 * xScale, 52 * yScale, 20,
					MenuRendering.rgba(160, 160, 160, 200, MenuRendering.colorA),
					MenuRendering.rgba(255, 255, 255, 255, MenuRendering.colorB));
			MenuRendering.renderText(
					"Position XYZ: " + gm.getCamera().getPosition().getX() + " " + gm.getCamera().getPosition().getY()
							+ " " + gm.getCamera().getPosition().getZ(),
					"Roboto-Bold", 5 * xScale, 142 * yScale, 20,
					MenuRendering.rgba(160, 160, 160, 200, MenuRendering.colorA),
					MenuRendering.rgba(255, 255, 255, 255, MenuRendering.colorB));
			MenuRendering.renderText(
					"Pitch: " + gm.getCamera().getPitch() + " Yaw: " + gm.getCamera().getYaw() + " Roll: "
							+ gm.getCamera().getRoll(),
					"Roboto-Bold", 5 * xScale, 164 * yScale, 20,
					MenuRendering.rgba(160, 160, 160, 200, MenuRendering.colorA),
					MenuRendering.rgba(255, 255, 255, 255, MenuRendering.colorB));
		}
	}

}
