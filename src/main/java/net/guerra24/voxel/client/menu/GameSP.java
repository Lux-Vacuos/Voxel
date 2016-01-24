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

import net.guerra24.voxel.client.core.CoreInfo;
import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.graphics.VectorsRendering;
import net.guerra24.voxel.client.graphics.nanovg.Timers;
import net.guerra24.voxel.client.input.Mouse;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.world.IWorld;
import net.guerra24.voxel.client.world.block.Block;

public class GameSP {
	private float xScale, yScale;

	private int ypos;

	private BlockGui[] blocks;

	private byte block;

	private float x, y, w, h;

	public GameSP(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		blocks = new BlockGui[10];
		blocks[0] = new BlockGui(Block.Stone.getId(), gm.getLoader().loadNVGTexture("Stone"));
		blocks[1] = new BlockGui(Block.Dirt.getId(), gm.getLoader().loadNVGTexture("Dirt"));
		blocks[3] = new BlockGui(Block.Glass.getId(), gm.getLoader().loadNVGTexture("Glass"));
		blocks[4] = new BlockGui(Block.Torch.getId(), gm.getLoader().loadNVGTexture("Torch"));
		blocks[5] = new BlockGui(Block.Water.getId(), gm.getLoader().loadNVGTexture("Water"));
		blocks[6] = new BlockGui(Block.Cobblestone.getId(), gm.getLoader().loadNVGTexture("Cobblestone"));
		x = gm.getDisplay().getDisplayWidth() / 2;
		y = gm.getDisplay().getDisplayHeight() / 2;
		w = 16;
		h = 16;
	}

	public void render(GameResources gm, IWorld world) {
		if (!VoxelVariables.hideHud)
			renderHud(gm);

		if (VoxelVariables.debug) {
			VectorsRendering.renderText(
					"Voxel " + VoxelVariables.version + " (" + VoxelVariables.state + "-Build " + VoxelVariables.build
							+ ")",
					"Roboto-Bold", 5 * xScale, 12 * yScale, 25 * yScale,
					VectorsRendering.rgba(160, 160, 160, 200, VectorsRendering.colorA),
					VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB));
			VectorsRendering.renderText("Used VRam: " + gm.getDisplay().getUsedVRAM() + "KB " + " UPS: " + CoreInfo.ups,
					"Roboto-Bold", 5 * xScale, 100 * yScale, 25 * yScale,
					VectorsRendering.rgba(160, 160, 160, 200, VectorsRendering.colorA),
					VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB));
			VectorsRendering.renderText(
					"Loaded Chunks: " + world.getLoadedChunks() + "   Rendered Chunks: " + world.getRenderedChunks(),
					"Roboto-Bold", 5 * xScale, 120 * yScale, 25 * yScale,
					VectorsRendering.rgba(160, 160, 160, 200, VectorsRendering.colorA),
					VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB));
			VectorsRendering.renderText(
					"Position XYZ:  " + gm.getCamera().getPosition().getX() + "  " + gm.getCamera().getPosition().getY()
							+ "  " + gm.getCamera().getPosition().getZ(),
					"Roboto-Bold", 5 * xScale, 142 * yScale, 25 * yScale,
					VectorsRendering.rgba(160, 160, 160, 200, VectorsRendering.colorA),
					VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB));
			VectorsRendering.renderText(
					"Pitch:  " + gm.getCamera().getPitch() + "   Yaw: " + gm.getCamera().getYaw() + "   Roll: "
							+ gm.getCamera().getRoll(),
					"Roboto-Bold", 5 * xScale, 164 * yScale, 25 * yScale,
					VectorsRendering.rgba(160, 160, 160, 200, VectorsRendering.colorA),
					VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB));
			Timers.renderDebugDisplay(5 * xScale, 24 * yScale, 300 * xScale, 55 * yScale);
		}
	}

	private void renderHud(GameResources gm) {
		for (int i = 0; i < 10; i++) {
			VectorsRendering.renderBox(5 * xScale, 5 + i * 64 * yScale, 60 * xScale, 60 * yScale,
					VectorsRendering.rgba(255, 255, 255, 100, VectorsRendering.colorA),
					VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB),
					VectorsRendering.rgba(0, 0, 0, 255, VectorsRendering.colorC));
			if (blocks[i] != null)
				VectorsRendering.renderImage(5 * xScale, 5 + i * 64 * yScale, 60 * xScale, 60 * yScale,
						blocks[i].getTex(), 1f);
		}
		ypos -= Mouse.getDWheel();

		if (ypos > 9)
			ypos = 0;
		if (ypos < 0)
			ypos = 9;
		VectorsRendering.renderBox(5 * xScale, 5 + ypos * 64 * yScale, 60 * xScale, 60 * yScale,
				VectorsRendering.rgba(255, 255, 255, 100, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 100, VectorsRendering.colorB),
				VectorsRendering.rgba(0, 0, 0, 100, VectorsRendering.colorC));
		if (blocks[ypos] != null)
			block = blocks[ypos].getId();
		else
			block = 0;
		VectorsRendering.renderBox(x - 8, y - 8, w, h,
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB),
				VectorsRendering.rgba(0, 0, 0, 255, VectorsRendering.colorC));
	}

	public byte getBlock() {
		return block;
	}

}
