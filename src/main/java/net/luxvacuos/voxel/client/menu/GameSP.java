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

package net.luxvacuos.voxel.client.menu;

import net.luxvacuos.voxel.client.core.CoreInfo;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Timers;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.world.IWorld;
import net.luxvacuos.voxel.client.world.block.Block;

public class GameSP {
	private float xScale, yScale;

	private int ypos;

	private BlockGui[] blocks;

	private BlockGui block;

	private float x, y, w, h;

	public GameSP(GameResources gm) {
		float width = VoxelVariables.WIDTH;
		float height = VoxelVariables.HEIGHT;
		yScale = height / 720f;
		xScale = width / 1280f;
		blocks = new BlockGui[10];
		blocks[0] = new BlockGui(Block.Stone.getId(), gm.getLoader().loadNVGTexture("Stone"), 100);
		blocks[1] = new BlockGui(Block.Dirt.getId(), gm.getLoader().loadNVGTexture("Dirt"), 100);
		blocks[2] = new BlockGui(Block.Ice.getId(), gm.getLoader().loadNVGTexture("Ice"), 100);
		blocks[3] = new BlockGui(Block.Glass.getId(), gm.getLoader().loadNVGTexture("Glass"), 100);
		blocks[4] = new BlockGui(Block.Torch.getId(), gm.getLoader().loadNVGTexture("Torch"), 100);
		blocks[5] = new BlockGui(Block.Water.getId(), gm.getLoader().loadNVGTexture("Water"), 100);
		blocks[6] = new BlockGui(Block.Cobblestone.getId(), gm.getLoader().loadNVGTexture("Cobblestone"), 100);
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
			if (blocks[i] != null) {
				VectorsRendering.renderImage(5 * xScale, 5 + i * 64 * yScale, 60 * xScale, 60 * yScale,
						blocks[i].getTex(), 1f);
				VectorsRendering.renderText("" + blocks[i].getTotal(), "Roboto-Bold", 10 * xScale, 39 + i * 64 * yScale,
						20 * yScale, VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
						VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB));
			}
		}
		VectorsRendering.renderBox(5 * xScale, 5 + ypos * 64 * yScale, 60 * xScale, 60 * yScale,
				VectorsRendering.rgba(255, 255, 255, 100, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 100, VectorsRendering.colorB),
				VectorsRendering.rgba(0, 0, 0, 100, VectorsRendering.colorC));
		VectorsRendering.renderBox(x - 8, y - 8, w, h,
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA),
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorB),
				VectorsRendering.rgba(0, 0, 0, 255, VectorsRendering.colorC));
	}

	public void update() {
		ypos -= Mouse.getDWheel();
		if (ypos > 9)
			ypos = 0;
		if (ypos < 0)
			ypos = 9;
		if (blocks[ypos] != null) {
			block = blocks[ypos];
			if (blocks[ypos].getTotal() < 0)
				blocks[ypos] = null;
		} else
			block = null;
	}

	public BlockGui getBlock() {
		return block;
	}

}
