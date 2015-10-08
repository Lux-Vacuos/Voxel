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

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.github.guerra24.voxel.client.kernel.api.VAPI;
import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.models.WaterTile;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.DimensionalWorld;
import io.github.guerra24.voxel.client.kernel.world.block.Block;
import io.github.guerra24.voxel.client.kernel.world.block.BlockEntity;
import io.github.guerra24.voxel.client.kernel.world.entities.Light;

public class Chunk {

	public int dim, cx, cz, posX, posZ;
	public volatile boolean sec1NotClear = false, sec2NotClear = false, sec3NotClear = false, sec4NotClear = false;
	public byte[][][] blocks;

	private transient Queue<BlockEntity> cubes1, cubes2, cubes3, cubes4;
	private transient Queue<BlockEntity> cubes1temp, cubes2temp, cubes3temp, cubes4temp;
	private transient Queue<WaterTile> waters;
	private transient Queue<WaterTile> waterstemp;
	private transient List<Light> lights1, lights2, lights3, lights4;
	private transient int sizeX, sizeY, sizeZ;
	private transient boolean readyToRender1 = true, readyToRender2 = true, readyToRender3 = true,
			readyToRender4 = true;
	public transient boolean rebuild1 = false, rebuild2 = false, rebuild3 = false, rebuild4 = false;

	public Chunk(int dim, int cx, int cz, DimensionalWorld world) {
		this.dim = dim;
		this.cx = cx;
		this.cz = cz;
		this.posX = cx * 16;
		this.posZ = cz * 16;
		init(world);
	}

	public void init(DimensionalWorld world) {
		loadInit();

		blocks = new byte[sizeX][sizeY][sizeZ];

		createChunk(world);
		rebuildChunk1(world);
		rebuildChunk2(world);
		rebuildChunk3(world);
		rebuildChunk4(world);
	}

	public void loadInit() {
		cubes1 = new ConcurrentLinkedQueue<BlockEntity>();
		cubes2 = new ConcurrentLinkedQueue<BlockEntity>();
		cubes3 = new ConcurrentLinkedQueue<BlockEntity>();
		cubes4 = new ConcurrentLinkedQueue<BlockEntity>();
		cubes1temp = new ConcurrentLinkedQueue<BlockEntity>();
		cubes2temp = new ConcurrentLinkedQueue<BlockEntity>();
		cubes3temp = new ConcurrentLinkedQueue<BlockEntity>();
		cubes4temp = new ConcurrentLinkedQueue<BlockEntity>();
		waters = new ConcurrentLinkedQueue<WaterTile>();
		waterstemp = new ConcurrentLinkedQueue<WaterTile>();
		lights1 = new ArrayList<Light>();
		lights2 = new ArrayList<Light>();
		lights3 = new ArrayList<Light>();
		lights4 = new ArrayList<Light>();
		sizeX = KernelConstants.CHUNK_SIZE;
		sizeY = KernelConstants.CHUNK_HEIGHT;
		sizeZ = KernelConstants.CHUNK_SIZE;
	}

	public void update1(DimensionalWorld world) {
		if (rebuild1) {
			cubes1temp.addAll(cubes1);
			readyToRender1 = false;
			clear();
			rebuildChunk1(world);
			readyToRender1 = true;
			cubes1temp.clear();
			rebuild1 = false;
		}
	}

	public void update2(DimensionalWorld world) {
		if (rebuild2) {
			cubes2temp.addAll(cubes2);
			readyToRender2 = false;
			clear();
			rebuildChunk2(world);
			readyToRender2 = true;
			cubes2temp.clear();
			rebuild2 = false;
		}
	}

	public void update3(DimensionalWorld world) {
		waterstemp.addAll(waters);
		cubes3temp.addAll(cubes3);
		readyToRender3 = false;
		clear();
		rebuildChunk3(world);
		readyToRender3 = true;
		cubes3temp.clear();
		waterstemp.clear();
	}

