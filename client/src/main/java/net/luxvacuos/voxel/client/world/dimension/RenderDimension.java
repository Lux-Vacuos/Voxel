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

import java.util.Random;

import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.rendering.api.opengl.Frustum;
import net.luxvacuos.voxel.client.world.chunks.ClientChunkManager;
import net.luxvacuos.voxel.client.world.chunks.RenderChunk;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.world.IWorld;
import net.luxvacuos.voxel.universal.world.chunk.FutureChunk;
import net.luxvacuos.voxel.universal.world.chunk.IChunk;
import net.luxvacuos.voxel.universal.world.chunk.generator.ChunkTerrainGenerator;
import net.luxvacuos.voxel.universal.world.chunk.generator.SimplexNoise;
import net.luxvacuos.voxel.universal.world.dimension.Dimension;

public class RenderDimension extends Dimension {

	private int renderedChunks = 0;

	public RenderDimension(IWorld world, int id) {
		super(world, id);
	}
	
	@Override
	protected void setupChunkManager() {
		this.chunkManager = new ClientChunkManager(this);
		ChunkTerrainGenerator gen = new ChunkTerrainGenerator();
		gen.setNoiseGenerator(new SimplexNoise(256, 0.15f, new Random().nextInt()));
		this.chunkManager.setGenerator(gen);
	}

	public void render(Camera camera, Camera sunCamera, ClientWorldSimulation clientWorldSimulation, Frustum frustum,
			int shadowMap) {
		this.renderedChunks = 0;
		BoundingBox aabb;
		RenderChunk rChunk;
		for (IChunk chunk : this.chunkManager.getLoadedChunks()) {
			if(chunk instanceof FutureChunk) continue;
			aabb = chunk.getBoundingBox(chunk.getNode());
			rChunk = (RenderChunk)chunk;
			
			//if(rChunk.needsMeshRebuild())
				//((ClientChunkManager) this.chunkManager).generateChunkMesh(rChunk);
			
			if (frustum.cubeInFrustum((float)aabb.min.x, (float)aabb.min.y, (float)aabb.min.z, 
					(float)aabb.max.x, (float)aabb.max.y, (float)aabb.max.z)) {
				this.renderedChunks++;
				rChunk.render(camera, sunCamera, clientWorldSimulation, shadowMap);
			}
		}
	}

	public void renderOcclusion(Camera camera, Frustum frustum) {
		BoundingBox aabb;
		for (IChunk chunk : this.chunkManager.getLoadedChunks()) {
			if(chunk instanceof FutureChunk) continue;
			aabb = chunk.getBoundingBox(chunk.getNode());
			
			if (frustum.cubeInFrustum((float)aabb.min.x, (float)aabb.min.y, (float)aabb.min.z, 
					(float)aabb.max.x, (float)aabb.max.y, (float)aabb.max.z)) {
				((RenderChunk) chunk).renderOcclusion(camera);
			}
		}
	}

	public void renderShadow(Camera sunCamera, Frustum frustum) {
		BoundingBox aabb;
		for (IChunk chunk : this.chunkManager.getLoadedChunks()) {
			if(chunk instanceof FutureChunk) continue;
			aabb = chunk.getBoundingBox(chunk.getNode());
			
			if (frustum.cubeInFrustum((float)aabb.min.x, (float)aabb.min.y, (float)aabb.min.z, 
					(float)aabb.max.x, (float)aabb.max.y, (float)aabb.max.z)) {
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
