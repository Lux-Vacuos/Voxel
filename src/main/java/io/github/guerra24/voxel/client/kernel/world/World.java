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

import io.github.guerra24.voxel.client.kernel.core.Kernel;
import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.util.Logger;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector2f;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.chunks.Chunk;
import io.github.guerra24.voxel.client.kernel.world.chunks.ChunkKey;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

/**
 * World
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.3 Build-60
 * @since 0.0.1 Build-52
 * @category World
 */
public class World {
	/**
	 * Timing and World Dimension
	 */
	public int dim = 0;
	/**
	 * Chunks Map
	 */
	public HashMap<ChunkKey, Chunk> chunks;
	/**
	 * World Seed
	 */
	public Random seed;
	/**
	 * World noise
	 */
	public SimplexNoise noise;
	/**
	 * World Name
	 */
	public String name;
	/**
	 * Player Position in Chunk
	 */
	private int xPlayChunk;
	private int zPlayChunk;

	/**
	 * Temporal Radius
	 */
	public transient int tempRadius = 0;

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
	public void startWorld(String name, Camera camera, Random seed,
			int dimension) {
		this.name = name;
		this.seed = seed;
		this.dim = dimension;
		if (existWorld()) {
			loadWorld();
		}
		saveWorld();
		initialize();
		createWorld(camera);
	}

