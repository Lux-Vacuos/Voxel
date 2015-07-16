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
import io.github.guerra24.voxel.client.kernel.KernelConstants;
import io.github.guerra24.voxel.client.kernel.util.ArrayList3;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.resources.models.WaterTile;
import io.github.guerra24.voxel.client.world.block.Block;
import io.github.guerra24.voxel.client.world.entities.Entity;

import org.lwjgl.util.vector.Vector3f;

public class Chunk {

	public int posX, posZ;
	public boolean isToRebuild = false;
	public boolean isChunkloaded = false;

	private int sizeX, sizeY, sizeZ;
	private ArrayList3<Entity> cubes1;
	private ArrayList3<Entity> cubes2;
	private ArrayList3<Entity> cubes3;
	private ArrayList3<Entity> cubes4;
	private ArrayList3<WaterTile> waters;
	private Vector3f pos;

	public Chunk(Vector3f pos, boolean rebuild) {
		this.pos = pos;
		this.posX = (int) pos.x;
		this.posZ = (int) pos.z;
		init(rebuild);
	}

	public void init(boolean rebuild) {
		sizeX = (int) (pos.getX() + KernelConstants.CHUNK_SIZE);
		sizeY = (int) (pos.getY() + KernelConstants.CHUNK_HEIGHT);
		sizeZ = (int) (pos.getZ() + KernelConstants.CHUNK_SIZE);

		cubes1 = new ArrayList3<Entity>();
		cubes2 = new ArrayList3<Entity>();
		cubes3 = new ArrayList3<Entity>();
		cubes4 = new ArrayList3<Entity>();

		waters = new ArrayList3<WaterTile>();
		createChunk();
		if (rebuild) {
			rebuild();
		}
		isChunkloaded = true;
	}

	public void update() {
		dispose();
		rebuild();
		isToRebuild = false;
	}

