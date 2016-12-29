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

package net.luxvacuos.voxel.client.world.chunks;

import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.client.world.block.RenderBlock;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.world.chunk.ChunkData;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class RenderChunk {

	private ChunkNode node;
	protected volatile ChunkData data;
	private Tessellator tess;

	public RenderChunk() {
		tess = new Tessellator(BlocksResources.getMaterial());
	}

	public void render(Camera camera, Camera sunCamera, ClientWorldSimulation clientWorldSimulation, int shadowMap) {
		if (data == null)
			return;
		tess.begin();
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 16; y++) {
					if (data.getBlockAt(x, y, z).getID() != 0) {
						tess.generateCube(node.getX() * 16 + x, y, node.getZ() * 16 + z, 1, true, false, false, false,
								false, false, ((RenderBlock) data.getBlockAt(x, y, z)));
					}
				}
			}

		}
		tess.end();
		tess.draw(camera, sunCamera, clientWorldSimulation, shadowMap);
	}

	public void setData(ChunkData data) {
		this.data = data;
	}

	public void setNode(ChunkNode node) {
		this.node = node;
	}

	public void dispose() {
		tess.cleanUp();
	}

}
