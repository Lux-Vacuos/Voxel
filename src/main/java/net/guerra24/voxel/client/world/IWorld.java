/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.guerra24.voxel.client.world;

import java.util.Random;

import net.guerra24.voxel.client.api.ModInitialization;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.world.chunks.Chunk;
import net.guerra24.voxel.client.world.chunks.ChunkGenerator;

public interface IWorld {

	public void startWorld(String name, Random seed, int chunkDim, ModInitialization api, GameResources gm);

	public void init(GameResources gm);

	public void createDimension(GameResources gm, ModInitialization api);

	public void updateChunksGeneration(GameResources gm, ModInitialization api, float delta);

	public void updateChunksRender(GameResources gm);

	public void updateChunksOcclusion(GameResources gm);

	public void updateChunksShadow(GameResources gm);

	public void setupLight(int x, int y, int z, int lightLevel);

	public void lighting();

	public void switchDimension(int id, GameResources gm, ModInitialization api);

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

	public void clearDimension(GameResources gm);

	public int getChunksCount();

	public int getWorldID();

	public int getChunkDimension();

	public int getzPlayChunk();

	public int getxPlayChunk();

	public int getyPlayChunk();
	
	public SimplexNoise getNoise();

	public Random getSeed();

	public ChunkGenerator getChunkGenerator();

	public void setTempRadius(int tempRadius);

	public int getTempRadius();

}
