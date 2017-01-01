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

package net.luxvacuos.voxel.client.world.dimension;

import com.badlogic.gdx.utils.Array;

import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.rendering.api.opengl.Frustum;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.world.chunks.ClientChunkManager;
import net.luxvacuos.voxel.client.world.chunks.RenderChunk;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.world.IWorld;
import net.luxvacuos.voxel.universal.world.chunk.IChunk;
import net.luxvacuos.voxel.universal.world.dimension.Dimension;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class RenderDimension extends Dimension {

	//private RenderChunk[][] renderChunks;
	private int maxLoadChunks = 9; //32
	private int renderedChunks = 0;

	public RenderDimension(IWorld world, int id, Camera camera, Camera sunCamera, ResourceLoader loader) {
		super(world, id);
		this.chunkManager = new ClientChunkManager(this);
		//renderChunks = new RenderChunk[maxRenderChunks][maxRenderChunks];
		Array<ChunkNode> nodes = new Array<>(ChunkNode.class);
		for (int x = 0; x < maxLoadChunks; x++) {
			for (int z = 0; z < maxLoadChunks; z++) {
				//renderChunks[x][z] = new RenderChunk(this);
				nodes.add(new ChunkNode(x, 0, z));
			}
		}
		
		this.chunkManager.batchLoadChunks(nodes.toArray());
	}

	public void render(Camera camera, Camera sunCamera, ClientWorldSimulation clientWorldSimulation, Frustum frustum,
			int shadowMap) {
		/*for (IChunk chunk : super.chunkManager.getLoadedChunks()) {
			renderChunks[chunk.getX() + maxRenderChunks / 2][chunk.getZ() + maxRenderChunks / 2].setChunk(chunk);
		}*/
		this.renderedChunks = 0;
		/*for (int x = 0; x < maxRenderChunks; x++) {
			for (int z = 0; z < maxRenderChunks; z++) {
				ChunkNode node = renderChunks[x][z].getNode();
				if (node == null)
					continue;
				if (frustum.cubeInFrustum(node.getX() * 16, 0, node.getZ() * 16, node.getX() * 16 + 16, 256,
						node.getZ() * 16 + 16)) {
					renderedChunks++;
					renderChunks[x][z].render(camera, sunCamera, clientWorldSimulation, shadowMap);
				}
			}
		} */
		for(IChunk chunk : this.chunkManager.getLoadedChunks()) {
			if(frustum.cubeInFrustum(chunk.getX() * 16, 0, chunk.getZ() * 16, chunk.getX() * 16 + 16, 256,
					chunk.getZ() * 16 + 16)) {
				this.renderedChunks++;
				((RenderChunk)chunk).render(camera, sunCamera, clientWorldSimulation, shadowMap);
			}
		}
	}

	public void renderOcclusion(Camera camera, Frustum frustum) {
		/* for (int x = 0; x < maxLoadChunks; x++) {
			for (int z = 0; z < maxLoadChunks; z++) {
				ChunkNode node = renderChunks[x][z].getNode();
				if (node == null)
					continue;
				if (frustum.cubeInFrustum(node.getX() * 16, 0, node.getZ() * 16, node.getX() * 16 + 16, 256,
						node.getZ() * 16 + 16))
					renderChunks[x][z].renderOcclusion(camera);
			}
		} */
		for(IChunk chunk : this.chunkManager.getLoadedChunks()) {
			if(frustum.cubeInFrustum(chunk.getX() * 16, 0, chunk.getZ() * 16, chunk.getX() * 16 + 16, 256,
					chunk.getZ() * 16 + 16)) {
				((RenderChunk)chunk).renderOcclusion(camera);
			}
		}
	}

	public void renderShadow(Camera sunCamera, Frustum frustum) {
		/* for (int x = 0; x < maxLoadChunks; x++) {
			for (int z = 0; z < maxLoadChunks; z++) {
				ChunkNode node = renderChunks[x][z].getNode();
				if (node == null)
					continue;
				if (frustum.cubeInFrustum(node.getX() * 16, 0, node.getZ() * 16, node.getX() * 16 + 16, 256,
						node.getZ() * 16 + 16))
					renderChunks[x][z].renderShadow(sunCamera);
			}
		} */
		for(IChunk chunk : this.chunkManager.getLoadedChunks()) {
			if(frustum.cubeInFrustum(chunk.getX() * 16, 0, chunk.getZ() * 16, chunk.getX() * 16 + 16, 256,
					chunk.getZ() * 16 + 16)) {
				((RenderChunk)chunk).renderShadow(sunCamera);
			}
		}
	}

	public int getRenderedChunks() {
		return renderedChunks;
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
