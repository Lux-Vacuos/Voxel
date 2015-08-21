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

package io.github.guerra24.voxel.client.kernel.world.chunks;

import io.github.guerra24.voxel.client.kernel.core.Kernel;
import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.graphics.MasterRenderer;
import io.github.guerra24.voxel.client.kernel.graphics.WaterRenderer;
import io.github.guerra24.voxel.client.kernel.resources.models.WaterTile;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.world.block.Block;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;
import io.github.guerra24.voxel.client.kernel.world.entities.Entity;
import io.github.guerra24.voxel.client.kernel.world.entities.Light;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.util.vector.Vector3f;

public class Chunk {

	public int dim, cx, cz, posX, posZ;
	public boolean isToRebuild, sec1NotClear = false, sec2NotClear = false,
			sec3NotClear = false, sec4NotClear = false;
	public byte[][][] blocks;

	private transient Queue<Entity> cubes1, cubes2, cubes3, cubes4;
	private transient Queue<WaterTile> waters;
	private transient List<Light> lights1, lights2, lights3, lights4;
	private int sizeX, sizeY, sizeZ;

	public Chunk(int dim, int cx, int cz) {
		this.dim = dim;
		this.cx = cx;
		this.cz = cz;
		this.posX = cx * 16;
		this.posZ = cz * 16;
		init();
	}

	public void init() {
		sizeX = KernelConstants.CHUNK_SIZE;
		sizeY = KernelConstants.CHUNK_HEIGHT;
		sizeZ = KernelConstants.CHUNK_SIZE;

		cubes1 = new ConcurrentLinkedQueue<Entity>();
		cubes2 = new ConcurrentLinkedQueue<Entity>();
		cubes3 = new ConcurrentLinkedQueue<Entity>();
		cubes4 = new ConcurrentLinkedQueue<Entity>();
		waters = new ConcurrentLinkedQueue<WaterTile>();
		lights1 = new ArrayList<Light>();
		lights2 = new ArrayList<Light>();
		lights3 = new ArrayList<Light>();
		lights4 = new ArrayList<Light>();

		blocks = new byte[sizeX][sizeY][sizeZ];

		createChunk();
		rebuildChunk();
	}

	public void loadInit() {
		cubes1 = new ConcurrentLinkedQueue<Entity>();
		cubes2 = new ConcurrentLinkedQueue<Entity>();
		cubes3 = new ConcurrentLinkedQueue<Entity>();
		cubes4 = new ConcurrentLinkedQueue<Entity>();
		waters = new ConcurrentLinkedQueue<WaterTile>();
		lights1 = new ArrayList<Light>();
		lights2 = new ArrayList<Light>();
		lights3 = new ArrayList<Light>();
		lights4 = new ArrayList<Light>();
	}

	public void update() {
		if (isToRebuild) {
			clear();
			rebuildChunk();
			isToRebuild = false;
		}
		if (cubes1.isEmpty() && cubes2.isEmpty() && cubes3.isEmpty()
				&& cubes4.isEmpty())
			isToRebuild = true;
	}

