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

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.world.chunks.RenderChunk;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.world.IWorld;
import net.luxvacuos.voxel.universal.world.chunk.IChunk;
import net.luxvacuos.voxel.universal.world.dimension.Dimension;
import net.luxvacuos.voxel.universal.world.dimension.PhysicsSystem;

public class RenderDimension extends Dimension {

	private RenderChunk[][] renderChunks;

	public RenderDimension(IWorld world, int id, Camera camera, Camera sunCamera, ResourceLoader loader) {
		super(world, id);
		entitiesManager.getSystem(PhysicsSystem.class)
				.addBox(new BoundingBox(new Vector3(-500, -1, -500), new Vector3(500, 0, 500)));
		renderChunks = new RenderChunk[16][16];
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				renderChunks[x][z] = new RenderChunk();
			}
		}
	}

	public void render(Camera camera, Camera sunCamera, ClientWorldSimulation clientWorldSimulation, int shadowMap) {
		for (IChunk chunk : super.chunkManager.getLoadedChunks()) {
			renderChunks[chunk.getX() + 8][chunk.getZ() + 8].setData(chunk.getChunkData());
			renderChunks[chunk.getX() + 8][chunk.getZ() + 8].setNode(chunk.getNode());
		}
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				renderChunks[x][z].render(camera, sunCamera, clientWorldSimulation, shadowMap);
			}

		}
	}

	@Override
	public void dispose() {
		super.dispose();
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				renderChunks[x][z].dispose();
			}

		}
	}

}
