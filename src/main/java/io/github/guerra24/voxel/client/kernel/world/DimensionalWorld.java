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

package io.github.guerra24.voxel.client.kernel.world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import com.google.gson.JsonSyntaxException;

import io.github.guerra24.voxel.client.kernel.api.VAPI;
import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.util.Logger;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector2f;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.chunks.Chunk;
import io.github.guerra24.voxel.client.kernel.world.chunks.ChunkGenerator;
import io.github.guerra24.voxel.client.kernel.world.chunks.ChunkKey;
import io.github.guerra24.voxel.client.kernel.world.chunks.ChunkWorkerDestroy;
import io.github.guerra24.voxel.client.kernel.world.chunks.ChunkWorkerGenerate;
import io.github.guerra24.voxel.client.kernel.world.chunks.ChunkWorkerMesh;
import io.github.guerra24.voxel.client.kernel.world.chunks.WorldService;

/**
 * Dimensional World
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category World
 */
public class DimensionalWorld {

	/**
	 * Dimensional World Data
	 */
	private int chunkDim;
	private int worldID;
	private HashMap<ChunkKey, Chunk> chunks;
	private Random seed;
	private SimplexNoise noise;
	private String name;
	private int xPlayChunk;
	private int zPlayChunk;
	private int tempRadius = 0;
	private int seedi;
	private ChunkGenerator chunkGenerator;
	private WorldService service;

