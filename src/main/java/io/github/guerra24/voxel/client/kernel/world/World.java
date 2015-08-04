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
import io.github.guerra24.voxel.client.kernel.graphics.Frustum;
import io.github.guerra24.voxel.client.kernel.util.Logger;
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

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class World {

	public int time = 0, time2 = 0, dim = 0;
	public HashMap<ChunkKey, Chunk> chunks;
	public Random seed;
	public SimplexNoise noise;
	public String path = "assets/game/world/", name;

	private int xPlayChunk;
	private int zPlayChunk;

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

	private void initialize() {
		noise = new SimplexNoise(128, 0.2f, seed.nextInt());
		chunks = new HashMap<ChunkKey, Chunk>();
		Kernel.gameResources.camera.setPosition(new Vector3f(0, 128, 0));
		Kernel.gameResources.player.setPosition(Kernel.gameResources.camera
				.getPosition());
	}

	private void createWorld(Camera camera) {
		Logger.log(Thread.currentThread(), "Generating World");
		xPlayChunk = (int) (camera.getPosition().x / 16);
		zPlayChunk = (int) (camera.getPosition().z / 16);
		float i = -0.45f;
		boolean k = true;
		for (int zr = -16; zr <= 16; zr++) {
			int zz = zPlayChunk + zr;
			for (int xr = -16; xr <= 16; xr++) {
				int xx = xPlayChunk + xr;
				if (zr * zr + xr * xr < 16 * 16) {
					if (i >= 0.4f)
						k = false;
					if (i <= -0.45f)
						k = true;
					if (k == false)
						Kernel.gameResources.guis3.get(1).setPosition(
								new Vector2f(i -= 0.01f, 0.1f));
					if (k == true)
						Kernel.gameResources.guis3.get(1).setPosition(
								new Vector2f(i += 0.01f, 0.1f));
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

	public void updateChunkGeneration(Camera camera) {
		time++;
		if (time % 10 == 0) {
			if (camera.getPosition().x < 0)
				xPlayChunk = (int) ((camera.getPosition().x - 16) / 16);
			if (camera.getPosition().z < 0)
				zPlayChunk = (int) ((camera.getPosition().z - 16) / 16);
			if (camera.getPosition().x > 0)
				xPlayChunk = (int) ((camera.getPosition().x) / 16);
			if (camera.getPosition().z > 0)
				zPlayChunk = (int) ((camera.getPosition().z) / 16);

			CHUNK_LOADING: for (int zr = -KernelConstants.radius; zr <= KernelConstants.radius; zr++) {
				int zz = zPlayChunk + zr;
				for (int xr = -KernelConstants.radius; xr <= KernelConstants.radius; xr++) {
					int xx = xPlayChunk + xr;
					if (zr * zr + xr * xr < KernelConstants.radius
							* KernelConstants.radius) {
						if (!hasChunk(dim, xx, zz)) {
							if (existChunkFile(dim, xx, zz)) {
								loadChunk(dim, xx, zz);
								break CHUNK_LOADING;
							} else {
								addChunk(new Chunk(dim, xx, zz));
								saveChunk(dim, xx, zz);
								break CHUNK_LOADING;
							}
						} else {
							getChunk(dim, xx, zz).update();
						}
					}
				}
			}
			time = 0;
		}
	}

	public void updateChunksRender(Camera camera) {
		time2++;
		if (time2 % 10 == 0) {
			Kernel.gameResources.cubes.clear();
			Kernel.gameResources.waters.clear();
			for (int zr = -KernelConstants.radius; zr <= KernelConstants.radius; zr++) {
				int zz = zPlayChunk + zr;
				for (int xr = -KernelConstants.radius; xr <= KernelConstants.radius; xr++) {
					int xx = xPlayChunk + xr;
					if (zr * zr + xr * xr < KernelConstants.radius
							* KernelConstants.radius) {
						if (hasChunk(dim, xx, zz)) {
							Chunk chunk = getChunk(dim, xx, zz);
							if (KernelConstants.advancedOpenGL) {
								if (chunk.sec1NotClear)
									if (Frustum.getFrustum().cubeInFrustum(
											chunk.posX, 0, chunk.posZ,
											chunk.posX + 16, 32,
											chunk.posZ + 16))
										chunk.sendToRender1();
								if (chunk.sec2NotClear)
									if (Frustum.getFrustum().cubeInFrustum(
											chunk.posX, 32, chunk.posZ,
											chunk.posX + 16, 64,
											chunk.posZ + 16))
										chunk.sendToRender2();
								if (chunk.sec3NotClear)
									if (Frustum.getFrustum().cubeInFrustum(
											chunk.posX, 64, chunk.posZ,
											chunk.posX + 16, 96,
											chunk.posZ + 16)) {
										chunk.sendToRender3();
										chunk.sendToRenderWater();
									}
								if (chunk.sec4NotClear)
									if (Frustum.getFrustum().cubeInFrustum(
											chunk.posX, 96, chunk.posZ,
											chunk.posX + 16, 128,
											chunk.posZ + 16))
										chunk.sendToRender4();
							} else {
								chunk.sendToRender1();
								chunk.sendToRender2();
								chunk.sendToRender3();
								chunk.sendToRender4();
								chunk.sendToRenderWater();
							}
						}
					}
				}
			}
			time2 = 0;
		}
	}

	public void test() {
	}

	public void saveWorld() {
		if (!existWorld()) {
			File file = new File(path + name + "/");
			file.mkdir();
		}
		if (!existChunkFolder(dim)) {
			File filec = new File(path + name + "/chunks_" + dim + "/");
			filec.mkdir();
		}
		String json = Kernel.gameResources.gson.toJson(seed);
		try {
			FileWriter file = new FileWriter(path + name + "/world.json");
			file.write(json);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadWorld() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + name
					+ "/world.json"));
			seed = Kernel.gameResources.gson.fromJson(br, Random.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void saveChunk(int dim, int cx, int cz) {
		String json = Kernel.gameResources.gson.toJson(getChunk(dim, cx, cz));
		try {
			FileWriter file = new FileWriter(path + name + "/chunks_" + dim
					+ "/chunk_" + dim + "_" + cx + "_" + cz + ".json");
			file.write(json);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadChunk(int dim, int cx, int cz) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + name
					+ "/chunks_" + dim + "/chunk_" + dim + "_" + cx + "_" + cz
					+ ".json"));
			Chunk chunk = Kernel.gameResources.gson.fromJson(br, Chunk.class);
			chunk.clear();
			addChunk(chunk);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean existChunkFile(int dim, int cx, int cz) {
		File file = new File(path + name + "/chunks_" + dim + "/chunk_" + dim
				+ "_" + cx + "_" + cz + ".json");
		return file.exists();
	}

	public boolean existWorld() {
		File file = new File(path + name + "/world.json");
		return file.exists();
	}

	public boolean existChunkFolder(int dim) {
		File file = new File(path + name + "/chunks_" + dim + "/");
		return file.exists();
	}

	public Chunk getChunk(int dim, int cx, int cz) {
		ChunkKey key = ChunkKey.alloc(dim, cx, cz);
		Chunk chunk;
		chunk = chunks.get(key);
		key.free();
		return chunk;
	}

	public boolean hasChunk(int dim, int cx, int cz) {
		ChunkKey key = ChunkKey.alloc(dim, cx, cz);
		boolean contains;
		contains = chunks.containsKey(key);
		key.free();
		return contains;
	}

	public void addChunk(Chunk chunk) {
		ChunkKey key = ChunkKey.alloc(chunk.dim, chunk.cx, chunk.cz);
		Chunk old = chunks.get(key);
		if (old != null) {
			removeChunk(old);
		}
		chunks.put(key.clone(), chunk);
	}

	public void removeChunk(Chunk chunk) {
		ChunkKey key = ChunkKey.alloc(chunk.dim, chunk.cx, chunk.cz);
		chunks.remove(key);
		key.free();
	}

	public int getCount() {
		int cnt;
		cnt = chunks.size();
		return cnt;
	}

	public byte getGlobalBlock(int dim, int x, int y, int z) {
		int cx = x >> 4;
		int cz = z >> 4;
		Chunk chunk = getChunk(dim, cx, cz);
		if (chunk != null)
			return chunk.getLocalBlock(x, y, z);
		else
			return 1;
	}

	public void removeAll() {
		chunks.clear();
	}

	public double distanceFromPlayer(int x, int z, int i, int k) {
		int xx = x - i;
		int zz = z - k;
		return Math.sqrt(xx * xx + zz * zz);
	}

}