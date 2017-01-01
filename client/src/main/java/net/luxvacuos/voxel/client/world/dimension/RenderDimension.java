/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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
import net.luxvacuos.voxel.client.world.chunks.ClientChunkManager;
import net.luxvacuos.voxel.client.world.chunks.RenderChunk;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.world.IWorld;
import net.luxvacuos.voxel.universal.world.chunk.IChunk;
import net.luxvacuos.voxel.universal.world.chunk.generator.ChunkTerrainGenerator;
import net.luxvacuos.voxel.universal.world.chunk.generator.SimplexNoise;
import net.luxvacuos.voxel.universal.world.dimension.Dimension;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class RenderDimension extends Dimension {

	private int maxLoadChunks = 3;
	private int renderedChunks = 0;

	public RenderDimension(IWorld world, int id) {
		super(world, id);
		this.chunkManager = new ClientChunkManager(this);
		Array<ChunkNode> nodes = new Array<>(ChunkNode.class);
		for (int x = 0; x < maxLoadChunks; x++) {
			for (int z = 0; z < maxLoadChunks; z++) {
				nodes.add(new ChunkNode(x, 0, z));
			}
		}

		this.chunkManager.batchLoadChunks(nodes.toArray());
		
		ChunkTerrainGenerator gen = new ChunkTerrainGenerator();
		gen.setNoiseGenerator(new SimplexNoise(256, 0.15f, 0));
		this.chunkManager.setGenerator(gen);
	}

	public void render(Camera camera, Camera sunCamera, ClientWorldSimulation clientWorldSimulation, Frustum frustum,
			int shadowMap) {
		this.renderedChunks = 0;
		for (IChunk chunk : this.chunkManager.getLoadedChunks()) {
			if (frustum.cubeInFrustum(chunk.getX() * 16, 0, chunk.getZ() * 16, chunk.getX() * 16 + 16, 256,
					chunk.getZ() * 16 + 16)) {
				this.renderedChunks++;
				((RenderChunk) chunk).render(camera, sunCamera, clientWorldSimulation, shadowMap);
			}
		}
	}

	public void renderOcclusion(Camera camera, Frustum frustum) {
		for (IChunk chunk : this.chunkManager.getLoadedChunks()) {
			if (frustum.cubeInFrustum(chunk.getX() * 16, 0, chunk.getZ() * 16, chunk.getX() * 16 + 16, 256,
					chunk.getZ() * 16 + 16)) {
				((RenderChunk) chunk).renderOcclusion(camera);
			}
		}
	}

	public void renderShadow(Camera sunCamera, Frustum frustum) {
		for (IChunk chunk : this.chunkManager.getLoadedChunks()) {
			if (frustum.cubeInFrustum(chunk.getX() * 16, 0, chunk.getZ() * 16, chunk.getX() * 16 + 16, 256,
					chunk.getZ() * 16 + 16)) {
				((RenderChunk) chunk).renderShadow(sunCamera);
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
