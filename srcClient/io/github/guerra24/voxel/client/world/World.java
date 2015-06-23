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

package io.github.guerra24.voxel.client.world;

import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.kernel.util.ArrayList3;
import io.github.guerra24.voxel.client.world.chunks.Chunk;
import io.github.guerra24.voxel.client.world.entities.types.Camera;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

public class World {

	public int viewDistance = 16;

	private int octaveCount, dist = 2, unloadDist = 3;
	public float[][] perlinNoiseArray;
	public int time = 0;
	public ArrayList3<Chunk> chunks;
	public boolean isCustomSeed = true;
	public Random seed;

	public void startWorld() {
		initialize();
		createWorld();
	}

	private void initialize() {
		chunks = new ArrayList3<Chunk>();
		octaveCount = 7;
		if (isCustomSeed) {
			seed = new Random("X".hashCode());
		} else {
			seed = new Random();
		}
		perlinNoiseArray = new float[Chunk.CHUNK_SIZE * viewDistance][];
		perlinNoiseArray = PerlinNoise.GeneratePerlinNoise(Chunk.CHUNK_SIZE
				* viewDistance, Chunk.CHUNK_SIZE * viewDistance, octaveCount);
	}

	private void createWorld() {
		for (int x = 0; x < 2; x++) {
			for (int z = 0; z < 2; z++) {
				chunks.add(new Chunk(new Vector3f(x * Chunk.CHUNK_SIZE, 0, z
						* Chunk.CHUNK_SIZE)));
			}
		}
	}

	public void update(Camera camera) {
		time++;

		/*if (time % 10 == 0) {
			Kernel.gameResources.allEntities.clear();
			Kernel.gameResources.waters.clear();
			for (int x = -dist + (int) camera.getPosition().x; x <= dist
					+ (int) camera.getPosition().x; x++) {
				for (int z = -dist + (int) camera.getPosition().z; z <= dist
						+ (int) camera.getPosition().z; z++) {
					double d = distanceFromPlayer(x * 16, z * 16,
							(int) camera.getPosition().x,
							(int) camera.getPosition().z);
					if (d > unloadDist * 16) {

					} else {

					}
				}
			}
			time = 0;
		}
	*/}

	public byte getBlock(int x, int y, int z) {
		for (Chunk chunk : chunks) {
			return chunk.getBlock(x, y, z);
		}
		return 0;
	}

	public double distanceFromPlayer(int x, int z, int i, int k) {
		int xx = x - i;
		int zz = z - k;
		return Math.sqrt(xx * xx + zz * zz);
	}

}
