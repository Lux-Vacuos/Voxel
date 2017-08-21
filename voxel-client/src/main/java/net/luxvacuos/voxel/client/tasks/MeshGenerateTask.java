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

package net.luxvacuos.voxel.client.tasks;

import java.util.concurrent.Callable;

import net.luxvacuos.voxel.client.rendering.world.block.ICustomRenderBlock;
import net.luxvacuos.voxel.client.rendering.world.chunk.IRenderChunk;
import net.luxvacuos.voxel.client.world.block.RenderBlock;
import net.luxvacuos.voxel.universal.world.utils.BlockFace;

public class MeshGenerateTask implements Callable<IRenderChunk> {
	private final IRenderChunk chunk;

	public MeshGenerateTask(IRenderChunk chunk) {
		this.chunk = chunk;
	}

	@Override
	public IRenderChunk call() throws Exception {
		while(this.chunk.getTessellator() == null)
			Thread.sleep(100);
			
		this.chunk.getTessellator().begin();
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 256; y++) {
					RenderBlock block = (RenderBlock) this.chunk.getChunkData().getBlockAt(x, y, z);
					if (block.isVisible()) {
						if (!block.hasCustomModel()) {
							this.chunk.getTessellator().generateCube(this.chunk.getX() * 16 + x, y,
									this.chunk.getZ() * 16 + z, 1, cullFace(block, BlockFace.UP, x, y, z),
									cullFace(block, BlockFace.DOWN, x, y, z), cullFace(block, BlockFace.EAST, x, y, z),
									cullFace(block, BlockFace.WEST, x, y, z), cullFace(block, BlockFace.NORTH, x, y, z),
									cullFace(block, BlockFace.SOUTH, x, y, z), block);
						} else {
							((ICustomRenderBlock) block).generateCustomModel(this.chunk.getTessellator(),
									this.chunk.getX() * 16 + x, y, this.chunk.getZ() * 16 + z, 1,
									cullFace(block, BlockFace.UP, x, y, z), cullFace(block, BlockFace.DOWN, x, y, z),
									cullFace(block, BlockFace.EAST, x, y, z), cullFace(block, BlockFace.WEST, x, y, z),
									cullFace(block, BlockFace.NORTH, x, y, z),
									cullFace(block, BlockFace.SOUTH, x, y, z));
						}
					}

				}
			}
		}

		this.chunk.getTessellator().end();

		return this.chunk;
	}

	private boolean cullFace(RenderBlock block, BlockFace face, int x, int y, int z) {
		RenderBlock b;
		if (this.isBlockOutside(face, x, y, z)) {
			b = ((RenderBlock) this.chunk.getChunkData().getBlockAt(x + face.getModX(), y + face.getModY(),
					z + face.getModZ()));
			if (b.getID() == block.getID())
				return false;
			if (b.isTransparent() || b.hasCustomModel() || b.isFluid())
				return true;
		}
		int cx = this.chunk.getX() * 16 + x;
		int cz = this.chunk.getZ() * 16 + z;
		b = ((RenderBlock) this.chunk.getDimension().getBlockAt(cx + face.getModX(), y + face.getModY(),
				cz + face.getModZ()));

		if (b == null || b.getID() == block.getID())
			return false;
		if (b.isTransparent() || b.hasCustomModel() || b.isFluid())
			return true;
		return false;
	}

	private boolean isBlockOutside(BlockFace face, int x, int y, int z) {
		switch (face) {
		case WEST:
			return (x > 1 && x < 16);
		case DOWN:
			return (y > 1 && y < 256);
		case NORTH:
			return (z > 1 && z < 16);
		case EAST:
			return (x > 0 && x < 15);
		case UP:
			return (y > 0 && y < 255);
		case SOUTH:
			return (z > 0 && z < 15);
		default:
			return false;
		}
	}

}