	public void createChunk() {
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					if (y <= 64)
						blocks[x][y][z] = Block.Water.getId();
				}
			}
		}
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				double tempHegiht = Kernel.world.noise.getNoise(x + cx * 16, z
						+ cz * 16);
				tempHegiht += 1;
				int height = (int) (64 * Maths.clamp(tempHegiht));
				for (int y = 0; y < height; y++) {
					if (y == height - 1 && y > 65)
						blocks[x][y][z] = Block.Grass.getId();
					else if (y == height - 2 && y > 65)
						blocks[x][y][z] = Block.Dirt.getId();
					else if (y == height - 1 && y < 66)
						blocks[x][y][z] = Block.Sand.getId();
					else if (Kernel.world.seed.nextInt(150) == 1 && y < 15)
						blocks[x][y][z] = Block.DiamondOre.getId();
					else if (Kernel.world.seed.nextInt(100) == 1 && y < 25)
						blocks[x][y][z] = Block.GoldOre.getId();
					else
						blocks[x][y][z] = Block.Stone.getId();

					if (y == 0)
						blocks[x][y][z] = Block.Indes.getId();
				}
			}
		}
	}

	/*
	 * MODEL FOR CULLING
	 * 
	 * if (cullFaceWest(x, y, z)) { } if (cullFaceEast(x, y, z)) { } if
	 * (cullFaceDown(x, y, z)) { } if (cullFaceUp(x, y, z)) { } if
	 * (cullFaceNorth(x, y, z)) { } if (cullFaceSouth(x, y, z)) { }
	 */
	public void rebuildChunk() {
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					if (Block.getBlock(blocks[x][y][z]) == Block.Torch) {
						if (y < 32) {
							cubes1.add(Block.getBlock(blocks[x][y][z])
									.getSingleModel(
											new Vector3f(x + cx * sizeX, y, z
													+ cz * sizeZ)));
							lights1.add(new Light(new Vector3f(
									(x + cx * sizeX) + 0.5f, y + 0.8f, (z + cz
											* sizeZ) - 0.5f), new Vector3f(1,
									1, 1), new Vector3f(1, 0.1f, 0.09f)));
							sec1NotClear = true;
						}
						if (y > 31 && y < 64) {
							cubes2.add(Block.getBlock(blocks[x][y][z])
									.getSingleModel(
											new Vector3f(x + cx * sizeX, y, z
													+ cz * sizeZ)));
							lights2.add(new Light(new Vector3f(
									(x + cx * sizeX) + 0.5f, y + 0.8f, (z + cz
											* sizeZ) - 0.5f), new Vector3f(1,
									1, 1), new Vector3f(1, 0.1f, 0.09f)));
							sec2NotClear = true;
						}
						if (y > 63 && y < 96) {
							cubes3.add(Block.getBlock(blocks[x][y][z])
									.getSingleModel(
											new Vector3f(x + cx * sizeX, y, z
													+ cz * sizeZ)));
							lights3.add(new Light(new Vector3f(
									(x + cx * sizeX) + 0.5f, y + 0.8f, (z + cz
											* sizeZ) - 0.5f), new Vector3f(1,
									1, 1), new Vector3f(1, 0.1f, 0.09f)));
							sec3NotClear = true;
						}
						if (y > 95 && y < 129) {
							cubes4.add(Block.getBlock(blocks[x][y][z])
									.getSingleModel(
											new Vector3f(x + cx * sizeX, y, z
													+ cz * sizeZ)));
							lights4.add(new Light(new Vector3f(
									(x + cx * sizeX) + 0.5f, y + 0.8f, (z + cz
											* sizeZ) - 0.5f), new Vector3f(1,
									1, 1), new Vector3f(1, 0.1f, 0.09f)));
							sec4NotClear = true;
						}
					} else if (Block.getBlock(blocks[x][y][z]) != Block.Air
							&& Block.getBlock(blocks[x][y][z]) != Block.Water) {
						if (cullFaceWest(x + cx * sizeX, y, z + cz * sizeZ)) {
							if (y < 32) {
								cubes1.add(Block.getBlock(blocks[x][y][z])
										.getFaceWest(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec1NotClear = true;
							}
							if (y > 31 && y < 64) {
								cubes2.add(Block.getBlock(blocks[x][y][z])
										.getFaceWest(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec2NotClear = true;
							}
							if (y > 63 && y < 96) {
								cubes3.add(Block.getBlock(blocks[x][y][z])
										.getFaceWest(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec3NotClear = true;
							}
							if (y > 95 && y < 129) {
								cubes4.add(Block.getBlock(blocks[x][y][z])
										.getFaceWest(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec4NotClear = true;
							}
						}
						if (cullFaceEast(x + cx * sizeX, y, z + cz * sizeZ)) {
							if (y < 32) {
								cubes1.add(Block.getBlock(blocks[x][y][z])
										.getFaceEast(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec1NotClear = true;
							}
							if (y > 31 && y < 64) {
								cubes2.add(Block.getBlock(blocks[x][y][z])
										.getFaceEast(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec2NotClear = true;
							}
							if (y > 63 && y < 96) {
								cubes3.add(Block.getBlock(blocks[x][y][z])
										.getFaceEast(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec3NotClear = true;
							}
							if (y > 95 && y < 129) {
								cubes4.add(Block.getBlock(blocks[x][y][z])
										.getFaceEast(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec4NotClear = true;
							}
						}
						if (cullFaceDown(x + cx * sizeX, y, z + cz * sizeZ)) {
							if (y < 32) {
								cubes1.add(Block.getBlock(blocks[x][y][z])
										.getFaceDown(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec1NotClear = true;
							}
							if (y > 31 && y < 64) {
								cubes2.add(Block.getBlock(blocks[x][y][z])
										.getFaceDown(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec2NotClear = true;
							}
							if (y > 63 && y < 96) {
								cubes3.add(Block.getBlock(blocks[x][y][z])
										.getFaceDown(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec3NotClear = true;
							}
							if (y > 95 && y < 129) {
								cubes4.add(Block.getBlock(blocks[x][y][z])
										.getFaceDown(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec4NotClear = true;
							}
						}
						if (cullFaceUpSolidBlock(x + cx * sizeX, y, z + cz
								* sizeZ)) {
							if (y < 32) {
								cubes1.add(Block.getBlock(blocks[x][y][z])
										.getFaceUp(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec1NotClear = true;
							}
							if (y > 31 && y < 64) {
								cubes2.add(Block.getBlock(blocks[x][y][z])
										.getFaceUp(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec2NotClear = true;
							}
							if (y > 63 && y < 96) {
								cubes3.add(Block.getBlock(blocks[x][y][z])
										.getFaceUp(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec3NotClear = true;
							}
							if (y > 95 && y < 129) {
								cubes4.add(Block.getBlock(blocks[x][y][z])
										.getFaceUp(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec4NotClear = true;
							}
						}
						if (cullFaceNorth(x + cx * sizeX, y, z + cz * sizeZ)) {
							if (y < 32) {
								cubes1.add(Block.getBlock(blocks[x][y][z])
										.getFaceNorth(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec1NotClear = true;
							}
							if (y > 31 && y < 64) {
								cubes2.add(Block.getBlock(blocks[x][y][z])
										.getFaceNorth(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec2NotClear = true;
							}
							if (y > 63 && y < 96) {
								cubes3.add(Block.getBlock(blocks[x][y][z])
										.getFaceNorth(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec3NotClear = true;
							}
							if (y > 95 && y < 129) {
								cubes4.add(Block.getBlock(blocks[x][y][z])
										.getFaceNorth(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec4NotClear = true;
							}
						}
						if (cullFaceSouth(x + cx * sizeX, y, z + cz * sizeZ)) {
							if (y < 32) {
								cubes1.add(Block.getBlock(blocks[x][y][z])
										.getFaceSouth(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec1NotClear = true;
							}
							if (y > 31 && y < 64) {
								cubes2.add(Block.getBlock(blocks[x][y][z])
										.getFaceSouth(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec2NotClear = true;
							}
							if (y > 63 && y < 96) {
								cubes3.add(Block.getBlock(blocks[x][y][z])
										.getFaceSouth(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec3NotClear = true;
							}
							if (y > 95 && y < 129) {
								cubes4.add(Block.getBlock(blocks[x][y][z])
										.getFaceSouth(
												new Vector3f(x + cx * sizeX, y,
														z + cz * sizeZ)));
								sec4NotClear = true;
							}
						}
					} else if (blocks[x][y][z] == Block.Water.getId()) {
						if (cullFaceUpWater(x + cx * sizeX, y, z + cz * sizeZ)) {
							waters.add(Block.Water.getWaterTitle(new Vector3f(x
									+ cx * sizeX, y, z + cz * sizeZ)));
							sec3NotClear = true;
						}
					}
				}
			}
		}
	}

	public byte getLocalBlock(int x, int y, int z) {
		return blocks[x & 0xF][y & 0x7F][z & 0xF];
	}

	private byte getLocal(int x, int y, int z) {
		return blocks[x & 0xF][y & 0x7F][z & 0xF];
	}

	public void setLocalBlock(int x, int y, int z, byte id) {
		blocks[x & 0xF][y & 0x7F][z & 0xF] = id;
	}

	public float getHeightOfChunk(int x, int z) {
		for (int k = 0; k < sizeY; k++) {
			if (blocks[x & 0xF][k][z & 0xF] != 0) {
				return k;
			}
		}
		return 0;
	}

	private boolean cullFaceWest(int x, int y, int z) {
		if (x > (cx * sizeX) + 1) {
			if (z > (cz * sizeZ) + 1) {
				if (x < (cx * sizeX) + 15) {
					if (z < (cz * sizeZ) + 15) {
						if (getLocal(x - 1, y, z) != Block.Air.getId()
								&& getLocal(x - 1, y, z) != Block.Water.getId()
								&& getLocal(x - 1, y, z) != Block.Glass.getId()
								&& getLocal(x - 1, y, z) != Block.Torch.getId()) {
							return false;
						} else {
							return true;
						}
					}
				}
			}
		}
		if (Kernel.world.getGlobalBlock(dim, x - 1, y, z) != Block.Air.getId()
				&& Kernel.world.getGlobalBlock(dim, x - 1, y, z) != Block.Water
						.getId()
				&& Kernel.world.getGlobalBlock(dim, x - 1, y, z) != Block.Glass
						.getId()
				&& Kernel.world.getGlobalBlock(dim, x - 1, y, z) != Block.Torch
						.getId()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean cullFaceEast(int x, int y, int z) {
		if (x > (cx * sizeX) + 1) {
			if (z > (cz * sizeZ) + 1) {
				if (x < (cx * sizeX) + 15) {
					if (z < (cz * sizeZ) + 15) {
						if (getLocal(x + 1, y, z) != Block.Air.getId()
								&& getLocal(x + 1, y, z) != Block.Water.getId()
								&& getLocal(x + 1, y, z) != Block.Glass.getId()
								&& getLocal(x + 1, y, z) != Block.Torch.getId()) {
							return false;
						} else {
							return true;
						}
					}
				}
			}
		}
		if (Kernel.world.getGlobalBlock(dim, x + 1, y, z) != Block.Air.getId()
				&& Kernel.world.getGlobalBlock(dim, x + 1, y, z) != Block.Water
						.getId()
				&& Kernel.world.getGlobalBlock(dim, x + 1, y, z) != Block.Glass
						.getId()
				&& Kernel.world.getGlobalBlock(dim, x + 1, y, z) != Block.Torch
						.getId()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean cullFaceDown(int x, int y, int z) {
		if (y <= 0) {
			return false;
		} else {
			if (x > (cx * sizeX) + 1) {
				if (z > (cz * sizeZ) + 1) {
					if (x < (cx * sizeX) + 15) {
						if (z < (cz * sizeZ) + 15) {
							if (getLocal(x, y - 1, z) != Block.Air.getId()
									&& getLocal(x, y - 1, z) != Block.Water
											.getId()
									&& getLocal(x, y - 1, z) != Block.Glass
											.getId()
									&& getLocal(x, y - 1, z) != Block.Torch
											.getId()) {
								return false;
							} else {
								return true;
							}
						}
					}
				}
			}
			if (Kernel.world.getGlobalBlock(dim, x, y - 1, z) != Block.Air
					.getId()
					&& Kernel.world.getGlobalBlock(dim, x, y - 1, z) != Block.Water
							.getId()
					&& Kernel.world.getGlobalBlock(dim, x, y - 1, z) != Block.Glass
							.getId()
					&& Kernel.world.getGlobalBlock(dim, x, y - 1, z) != Block.Torch
							.getId()) {
				return false;
			} else {
				return true;
			}
		}
	}

	private boolean cullFaceUpSolidBlock(int x, int y, int z) {
		if (x > (cx * sizeX) + 1) {
			if (z > (cz * sizeZ) + 1) {
				if (x < (cx * sizeX) + 15) {
					if (z < (cz * sizeZ) + 15) {
						if (getLocal(x, y + 1, z) != Block.Air.getId()
								&& getLocal(x, y + 1, z) != Block.Water.getId()
								&& getLocal(x, y + 1, z) != Block.Glass.getId()
								&& getLocal(x, y + 1, z) != Block.Torch.getId()) {
							return false;
						} else {
							return true;
						}
					}
				}
			}
		}
		if (y < sizeY - 1) {
			if (Kernel.world.getGlobalBlock(dim, x, y + 1, z) != Block.Air
					.getId()
					&& Kernel.world.getGlobalBlock(dim, x, y + 1, z) != Block.Water
							.getId()
					&& Kernel.world.getGlobalBlock(dim, x, y + 1, z) != Block.Glass
							.getId()
					&& Kernel.world.getGlobalBlock(dim, x, y + 1, z) != Block.Torch
							.getId()) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private boolean cullFaceUpWater(int x, int y, int z) {
		if (x > (cx * sizeX) + 1) {
			if (z > (cz * sizeZ) + 1) {
				if (x < (cx * sizeX) + 15) {
					if (z < (cz * sizeZ) + 15) {
						if (getLocal(x, y + 1, z) != Block.Air.getId()
								&& getLocal(x, y + 1, z) != Block.Glass.getId()) {
							return false;
						} else {
							return true;
						}
					}
				}
			}
		}
		if (y < sizeY - 1) {
			if (Kernel.world.getGlobalBlock(dim, x, y + 1, z) != Block.Air
					.getId()
					&& Kernel.world.getGlobalBlock(dim, x, y + 1, z) != Block.Glass
							.getId()) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private boolean cullFaceNorth(int x, int y, int z) {
		if (x > (cx * sizeX) + 1) {
			if (z > (cz * sizeZ) + 1) {
				if (x < (cx * sizeX) + 15) {
					if (z < (cz * sizeZ) + 15) {
						if (getLocal(x, y, z - 1) != Block.Air.getId()
								&& getLocal(x, y, z - 1) != Block.Water.getId()
								&& getLocal(x, y, z - 1) != Block.Glass.getId()
								&& getLocal(x, y, z - 1) != Block.Torch.getId()) {
							return false;
						} else {
							return true;
						}
					}
				}
			}
		}
		if (Kernel.world.getGlobalBlock(dim, x, y, z - 1) != Block.Air.getId()
				&& Kernel.world.getGlobalBlock(dim, x, y, z - 1) != Block.Water
						.getId()
				&& Kernel.world.getGlobalBlock(dim, x, y, z - 1) != Block.Glass
						.getId()
				&& Kernel.world.getGlobalBlock(dim, x, y, z - 1) != Block.Torch
						.getId()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean cullFaceSouth(int x, int y, int z) {
		if (x > (cx * sizeX) + 1) {
			if (z > (cz * sizeZ) + 1) {
				if (x < (cx * sizeX) + 15) {
					if (z < (cz * sizeZ) + 15) {
						if (getLocal(x, y, z + 1) != Block.Air.getId()
								&& getLocal(x, y, z + 1) != Block.Water.getId()
								&& getLocal(x, y, z + 1) != Block.Glass.getId()
								&& getLocal(x, y, z + 1) != Block.Torch.getId()) {
							return false;
						} else {
							return true;
						}
					}
				}
			}
		}
		if (Kernel.world.getGlobalBlock(dim, x, y, z + 1) != Block.Air.getId()
				&& Kernel.world.getGlobalBlock(dim, x, y, z + 1) != Block.Water
						.getId()
				&& Kernel.world.getGlobalBlock(dim, x, y, z + 1) != Block.Glass
						.getId()
				&& Kernel.world.getGlobalBlock(dim, x, y, z + 1) != Block.Torch
						.getId()) {
			return false;
		} else {
			return true;
		}
	}

	public void render1(MasterRenderer renderer, Camera camera) {
		renderer.renderChunk(cubes1, lights1, camera);
	}

	public void render2(MasterRenderer renderer, Camera camera) {
		renderer.renderChunk(cubes2, lights2, camera);
	}

	public void render3(MasterRenderer renderer, WaterRenderer waterRenderer,
			Camera camera) {
		renderer.renderChunk(cubes3, lights3, camera);
		waterRenderer.render(waters, camera);
	}

	public void render4(MasterRenderer renderer, Camera camera) {
		renderer.renderChunk(cubes4, lights4, camera);
	}

	public void sendToRenderLights1() {
		Kernel.gameResources.lights.addAll(lights1);
	}

	public void sendToRenderLights2() {
		Kernel.gameResources.lights.addAll(lights2);
	}

	public void sendToRenderLights3() {
		Kernel.gameResources.lights.addAll(lights3);
	}

	public void sendToRenderLights4() {
		Kernel.gameResources.lights.addAll(lights4);
	}

	public void clear() {
		waters.clear();
		cubes1.clear();
		cubes2.clear();
		cubes3.clear();
		cubes4.clear();
		lights1.clear();
		lights2.clear();
		lights3.clear();
		lights4.clear();
	}

	public void dispose() {
		clear();
	}

}