	public void update4(DimensionalWorld world) {
		if (rebuild4) {
			cubes4temp.addAll(cubes4);
			readyToRender4 = false;
			clear();
			rebuildChunk4(world);
			readyToRender4 = true;
			cubes4temp.clear();
			rebuild4 = false;
		}
	}

	public void createChunk(DimensionalWorld world) {
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
				double tempHegiht = world.getNoise().getNoise(x + cx * 16, z + cz * 16);
				tempHegiht += 1;
				int height = (int) (64 * Maths.clamp(tempHegiht));
				for (int y = 0; y < height; y++) {
					if (y == height - 1 && y > 65)
						blocks[x][y][z] = Block.Grass.getId();
					else if (y == height - 2 && y > 65)
						blocks[x][y][z] = Block.Dirt.getId();
					else if (y == height - 1 && y < 66)
						blocks[x][y][z] = Block.Sand.getId();
					else if (world.getSeed().nextInt(150) == 1 && y < 15)
						blocks[x][y][z] = Block.DiamondOre.getId();
					else if (world.getSeed().nextInt(100) == 1 && y < 25)
						blocks[x][y][z] = Block.GoldOre.getId();
					else
						blocks[x][y][z] = Block.Stone.getId();

					for (int i = 0; i < VAPI.getWorldAPI().getLastID(); i++) {
						VAPI.getWorldAPI().getChunkHandler(i).chunkGen(blocks, x, y, z);
					}

					if (y == 0)
						blocks[x][y][z] = Block.Indes.getId();
				}
			}
		}
	}

	public void rebuildChunk1(DimensionalWorld world) {
		sec1NotClear = false;
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < 32; y++) {
					if (Block.getBlock(blocks[x][y][z]) == Block.Torch) {
						if (cullFaceDown(x, y, z) && cullFaceEast(x, y, z, world) && cullFaceNorth(x, y, z, world)
								&& cullFaceSouth(x, y, z, world) && cullFaceUpSolidBlock(x, y, z)
								&& cullFaceWest(x, y, z, world)) {
							cubes1.add(Block.getBlock(blocks[x][y][z])
									.getSingleModel(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							lights1.add(
									new Light(new Vector3f((x + cx * sizeX) + 0.5f, y + 0.8f, (z + cz * sizeZ) - 0.5f),
											new Vector3f(1, 1, 1), new Vector3f(1, 0.1f, 0.09f)));
							sec1NotClear = true;
						}
					} else if (Block.getBlock(blocks[x][y][z]) != Block.Air
							&& Block.getBlock(blocks[x][y][z]) != Block.Water) {
						if (cullFaceWest(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes1.add(Block.getBlock(blocks[x][y][z])
									.getFaceWest(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec1NotClear = true;
						}
						if (cullFaceEast(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes1.add(Block.getBlock(blocks[x][y][z])
									.getFaceEast(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec1NotClear = true;
						}
						if (cullFaceDown(x + cx * sizeX, y, z + cz * sizeZ)) {
							cubes1.add(Block.getBlock(blocks[x][y][z])
									.getFaceDown(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec1NotClear = true;
						}
						if (cullFaceUpSolidBlock(x + cx * sizeX, y, z + cz * sizeZ)) {
							cubes1.add(Block.getBlock(blocks[x][y][z])
									.getFaceUp(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec1NotClear = true;
						}
						if (cullFaceNorth(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes1.add(Block.getBlock(blocks[x][y][z])
									.getFaceNorth(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec1NotClear = true;
						}
						if (cullFaceSouth(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes1.add(Block.getBlock(blocks[x][y][z])
									.getFaceSouth(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec1NotClear = true;
						}
					}
				}
			}
		}
	}

	public void rebuildChunk2(DimensionalWorld world) {
		sec2NotClear = false;
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 32; y < 64; y++) {
					if (Block.getBlock(blocks[x][y][z]) == Block.Torch) {
						if (cullFaceDown(x, y, z) && cullFaceEast(x, y, z, world) && cullFaceNorth(x, y, z, world)
								&& cullFaceSouth(x, y, z, world) && cullFaceUpSolidBlock(x, y, z)
								&& cullFaceWest(x, y, z, world)) {
							cubes2.add(Block.getBlock(blocks[x][y][z])
									.getSingleModel(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							lights2.add(
									new Light(new Vector3f((x + cx * sizeX) + 0.5f, y + 0.8f, (z + cz * sizeZ) - 0.5f),
											new Vector3f(1, 1, 1), new Vector3f(1, 0.1f, 0.09f)));
							sec2NotClear = true;
						}
					} else if (Block.getBlock(blocks[x][y][z]) != Block.Air
							&& Block.getBlock(blocks[x][y][z]) != Block.Water) {
						if (cullFaceWest(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes2.add(Block.getBlock(blocks[x][y][z])
									.getFaceWest(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec2NotClear = true;
						}
						if (cullFaceEast(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes2.add(Block.getBlock(blocks[x][y][z])
									.getFaceEast(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec2NotClear = true;
						}
						if (cullFaceDown(x + cx * sizeX, y, z + cz * sizeZ)) {
							cubes2.add(Block.getBlock(blocks[x][y][z])
									.getFaceDown(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec2NotClear = true;
						}
						if (cullFaceUpSolidBlock(x + cx * sizeX, y, z + cz * sizeZ)) {
							cubes2.add(Block.getBlock(blocks[x][y][z])
									.getFaceUp(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec2NotClear = true;
						}
						if (cullFaceNorth(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes2.add(Block.getBlock(blocks[x][y][z])
									.getFaceNorth(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec2NotClear = true;
						}
						if (cullFaceSouth(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes2.add(Block.getBlock(blocks[x][y][z])
									.getFaceSouth(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec2NotClear = true;
						}
					}
				}
			}
		}
	}

	public void rebuildChunk3(DimensionalWorld world) {
		sec3NotClear = false;
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 64; y < 96; y++) {
					if (Block.getBlock(blocks[x][y][z]) == Block.Torch) {
						cubes3.add(Block.getBlock(blocks[x][y][z])
								.getSingleModel(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
						lights3.add(new Light(new Vector3f((x + cx * sizeX) + 0.5f, y + 0.8f, (z + cz * sizeZ) - 0.5f),
								new Vector3f(1, 1, 1), new Vector3f(1, 0.1f, 0.09f)));
						sec3NotClear = true;
					} else if (Block.getBlock(blocks[x][y][z]).usesSingleModel()) {
						cubes3.add(Block.getBlock(blocks[x][y][z])
								.getSingleModel(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
						sec3NotClear = true;
					} else if (Block.getBlock(blocks[x][y][z]) != Block.Air
							&& Block.getBlock(blocks[x][y][z]) != Block.Water
							&& !Block.getBlock(blocks[x][y][z]).usesSingleModel()) {
						if (cullFaceWest(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes3.add(Block.getBlock(blocks[x][y][z])
									.getFaceWest(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec3NotClear = true;
						}
						if (cullFaceEast(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes3.add(Block.getBlock(blocks[x][y][z])
									.getFaceEast(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec3NotClear = true;
						}
						if (cullFaceDown(x + cx * sizeX, y, z + cz * sizeZ)) {
							cubes3.add(Block.getBlock(blocks[x][y][z])
									.getFaceDown(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec3NotClear = true;
						}
						if (cullFaceUpSolidBlock(x + cx * sizeX, y, z + cz * sizeZ)) {
							cubes3.add(Block.getBlock(blocks[x][y][z])
									.getFaceUp(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec3NotClear = true;
						}
						if (cullFaceNorth(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes3.add(Block.getBlock(blocks[x][y][z])
									.getFaceNorth(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec3NotClear = true;
						}
						if (cullFaceSouth(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes3.add(Block.getBlock(blocks[x][y][z])
									.getFaceSouth(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec3NotClear = true;
						}
					} else if (blocks[x][y][z] == Block.Water.getId()) {
						if (cullFaceUpWater(x + cx * sizeX, y, z + cz * sizeZ)) {
							waters.add(Block.Water.getWaterTitle(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec3NotClear = true;
						}
					}
				}
			}
		}
	}

	public void rebuildChunk4(DimensionalWorld world) {
		sec4NotClear = false;
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 96; y < 128; y++) {
					if (Block.getBlock(blocks[x][y][z]) == Block.Torch) {
						if (cullFaceDown(x, y, z) && cullFaceEast(x, y, z, world) && cullFaceNorth(x, y, z, world)
								&& cullFaceSouth(x, y, z, world) && cullFaceUpSolidBlock(x, y, z)
								&& cullFaceWest(x, y, z, world)) {
							cubes4.add(Block.getBlock(blocks[x][y][z])
									.getSingleModel(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							lights4.add(
									new Light(new Vector3f((x + cx * sizeX) + 0.5f, y + 0.8f, (z + cz * sizeZ) - 0.5f),
											new Vector3f(1, 1, 1), new Vector3f(1, 0.1f, 0.09f)));
							sec4NotClear = true;
						}
					} else if (Block.getBlock(blocks[x][y][z]) != Block.Air
							&& Block.getBlock(blocks[x][y][z]) != Block.Water) {
						if (cullFaceWest(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes4.add(Block.getBlock(blocks[x][y][z])
									.getFaceWest(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec4NotClear = true;
						}
						if (cullFaceEast(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes4.add(Block.getBlock(blocks[x][y][z])
									.getFaceEast(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec4NotClear = true;
						}
						if (cullFaceDown(x + cx * sizeX, y, z + cz * sizeZ)) {
							cubes4.add(Block.getBlock(blocks[x][y][z])
									.getFaceDown(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec4NotClear = true;
						}
						if (cullFaceUpSolidBlock(x + cx * sizeX, y, z + cz * sizeZ)) {
							cubes4.add(Block.getBlock(blocks[x][y][z])
									.getFaceUp(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec4NotClear = true;
						}
						if (cullFaceNorth(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes4.add(Block.getBlock(blocks[x][y][z])
									.getFaceNorth(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec4NotClear = true;
						}
						if (cullFaceSouth(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes4.add(Block.getBlock(blocks[x][y][z])
									.getFaceSouth(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							sec4NotClear = true;
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

	private boolean cullFaceWest(int x, int y, int z, DimensionalWorld world) {
		if (x > (cx * sizeX) + 1) {
			if (z > (cz * sizeZ)) {
				if (x < (cx * sizeX) + 16) {
					if (z < (cz * sizeZ) + 16) {
						if (getLocal(x - 1, y, z) != Block.Air.getId() && getLocal(x - 1, y, z) != Block.Water.getId()
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
		if (world.getGlobalBlock(dim, x - 1, y, z) != Block.Air.getId()
				&& world.getGlobalBlock(dim, x - 1, y, z) != Block.Water.getId()
				&& world.getGlobalBlock(dim, x - 1, y, z) != Block.Glass.getId()
				&& world.getGlobalBlock(dim, x - 1, y, z) != Block.Torch.getId()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean cullFaceEast(int x, int y, int z, DimensionalWorld world) {
		if (x > (cx * sizeX)) {
			if (z > (cz * sizeZ)) {
				if (x < (cx * sizeX) + 15) {
					if (z < (cz * sizeZ) + 16) {
						if (getLocal(x + 1, y, z) != Block.Air.getId() && getLocal(x + 1, y, z) != Block.Water.getId()
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
		if (world.getGlobalBlock(dim, x + 1, y, z) != Block.Air.getId()
				&& world.getGlobalBlock(dim, x + 1, y, z) != Block.Water.getId()
				&& world.getGlobalBlock(dim, x + 1, y, z) != Block.Glass.getId()
				&& world.getGlobalBlock(dim, x + 1, y, z) != Block.Torch.getId()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean cullFaceDown(int x, int y, int z) {
		if (x >= (cx * sizeX)) {
			if (z >= (cz * sizeZ)) {
				if (x < (cx * sizeX) + 16) {
					if (z < (cz * sizeZ) + 16) {
						if (getLocal(x, y - 1, z) != Block.Air.getId() && getLocal(x, y - 1, z) != Block.Water.getId()
								&& getLocal(x, y - 1, z) != Block.Glass.getId()
								&& getLocal(x, y - 1, z) != Block.Torch.getId()) {
							return false;
						} else {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean cullFaceUpSolidBlock(int x, int y, int z) {
		if (x >= (cx * sizeX)) {
			if (z >= (cz * sizeZ)) {
				if (x < (cx * sizeX) + 16) {
					if (z < (cz * sizeZ) + 16) {
						if (getLocal(x, y + 1, z) != Block.Air.getId() && getLocal(x, y + 1, z) != Block.Water.getId()
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
		return false;
	}

	private boolean cullFaceUpWater(int x, int y, int z) {
		if (x >= (cx * sizeX)) {
			if (z >= (cz * sizeZ)) {
				if (x < (cx * sizeX) + 16) {
					if (z < (cz * sizeZ) + 16) {
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
		return false;
	}

	private boolean cullFaceNorth(int x, int y, int z, DimensionalWorld world) {
		if (x > (cx * sizeX)) {
			if (z > (cz * sizeZ) + 1) {
				if (x < (cx * sizeX) + 16) {
					if (z < (cz * sizeZ) + 15) {
						if (getLocal(x, y, z - 1) != Block.Air.getId() && getLocal(x, y, z - 1) != Block.Water.getId()
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
		if (world.getGlobalBlock(dim, x, y, z - 1) != Block.Air.getId()
				&& world.getGlobalBlock(dim, x, y, z - 1) != Block.Water.getId()
				&& world.getGlobalBlock(dim, x, y, z - 1) != Block.Glass.getId()
				&& world.getGlobalBlock(dim, x, y, z - 1) != Block.Torch.getId()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean cullFaceSouth(int x, int y, int z, DimensionalWorld world) {
		if (x > (cx * sizeX)) {
			if (z > (cz * sizeZ)) {
				if (x < (cx * sizeX) + 16) {
					if (z < (cz * sizeZ) + 15) {
						if (getLocal(x, y, z + 1) != Block.Air.getId() && getLocal(x, y, z + 1) != Block.Water.getId()
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
		if (world.getGlobalBlock(dim, x, y, z + 1) != Block.Air.getId()
				&& world.getGlobalBlock(dim, x, y, z + 1) != Block.Water.getId()
				&& world.getGlobalBlock(dim, x, y, z + 1) != Block.Glass.getId()
				&& world.getGlobalBlock(dim, x, y, z + 1) != Block.Torch.getId()) {
			return false;
		} else {
			return true;
		}
	}

	public void render1(GameResources gm) {
		if (readyToRender1)
			gm.getRenderer().renderChunk(cubes1, lights1, gm);
		else
			gm.getRenderer().renderChunk(cubes1temp, lights1, gm);
	}

	public void render2(GameResources gm) {
		if (readyToRender2)
			gm.getRenderer().renderChunk(cubes2, lights2, gm);
		else
			gm.getRenderer().renderChunk(cubes2temp, lights2, gm);
	}

	public void render3(GameResources gm) {
		if (readyToRender3) {
			gm.getRenderer().renderChunk(cubes3, lights3, gm);
			gm.getWaterRenderer().render(waters, gm);
		} else {
			gm.getRenderer().renderChunk(cubes3temp, lights3, gm);
			gm.getWaterRenderer().render(waterstemp, gm);
		}
	}

	public void render4(GameResources gm) {
		if (readyToRender4)
			gm.getRenderer().renderChunk(cubes4, lights4, gm);
		else
			gm.getRenderer().renderChunk(cubes4temp, lights4, gm);
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