	private void createChunk() {
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int z = (int) pos.getZ(); z < sizeZ; z++) {
				for (int y = (int) pos.getY(); y < sizeY; y++) {
					if (y == 64) {
						Kernel.world.water[x][y][z] = Block.Water.getId();
					}
				}
			}
		}
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int z = (int) pos.getZ(); z < sizeZ; z++) {
				int rand = (int) (sizeY * Maths
						.clamp(Kernel.world.perlinNoiseArray[x][z]));
				for (int y = (int) pos.getY(); y < rand; y++) {
					if (y == rand - 1 && y > 65)
						Kernel.world.blocks[x][y][z] = Block.Grass.getId();
					else if (y == rand - 2 && y > 65)
						Kernel.world.blocks[x][y][z] = Block.Dirt.getId();
					else if (y == rand - 1 && y < 66)
						Kernel.world.blocks[x][y][z] = Block.Sand.getId();
					else if (Kernel.world.seed.nextInt(150) == 1 && y < 15)
						Kernel.world.blocks[x][y][z] = Block.DiamondOre.getId();
					else if (Kernel.world.seed.nextInt(100) == 1 && y < 25)
						Kernel.world.blocks[x][y][z] = Block.GoldOre.getId();
					// } else if (Kernel.world.seed.nextInt(100) == 1 && y >
					// 40
					// && y < 60) {
					// Kernel.world.blocks[x][y][z] = Block.Glass.getId();
					else
						Kernel.world.blocks[x][y][z] = Block.Stone.getId();

					if (y == 0)
						Kernel.world.blocks[x][y][z] = Block.Indes.getId();

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

	public void rebuild() {
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int z = (int) pos.getZ(); z < sizeZ; z++) {
				for (int y = (int) pos.getY(); y < sizeY; y++) {
					if (Kernel.world.blocks[x][y][z] == Block.Indes.getId()) {
						if (cullFaceWest(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Indes
										.getFaceWest(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Indes
										.getFaceWest(new Vector3f(x, y, z)));

							if (y > 63 && y < 96)
								cubes3.add(Block.Indes
										.getFaceWest(new Vector3f(x, y, z)));

							if (y > 95 && y < 129)
								cubes4.add(Block.Indes
										.getFaceWest(new Vector3f(x, y, z)));

						}
						if (cullFaceEast(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Indes
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Indes
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Indes
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Indes
										.getFaceEast(new Vector3f(x, y, z)));
						}
						if (cullFaceDown(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Indes
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Indes
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Indes
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Indes
										.getFaceDown(new Vector3f(x, y, z)));
						}
						if (cullFaceUp(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Indes.getFaceUp(new Vector3f(
										x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Indes.getFaceUp(new Vector3f(
										x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Indes.getFaceUp(new Vector3f(
										x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Indes.getFaceUp(new Vector3f(
										x, y, z)));
						}
						if (cullFaceNorth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Indes
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Indes
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Indes
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Indes
										.getFaceNorth(new Vector3f(x, y, z)));
						}
						if (cullFaceSouth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Indes
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Indes
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Indes
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Indes
										.getFaceSouth(new Vector3f(x, y, z)));
						}
					} else if (Kernel.world.blocks[x][y][z] == Block.Stone
							.getId()) {
						if (cullFaceWest(x, y, z)) {
							if (y < 32) {
								cubes1.add(Block.Stone
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 31 && y < 64) {
								cubes2.add(Block.Stone
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 63 && y < 96) {
								cubes3.add(Block.Stone
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 95 && y < 129) {
								cubes4.add(Block.Stone
										.getFaceWest(new Vector3f(x, y, z)));
							}
						}
						if (cullFaceEast(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Stone
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Stone
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Stone
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Stone
										.getFaceEast(new Vector3f(x, y, z)));
						}
						if (cullFaceDown(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Stone
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Stone
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Stone
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Stone
										.getFaceDown(new Vector3f(x, y, z)));
						}
						if (cullFaceUp(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Stone.getFaceUp(new Vector3f(
										x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Stone.getFaceUp(new Vector3f(
										x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Stone.getFaceUp(new Vector3f(
										x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Stone.getFaceUp(new Vector3f(
										x, y, z)));
						}
						if (cullFaceNorth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Stone
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Stone
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Stone
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Stone
										.getFaceNorth(new Vector3f(x, y, z)));
						}
						if (cullFaceSouth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Stone
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Stone
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Stone
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Stone
										.getFaceSouth(new Vector3f(x, y, z)));
						}
					} else if (Kernel.world.blocks[x][y][z] == Block.Grass
							.getId()) {
						if (cullFaceWest(x, y, z)) {
							if (y < 32) {
								cubes1.add(Block.Grass
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 31 && y < 64) {
								cubes2.add(Block.Grass
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 63 && y < 96) {
								cubes3.add(Block.Grass
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 95 && y < 129) {
								cubes4.add(Block.Grass
										.getFaceWest(new Vector3f(x, y, z)));
							}
						}
						if (cullFaceEast(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Grass
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Grass
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Grass
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Grass
										.getFaceEast(new Vector3f(x, y, z)));
						}
						if (cullFaceDown(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Grass
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Grass
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Grass
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Grass
										.getFaceDown(new Vector3f(x, y, z)));
						}
						if (cullFaceUp(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Grass.getFaceUp(new Vector3f(
										x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Grass.getFaceUp(new Vector3f(
										x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Grass.getFaceUp(new Vector3f(
										x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Grass.getFaceUp(new Vector3f(
										x, y, z)));
						}
						if (cullFaceNorth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Grass
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Grass
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Grass
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Grass
										.getFaceNorth(new Vector3f(x, y, z)));
						}
						if (cullFaceSouth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Grass
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Grass
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Grass
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Grass
										.getFaceSouth(new Vector3f(x, y, z)));
						}
					} else if (Kernel.world.blocks[x][y][z] == Block.Sand
							.getId()) {
						if (cullFaceWest(x, y, z)) {
							if (y < 32) {
								cubes1.add(Block.Sand.getFaceWest(new Vector3f(
										x, y, z)));
							}
							if (y > 31 && y < 64) {
								cubes2.add(Block.Sand.getFaceWest(new Vector3f(
										x, y, z)));
							}
							if (y > 63 && y < 96) {
								cubes3.add(Block.Sand.getFaceWest(new Vector3f(
										x, y, z)));
							}
							if (y > 95 && y < 129) {
								cubes4.add(Block.Sand.getFaceWest(new Vector3f(
										x, y, z)));
							}
						}
						if (cullFaceEast(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Sand.getFaceEast(new Vector3f(
										x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Sand.getFaceEast(new Vector3f(
										x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Sand.getFaceEast(new Vector3f(
										x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Sand.getFaceEast(new Vector3f(
										x, y, z)));
						}
						if (cullFaceDown(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Sand.getFaceDown(new Vector3f(
										x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Sand.getFaceDown(new Vector3f(
										x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Sand.getFaceDown(new Vector3f(
										x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Sand.getFaceDown(new Vector3f(
										x, y, z)));
						}
						if (cullFaceUp(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Sand.getFaceUp(new Vector3f(x,
										y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Sand.getFaceUp(new Vector3f(x,
										y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Sand.getFaceUp(new Vector3f(x,
										y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Sand.getFaceUp(new Vector3f(x,
										y, z)));
						}
						if (cullFaceNorth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Sand
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Sand
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Sand
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Sand
										.getFaceNorth(new Vector3f(x, y, z)));
						}
						if (cullFaceSouth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Sand
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Sand
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Sand
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Sand
										.getFaceSouth(new Vector3f(x, y, z)));
						}
					} else if (Kernel.world.blocks[x][y][z] == Block.Dirt
							.getId()) {
						if (cullFaceWest(x, y, z)) {
							if (y < 32) {
								cubes1.add(Block.Dirt.getFaceWest(new Vector3f(
										x, y, z)));
							}
							if (y > 31 && y < 64) {
								cubes2.add(Block.Dirt.getFaceWest(new Vector3f(
										x, y, z)));
							}
							if (y > 63 && y < 96) {
								cubes3.add(Block.Dirt.getFaceWest(new Vector3f(
										x, y, z)));
							}
							if (y > 95 && y < 129) {
								cubes4.add(Block.Dirt.getFaceWest(new Vector3f(
										x, y, z)));
							}
						}
						if (cullFaceEast(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Dirt.getFaceEast(new Vector3f(
										x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Dirt.getFaceEast(new Vector3f(
										x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Dirt.getFaceEast(new Vector3f(
										x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Dirt.getFaceEast(new Vector3f(
										x, y, z)));
						}
						if (cullFaceDown(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Dirt.getFaceDown(new Vector3f(
										x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Dirt.getFaceDown(new Vector3f(
										x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Dirt.getFaceDown(new Vector3f(
										x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Dirt.getFaceDown(new Vector3f(
										x, y, z)));
						}
						if (cullFaceUp(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Dirt.getFaceUp(new Vector3f(x,
										y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Dirt.getFaceUp(new Vector3f(x,
										y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Dirt.getFaceUp(new Vector3f(x,
										y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Dirt.getFaceUp(new Vector3f(x,
										y, z)));
						}
						if (cullFaceNorth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Dirt
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Dirt
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Dirt
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Dirt
										.getFaceNorth(new Vector3f(x, y, z)));
						}
						if (cullFaceSouth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Dirt
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Dirt
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Dirt
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Dirt
										.getFaceSouth(new Vector3f(x, y, z)));
						}
					} else if (Kernel.world.blocks[x][y][z] == Block.DiamondOre
							.getId()) {
						if (cullFaceWest(x, y, z)) {
							if (y < 32) {
								cubes1.add(Block.DiamondOre
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 31 && y < 64) {
								cubes2.add(Block.DiamondOre
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 63 && y < 96) {
								cubes3.add(Block.DiamondOre
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 95 && y < 129) {
								cubes4.add(Block.DiamondOre
										.getFaceWest(new Vector3f(x, y, z)));
							}
						}
						if (cullFaceEast(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.DiamondOre
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.DiamondOre
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.DiamondOre
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.DiamondOre
										.getFaceEast(new Vector3f(x, y, z)));
						}
						if (cullFaceDown(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.DiamondOre
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.DiamondOre
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.DiamondOre
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.DiamondOre
										.getFaceDown(new Vector3f(x, y, z)));
						}
						if (cullFaceUp(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.DiamondOre
										.getFaceUp(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.DiamondOre
										.getFaceUp(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.DiamondOre
										.getFaceUp(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.DiamondOre
										.getFaceUp(new Vector3f(x, y, z)));
						}
						if (cullFaceNorth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.DiamondOre
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.DiamondOre
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.DiamondOre
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.DiamondOre
										.getFaceNorth(new Vector3f(x, y, z)));
						}
						if (cullFaceSouth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.DiamondOre
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.DiamondOre
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.DiamondOre
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.DiamondOre
										.getFaceSouth(new Vector3f(x, y, z)));
						}
					} else if (Kernel.world.blocks[x][y][z] == Block.GoldOre
							.getId()) {
						if (cullFaceWest(x, y, z)) {
							if (y < 32) {
								cubes1.add(Block.GoldOre
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 31 && y < 64) {
								cubes2.add(Block.GoldOre
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 63 && y < 96) {
								cubes3.add(Block.GoldOre
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 95 && y < 129) {
								cubes4.add(Block.GoldOre
										.getFaceWest(new Vector3f(x, y, z)));
							}
						}
						if (cullFaceEast(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.GoldOre
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.GoldOre
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.GoldOre
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.GoldOre
										.getFaceEast(new Vector3f(x, y, z)));
						}
						if (cullFaceDown(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.GoldOre
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.GoldOre
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.GoldOre
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.GoldOre
										.getFaceDown(new Vector3f(x, y, z)));
						}
						if (cullFaceUp(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.GoldOre
										.getFaceUp(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.GoldOre
										.getFaceUp(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.GoldOre
										.getFaceUp(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.GoldOre
										.getFaceUp(new Vector3f(x, y, z)));
						}
						if (cullFaceNorth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.GoldOre
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.GoldOre
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.GoldOre
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.GoldOre
										.getFaceNorth(new Vector3f(x, y, z)));
						}
						if (cullFaceSouth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.GoldOre
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.GoldOre
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.GoldOre
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.GoldOre
										.getFaceSouth(new Vector3f(x, y, z)));
						}
					} else if (Kernel.world.blocks[x][y][z] == Block.Glass
							.getId()) {
						if (cullFaceWest(x, y, z)) {
							if (y < 32) {
								cubes1.add(Block.Glass
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 31 && y < 64) {
								cubes2.add(Block.Glass
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 63 && y < 96) {
								cubes3.add(Block.Glass
										.getFaceWest(new Vector3f(x, y, z)));
							}
							if (y > 95 && y < 129) {
								cubes4.add(Block.Glass
										.getFaceWest(new Vector3f(x, y, z)));
							}
						}
						if (cullFaceEast(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Glass
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Glass
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Glass
										.getFaceEast(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Glass
										.getFaceEast(new Vector3f(x, y, z)));
						}
						if (cullFaceDown(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Glass
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Glass
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Glass
										.getFaceDown(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Glass
										.getFaceDown(new Vector3f(x, y, z)));
						}
						if (cullFaceUp(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Glass.getFaceUp(new Vector3f(
										x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Glass.getFaceUp(new Vector3f(
										x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Glass.getFaceUp(new Vector3f(
										x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Glass.getFaceUp(new Vector3f(
										x, y, z)));
						}
						if (cullFaceNorth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Glass
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Glass
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Glass
										.getFaceNorth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Glass
										.getFaceNorth(new Vector3f(x, y, z)));
						}
						if (cullFaceSouth(x, y, z)) {
							if (y < 32)
								cubes1.add(Block.Glass
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 31 && y < 64)
								cubes2.add(Block.Glass
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 63 && y < 96)
								cubes3.add(Block.Glass
										.getFaceSouth(new Vector3f(x, y, z)));
							if (y > 95 && y < 129)
								cubes4.add(Block.Glass
										.getFaceSouth(new Vector3f(x, y, z)));
						}
					} else if (Kernel.world.water[x][y][z] == Block.Water
							.getId()) {
						waters.add(Block.Water.getWaterTitle(new Vector3f(x, y,
								z)));
					}
				}
			}
		}
	}

	private boolean cullFaceWest(int x, int y, int z) {
		if (x == 0) {
			return true;
		} else {
			if (Kernel.world.getBlock(x - 1, y, z) != 0) {
				return false;
			} else {
				return true;
			}
		}
	}

	private boolean cullFaceEast(int x, int y, int z) {
		if (x == KernelConstants.viewDistance * 16 - 1) {
			return true;
		} else {
			if (Kernel.world.getBlock(x + 1, y, z) != 0) {
				return false;
			} else {
				return true;
			}
		}
	}

	private boolean cullFaceDown(int x, int y, int z) {
		if (y == 0) {
			return true;
		} else {
			if (Kernel.world.getBlock(x, y - 1, z) != 0) {
				return false;
			} else {
				return true;
			}
		}
	}

	private boolean cullFaceUp(int x, int y, int z) {
		if (y < sizeY - 1) {
			if (Kernel.world.getBlock(x, y + 1, z) != 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private boolean cullFaceNorth(int x, int y, int z) {
		if (z == 0) {
			return true;
		}
		if (Kernel.world.getBlock(x, y, z - 1) != 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean cullFaceSouth(int x, int y, int z) {
		if (z == KernelConstants.viewDistance * 16 - 1) {
			return true;
		} else {
			if (Kernel.world.getBlock(x, y, z + 1) != 0) {
				return false;
			} else {
				return true;
			}
		}
	}

	public void sendToRender1() {
		Kernel.gameResources.cubes.addAll(cubes1);
	}

	public void sendToRender2() {
		Kernel.gameResources.cubes.addAll(cubes2);
	}

	public void sendToRender3() {
		Kernel.gameResources.cubes.addAll(cubes3);
	}

	public void sendToRender4() {
		Kernel.gameResources.cubes.addAll(cubes4);
	}

	public void sendToRenderWater() {
		Kernel.gameResources.waters.addAll(waters);
	}

	public void clear() {
		waters.clear();
		cubes1.clear();
		cubes2.clear();
		cubes3.clear();
		cubes4.clear();
	}

	public void dispose() {
		Kernel.gameResources.cubes.removeAll(cubes1);
		Kernel.gameResources.cubes.removeAll(cubes2);
		Kernel.gameResources.cubes.removeAll(cubes3);
		Kernel.gameResources.cubes.removeAll(cubes4);
		Kernel.gameResources.waters.removeAll(waters);
		waters.clear();
		cubes1.clear();
		cubes2.clear();
		cubes3.clear();
		cubes4.clear();
		isChunkloaded = false;
	}
}
