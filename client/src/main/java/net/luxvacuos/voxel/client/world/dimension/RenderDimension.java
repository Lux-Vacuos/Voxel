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
import com.hackhalo2.nbt.tags.TagCompound;

import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.rendering.api.opengl.Frustum;
import net.luxvacuos.voxel.client.rendering.api.opengl.ShadowFBO;
import net.luxvacuos.voxel.client.rendering.world.dimension.IRenderDimension;
import net.luxvacuos.voxel.client.world.chunks.ClientChunkManager;
import net.luxvacuos.voxel.client.world.chunks.RenderChunk;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.ChunkLoader;
import net.luxvacuos.voxel.universal.world.IWorld;
import net.luxvacuos.voxel.universal.world.chunk.FutureChunk;
import net.luxvacuos.voxel.universal.world.chunk.IChunk;
import net.luxvacuos.voxel.universal.world.chunk.generator.ChunkTerrainGenerator;
import net.luxvacuos.voxel.universal.world.chunk.generator.SimplexNoise;
import net.luxvacuos.voxel.universal.world.dimension.Dimension;

public class RenderDimension extends Dimension implements IRenderDimension {

	private int renderedChunks = 0;

	public RenderDimension(IWorld world, TagCompound data, int id) {
		super(world, data, id);
	}

	@Override
	protected void setupChunkManager(Random rgn) {
		this.chunkManager = new ClientChunkManager(this);
		ChunkTerrainGenerator gen = new ChunkTerrainGenerator();
		gen.setNoiseGenerator(new SimplexNoise(256, 0.15f, rgn.nextInt()));
		this.chunkManager.setGenerator(gen);
	}

	@Override
	protected void setupWorldSimulator() {
		this.worldSimulation = new ClientWorldSimulation(6500);
	}

	@Override
	public void render(Camera camera, Camera sunCamera, Frustum frustum, ShadowFBO shadow) {
		this.renderedChunks = 0;
		BoundingBox aabb;
		RenderChunk rChunk;
		int entityCX = 0, entityCZ = 0;

		ChunkLoader loader = Components.CHUNK_LOADER.get(camera);

		if (camera.getPosition().x < 0)
			entityCX = (int) ((camera.getPosition().x - 16) / 16);
		else
			entityCX = (int) ((camera.getPosition().x) / 16);

		if (camera.getPosition().z < 0)
			entityCZ = (int) ((camera.getPosition().z - 16) / 16);
		else
			entityCZ = (int) ((camera.getPosition().z) / 16);

		for (IChunk chunk : this.chunkManager.getLoadedChunks()) {
			if (chunk instanceof FutureChunk)
				continue;

			aabb = chunk.getBoundingBox(chunk.getNode());
			if (Math.abs(chunk.getNode().getX() - entityCX) < loader.getChunkRadius()
					&& Math.abs(chunk.getNode().getZ() - entityCZ) < loader.getChunkRadius())
				if (frustum.cubeInFrustum((float) aabb.min.x, (float) aabb.min.y, (float) aabb.min.z,
						(float) aabb.max.x, (float) aabb.max.y, (float) aabb.max.z)) {
					rChunk = (RenderChunk) chunk;
					if (rChunk.needsMeshRebuild())
						((ClientChunkManager) this.chunkManager).generateChunkMesh(rChunk);

					this.renderedChunks++;
					rChunk.render(camera, sunCamera, this.getWorldSimulator(), shadow);
				}

		}
	}

	@Override
	public void renderOcclusion(Camera camera, Frustum frustum) {
		BoundingBox aabb;
		for (IChunk chunk : this.chunkManager.getLoadedChunks()) {
			if (chunk instanceof FutureChunk)
				continue;
			aabb = chunk.getBoundingBox(chunk.getNode());

			if (frustum.cubeInFrustum((float) aabb.min.x, (float) aabb.min.y, (float) aabb.min.z, (float) aabb.max.x,
					(float) aabb.max.y, (float) aabb.max.z)) {
				((RenderChunk) chunk).renderOcclusion(camera);
			}
		}
	}

	@Override
	public void renderShadow(Camera sunCamera, Frustum frustum) {
		BoundingBox aabb;
		for (IChunk chunk : this.chunkManager.getLoadedChunks()) {
			if (chunk instanceof FutureChunk)
				continue;
			aabb = chunk.getBoundingBox(chunk.getNode());

			if (frustum.cubeInFrustum((float) aabb.min.x, (float) aabb.min.y, (float) aabb.min.z, (float) aabb.max.x,
					(float) aabb.max.y, (float) aabb.max.z)) {
				((RenderChunk) chunk).renderShadow(sunCamera);
			}
		}
	}

	public int getRenderedChunks() {
		return renderedChunks;
	}

	@Override
	public ClientWorldSimulation getWorldSimulator() {
		return (ClientWorldSimulation) this.worldSimulation;
	}

}
