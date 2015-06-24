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

package io.github.guerra24.voxel.client.world.chunks;

import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.kernel.util.ArrayList3;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.resources.models.WaterTile;
import io.github.guerra24.voxel.client.world.block.Block;
import io.github.guerra24.voxel.client.world.entities.Entity;

import org.lwjgl.util.vector.Vector3f;

public class Chunk {

	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_HEIGHT = 144;

	private ArrayList3<Entity> cubes;
	private ArrayList3<WaterTile> waters;
	private Vector3f pos;
	private int sizeX, sizeY, sizeZ;
	public int posX, posZ;
	public boolean isNotLoaded, isToRebuild = false;
	public ChunkInfo blocksData;
	public float lastUsed;
	public byte[][][] water;

	public Chunk(Vector3f pos) {
		this.pos = pos;
		this.posX = (int) pos.x;
		this.posZ = (int) pos.z;
		blocksData = new ChunkInfo();
		init();
	}

	public void init() {
		sizeX = (int) (pos.getX() + CHUNK_SIZE);
		sizeY = (int) (pos.getY() + CHUNK_HEIGHT);
		sizeZ = (int) (pos.getZ() + CHUNK_SIZE);

		blocksData.blocks = new byte[sizeX][sizeY][sizeZ];
		water = new byte[sizeX][sizeY][sizeZ];
		cubes = new ArrayList3<Entity>();

		waters = new ArrayList3<WaterTile>();
		createChunk();
		rebuild();
	}

	public void update() {
		if (isToRebuild) {
			Kernel.gameResources.allEntities.removeAll(cubes);
			Kernel.gameResources.waters.removeAll(waters);
			cubes.clear();
			waters.clear();
			rebuild();
			isToRebuild = false;
		}
	}