	/**
	 * Initialize The Basic World System
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void initialize() {
		noise = new SimplexNoise(128, 0.2f, seed.nextInt());
		chunks = new HashMap<ChunkKey, Chunk>();
		Kernel.gameResources.camera.setPosition(new Vector3f(Maths.randInt(-200, 200), 128, Maths.randInt(-200, 200)));
		Kernel.gameResources.player.setPosition(Kernel.gameResources.camera
				.getPosition());
	}

	/**
	 * Creates the World in a range of 16*16 Chunks
	 * 
	 * @param camera
	 *            Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void createWorld(Camera camera) {
		Logger.log(Thread.currentThread(), "Generating World");
		xPlayChunk = (int) (camera.getPosition().x / 16);
		zPlayChunk = (int) (camera.getPosition().z / 16);
		float i = 0f;
		for (int zr = -10; zr <= 10; zr++) {
			int zz = zPlayChunk + zr;
			for (int xr = -10; xr <= 10; xr++) {
				int xx = xPlayChunk + xr;
				if (zr * zr + xr * xr < 10 * 10) {
					i += 0.00200f;
					Kernel.gameResources.guis3.get(1).setScale(
							new Vector2f(i, 0.041f));
					if (!hasChunk(dim, xx, zz)) {
						if (existChunkFile(dim, xx, zz)) {
							loadChunk(dim, xx, zz);
						} else {
							addChunk(new Chunk(dim, xx, zz));
							saveChunk(dim, xx, zz);
						}
					} else {
						getChunk(dim, xx, zz).update();
					}
				}
			}
		}
	}

	/**
	 * Updates the Chunk Generation
	 * 
	 * @param camera
	 *            Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void updateChunkGeneration(Camera camera) {
		if (camera.getPosition().x < 0)
			xPlayChunk = (int) ((camera.getPosition().x - 16) / 16);
		if (camera.getPosition().z < 0)
			zPlayChunk = (int) ((camera.getPosition().z - 16) / 16);
		if (camera.getPosition().x > 0)
			xPlayChunk = (int) ((camera.getPosition().x) / 16);
		if (camera.getPosition().z > 0)
			zPlayChunk = (int) ((camera.getPosition().z) / 16);
		KernelConstants.update();
		for (int zr = -tempRadius; zr <= tempRadius; zr++) {
			int zz = zPlayChunk + zr;
			for (int xr = -tempRadius; xr <= tempRadius; xr++) {
				int xx = xPlayChunk + xr;
				if (zr * zr + xr * xr <= (KernelConstants.genRadius - KernelConstants.radiusLimit)
						* (KernelConstants.genRadius - KernelConstants.radiusLimit)) {
					if (!hasChunk(dim, xx, zz)) {
						if (existChunkFile(dim, xx, zz)) {
							loadChunk(dim, xx, zz);
						} else {
							addChunk(new Chunk(dim, xx, zz));
							saveChunk(dim, xx, zz);
						}
					} else {
						getChunk(dim, xx, zz).update();
					}
				}
				if (zr * zr + xr * xr <= KernelConstants.genRadius
						* KernelConstants.genRadius
						&& zr * zr + xr * xr >= (KernelConstants.genRadius - KernelConstants.radiusLimit)
								* (KernelConstants.genRadius
										- KernelConstants.radiusLimit - 1)) {
					if (hasChunk(dim, xx, zz)) {
						saveChunk(dim, xx, zz);
						removeChunk(getChunk(dim, xx, zz));
					}
				}
			}
		}
		if (tempRadius <= KernelConstants.genRadius)
			tempRadius++;
	}

	/**
	 * Updates the Chunk Rendering
	 * 
	 * @param camera
	 *            Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void updateChunksRender(GameResources gm, Camera camera) {
		Kernel.gameResources.lights.clear();
		for (int zr = -tempRadius; zr <= tempRadius; zr++) {
			int zz = zPlayChunk + zr;
			for (int xr = -tempRadius; xr <= tempRadius; xr++) {
				int xx = xPlayChunk + xr;
				if (zr * zr + xr * xr <= KernelConstants.radius
						* KernelConstants.radius) {
					if (hasChunk(dim, xx, zz)) {
						Chunk chunk = getChunk(dim, xx, zz);
						if (KernelConstants.advancedOpenGL) {
							if (chunk.sec1NotClear)
								if (Kernel.gameResources.frustum.cubeInFrustum(
										chunk.posX, 0, chunk.posZ,
										chunk.posX + 16, 32, chunk.posZ + 16)) {
									chunk.render1(gm.renderer, camera);
									chunk.sendToRenderLights1();
								}
							if (chunk.sec2NotClear)
								if (Kernel.gameResources.frustum.cubeInFrustum(
										chunk.posX, 32, chunk.posZ,
										chunk.posX + 16, 64, chunk.posZ + 16)) {
									chunk.render2(gm.renderer, camera);
									chunk.sendToRenderLights2();
								}
							if (chunk.sec3NotClear)
								if (Kernel.gameResources.frustum.cubeInFrustum(
										chunk.posX, 64, chunk.posZ,
										chunk.posX + 16, 96, chunk.posZ + 16)) {
									chunk.render3(gm.renderer,
											gm.waterRenderer, camera);
									chunk.sendToRenderLights3();
								}
							if (chunk.sec4NotClear)
								if (Kernel.gameResources.frustum.cubeInFrustum(
										chunk.posX, 96, chunk.posZ,
										chunk.posX + 16, 128, chunk.posZ + 16)) {
									chunk.render4(gm.renderer, camera);
									chunk.sendToRenderLights4();
								}
						} else {
							chunk.render1(gm.renderer, camera);
							chunk.sendToRenderLights1();
							chunk.render2(gm.renderer, camera);
							chunk.sendToRenderLights2();
							chunk.render3(gm.renderer, gm.waterRenderer, camera);
							chunk.sendToRenderLights3();
							chunk.render4(gm.renderer, camera);
							chunk.sendToRenderLights4();
						}
					}
				}
			}
		}
	}

	/**
	 * ONLY FOR TESTING
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void test() {
	}

	/**
	 * Saves the World info
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void saveWorld() {
		if (!existWorld()) {
			File file = new File(KernelConstants.worldPath + name + "/");
			file.mkdir();
		}
		if (!existChunkFolder(dim)) {
			File filec = new File(KernelConstants.worldPath + name + "/chunks_"
					+ dim + "/");
			filec.mkdir();
		}
		String json = Kernel.gameResources.gson.toJson(seed);
		try {
			FileWriter file = new FileWriter(KernelConstants.worldPath + name
					+ "/world.json");
			file.write(json);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the World Info
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadWorld() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					KernelConstants.worldPath + name + "/world.json"));
			seed = Kernel.gameResources.gson.fromJson(br, Random.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save a Chunk
	 * 
	 * @param dim
	 *            World Dimension
	 * @param cx
	 *            X Coord
	 * @param cz
	 *            Z Coord
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void saveChunk(int dim, int cx, int cz) {
		String json = Kernel.gameResources.gson.toJson(getChunk(dim, cx, cz));
		try {
			FileWriter file = new FileWriter(KernelConstants.worldPath + name
					+ "/chunks_" + dim + "/chunk_" + dim + "_" + cx + "_" + cz
					+ ".json");
			file.write(json);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load a Chunk to the Map
	 * 
	 * @param dim
	 *            World Dimension
	 * @param cx
	 *            X Coord
	 * @param cz
	 *            Z Coord
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadChunk(int dim, int cx, int cz) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					KernelConstants.worldPath + name + "/chunks_" + dim
							+ "/chunk_" + dim + "_" + cx + "_" + cz + ".json"));
			Chunk chunk = Kernel.gameResources.gson.fromJson(br, Chunk.class);
			chunk.loadInit();
			addChunk(chunk);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if exist the chunk file
	 * 
	 * @param dim
	 *            World Dimension
	 * @param cx
	 *            X Coord
	 * @param cz
	 *            Z Coord
	 * @return Boolean
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public boolean existChunkFile(int dim, int cx, int cz) {
		File file = new File(KernelConstants.worldPath + name + "/chunks_"
				+ dim + "/chunk_" + dim + "_" + cx + "_" + cz + ".json");
		return file.exists();
	}

	/**
	 * Check if exist the World info file
	 * 
	 * @return Boolean
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public boolean existWorld() {
		File file = new File(KernelConstants.worldPath + name + "/world.json");
		return file.exists();
	}

	/**
	 * Check if exist chunk folder
	 * 
	 * @param dim
	 *            World Dimension
	 * @return Boolean
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public boolean existChunkFolder(int dim) {
		File file = new File(KernelConstants.worldPath + name + "/chunks_"
				+ dim + "/");
		return file.exists();
	}

	/**
	 * Get the chunk of the map
	 * 
	 * @param dim
	 *            World Dimension
	 * @param cx
	 *            X Coord
	 * @param cz
	 *            Z Coord
	 * @return Chunk
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public Chunk getChunk(int dim, int cx, int cz) {
		ChunkKey key = ChunkKey.alloc(dim, cx, cz);
		Chunk chunk;
		chunk = chunks.get(key);
		key.free();
		return chunk;
	}

	/**
	 * If in the position exist a chunk
	 * 
	 * @param dim
	 *            World Dimension
	 * @param cx
	 *            X Coord
	 * @param cz
	 *            Z Coord
	 * @return Boolean
	 */
	public boolean hasChunk(int dim, int cx, int cz) {
		ChunkKey key = ChunkKey.alloc(dim, cx, cz);
		boolean contains;
		contains = chunks.containsKey(key);
		key.free();
		return contains;
	}

