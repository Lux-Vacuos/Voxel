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

package net.luxvacuos.voxel.client.world;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.ashley.core.Engine;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.models.ParticlePoint;
import net.luxvacuos.voxel.client.resources.models.ParticleSystem;
import net.luxvacuos.voxel.client.world.chunks.Chunk;
import net.luxvacuos.voxel.client.world.chunks.ChunkGenerator;
import net.luxvacuos.voxel.client.world.chunks.ChunkNode;

public class ClientDimension extends Dimension {

	public ClientDimension(String name, Random seed, int chunkDim, GameResources gm) {
		super(name, seed, chunkDim, gm);
	}

	@Override
	protected void init(GameResources gm) {
		particleSystem = new ParticleSystem(gm.getTorchTexture(), 2, 1, -0.01f, 4, 0.5f);
		particleSystem.setDirection(new Vector3f(0, 1, 0), 0.1f);
		particleSystem.setLifeError(0.8f);
		particleSystem.setScaleError(0.2f);
		particleSystem.setSpeedError(0.2f);
		seedi = (int) data.getObject("Seed");
		noise = new SimplexNoise(256, 0.15f, seedi);
		lightNodeAdds = new LinkedList<>();
		lightNodeRemovals = new LinkedList<>();
		chunks = new ConcurrentHashMap<>();
		chunkGenerator = new ChunkGenerator();
		physicsEngine = new Engine();
		physicsSystem = new PhysicsSystem(this);
		physicsEngine.addSystem(physicsSystem);
		addQueue = new ConcurrentLinkedQueue<>();
		removeQueue = new ConcurrentLinkedQueue<>();
	}

	@Override
	protected void load() {
	}

	@Override
	protected void save() {
	}

	@Override
	public void updateChunksGeneration(GameResources gm, float delta) {
		if (gm.getCamera().getPosition().x < 0)
			playerCX = (int) ((gm.getCamera().getPosition().x - 16) / 16);
		if (gm.getCamera().getPosition().y < 0)
			playerCY = (int) ((gm.getCamera().getPosition().y - 16) / 16);
		if (gm.getCamera().getPosition().z < 0)
			playerCZ = (int) ((gm.getCamera().getPosition().z - 16) / 16);
		if (gm.getCamera().getPosition().x > 0)
			playerCX = (int) ((gm.getCamera().getPosition().x) / 16);
		if (gm.getCamera().getPosition().y > 0)
			playerCY = (int) ((gm.getCamera().getPosition().y) / 16);
		if (gm.getCamera().getPosition().z > 0)
			playerCZ = (int) ((gm.getCamera().getPosition().z) / 16);
		for (int zr = -VoxelVariables.radius; zr <= VoxelVariables.radius; zr++) {
			int zz = playerCZ + zr;
			for (int xr = -VoxelVariables.radius; xr <= VoxelVariables.radius; xr++) {
				int xx = playerCX + xr;
				for (int yr = -VoxelVariables.radius; yr <= VoxelVariables.radius; yr++) {
					int yy = playerCY + yr;

					if (!hasChunk(xx, yy, zz)) {
						addChunk(new Chunk(new ChunkNode(xx, yy, zz), this));
						addTo(new ChunkNode(xx, yy, zz), addQueue);
					} else if (hasChunk(xx, yy, zz)) {
						Chunk chunk = getChunk(xx, yy, zz);
						if (!chunk.loaded)
							continue;
						chunk.update(this, gm.getCamera(), delta);
						if (gm.getFrustum().cubeInFrustum(chunk.posX, chunk.posY, chunk.posZ, chunk.posX + 16,
								chunk.posY + 16, chunk.posZ + 16)) {
							chunk.rebuildMesh(this);
						}
						for (ParticlePoint particlePoint : chunk.getParticlePoints()) {
							particleSystem.generateParticles(particlePoint, delta);
						}
					}

				}
			}
		}

		for (Chunk chunk : chunks.values()) {
			if (Math.abs(chunk.node.cx - playerCX) > VoxelVariables.radius) {
				addTo(chunk.node, removeQueue);
			} else if (Math.abs(chunk.node.cz - playerCZ) > VoxelVariables.radius) {
				addTo(chunk.node, removeQueue);
			} else if (Math.abs(chunk.node.cy - playerCY) > VoxelVariables.radius) {
				addTo(chunk.node, removeQueue);
			}
		}
		int chunksLoaded = 0;
		int chunksUnloaded = 0;
		while (!removeQueue.isEmpty()) {
			ChunkNode node = removeQueue.poll();
			Chunk chnk = getChunk(node.cx, node.cy, node.cz);
			saveChunk(gm.getKryo(), chnk);
			removeChunk(chnk);
			chunksUnloaded++;
		}

		while (!addQueue.isEmpty()) {
			ChunkNode node = addQueue.poll();
			if (existChunkFile(node.cx, node.cy, node.cz))
				loadChunk(gm.getKryo(), node.cx, node.cy, node.cz);
			else
				getChunk(node.cx, node.cy, node.cz).init(this);
			chunksLoaded++;
		}
	}

}
