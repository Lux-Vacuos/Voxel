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

package net.guerra24.voxel.world.chunks;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.guerra24.voxel.core.VoxelVariables;
import net.guerra24.voxel.resources.GameResources;
import net.guerra24.voxel.resources.models.WaterTile;
import net.guerra24.voxel.util.Maths;
import net.guerra24.voxel.util.vector.Vector3f;
import net.guerra24.voxel.world.DimensionalWorld;
import net.guerra24.voxel.world.block.Block;
import net.guerra24.voxel.world.block.BlockEntity;

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
	public int dim, cx, cy, cz, posX, posY, posZ;
	public byte[][][] blocks;
	private transient Queue<BlockEntity> cubes;
	private transient Queue<WaterTile> water;
	private transient Queue<WaterTile> watertemp;
	private transient Queue<BlockEntity> cubestemp;
	private transient int sizeX, sizeY, sizeZ;
	private transient boolean readyToRender = true;
	public transient boolean needsRebuild = true, updated = false, updating = false, empty = true, created = false;

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
	public Chunk(int dim, int cx, int cy, int cz, DimensionalWorld world) {
		this.dim = dim;
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
		this.posX = cx * 16;
		this.posZ = cz * 16;
		this.posY = cy * 16;
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
	}

	/**
	 * Initialize List
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void createList() {
		cubes = new ConcurrentLinkedQueue<BlockEntity>();
		cubestemp = new ConcurrentLinkedQueue<BlockEntity>();
		water = new ConcurrentLinkedQueue<WaterTile>();
		watertemp = new ConcurrentLinkedQueue<WaterTile>();
		sizeX = VoxelVariables.CHUNK_SIZE;
		sizeY = VoxelVariables.CHUNK_HEIGHT;
		sizeZ = VoxelVariables.CHUNK_SIZE;
	}

	public void rebuild(WorldService server, DimensionalWorld world) {
		if (needsRebuild || !updated && !updating) {
			updating = true;
			server.add_worker(new ChunkWorkerMesh(world, this));
		}
		empty = cubes.isEmpty();
	}

	public void update(DimensionalWorld world) {
		watertemp.addAll(water);
		cubestemp.addAll(cubes);
		readyToRender = false;
		water.clear();
		cubes.clear();
		rebuildChunkSection(empty, cubes, water, world);
		calculateLight(cubes, world);
		readyToRender = true;
		cubestemp.clear();
		watertemp.clear();
	}

	public void createBasicTerrain(DimensionalWorld world) {
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					double tempHeight = world.getNoise().getNoise(x + cx * 16, y + cy * 16, z + cz * 16);
					tempHeight++;
					if (tempHeight > 0.9) {
					} else
						blocks[x][y][z] = Block.Stone.getId();
					if (cy == 0)
						if (y == 0)
							blocks[x][y][z] = Block.Indes.getId();
				}
			}
		}
		created = true;
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

	public void rebuildChunkSection(boolean secClear, Queue<BlockEntity> cubes, Queue<WaterTile> water,
			DimensionalWorld world) {
		secClear = false;
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					if (Block.getBlock(blocks[x][y][z]) == Block.Torch) {
						cubes.add(Block.getBlock(blocks[x][y][z])
								.getSingleModel(new Vector3f(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)));
						secClear = true;
					} else if (Block.getBlock(blocks[x][y][z]).usesSingleModel()) {
						cubes.add(Block.getBlock(blocks[x][y][z])
								.getSingleModel(new Vector3f(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)));
					} else if (Block.getBlock(blocks[x][y][z]) != Block.Air
							&& Block.getBlock(blocks[x][y][z]) != Block.Water) {
						if (cullFaceWest(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ, world)) {
							cubes.add(Block.getBlock(blocks[x][y][z])
									.getFaceWest(new Vector3f(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)));
							secClear = true;
						}
						if (cullFaceEast(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ, world)) {
							cubes.add(Block.getBlock(blocks[x][y][z])
									.getFaceEast(new Vector3f(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)));
							secClear = true;
						}
						if (cullFaceDown(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)) {
							cubes.add(Block.getBlock(blocks[x][y][z])
									.getFaceDown(new Vector3f(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)));
							secClear = true;
						}
						if (cullFaceUpSolidBlock(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)) {
							cubes.add(Block.getBlock(blocks[x][y][z])
									.getFaceUp(new Vector3f(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)));
							secClear = true;
						}
						if (cullFaceNorth(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ, world)) {
							cubes.add(Block.getBlock(blocks[x][y][z])
									.getFaceNorth(new Vector3f(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)));
							secClear = true;
						}
						if (cullFaceSouth(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ, world)) {
							cubes.add(Block.getBlock(blocks[x][y][z])
									.getFaceSouth(new Vector3f(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)));
							secClear = true;
						}
					} else if (Block.getBlock(blocks[x][y][z]) == Block.Water) {
						if (cullFaceUpWater(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)) {
							water.add(Block.Water
									.getWaterTitle(new Vector3f(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)));
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
		return blocks[x & 0xF][y & 0xF][z & 0xF];
	}

	public void setLocalBlock(int x, int y, int z, byte id) {
		blocks[x & 0xF][y & 0xF][z & 0xF] = id;
	}

	private float getLight(DimensionalWorld world, int x, int y, int z) {
		float result = 1;
		if ((x > (cx * sizeX) + 1) && (z > (cz * sizeZ) + 1) && (x < (cx * sizeX) + 15) && (z < (cz * sizeZ) + 15)) {
			for (int yo = 16; yo > y; yo--) {
				if (getLocalBlock(x, yo, z) != Block.Air.getId()) {
					result -= 0.05f;
				}
				if (result <= 0.1f)
					return result;
			}
		}
		return result;
	}

	private boolean cullFaceWest(int x, int y, int z, DimensionalWorld world) {
		if (x > (cx * sizeX) + 1) {
			if (z > (cz * sizeZ) + 1) {
				if (x < (cx * sizeX) + 15) {
					if (z < (cz * sizeZ) + 15) {
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
		if (x > (cx * sizeX) + 1) {
			if (z > (cz * sizeZ) + 1) {
				if (x < (cx * sizeX) + 15) {
					if (z < (cz * sizeZ) + 15) {
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
		if (x > (cx * sizeX) + 1) {
			if (z > (cz * sizeZ) + 1) {
				if (x < (cx * sizeX) + 15) {
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
		if (x > (cx * sizeX) + 1) {
			if (z > (cz * sizeZ) + 1) {
				if (x < (cx * sizeX) + 15) {
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

	public void render(GameResources gm) {
		if (readyToRender) {
			gm.getRenderer().renderChunk(cubes, gm);
			gm.getWaterRenderer().render(water, gm);
		} else {
			gm.getRenderer().renderChunk(cubestemp, gm);
			gm.getWaterRenderer().render(watertemp, gm);
		}
	}

	private void clear() {
		water.clear();
		cubes.clear();
		cubestemp.clear();
		watertemp.clear();
	}

	public void dispose() {
		clear();
	}

}