	private void createChunk() {
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int z = (int) pos.getZ(); z < sizeZ; z++) {
				for (int y = (int) pos.getY(); y < sizeY; y++) {
					if (y == 64) {
						water[x][y][z] = Block.Water.getId();
					}
				}
			}
		}
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int z = (int) pos.getZ(); z < sizeZ; z++) {
				int rand = (int) (sizeY * Maths
						.clamp(Kernel.world.perlinNoiseArray[x][z]));
				for (int y = (int) pos.getY(); y < rand; y++) {
					if (y == rand - 1 && y > 65) {
						blocksData.blocks[x][y][z] = Block.Grass.getId();
					} else if (y == rand - 2 && y > 65) {
						blocksData.blocks[x][y][z] = Block.Dirt.getId();
					} else if (y == rand - 1 && y < 66) {
						blocksData.blocks[x][y][z] = Block.Sand.getId();
					} else if (Kernel.world.seed.nextInt(100) == 1 && y < 15) {
						blocksData.blocks[x][y][z] = Block.DiamondOre.getId();
					} else {
						blocksData.blocks[x][y][z] = Block.Stone.getId();
					}
					if (y == 0) {
						blocksData.blocks[x][y][z] = Block.Indes.getId();
					}
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

	private void rebuild() {
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int z = (int) pos.getZ(); z < sizeZ; z++) {
				for (int y = (int) pos.getY(); y < sizeY; y++) {
					if (blocksData.blocks[x][y][z] == Block.Indes.getId()) {
						if (cullFaceWest(x, y, z)) {
							cubes.add(Block.Indes.getFaceWest(new Vector3f(x,
									y, z)));
						}
						if (cullFaceEast(x, y, z)) {
							cubes.add(Block.Indes.getFaceEast(new Vector3f(x,
									y, z)));
						}
						if (cullFaceDown(x, y, z)) {
							cubes.add(Block.Indes.getFaceDown(new Vector3f(x,
									y, z)));
						}
						if (cullFaceUp(x, y, z)) {
							cubes.add(Block.Indes.getFaceUp(new Vector3f(x, y,
									z)));
						}
						if (cullFaceNorth(x, y, z)) {
							cubes.add(Block.Indes.getFaceNorth(new Vector3f(x,
									y, z)));
						}
						if (cullFaceSouth(x, y, z)) {
							cubes.add(Block.Indes.getFaceSouth(new Vector3f(x,
									y, z)));
						}
					} else if (blocksData.blocks[x][y][z] == Block.Stone
							.getId()) {
						if (cullFaceWest(x, y, z)) {
							cubes.add(Block.Stone.getFaceWest(new Vector3f(x,
									y, z)));
						}
						if (cullFaceEast(x, y, z)) {
							cubes.add(Block.Stone.getFaceEast(new Vector3f(x,
									y, z)));
						}
						if (cullFaceDown(x, y, z)) {
							cubes.add(Block.Stone.getFaceDown(new Vector3f(x,
									y, z)));
						}
						if (cullFaceUp(x, y, z)) {
							cubes.add(Block.Stone.getFaceUp(new Vector3f(x, y,
									z)));
						}
						if (cullFaceNorth(x, y, z)) {
							cubes.add(Block.Stone.getFaceNorth(new Vector3f(x,
									y, z)));
						}
						if (cullFaceSouth(x, y, z)) {
							cubes.add(Block.Stone.getFaceSouth(new Vector3f(x,
									y, z)));
						}
					} else if (blocksData.blocks[x][y][z] == Block.Grass
							.getId()) {
						if (cullFaceWest(x, y, z)) {
							cubes.add(Block.Grass.getFaceWest(new Vector3f(x,
									y, z)));
						}
						if (cullFaceEast(x, y, z)) {
							cubes.add(Block.Grass.getFaceEast(new Vector3f(x,
									y, z)));
						}
						if (cullFaceDown(x, y, z)) {
							cubes.add(Block.Grass.getFaceDown(new Vector3f(x,
									y, z)));
						}
						if (cullFaceUp(x, y, z)) {
							cubes.add(Block.Grass.getFaceUp(new Vector3f(x, y,
									z)));
						}
						if (cullFaceNorth(x, y, z)) {
							cubes.add(Block.Grass.getFaceNorth(new Vector3f(x,
									y, z)));
						}
						if (cullFaceSouth(x, y, z)) {
							cubes.add(Block.Grass.getFaceSouth(new Vector3f(x,
									y, z)));
						}
					} else if (blocksData.blocks[x][y][z] == Block.Sand.getId()) {
						if (cullFaceWest(x, y, z)) {
							cubes.add(Block.Sand.getFaceWest(new Vector3f(x, y,
									z)));
						}
						if (cullFaceEast(x, y, z)) {
							cubes.add(Block.Sand.getFaceEast(new Vector3f(x, y,
									z)));
						}
						if (cullFaceDown(x, y, z)) {
							cubes.add(Block.Sand.getFaceDown(new Vector3f(x, y,
									z)));
						}
						if (cullFaceUp(x, y, z)) {
							cubes.add(Block.Sand
									.getFaceUp(new Vector3f(x, y, z)));
						}
						if (cullFaceNorth(x, y, z)) {
							cubes.add(Block.Sand.getFaceNorth(new Vector3f(x,
									y, z)));
						}
						if (cullFaceSouth(x, y, z)) {
							cubes.add(Block.Sand.getFaceSouth(new Vector3f(x,
									y, z)));
						}
					} else if (blocksData.blocks[x][y][z] == Block.Dirt.getId()) {
						if (cullFaceWest(x, y, z)) {
							cubes.add(Block.Dirt.getFaceWest(new Vector3f(x, y,
									z)));
						}
						if (cullFaceEast(x, y, z)) {
							cubes.add(Block.Dirt.getFaceEast(new Vector3f(x, y,
									z)));
						}
						if (cullFaceDown(x, y, z)) {
							cubes.add(Block.Dirt.getFaceDown(new Vector3f(x, y,
									z)));
						}
						if (cullFaceUp(x, y, z)) {
							cubes.add(Block.Dirt
									.getFaceUp(new Vector3f(x, y, z)));
						}
						if (cullFaceNorth(x, y, z)) {
							cubes.add(Block.Dirt.getFaceNorth(new Vector3f(x,
									y, z)));
						}
						if (cullFaceSouth(x, y, z)) {
							cubes.add(Block.Dirt.getFaceSouth(new Vector3f(x,
									y, z)));
						}
					} else if (blocksData.blocks[x][y][z] == Block.DiamondOre
							.getId()) {
						if (cullFaceWest(x, y, z)) {
							cubes.add(Block.DiamondOre
									.getFaceWest(new Vector3f(x, y, z)));
						}
						if (cullFaceEast(x, y, z)) {
							cubes.add(Block.DiamondOre
									.getFaceEast(new Vector3f(x, y, z)));
						}
						if (cullFaceDown(x, y, z)) {
							cubes.add(Block.DiamondOre
									.getFaceDown(new Vector3f(x, y, z)));
						}
						if (cullFaceUp(x, y, z)) {
							cubes.add(Block.DiamondOre.getFaceUp(new Vector3f(
									x, y, z)));
						}
						if (cullFaceNorth(x, y, z)) {
							cubes.add(Block.DiamondOre
									.getFaceNorth(new Vector3f(x, y, z)));
						}
						if (cullFaceSouth(x, y, z)) {
							cubes.add(Block.DiamondOre
									.getFaceSouth(new Vector3f(x, y, z)));
						}
					} else if (water[x][y][z] == Block.Water.getId()) {
						waters.add(Block.Water.getWaterTitle(new Vector3f(x, y,
								z)));
					}
				}
			}
		}
		Kernel.gameResources.waters.addAll(waters);
		Kernel.gameResources.allEntities.addAll(cubes);
	}

	private boolean cullFaceWest(int x, int y, int z) {
		if (x > pos.getX()) {
			if (getBlock(x - 1, y, z) != 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private boolean cullFaceEast(int x, int y, int z) {
		if (x < sizeX - 1) {
			if (getBlock(x + 1, y, z) != 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private boolean cullFaceDown(int x, int y, int z) {
		if (y > pos.getY()) {
			if (getBlock(x, y - 1, z) != 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private boolean cullFaceUp(int x, int y, int z) {
		if (y < sizeY - 1) {
			if (getBlock(x, y + 1, z) != 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private boolean cullFaceNorth(int x, int y, int z) {
		if (z > pos.getZ()) {
			if (getBlock(x, y, z - 1) != 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private boolean cullFaceSouth(int x, int y, int z) {
		if (z < sizeZ - 1) {
			if (getBlock(x, y, z + 1) != 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public byte getBlock(int x, int y, int z) {
		return blocksData.blocks[x][y][z];
	}

	public void dispose() {
		Kernel.gameResources.allEntities.removeAll(cubes);
		Kernel.gameResources.waters.removeAll(waters);
		waters.clear();
		cubes.clear();
	}
}