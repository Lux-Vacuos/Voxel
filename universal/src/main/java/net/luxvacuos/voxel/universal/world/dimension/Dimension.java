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

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.voxel.universal.core.GlobalVariables;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.Player;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.world.IWorld;
import net.luxvacuos.voxel.universal.world.block.Blocks;
import net.luxvacuos.voxel.universal.world.block.IBlock;
import net.luxvacuos.voxel.universal.world.chunk.ChunkManager;
import net.luxvacuos.voxel.universal.world.chunk.IChunk;
import net.luxvacuos.voxel.universal.world.chunk.generator.ChunkTerrainGenerator;
import net.luxvacuos.voxel.universal.world.chunk.generator.SimplexNoise;
import net.luxvacuos.voxel.universal.world.utils.BlockCoords;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;

public class Dimension implements IDimension {

	private int id;
	protected IWorld world;
	protected ChunkManager chunkManager;
	protected Engine entitiesManager;

	public Dimension(IWorld world, int id) {
		this.world = world;
		this.id = id;
		this.chunkManager = new ChunkManager(this);
		this.entitiesManager = new Engine();
		this.entitiesManager.addSystem(new PhysicsSystem(this));
		ChunkTerrainGenerator gen = new ChunkTerrainGenerator();
		gen.setNoiseGenerator(new SimplexNoise(256, 0.15f, 0));
		this.chunkManager.setGenerator(gen);
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
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(float delta) {
		int playerCX = 0, playerCZ = 0;
		for (Entity players : entitiesManager.getEntitiesFor(Family.all(Player.class).get())) {

			Position pos = Components.POSITION.get(players);

			if (pos.getPosition().x < 0)
				playerCX = (int) ((pos.getPosition().x - 16) / 16);
			if (pos.getPosition().z < 0)
				playerCZ = (int) ((pos.getPosition().z - 16) / 16);
			if (pos.getPosition().x > 0)
				playerCX = (int) ((pos.getPosition().x) / 16);
			if (pos.getPosition().z > 0)
				playerCZ = (int) ((pos.getPosition().z) / 16);
			ChunkNode node;
			int xx, zz;
			for (int zr = -GlobalVariables.chunk_radius; zr <= GlobalVariables.chunk_radius; zr++) {
				zz = playerCZ + zr;
				for (int xr = -GlobalVariables.chunk_radius; xr <= GlobalVariables.chunk_radius; xr++) {
					xx = playerCX + xr;
					node = new ChunkNode(xx, 0, zz);
					if (!chunkManager.isChunkLoaded(node)) {
						chunkManager.loadChunk(node);
					}
				}
			}
			/*
			 * for (IChunk chunk : chunkManager.getLoadedChunks()) { if
			 * (Math.abs(chunk.getNode().getX() - playerCX) >
			 * GlobalVariables.chunk_radius) {
			 * chunkManager.unloadChunk(chunk.getNode()); } else if
			 * (Math.abs(chunk.getNode().getZ() - playerCZ) >
			 * GlobalVariables.chunk_radius) {
			 * chunkManager.unloadChunk(chunk.getNode()); } }
			 */
		}

		chunkManager.update(delta);
		entitiesManager.update(delta);
	}

	@Override
	public IBlock getBlockAt(int x, int y, int z) {
		IChunk c = chunkManager.getChunkAt(ChunkNode.getFromBlockCoords(x, 0, z));
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
			for (int j = (int) Math.floor(box.min.y); j < (int) Math.ceil(box.max.y); j++) {
				for (int k = (int) Math.floor(box.min.z); k < (int) Math.ceil(box.max.z); k++) {
					IBlock block = getBlockAt(i, j, k);
					if (block.hasCollision())
						array.add(block.getBoundingBox(new BlockCoords(i, j, k)));
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

}
