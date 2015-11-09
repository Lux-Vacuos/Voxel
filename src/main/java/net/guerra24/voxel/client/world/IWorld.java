package net.guerra24.voxel.client.world;

import java.util.Random;

import net.guerra24.voxel.client.api.API;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.world.chunks.Chunk;
import net.guerra24.voxel.client.world.chunks.ChunkGenerator;

public interface IWorld {

	public void startWorld(String name, Random seed, int chunkDim, API api, GameResources gm);

	public void init(GameResources gm);

	public void createDimension(GameResources gm, API api);

	public void updateChunksGeneration(GameResources gm, API api);

	public void updateChunksRender(GameResources gm);

	public void updateChunksOcclusion(GameResources gm);

	public void updateChunksShadow(GameResources gm);

	public void setupLight(int x, int y, int z, int lightLevel);

	public void lighting();

	public void switchDimension(int id, GameResources gm, API api);

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
