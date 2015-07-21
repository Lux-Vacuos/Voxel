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

import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.kernel.KernelConstants;
import io.github.guerra24.voxel.client.kernel.util.Frustum;
import io.github.guerra24.voxel.client.world.chunks.Chunk;
import io.github.guerra24.voxel.client.world.entities.Camera;

import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class World {

	public int time = 0, time2 = 0;
	public Chunk[][] chunks;
	public byte[][][] blocks;
	public byte[][][] water;
	// public SimplexNoise noise;
	public float[][] perlin;
	public Random seed;

	public void startWorld() {
		initialize();
		createWorld();
	}

	private void initialize() {
		chunks = new Chunk[KernelConstants.viewDistance][KernelConstants.viewDistance];
		if (KernelConstants.isCustomSeed) {
			seed = new Random(KernelConstants.seed.hashCode());
		} else {
			seed = new Random();
		}
		// noise = new SimplexNoise(100, 0.1, 5000);
		perlin = new float[KernelConstants.CHUNK_SIZE
				* KernelConstants.viewDistance][];
		perlin = PerlinNoise.GeneratePerlinNoise(KernelConstants.CHUNK_SIZE
				* KernelConstants.viewDistance, KernelConstants.CHUNK_SIZE
				* KernelConstants.viewDistance, KernelConstants.octaveCount);
		blocks = new byte[KernelConstants.viewDistance * 16][144][KernelConstants.viewDistance * 16];
		water = new byte[KernelConstants.viewDistance * 16][144][KernelConstants.viewDistance * 16];
		Kernel.gameResources.camera.setPosition(new Vector3f(
				KernelConstants.viewDistance / 2 * 16, 64,
				KernelConstants.viewDistance / 2 * 16));
		Kernel.gameResources.player.setPosition(Kernel.gameResources.camera
				.getPosition());
	}

	private void createWorld() {
	}

	public void update(Camera camera) {
		time++;

		glMatrixMode(5889);
		glLoadIdentity();
		gluPerspective(KernelConstants.FOV,
				Kernel.gameResources.renderer.aspectRatio,
				KernelConstants.NEAR_PLANE, KernelConstants.FAR_PLANE);
		glMatrixMode(5888);
		Frustum.updateFrustum();

		if (time % 10 == 0) {
			Kernel.gameResources.cubes.clear();
			Kernel.gameResources.waters.clear();
			int xPlayChunk = (int) (camera.getPosition().x / 16);
			int zPlayChunk = (int) (camera.getPosition().z / 16);
			CHUNK_LOAD: for (int zr = -KernelConstants.radius; zr <= KernelConstants.radius; zr++) {
				int zz = zPlayChunk + zr;
				if (zz < 0)
					zz = 0;
				if (zz > KernelConstants.viewDistance - 1)
					zz = KernelConstants.viewDistance - 1;

				for (int xr = -KernelConstants.radius; xr <= KernelConstants.radius; xr++) {
					int xx = xPlayChunk + xr;
					if (xx < 0)
						xx = 0;
					if (xx > KernelConstants.viewDistance - 1)
						xx = KernelConstants.viewDistance - 1;

					if (zr * zr + xr * xr < KernelConstants.radius
							* KernelConstants.radius) {
						if (chunks[xx][zz] == null) {
							chunks[xx][zz] = new Chunk(new Vector3f(xx
									* KernelConstants.CHUNK_SIZE, 0, zz
									* KernelConstants.CHUNK_SIZE));
							break CHUNK_LOAD;
						} else {
							chunks[xx][zz].update();
							if (KernelConstants.advancedOpenGL) {
								if (chunks[xx][zz].sec1NotClear)
									if (Frustum.getFrustum().cubeInFrustum(
											chunks[xx][zz].posX, 0,
											chunks[xx][zz].posZ,
											chunks[xx][zz].posX + 16, 32,
											chunks[xx][zz].posZ + 16))
										chunks[xx][zz].sendToRender1();

								if (chunks[xx][zz].sec2NotClear)
									if (Frustum.getFrustum().cubeInFrustum(
											chunks[xx][zz].posX, 32,
											chunks[xx][zz].posZ,
											chunks[xx][zz].posX + 16, 64,
											chunks[xx][zz].posZ + 16))
										chunks[xx][zz].sendToRender2();

								if (chunks[xx][zz].sec3NotClear)
									if (Frustum.getFrustum().cubeInFrustum(
											chunks[xx][zz].posX, 64,
											chunks[xx][zz].posZ,
											chunks[xx][zz].posX + 16, 96,
											chunks[xx][zz].posZ + 16)) {
										chunks[xx][zz].sendToRender3();
										chunks[xx][zz].sendToRenderWater();
									}
								if (chunks[xx][zz].sec4NotClear)
									if (Frustum.getFrustum().cubeInFrustum(
											chunks[xx][zz].posX, 96,
											chunks[xx][zz].posZ,
											chunks[xx][zz].posX + 16, 128,
											chunks[xx][zz].posZ + 16))
										chunks[xx][zz].sendToRender4();

							} else {
								chunks[xx][zz].sendToRender1();
								chunks[xx][zz].sendToRender2();
								chunks[xx][zz].sendToRender3();
								chunks[xx][zz].sendToRender4();
								chunks[xx][zz].sendToRenderWater();
							}
						}
					}
				}
			}
			time = 0;
		}
	}

	public void test() {
		if (Mouse.isButtonDown(1)) {
			Kernel.gameResources.mouse.update();
			if (Kernel.gameResources.mouse.getCurrentRay().x
					+ Kernel.gameResources.camera.getPosition().x >= 0
					&& Kernel.gameResources.mouse.getCurrentRay().y
							+ Kernel.gameResources.camera.getPosition().y >= 0
					&& Kernel.gameResources.mouse.getCurrentRay().z
							+ Kernel.gameResources.camera.getPosition().z >= 0) {
				blocks[(int) (Kernel.gameResources.mouse.getCurrentRay().x + Kernel.gameResources.camera
						.getPosition().x)][(int) (Kernel.gameResources.mouse
						.getCurrentRay().y + Kernel.gameResources.camera
						.getPosition().y)][(int) (Kernel.gameResources.mouse
						.getCurrentRay().z + Kernel.gameResources.camera
						.getPosition().z)] = 0;
				chunks[(int) (Kernel.gameResources.camera.getPosition().x / 16)][(int) (Kernel.gameResources.camera
						.getPosition().z / 16)].update();
			}
		}
	}

	public byte getBlock(int x, int y, int z) {
		return blocks[x][y][z];
	}

	public double distanceFromPlayer(int x, int z, int i, int k) {
		int xx = x - i;
		int zz = z - k;
		return Math.sqrt(xx * xx + zz * zz);
	}

}