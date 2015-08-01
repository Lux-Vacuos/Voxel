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
import io.github.guerra24.voxel.client.kernel.world.chunks.Chunk;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;

import java.util.HashMap;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

public class World {

	public int time = 0, time2 = 0;
	public HashMap<ChunkKey, Chunk> chunks;
	public Random seed;
	public SimplexNoise noise;

	public void startWorld(Camera camera) {
		initialize();
		createWorld(camera);
	}

	private void initialize() {
		if (KernelConstants.isCustomSeed) {
			seed = new Random(KernelConstants.seed.hashCode());
		} else {
			seed = new Random();
		}
		noise = new SimplexNoise(128, 0.2f, seed.nextInt());
		chunks = new HashMap<ChunkKey, Chunk>();
		Kernel.gameResources.camera.setPosition(new Vector3f(0, 128, 0));
		Kernel.gameResources.player.setPosition(Kernel.gameResources.camera
				.getPosition());
	}

	private void createWorld(Camera camera) {
		int xPlayChunk = (int) (camera.getPosition().x / 16);
		int zPlayChunk = (int) (camera.getPosition().z / 16);
		for (int zr = -5; zr <= 5; zr++) {
			int zz = zPlayChunk + zr;
			for (int xr = -5; xr <= 5; xr++) {
				int xx = xPlayChunk + xr;
				if (zr * zr + xr * xr < 5 * 5) {
					if (!hasChunk(0, xx, zz)) {
						addChunk(new Chunk(0, xx, zz));
					}
				}
			}
		}
	}

	public void updateChunkGeneration(Camera camera) {
		time++;
		if (time % 10 == 0) {
			int xPlayChunk = (int) (camera.getPosition().x / 16);
			int zPlayChunk = (int) (camera.getPosition().z / 16);
			CHUNK_LOADING: for (int zr = -KernelConstants.radius; zr <= KernelConstants.radius; zr++) {
				int zz = zPlayChunk + zr;
				for (int xr = -KernelConstants.radius; xr <= KernelConstants.radius; xr++) {
					int xx = xPlayChunk + xr;
					if (zr * zr + xr * xr < KernelConstants.radius
							* KernelConstants.radius) {
						if (!hasChunk(0, xx, zz)) {
							addChunk(new Chunk(0, xx, zz));
							break CHUNK_LOADING;
						}
					}
				}
			}
			time = 0;
		}
	}

	public void updateChunksRender(Camera camera) {
		time2++;
		if (time % 10 == 0) {
			Kernel.gameResources.cubes.clear();
			Kernel.gameResources.waters.clear();
			int xPlayChunk = (int) (camera.getPosition().x / 16);
			int zPlayChunk = (int) (camera.getPosition().z / 16);
			for (int zr = -KernelConstants.radius; zr <= KernelConstants.radius; zr++) {
				int zz = zPlayChunk + zr;
				for (int xr = -KernelConstants.radius; xr <= KernelConstants.radius; xr++) {
					int xx = xPlayChunk + xr;
					if (zr * zr + xr * xr < KernelConstants.radius
							* KernelConstants.radius) {
						if (hasChunk(0, xx, zz)) {
							Chunk chunk = getChunk(0, xx, zz);
							chunk.update();
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
		int cx = x / 16;
		int cz = z / 16;
		Chunk chunk = getChunk(dim, cx, cz);
		if (chunk != null)
			return chunk.getLocalBlock(x, y, z);
		else
			return 0;
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