	/**
	 * Add a chunk to the Map
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
	}

	/**
	 * Remove the chunk of the map
	 * 
	 * @param chunk
	 *            Chunk
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void removeChunk(Chunk chunk) {
		if (chunk != null) {
			ChunkKey key = ChunkKey.alloc(chunk.dim, chunk.cx, chunk.cz);
			chunks.remove(key);
			key.free();
		}
	}

	/**
	 * Get Map Size
	 * 
	 * @return Size
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public int getCount() {
		int cnt;
		cnt = chunks.size();
		return cnt;
	}

	/**
	 * Gets the Block in global coords
	 * 
	 * @param dim
	 *            World Dimension
	 * @param x
	 *            X Coord
	 * @param y
	 *            Y Coord
	 * @param z
	 *            Z Coord
	 * @return Block ID
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public byte getGlobalBlock(int dim, int x, int y, int z) {
		int cx = x >> 4;
		int cz = z >> 4;
		Chunk chunk = getChunk(dim, cx, cz);
		if (chunk != null)
			return chunk.getLocalBlock(x, y, z);
		else
			return 1;
	}

	/**
	 * Set a block in global coords
	 * 
	 * @param dim
	 *            World Dimension
	 * @param x
	 *            X Coord
	 * @param y
	 *            Y Coord
	 * @param z
	 *            Z Coord
	 * @param id
	 *            Block ID
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void setGlobalBlock(int dim, int x, int y, int z, byte id) {
		int cx = x >> 4;
		int cz = z >> 4;
		Chunk chunk = getChunk(dim, cx, cz);
		chunk.setLocalBlock(x, y, z, id);
		chunk.clear();
		chunk.rebuildChunk();
	}

	/**
	 * Clear the chunk map
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void removeAll() {
		chunks.clear();
	}

	/**
	 * Gets Player Position in Chunk Style
	 * 
	 * @return Z Position
	 */
	public int getzPlayChunk() {
		return zPlayChunk;
	}

	/**
	 * Gets Player Position in Chunk Style
	 * 
	 * @return X Position
	 */
	public int getxPlayChunk() {
		return xPlayChunk;
	}

}