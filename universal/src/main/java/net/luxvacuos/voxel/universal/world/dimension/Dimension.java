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

package net.luxvacuos.voxel.universal.world.dimension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

import net.luxvacuos.voxel.universal.core.IWorldSimulation;
import net.luxvacuos.voxel.universal.core.WorldSimulation;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.ChunkLoader;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.world.IWorld;
import net.luxvacuos.voxel.universal.world.block.Blocks;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.chunk.ChunkManager;
import net.luxvacuos.voxel.universal.world.chunk.IChunk;
import net.luxvacuos.voxel.universal.world.chunk.generator.ChunkTerrainGenerator;
import net.luxvacuos.voxel.universal.world.chunk.generator.SimplexNoise;
import net.luxvacuos.voxel.universal.world.utils.BlockNode;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class Dimension implements IDimension {

	private int id;
	protected IWorld world;
	protected IWorldSimulation worldSimulation;
	protected ChunkManager chunkManager;
	protected Engine entitiesManager;

	public Dimension(IWorld world, int id) {
		this.world = world;
		this.id = id;
		this.setupChunkManager();
		this.setupWorldSimulator();
		this.entitiesManager = new Engine();
		this.entitiesManager.addSystem(new PhysicsSystem(this));
	}

	protected void setupChunkManager() {
		this.chunkManager = new ChunkManager(this);
		ChunkTerrainGenerator gen = new ChunkTerrainGenerator();
		gen.setNoiseGenerator(new SimplexNoise(256, 0.15f, new Random().nextInt()));
		this.chunkManager.setGenerator(gen);
	}
	
	protected void setupWorldSimulator() {
		this.worldSimulation = new WorldSimulation();
	}

	@Override
	public String getWorldName() {
		return world.getName();
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void update(float delta) {
		this.worldSimulation.update(delta);
		int entityCX = 0, entityCZ = 0, chunkRadius = 0;
		ImmutableArray<Entity> players = entitiesManager
				.getEntitiesFor(Family.all(Position.class, ChunkLoader.class).get());
		Array<ChunkNode> toRemove = new Array<>();
		for (Entity entity : players) {
			ChunkLoader loader = Components.CHUNK_LOADER.get(entity);
			Position pos = Components.POSITION.get(entity);

			if (pos.getPosition().x < 0)
				entityCX = (int) ((pos.getPosition().x - 8) / 16);
			else
				entityCX = (int) ((pos.getPosition().x + 8) / 16);

			if (pos.getPosition().z < 0)
				entityCZ = (int) ((pos.getPosition().z - 8) / 16);
			else
				entityCZ = (int) ((pos.getPosition().z + 8) / 16);

			chunkRadius = loader.getChunkRadius();

			ChunkNode node;
			int xx, zz;
			for (int zr = -chunkRadius; zr <= chunkRadius; zr++) {
				zz = entityCZ + zr;
				for (int xr = -chunkRadius; xr <= chunkRadius; xr++) {
					xx = entityCX + xr;
					node = new ChunkNode(xx, zz);
					if (!chunkManager.isChunkLoaded(node))
						chunkManager.loadChunk(node);
					else
						chunkManager.getChunkAt(node).registerChunkLoader(entity);
				}
			}
			for (IChunk chunk : chunkManager.getLoadedChunks()) {
				if (Math.abs(chunk.getNode().getX() - entityCX) > chunkRadius) {
					chunk.removeChunkLoader(entity);
				} else if (Math.abs(chunk.getNode().getZ() - entityCZ) > chunkRadius) {
					chunk.removeChunkLoader(entity);
				}
			}

		}
		for (IChunk chunk : chunkManager.getLoadedChunks()) {
			if (chunk.chunkLoaders() == 0)
				toRemove.add(chunk.getNode());
		}
		
		for (ChunkNode chunkNode : toRemove) {
			chunkManager.unloadChunk(chunkNode);
		}

		chunkManager.update(delta);
		entitiesManager.update(delta);
	}

	@Override
	public IBlock getBlockAt(int x, int y, int z) {
		IChunk c = this.chunkManager.getChunkAt(ChunkNode.getFromBlockCoords(x, 0, z));
		if (c == null)
			return Blocks.getBlockByName("voxel:air");
		IBlock b = c.getBlockAt(x & 0xF, y, z & 0xF);
		if (b == null)
			return Blocks.getBlockByName("voxel:air");
		return b;
	}

	@Override
	public List<BoundingBox> getGlobalBoundingBox(BoundingBox box) {
		List<BoundingBox> array = new ArrayList<>();

		for (int i = (int) Math.floor(box.min.x); i < (int) Math.ceil(box.max.x); i++) {
			// XXX: Hardcoded 255 limit until custom world height is implemented
			for (int j = (int) Math.floor(box.min.y); j < (int) Math.min(Math.ceil(box.max.y), 255); j++) {
				for (int k = (int) Math.floor(box.min.z); k < (int) Math.ceil(box.max.z); k++) {
					IBlock block = getBlockAt(i, j, k);
					if (block.hasCollision())
						array.add(block.getBoundingBox(new BlockNode(i, j, k)));
				}
			}
		}
		return array;
	}

	@Override
	public void dispose() {
		entitiesManager.removeAllEntities();
		chunkManager.dispose();
	}

	@Override
	public Engine getEntitiesManager() {
		return entitiesManager;
	}

	@Override
	public Collection<IChunk> getLoadedChunks() {
		return chunkManager.getLoadedChunks();
	}

	@Override
	public WorldSimulation getWorldSimulator() {
		return (WorldSimulation)this.worldSimulation;
	}

}