	/**
	 * Start a new World
	 * 
	 * @param name
	 *            World Name
	 * @param camera
	 *            Camera
	 * @param seed
	 *            Seed
	 * @param dimension
	 *            World Dimension
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void startWorld(String name, Random seed, int chunkDim, VAPI api, GameResources gm) {
		this.name = name;
		this.seed = seed;
		this.chunkDim = chunkDim;
		gm.getCamera().setPosition(new Vector3f(10, 128, 10));
		if (existWorld()) {
			loadWorld(gm);
		}
		saveWorld(gm);
		initialize(gm);
		createDimension(gm, api);
	}

	/**
	 * Initialize the World
	 * 
	 * @param gm
	 *            Game Resources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void initialize(GameResources gm) {
		seedi = seed.nextInt();
		noise = new SimplexNoise(128, 0.3f, seedi);
		chunks = new HashMap<ChunkKey, Chunk>();
		chunkGenerator = new ChunkGenerator();
		service = new WorldService();
		gm.getPhysics().getMobManager().getPlayer().setPosition(gm.getCamera().getPosition());
	}

	/**
	 * Create Dimension
	 * 
	 * @param gm
	 *            Game Resources
	 * @param api
	 *            Voxel API
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void createDimension(GameResources gm, VAPI api) {
		Logger.log(Thread.currentThread(), "Generating World");
		xPlayChunk = (int) (gm.getCamera().getPosition().x / 16);
		zPlayChunk = (int) (gm.getCamera().getPosition().z / 16);
		float o = 1f;
		float i = 0f;
		for (int zr = -4; zr <= 4; zr++) {
			int zz = zPlayChunk + zr;
			for (int xr = -4; xr <= 4; xr++) {
				int xx = xPlayChunk + xr;
				if (zr * zr + xr * xr < 4 * 4) {
					i += 0.00200f;
					gm.guis3.get(1).setScale(new Vector2f(i, 0.041f));
					if (i > 0.5060006f) {
						o -= 0.04f;
						if (o >= 0)
							gm.getSoundSystem().setVolume("menu1", o);
					}
					if (!hasChunk(chunkDim, xx, zz)) {
						if (existChunkFile(chunkDim, xx, zz)) {
							loadChunk(chunkDim, xx, zz, gm);
						} else {
							addChunk(new Chunk(chunkDim, xx, zz, this));
							saveChunk(chunkDim, xx, zz, gm);
						}
					}
				}
			}
		}
	}

	/**
	 * Update Chunk Generation
	 * 
	 * @param gm
	 *            Game Resources
	 * @param api
	 *            Voxel API
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void updateChunkGeneration(GameResources gm, VAPI api) {
		if (gm.getCamera().getPosition().x < 0)
			xPlayChunk = (int) ((gm.getCamera().getPosition().x - 16) / 16);
		if (gm.getCamera().getPosition().z < 0)
			zPlayChunk = (int) ((gm.getCamera().getPosition().z - 16) / 16);
		if (gm.getCamera().getPosition().x > 0)
			xPlayChunk = (int) ((gm.getCamera().getPosition().x) / 16);
		if (gm.getCamera().getPosition().z > 0)
			zPlayChunk = (int) ((gm.getCamera().getPosition().z) / 16);
		KernelConstants.update();
		for (int zr = -tempRadius; zr <= tempRadius; zr++) {
			int zz = zPlayChunk + zr;
			for (int xr = -tempRadius; xr <= tempRadius; xr++) {
				int xx = xPlayChunk + xr;
				if (zr * zr + xr * xr <= (KernelConstants.genRadius - KernelConstants.radiusLimit)
						* (KernelConstants.genRadius - KernelConstants.radiusLimit)) {
					if (!hasChunk(chunkDim, xx, zz)) {
						if (!Thread.holdsLock(chunks))
							service.add_worker(new ChunkWorkerGenerate(this, gm, xx, zz));
					} else {
						Chunk chunk = getChunk(chunkDim, xx, zz);
						if (!Thread.holdsLock(chunk))
							if (chunk.needsRebuild) {
								if (!chunk.rebuilding) {
									chunk.rebuilding = true;
									service.add_worker(new ChunkWorkerMesh(this, chunk));
								}
							}
					}
				}
				if (zr * zr + xr * xr <= KernelConstants.genRadius * KernelConstants.genRadius
						&& zr * zr + xr * xr >= (KernelConstants.genRadius - KernelConstants.radiusLimit + 1)
								* (KernelConstants.genRadius - KernelConstants.radiusLimit + 1)) {
					if (!Thread.holdsLock(chunks))
						service.add_worker(new ChunkWorkerDestroy(this, gm, xx, zz));
				}
			}
		}
		if (tempRadius <= KernelConstants.genRadius)
			tempRadius++;
	}

	/**
	 * Render Chunks
	 * 
	 * @param gm
	 *            GameResources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void updateChunksRender(GameResources gm) {
		for (int zr = -KernelConstants.radius; zr <= KernelConstants.radius; zr++) {
			int zz = zPlayChunk + zr;
			for (int xr = -KernelConstants.radius; xr <= KernelConstants.radius; xr++) {
				int xx = xPlayChunk + xr;
				if (hasChunk(chunkDim, xx, zz)) {
					Chunk chunk = getChunk(chunkDim, xx, zz);
					if (gm.getFrustum().cubeInFrustum(chunk.posX, 0, chunk.posZ, chunk.posX + 16, 32, chunk.posZ + 16))
						chunk.render1(gm);
					if (gm.getFrustum().cubeInFrustum(chunk.posX, 32, chunk.posZ, chunk.posX + 16, 64, chunk.posZ + 16))
						chunk.render2(gm);
					if (gm.getFrustum().cubeInFrustum(chunk.posX, 64, chunk.posZ, chunk.posX + 16, 96, chunk.posZ + 16))
						chunk.render3(gm);
					if (gm.getFrustum().cubeInFrustum(chunk.posX, 96, chunk.posZ, chunk.posX + 16, 128,
							chunk.posZ + 16))
						chunk.render4(gm);

				}
			}
		}

	}

	/**
	 * Switch Dimension
	 * 
	 * @param id
	 *            Dimension ID
	 * @param gm
	 *            Game Resources
	 * @param api
	 *            Voxel API
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void switchDimension(int id, GameResources gm, VAPI api) {
		if (id != chunkDim) {
			clearChunkDimension(gm);
			chunkDim = id;
			initialize(gm);
			createDimension(gm, api);
		}
	}

	/**
	 * Save World Data
	 * 
	 * @param gm
	 *            GameResources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void saveWorld(GameResources gm) {
		if (!existWorld()) {
			File file = new File(KernelConstants.worldPath + name + "/");
			file.mkdirs();
		}
		if (!existChunkFolder(chunkDim)) {
			File filec = new File(KernelConstants.worldPath + name + "/chunks_" + chunkDim + "/");
			filec.mkdirs();
		}
		String jsonwo = gm.getGson().toJson(seed);
		try {
			FileWriter file = new FileWriter(KernelConstants.worldPath + name + "/world.json");
			file.write(jsonwo);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load World Data
	 * 
	 * @param gm
	 *            GameResources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void loadWorld(GameResources gm) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(KernelConstants.worldPath + name + "/world.json"));
			seed = gm.getGson().fromJson(br, Random.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save Chunk Data
	 * 
	 * @param chunkDim
	 *            Chunk Dimension
	 * @param cx
	 *            Chunk X
	 * @param cz
	 *            Chunk Z
	 * @param gm
	 *            GameResources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void saveChunk(int chunkDim, int cx, int cz, GameResources gm) {
		String json = gm.getGson().toJson(getChunk(chunkDim, cx, cz));
		try {
			File chunksFolder = new File(KernelConstants.worldPath + name + "/chunks_" + chunkDim);
			if (!chunksFolder.exists())
				chunksFolder.mkdirs();
			FileWriter file = new FileWriter(KernelConstants.worldPath + name + "/chunks_" + chunkDim + "/chunk_"
					+ chunkDim + "_" + cx + "_" + cz + ".json");
			file.write(json);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load Chunk Data
	 * 
	 * @param chunkDim
	 *            Chunk Dimension
	 * @param cx
	 *            Chunk X
	 * @param cz
	 *            Chunk Z
	 * @param gm
	 *            Game Resources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadChunk(int chunkDim, int cx, int cz, GameResources gm) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(KernelConstants.worldPath + name + "/chunks_"
					+ chunkDim + "/chunk_" + chunkDim + "_" + cx + "_" + cz + ".json"));
			Chunk chunk = gm.getGson().fromJson(br, Chunk.class);
			if (chunk != null)
				chunk.createList();
			else {
				Logger.warn(Thread.currentThread(), "Re-Creating Chunk " + chunkDim + " " + cx + " " + cz);
				chunk = new Chunk(chunkDim, cx, cz, this);
			}
			addChunk(chunk);
		} catch (JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
			Logger.warn(Thread.currentThread(), "Re-Creating Chunk " + chunkDim + " " + cx + " " + cz);
			Chunk chunk = new Chunk(chunkDim, cx, cz, this);
			addChunk(chunk);
			saveChunk(chunkDim, cx, cz, gm);
		}
	}

	/**
	 * Check if exist a chunk file
	 * 
	 * @param chunkDim
	 *            Chunk Dimension
	 * @param cx
	 *            Chunk X
	 * @param cz
	 *            Chunk Z
	 * @return true if exist
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public boolean existChunkFile(int chunkDim, int cx, int cz) {
		File file = new File(KernelConstants.worldPath + name + "/chunks_" + chunkDim + "/chunk_" + chunkDim + "_" + cx
				+ "_" + cz + ".json");
		return file.exists();
	}

	/**
	 * Check if exist a world file
	 * 
	 * @return true if exist
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private boolean existWorld() {
		File file = new File(KernelConstants.worldPath + name + "/world.json");
		return file.exists();
	}

	/**
	 * Check if exist a chunk folder
	 * 
	 * @param chunkDim
	 *            Chunk Dimension
	 * @return true if exist
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private boolean existChunkFolder(int chunkDim) {
		File file = new File(KernelConstants.worldPath + name + "/chunks_" + chunkDim + "/");
		return file.exists();
	}

	/**
	 * Get Chunk from coords
	 * 
	 * @param chunkDim
	 *            Chunk Dimension
	 * @param cx
	 *            Chunk X
	 * @param cz
	 *            Chunk Z
	 * @return Chunk
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public Chunk getChunk(int chunkDim, int cx, int cz) {
		ChunkKey key = ChunkKey.alloc(chunkDim, cx, cz);
		Chunk chunk;
		chunk = chunks.get(key);
		key.free();
		return chunk;
	}

	/**
	 * Check if the Map contain a chunk
	 * 
	 * @param chunkDim
	 *            Chunk Dimension
	 * @param cx
	 *            Chunk X
	 * @param cz
	 *            Chunk Z
	 * @return true if exist
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public boolean hasChunk(int chunkDim, int cx, int cz) {
		ChunkKey key = ChunkKey.alloc(chunkDim, cx, cz);
		boolean contains;
		contains = chunks.containsKey(key);
		key.free();
		return contains;
	}

	/**
	 * Add a new Chunk
	 * 
	 * @param chunk
	 *            Chunk
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void addChunk(Chunk chunk) {
		ChunkKey key = ChunkKey.alloc(chunk.dim, chunk.cx, chunk.cz);
		Chunk old = chunks.get(key);
		if (old != null) {
			removeChunk(old);
		}
		chunks.put(key.clone(), chunk);
		for (int xx = chunk.cx - 2; xx < chunk.cx + 2; xx++) {
			for (int zz = chunk.cz - 2; zz < chunk.cz + 2; zz++) {
				Chunk chunka = getChunk(chunkDim, xx, zz);
				if (chunka != null)
					chunka.needsRebuild = true;
			}
		}
	}

	/**
	 * Remove Chunk
	 * 
	 * @param chunk
	 *            Chunk
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void removeChunk(Chunk chunk) {
		if (chunk != null) {
			ChunkKey key = ChunkKey.alloc(chunk.dim, chunk.cx, chunk.cz);
			chunk.dispose();
			chunks.remove(key);
			key.free();
			for (int xx = chunk.cx - 2; xx < chunk.cx + 2; xx++) {
				for (int zz = chunk.cz - 2; zz < chunk.cz + 2; zz++) {
					Chunk chunka = getChunk(chunkDim, xx, zz);
					if (chunka != null)
						chunka.needsRebuild = true;
				}
			}
		}
	}

	/**
	 * @return Chunks in Memory
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public int getCount() {
		int cnt;
		cnt = chunks.size();
		return cnt;
	}

	/**
	 * Get a block from global Coords
	 * 
	 * @param chunkDim
	 *            Chunk Dimension
	 * @param x
	 *            Postion X
	 * @param y
	 *            Postion Y
	 * @param z
	 *            Postion Z
	 * @return block ID
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public byte getGlobalBlock(int chunkDim, int x, int y, int z) {
		int cx = x >> 4;
		int cz = z >> 4;
		Chunk chunk = getChunk(chunkDim, cx, cz);
		if (chunk != null)
			return chunk.getLocalBlock(x, y, z);
		else
			return 0;
	}

	/**
	 * Set a block from gloal coords
	 * 
	 * @param chunkDim
	 *            Chunk Dimension
	 * @param x
	 *            Position X
	 * @param y
	 *            Position Y
	 * @param z
	 *            Position Z
	 * @param id
	 *            Block ID
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void setGlobalBlock(int chunkDim, int x, int y, int z, byte id) {
		int cx = x >> 4;
		int cz = z >> 4;
		Chunk chunk = getChunk(chunkDim, cx, cz);
		if (chunk != null) {
			chunk.setLocalBlock(x, y, z, id);
		}
	}

	/**
	 * Clear the chunks of a dimension
	 * 
	 * @param gm
	 *            Game Resources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void clearChunkDimension(GameResources gm) {
		Logger.log(Thread.currentThread(), "Saving World");
		for (int zr = -KernelConstants.genRadius; zr <= KernelConstants.genRadius; zr++) {
			int zz = getzPlayChunk() + zr;
			for (int xr = -KernelConstants.genRadius; xr <= KernelConstants.genRadius; xr++) {
				int xx = getxPlayChunk() + xr;
				if (zr * zr + xr * xr <= KernelConstants.genRadius * KernelConstants.genRadius) {
					if (hasChunk(chunkDim, xx, zz)) {
						saveChunk(chunkDim, xx, zz, gm);
					}
				}
			}
		}
		service.es.shutdown();
		chunks.clear();
	}

	public int getzPlayChunk() {
		return zPlayChunk;
	}

	public int getxPlayChunk() {
		return xPlayChunk;
	}

	public int getWorldID() {
		return worldID;
	}

	public int getChunkDimension() {
		return chunkDim;
	}

	public HashMap<ChunkKey, Chunk> getChunks() {
		return chunks;
	}

	public SimplexNoise getNoise() {
		return noise;
	}

	public Random getSeed() {
		return seed;
	}

	public ChunkGenerator getChunkGenerator() {
		return chunkGenerator;
	}

	public void setTempRadius(int tempRadius) {
		this.tempRadius = tempRadius;
	}

	public int getTempRadius() {
		return tempRadius;
	}

}
