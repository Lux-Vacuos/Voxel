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

package net.luxvacuos.voxel.server.world;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.voxel.server.resources.GameResources;
import net.luxvacuos.voxel.server.world.chunks.Chunk;
import net.luxvacuos.voxel.server.world.chunks.ChunkGenerator;

/**
 * 
 * Interface of World, this is used as template for all worlds
 * 
 * @author pablo
 *
 */
public interface IWorld {

	public void startWorld(String name, Random seed, int chunkDim, GameResources gm);

	public void init(GameResources gm);

	public void createDimension(GameResources gm);

	public void updateChunksGeneration(GameResources gm, float delta);

	public void updateChunksRender(GameResources gm);

	public void updateChunksOcclusion(GameResources gm);

	public void updateChunksShadow(GameResources gm);

	public void setupLight(int x, int y, int z, int lightLevel);

	public void lighting();

	public void switchDimension(int id, GameResources gm);

	public void saveWorld(GameResources gm);

	public void loadWorld(GameResources gm);

	public void saveChunk(int chunkDim, int cx, int cy, int cz, GameResources gm);

	public void loadChunk(int chunkDim, int cx, int cy, int cz, GameResources gm);

	public boolean existChunkFile(int chunkDim, int cx, int cy, int cz);

	public boolean existWorld();

	public boolean existChunkFolder(int chunkDim);

	public Chunk getChunk(int chunkDim, int cx, int cy, int cz);

	public boolean hasChunk(int chunkDim, int cx, int cy, int cz);

	public void addChunk(Chunk chunk);

	public void removeChunk(Chunk chunk);

	public byte getGlobalBlock(int x, int y, int z);

	public void setGlobalBlock(int x, int y, int z, byte id);

	public void lighting(int x, int y, int z, int val);

	public float getLight(int x, int y, int z);

	public void clearDimension(GameResources gm);

	public int getLoadedChunks();

	public int getRenderedChunks();

	public int getWorldID();

	public int getChunkDimension();

	public int getzPlayChunk();

	public int getxPlayChunk();

	public int getyPlayChunk();

	public List<BoundingBox> getGlobalBoundingBox(BoundingBox box);

	public SimplexNoise getNoise();

	public Random getSeed();

	public ChunkGenerator getChunkGenerator();

	public void setTempRadius(int tempRadius);

	public int getTempRadius();

}
