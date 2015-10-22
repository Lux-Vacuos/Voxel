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

/**
 * Chunk
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category World
 */
public class Chunk {

	/**
	 * Chunk Data
	 */
	public int dim, cx, cz, posX, posZ;
	public byte[][][] blocks;
	public volatile boolean sec1NotClear = false, sec2NotClear = false, sec3NotClear = false, sec4NotClear = false;
	private transient Queue<BlockEntity> cubes1, cubes2, cubes3, cubes4;
	private transient Queue<WaterTile> water1, water2, water3, water4;
	private transient Queue<WaterTile> water1temp, water2temp, water3temp, water4temp;
	private transient Queue<BlockEntity> cubes1temp, cubes2temp, cubes3temp, cubes4temp;
	private transient Queue<Light> lights1, lights2, lights3, lights4;
	private transient int sizeX, sizeY, sizeZ;
	private transient boolean readyToRender1 = true, readyToRender2 = true, readyToRender3 = true,
			readyToRender4 = true;
	public transient boolean rebuilding = false;
	public transient boolean needsRebuild = true;

	/**
	 * Constructor
	 * 
	 * @param dim
	 *            Chunk Dimension
	 * @param cx
	 *            Chunk X
	 * @param cz
	 *            Chunk Z
	 * @param world
	 *            Dimensional World
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public Chunk(int dim, int cx, int cz, DimensionalWorld world) {
		this.dim = dim;
		this.cx = cx;
		this.cz = cz;
		this.posX = cx * 16;
		this.posZ = cz * 16;
		init(world);
	}

	/**
	 * Initialize Chunk
	 * 
	 * @param world
	 *            Dimensional World
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void init(DimensionalWorld world) {
		createList();
		blocks = new byte[sizeX][sizeY][sizeZ];
		createBasicTerrain(world);
		createStructures(world.getChunkGenerator(), world);
	}

	/**
	 * Initialize List
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void createList() {
		cubes1 = new ConcurrentLinkedQueue<BlockEntity>();
		cubes2 = new ConcurrentLinkedQueue<BlockEntity>();
		cubes3 = new ConcurrentLinkedQueue<BlockEntity>();
		cubes4 = new ConcurrentLinkedQueue<BlockEntity>();
		cubes1temp = new ConcurrentLinkedQueue<BlockEntity>();
		cubes2temp = new ConcurrentLinkedQueue<BlockEntity>();
		cubes3temp = new ConcurrentLinkedQueue<BlockEntity>();
		cubes4temp = new ConcurrentLinkedQueue<BlockEntity>();
		water1 = new ConcurrentLinkedQueue<WaterTile>();
		water2 = new ConcurrentLinkedQueue<WaterTile>();
		water3 = new ConcurrentLinkedQueue<WaterTile>();
		water4 = new ConcurrentLinkedQueue<WaterTile>();
		water1temp = new ConcurrentLinkedQueue<WaterTile>();
		water2temp = new ConcurrentLinkedQueue<WaterTile>();
		water3temp = new ConcurrentLinkedQueue<WaterTile>();
		water4temp = new ConcurrentLinkedQueue<WaterTile>();
		lights1 = new ConcurrentLinkedQueue<Light>();
		lights2 = new ConcurrentLinkedQueue<Light>();
		lights3 = new ConcurrentLinkedQueue<Light>();
		lights4 = new ConcurrentLinkedQueue<Light>();
		sizeX = KernelConstants.CHUNK_SIZE;
		sizeY = KernelConstants.CHUNK_HEIGHT;
		sizeZ = KernelConstants.CHUNK_SIZE;
	}

	public void update1(DimensionalWorld world) {
		water1temp.addAll(water1);
		cubes1temp.addAll(cubes1);
		readyToRender1 = false;
		water1.clear();
		cubes1.clear();
		lights1.clear();
		rebuildChunkSection(sec1NotClear, lights1, cubes1, water1, world, 0, 32);
		calculateLight(cubes1, world);
		readyToRender1 = true;
		cubes1temp.clear();
		water1temp.clear();
	}

	public void update2(DimensionalWorld world) {
		water2temp.addAll(water2);
		cubes2temp.addAll(cubes2);
		readyToRender2 = false;
		water2.clear();
		cubes2.clear();
		lights2.clear();
		rebuildChunkSection(sec2NotClear, lights2, cubes2, water2, world, 32, 64);
		calculateLight(cubes2, world);
		readyToRender2 = true;
		cubes2temp.clear();
		water2temp.clear();
	}

	public void update3(DimensionalWorld world) {
		water3temp.addAll(water3);
		cubes3temp.addAll(cubes3);
		readyToRender3 = false;
		water3.clear();
		cubes3.clear();
		lights3.clear();
		rebuildChunkSection(sec3NotClear, lights3, cubes3, water3, world, 64, 96);
		calculateLight(cubes3, world);
		readyToRender3 = true;
		cubes3temp.clear();
		water3temp.clear();
	}

	public void update4(DimensionalWorld world) {
		water4temp.addAll(water4);
		cubes4temp.addAll(cubes4);
		readyToRender4 = false;
		water4.clear();
		cubes4.clear();
		lights4.clear();
		rebuildChunkSection(sec4NotClear, lights4, cubes4, water4, world, 96, 128);
		calculateLight(cubes4, world);
		readyToRender4 = true;
		cubes4temp.clear();
		water4temp.clear();
	}

	private void createBasicTerrain(DimensionalWorld world) {
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
				double tempHeight = world.getNoise().getNoise(x + cx * 16, z + cz * 16);
				tempHeight += 1;
				int height = (int) (64 * Maths.clamp(tempHeight));
				for (int y = 0; y < height; y++) {
					if (y == height - 1 && y > 65)
						blocks[x][y][z] = Block.Grass.getId();
					else if (y == height - 2 && y > 65)
						blocks[x][y][z] = Block.Dirt.getId();
					else if (y == height - 1 && y < 66)
						blocks[x][y][z] = Block.Sand.getId();
					else
						blocks[x][y][z] = Block.Stone.getId();

					for (int i = 0; i < VAPI.getWorldAPI().getLastID(); i++) {
						VAPI.getWorldAPI().getChunkHandler(i).chunkGen(blocks, x, y, z);
					}

				}
			}
		}
	}

	private void createStructures(ChunkGenerator generator, DimensionalWorld world) {
		// generator.generateCaves(blocks, world, sizeX, sizeZ, sizeY, cx, cz);
		for (int i = 0; i < 4; i++) {
			int xx = Maths.randInt(4, 12);
			int zz = Maths.randInt(4, 12);
			double tempHeight = world.getNoise().getNoise(xx + cx * 16, zz + cz * 16);
			tempHeight += 1;
			int height = (int) (64 * Maths.clamp(tempHeight));
			int h = getLocalBlock(xx, height - 1, zz);
			if (h == Block.Grass.getId() || h == Block.Dirt.getId())
				generator.addTree(blocks, xx, height, zz, Maths.randInt(4, 10), world.getSeed());
		}
	}

	public void rebuildChunkSection(boolean secClear, Queue<Light> lights, Queue<BlockEntity> cubes,
			Queue<WaterTile> water, DimensionalWorld world, int minY, int maxY) {
		secClear = false;
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = minY; y < maxY; y++) {
					if (Block.getBlock(blocks[x][y][z]) == Block.Torch) {
						cubes.add(Block.getBlock(blocks[x][y][z])
								.getSingleModel(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
						lights.add(new Light(new Vector3f((x + cx * sizeX) + 0.5f, y + 0.8f, (z + cz * sizeZ) - 0.5f),
								new Vector3f(1, 1, 1), new Vector3f(1, 0.1f, 0.09f)));
						secClear = true;
					} else if (Block.getBlock(blocks[x][y][z]).usesSingleModel()) {
						cubes.add(Block.getBlock(blocks[x][y][z])
								.getSingleModel(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
					} else if (Block.getBlock(blocks[x][y][z]) != Block.Air
							&& Block.getBlock(blocks[x][y][z]) != Block.Water) {
						if (cullFaceWest(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes.add(Block.getBlock(blocks[x][y][z])
									.getFaceWest(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							secClear = true;
						}
						if (cullFaceEast(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes.add(Block.getBlock(blocks[x][y][z])
									.getFaceEast(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							secClear = true;
						}
						if (cullFaceDown(x + cx * sizeX, y, z + cz * sizeZ)) {
							cubes.add(Block.getBlock(blocks[x][y][z])
									.getFaceDown(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							secClear = true;
						}
						if (cullFaceUpSolidBlock(x + cx * sizeX, y, z + cz * sizeZ)) {
							cubes.add(Block.getBlock(blocks[x][y][z])
									.getFaceUp(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							secClear = true;
						}
						if (cullFaceNorth(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes.add(Block.getBlock(blocks[x][y][z])
									.getFaceNorth(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							secClear = true;
						}
						if (cullFaceSouth(x + cx * sizeX, y, z + cz * sizeZ, world)) {
							cubes.add(Block.getBlock(blocks[x][y][z])
									.getFaceSouth(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							secClear = true;
						}
					} else if (Block.getBlock(blocks[x][y][z]) == Block.Water) {
						if (cullFaceUpWater(x + cx * sizeX, y, z + cz * sizeZ)) {
							water.add(Block.Water.getWaterTitle(new Vector3f(x + cx * sizeX, y, z + cz * sizeZ)));
							secClear = true;
						}
					}
				}
			}
		}
	}

	private void calculateLight(Queue<BlockEntity> cubes, DimensionalWorld world) {
		for (BlockEntity blockEntity : cubes) {
			int x, y, z;
			x = (int) blockEntity.getPosition().x;
			y = (int) blockEntity.getPosition().y;
			z = (int) blockEntity.getPosition().z;
			switch (blockEntity.getSide()) {
			case "UP":
				blockEntity.setLocalLight(getLight(world, x, y, z));
				break;
			case "DOWN":
				blockEntity.setLocalLight(getLight(world, x, y, z));
				break;
			case "EAST":
				blockEntity.setLocalLight(getLight(world, x + 1, y, z));
				break;
			case "WEST":
				blockEntity.setLocalLight(getLight(world, x - 1, y, z));
				break;
			case "NORTH":
				blockEntity.setLocalLight(getLight(world, x, y, z - 1));
				break;
			case "SOUTH":
				blockEntity.setLocalLight(getLight(world, x, y, z + 1));
				break;
			case "SINGLE MODEL":
				blockEntity.setLocalLight(getLight(world, x, y, z));
				break;
			}
		}
	}

	public byte getLocalBlock(int x, int y, int z) {
		return blocks[x & 0xF][y & 0x7F][z & 0xF];
	}

	public void setLocalBlock(int x, int y, int z, byte id) {
		blocks[x & 0xF][y & 0x7F][z & 0xF] = id;
	}

	private float getLight(DimensionalWorld world, int x, int blockPos, int z) {
		float result = 1;
		for (int y = 128; y > blockPos; y--) {
			if (x > ((cx * sizeX) + 1) && (z > (cz * sizeZ) + 1) && (x < (cx * sizeX) + 15)
					&& (z < (cz * sizeZ) + 15)) {
				if (getLocalBlock(x, y, z) != Block.Air.getId())
					result -= 0.05f;
			} else {
				if (world.getGlobalBlock(dim, x, y, z) != Block.Air.getId())
					result -= 0.05f;
			}
		}
		return result;
	}

	private boolean cullFaceWest(int x, int y, int z, DimensionalWorld world) {
		if (x > (cx * sizeX) + 1) {
			if (z > (cz * sizeZ)) {
				if (x < (cx * sizeX) + 16) {
					if (z < (cz * sizeZ) + 16) {
						if (getLocalBlock(x - 1, y, z) != Block.Air.getId()
								&& getLocalBlock(x - 1, y, z) != Block.Water.getId()
								&& getLocalBlock(x - 1, y, z) != Block.Glass.getId()
								&& getLocalBlock(x - 1, y, z) != Block.Torch.getId()
								&& getLocalBlock(x - 1, y, z) != Block.Leaves.getId()) {
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
				&& world.getGlobalBlock(dim, x - 1, y, z) != Block.Torch.getId()
				&& world.getGlobalBlock(dim, x - 1, y, z) != Block.Leaves.getId()) {
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
						if (getLocalBlock(x + 1, y, z) != Block.Air.getId()
								&& getLocalBlock(x + 1, y, z) != Block.Water.getId()
								&& getLocalBlock(x + 1, y, z) != Block.Glass.getId()
								&& getLocalBlock(x + 1, y, z) != Block.Torch.getId()
								&& getLocalBlock(x + 1, y, z) != Block.Leaves.getId()) {
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
				&& world.getGlobalBlock(dim, x + 1, y, z) != Block.Torch.getId()
				&& world.getGlobalBlock(dim, x + 1, y, z) != Block.Leaves.getId()) {
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
						if (getLocalBlock(x, y - 1, z) != Block.Air.getId()
								&& getLocalBlock(x, y - 1, z) != Block.Water.getId()
								&& getLocalBlock(x, y - 1, z) != Block.Glass.getId()
								&& getLocalBlock(x, y - 1, z) != Block.Torch.getId()
								&& getLocalBlock(x, y - 1, z) != Block.Leaves.getId()) {
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
						if (getLocalBlock(x, y + 1, z) != Block.Air.getId()
								&& getLocalBlock(x, y + 1, z) != Block.Water.getId()
								&& getLocalBlock(x, y + 1, z) != Block.Glass.getId()
								&& getLocalBlock(x, y + 1, z) != Block.Torch.getId()
								&& getLocalBlock(x, y + 1, z) != Block.Leaves.getId()) {
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
						if (getLocalBlock(x, y + 1, z) != Block.Air.getId()
								&& getLocalBlock(x, y + 1, z) != Block.Glass.getId()
								&& getLocalBlock(x, y + 1, z) != Block.Leaves.getId()) {
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
						if (getLocalBlock(x, y, z - 1) != Block.Air.getId()
								&& getLocalBlock(x, y, z - 1) != Block.Water.getId()
								&& getLocalBlock(x, y, z - 1) != Block.Glass.getId()
								&& getLocalBlock(x, y, z - 1) != Block.Torch.getId()
								&& getLocalBlock(x, y, z - 1) != Block.Leaves.getId()) {
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
				&& world.getGlobalBlock(dim, x, y, z - 1) != Block.Torch.getId()
				&& world.getGlobalBlock(dim, x, y, z - 1) != Block.Leaves.getId()) {
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
						if (getLocalBlock(x, y, z + 1) != Block.Air.getId()
								&& getLocalBlock(x, y, z + 1) != Block.Water.getId()
								&& getLocalBlock(x, y, z + 1) != Block.Glass.getId()
								&& getLocalBlock(x, y, z + 1) != Block.Torch.getId()
								&& getLocalBlock(x, y, z + 1) != Block.Leaves.getId()) {
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
				&& world.getGlobalBlock(dim, x, y, z + 1) != Block.Torch.getId()
				&& world.getGlobalBlock(dim, x, y, z + 1) != Block.Leaves.getId()) {
			return false;
		} else {
			return true;
		}
	}

	public void render1(GameResources gm) {
		if (readyToRender1) {
			gm.getRenderer().renderChunk(cubes1, gm);
			gm.getWaterRenderer().render(water1, gm);
		} else {
			gm.getRenderer().renderChunk(cubes1temp, gm);
			gm.getWaterRenderer().render(water1temp, gm);
		}
	}

	public void render2(GameResources gm) {
		if (readyToRender2) {
			gm.getRenderer().renderChunk(cubes2, gm);
			gm.getWaterRenderer().render(water2, gm);
		} else {
			gm.getRenderer().renderChunk(cubes2temp, gm);
			gm.getWaterRenderer().render(water2temp, gm);
		}
	}

	public void render3(GameResources gm) {
		if (readyToRender3) {
			gm.getRenderer().renderChunk(cubes3, gm);
			gm.getWaterRenderer().render(water3, gm);
		} else {
			gm.getRenderer().renderChunk(cubes3temp, gm);
			gm.getWaterRenderer().render(water3temp, gm);
		}
	}

	public void render4(GameResources gm) {
		if (readyToRender4) {
			gm.getRenderer().renderChunk(cubes4, gm);
			gm.getWaterRenderer().render(water4, gm);
		} else {
			gm.getRenderer().renderChunk(cubes4temp, gm);
			gm.getWaterRenderer().render(water4temp, gm);
		}
	}

	private void clear() {
		water1.clear();
		water2.clear();
		water3.clear();
		water4.clear();
		cubes1.clear();
		cubes2.clear();
		cubes3.clear();
		cubes4.clear();
		lights1.clear();
		lights2.clear();
		lights3.clear();
		lights4.clear();
		cubes1temp.clear();
		cubes2temp.clear();
		cubes3temp.clear();
		cubes4temp.clear();
		water1temp.clear();
		water2temp.clear();
		water3temp.clear();
		water4temp.clear();
	}

	public void dispose() {
		clear();
	}

}