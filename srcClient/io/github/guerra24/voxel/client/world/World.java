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

import io.github.guerra24.voxel.client.kernel.DisplayManager;
import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.kernel.util.Logger;
import io.github.guerra24.voxel.client.resources.GuiResources;
import io.github.guerra24.voxel.client.resources.models.WaterTile;
import io.github.guerra24.voxel.client.world.chunks.Chunk;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class World {

	private static float pos = -0.85f;
	private static double pos2 = 0.0d;
	public int viewDistance = 8;
	private int sizeX = 16;
	private int sizeZ = 16;
	private int x;
	private int z;
	private int octaveCount;
	public float[][] perlinNoiseArray;
	public float time = 0;
	public Chunk chunks[][];

	public void startWorld() {
		initialize();
		createWorld();
	}

	private void createWorld() {
		Kernel.gameResources.guis5.add(Kernel.guiResources.loadW);
		Kernel.gameResources.guis5.add(Kernel.guiResources.loadBar);
		Kernel.gameResources.guis5.remove(GuiResources.load);
		Logger.log(Thread.currentThread(), "Generation World");
		pos = -0.85f;
		for (x = 0; x < viewDistance; x++) {
			for (z = 0; z < viewDistance; z++) {
				pos = (float) (pos + pos2);
				chunks[x][z] = new Chunk(new Vector3f(x * Chunk.CHUNK_SIZE, 0,
						z * Chunk.CHUNK_SIZE));
				Kernel.gameResources.waters
						.add(new WaterTile(x * Chunk.CHUNK_SIZE
								+ WaterTile.TILE_SIZE - 0.5f,
								z * Chunk.CHUNK_SIZE + WaterTile.TILE_SIZE
										- 0.5f, 64.4f));
				Kernel.guiResources.loadBar.setPosition(new Vector2f(pos, 0));
				Kernel.gameResources.guiRenderer
						.render(Kernel.gameResources.guis5);
				DisplayManager.updateDisplay();
			}
		}
		Kernel.gameResources.guis5.remove(Kernel.guiResources.loadW);
		Kernel.gameResources.guis5.remove(Kernel.guiResources.loadBar);
		Kernel.gameResources.guis5.add(GuiResources.load);
	}

	private void initialize() {
		chunks = new Chunk[viewDistance][viewDistance];
		octaveCount = 6;
		perlinNoiseArray = new float[sizeX * viewDistance][];
		perlinNoiseArray = PerlinNoise.GeneratePerlinNoise(
				sizeX * viewDistance, sizeZ * viewDistance, octaveCount);
		if (viewDistance == 16) {
			pos2 = 16 / 6500.0;
		} else if (viewDistance == 8) {
			pos2 = 8 / 800.0;
		}
	}

	public void update() {
		time++;
		if (time == 10) {
			for (int x = 0; x < viewDistance; x++) {
				for (int z = 0; z < viewDistance; z++) {
					chunks[x][z].update();
				}
			}
			time = 0;
		}
	}
}
