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

package net.guerra24.voxel.client.world.chunks;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.particle.ParticlePoint;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.models.Tessellator;
import net.guerra24.voxel.client.util.Logger;
import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.client.world.IWorld;
import net.guerra24.voxel.client.world.block.Block;
import net.guerra24.voxel.client.world.block.BlockEntity;
import net.guerra24.voxel.universal.util.vector.Vector3f;

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
	public int dim, posX, posY, posZ, cx, cy, cz;
	public byte[][][] blocks;
	public byte[][][] lightMap;
	private transient Queue<Object> blocksMesh;
	private transient Queue<Object> blocksMeshtemp;
	private transient Queue<ParticlePoint> particlePoints;
	private transient int sizeX, sizeY, sizeZ;
	private transient boolean readyToRender = true;
	private transient Tessellator tess;
	public transient boolean needsRebuild = true, updated = false, updating = false, empty = true, genQuery = false,
			visible = false;
	public boolean created = false, decorated = false;

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
	 */
	public Chunk(int dim, int cx, int cy, int cz, IWorld world, GameResources gm) {
		this.dim = dim;
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
		this.posX = cx * 16;
		this.posZ = cz * 16;
		this.posY = cy * 16;
		init(world, gm);
	}

	/**
	 * Empty Constructor only for load from file
	 */
	public Chunk() {
	}

	/**
	 * Initialize Chunk
	 * 
	 * @param world
	 *            Dimensional World
	 */
	public void init(IWorld world, GameResources gm) {
		load(gm);
		blocks = new byte[sizeX][sizeY][sizeZ];
		lightMap = new byte[sizeX][sizeY][sizeZ];
	}

	/**
	 * Initialize List
	 * 
	 */
	public void load(GameResources gm) {
		blocksMesh = new ConcurrentLinkedQueue<Object>();
		blocksMeshtemp = new ConcurrentLinkedQueue<Object>();
		particlePoints = new ConcurrentLinkedQueue<ParticlePoint>();
		sizeX = VoxelVariables.CHUNK_SIZE;
		sizeY = VoxelVariables.CHUNK_HEIGHT;
		sizeZ = VoxelVariables.CHUNK_SIZE;
		tess = new Tessellator(gm.getRenderer());
	}

	public void checkForMissingBlocks() {
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					if (Block.getBlock(blocks[x][y][z]) == null) {
						Logger.warn("Chunk " + dim + " " + cx + " " + cy + " " + cz
								+ " contains a missing block with ID: " + blocks[x][y][z]);
						blocks[x][y][z] = 0;
					}
				}
			}
		}
	}

	public void rebuild(IWorld world) {
		if ((needsRebuild || !updated) && !updating) {
			updating = true;
			update(world);
			needsRebuild = false;
			updated = true;
			updating = false;
		}
	}

	public void update(IWorld world) {
		blocksMeshtemp.addAll(blocksMesh);
		readyToRender = false;
		blocksMesh.clear();
		particlePoints.clear();
		calculateLight(blocksMesh, world);
		rebuildChunkSection(blocksMesh, world);
		rebuildChunkSection(world);
		readyToRender = true;
		blocksMeshtemp.clear();
	}

	public void createBasicTerrain(IWorld world) {

		byte a = 0;
		if (VoxelVariables.christmas)
			a = Block.Ice.getId();
		else
			a = Block.Water.getId();

		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < 128; y++) {
					if (y >= cy * 16 && y < 16 + cy * 16) {
						if (y <= 64)
							setLocalBlock(x, y, z, a);
					}
				}
			}
		}
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				double tempHeight = world.getNoise().getNoise(x + cx * 16, z + cz * 16);
				tempHeight += 1;
				int height = (int) (64 * Maths.clamp(tempHeight));
				for (int y = 0; y < height; y++) {
					if (y >= cy * 16 && y < 16 + cy * 16) {
						if (y == height - 1 && y > 65)
							setLocalBlock(x, y, z, Block.Grass.getId());
						else if (y == height - 2 && y > 65)
							setLocalBlock(x, y, z, Block.Dirt.getId());
						else if (y == height - 1 && y < 66)
							setLocalBlock(x, y, z, Block.Sand.getId());
						else
							setLocalBlock(x, y, z, Block.Stone.getId());
					}
				}
			}
		}
		created = true;
	}

	public void decorate(IWorld world, ChunkGenerator generator) {
		for (int i = 0; i < 4; i++) {
			int xx = Maths.randInt(4, 12);
			int zz = Maths.randInt(4, 12);
			double tempHeight = world.getNoise().getNoise(xx + cx * 16, zz + cz * 16);
			tempHeight += 1;
			int height = (int) (64 * Maths.clamp(tempHeight));
			int h = getLocalBlock(xx, height - 1, zz);
			if (h == Block.Grass.getId() || h == Block.Dirt.getId())
				generator.addTree(world, xx + cx * 16, height, zz + cz * 16, Maths.randInt(4, 10), world.getSeed());
		}
		decorated = true;
	}

	public void rebuildChunkSection(Queue<Object> cubes, IWorld world) {
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					if (Block.getBlock(blocks[x][y][z]) == Block.Torch) {
						cubes.add(Block.getBlock(blocks[x][y][z])
								.getSingleModel(new Vector3f(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)));
						particlePoints.add(new ParticlePoint(new Vector3f((x + cx * sizeX) + 0.5f,
								(y + cy * sizeY) + 0.8f, (z + cz * sizeZ) - 0.5f)));
					} else if (Block.getBlock(blocks[x][y][z]).usesSingleModel()) {
						cubes.add(Block.getBlock(blocks[x][y][z])
								.getSingleModel(new Vector3f(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)));
					} else if (Block.getBlock(blocks[x][y][z]) != Block.Air
							&& Block.getBlock(blocks[x][y][z]) != Block.Water) {
						if (cullFaceDown(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ, world)) {
							cubes.add(Block.getBlock(blocks[x][y][z])
									.getFaceDown(new Vector3f(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)));
						}
						if (cullFaceUpSolidBlock(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ, world)) {
							cubes.add(Block.getBlock(blocks[x][y][z])
									.getFaceUp(new Vector3f(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)));
						}
					} else if (Block.getBlock(blocks[x][y][z]) == Block.Water) {
						if (cullFaceUpWater(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ, world)) {
							cubes.add(Block.Water
									.getWaterTitle(new Vector3f(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ)));
						}
					}
				}
			}
		}
	}

	public void rebuildChunkSection(IWorld world) {
		tess.begin(16);
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					if (Block.getBlock(blocks[x][y][z]) == Block.Torch) {
					} else if (Block.getBlock(blocks[x][y][z]).usesSingleModel()) {
					} else if (Block.getBlock(blocks[x][y][z]) != Block.Air
							&& Block.getBlock(blocks[x][y][z]) != Block.Water) {
						tess.generateCube(x + cx * sizeX, y + cy * sizeY, (z + cz * sizeZ) -1, 1,
								cullFaceUpSolidBlock(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ, world),
								cullFaceDown(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ, world),
								cullFaceEast(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ, world),
								cullFaceWest(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ, world),
								cullFaceNorth(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ, world),
								cullFaceSouth(x + cx * sizeX, y + cy * sizeY, z + cz * sizeZ, world));
					} else if (Block.getBlock(blocks[x][y][z]) == Block.Water) {
					}
				}
			}
		}
		tess.end();
	}

	private void calculateLight(Queue<Object> cubes, IWorld world) {
		for (Object blockEnt : cubes) {
			if (blockEnt.getClass().equals(BlockEntity.class)) {
				BlockEntity blockEntity = (BlockEntity) blockEnt;
				int x, y, z;
				x = (int) blockEntity.getPosition().x;
				y = (int) blockEntity.getPosition().y;
				z = (int) blockEntity.getPosition().z;
				switch (blockEntity.getSide()) {
				case "UP":
					blockEntity.setLocalLight(getTorchLight(x, y + 1, z) / 15f);
					break;
				case "DOWN":
					blockEntity.setLocalLight(getTorchLight(x, y - 1, z) / 15f);
					break;
				case "EAST":
					blockEntity.setLocalLight(getTorchLight(x + 1, y, z) / 15f);
					break;
				case "WEST":
					blockEntity.setLocalLight(getTorchLight(x - 1, y, z) / 15f);
					break;
				case "NORTH":
					blockEntity.setLocalLight(getTorchLight(x, y, z - 1) / 15f);
					break;
				case "SOUTH":
					blockEntity.setLocalLight(getTorchLight(x, y, z + 1) / 15f);
					break;
				case "SINGLE MODEL":
					blockEntity.setLocalLight(getTorchLight(x, y, z) / 15f);
					break;
				}
			}
		}
	}

	public byte getLocalBlock(int x, int y, int z) {
		return blocks[x & 0xF][y & 0xF][z & 0xF];
	}

	public void setLocalBlock(int x, int y, int z, byte id) {
		blocks[x & 0xF][y & 0xF][z & 0xF] = id;
	}

	public float getSunLight(int x, int y, int z) {
		return (lightMap[y & 0xF][z & 0xF][x & 0xF] >> 4) & 0xF;
	}

	public void setSunLight(int x, int y, int z, int val) {
		lightMap[x & 0xF][y & 0xF][z & 0xF] = (byte) ((lightMap[x & 0xF][y & 0xF][z & 0xF] & 0xF) | (val << 4));
	}

	public float getTorchLight(int x, int y, int z) {
		return lightMap[x & 0xF][y & 0xF][z & 0xF] & 0xF;
	}

	public void setTorchLight(int x, int y, int z, int val) {
		lightMap[x & 0xF][y & 0xF][z & 0xF] = (byte) ((lightMap[x & 0xF][y & 0xF][z & 0xF] & 0xF0) | val);
	}

	public Queue<ParticlePoint> getParticlePoints() {
		return particlePoints;
	}

	private boolean cullFaceWest(int x, int y, int z, IWorld world) {
		if (x > (cx * sizeX) + 1 && x < (cx * sizeX) + 16) {
			if (getLocalBlock(x - 1, y, z) != Block.Air.getId() && getLocalBlock(x - 1, y, z) != Block.Water.getId()
					&& getLocalBlock(x - 1, y, z) != Block.Glass.getId()
					&& getLocalBlock(x - 1, y, z) != Block.Torch.getId()
					&& getLocalBlock(x - 1, y, z) != Block.Leaves.getId()) {
				return false;
			} else {
				return true;
			}
		}
		if (world.getGlobalBlock(x - 1, y, z) != Block.Air.getId()
				&& world.getGlobalBlock(x - 1, y, z) != Block.Water.getId()
				&& world.getGlobalBlock(x - 1, y, z) != Block.Glass.getId()
				&& world.getGlobalBlock(x - 1, y, z) != Block.Torch.getId()
				&& world.getGlobalBlock(x - 1, y, z) != Block.Leaves.getId()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean cullFaceEast(int x, int y, int z, IWorld world) {
		if (x > (cx * sizeX) && x < (cx * sizeX) + 15) {
			if (getLocalBlock(x + 1, y, z) != Block.Air.getId() && getLocalBlock(x + 1, y, z) != Block.Water.getId()
					&& getLocalBlock(x + 1, y, z) != Block.Glass.getId()
					&& getLocalBlock(x + 1, y, z) != Block.Torch.getId()
					&& getLocalBlock(x + 1, y, z) != Block.Leaves.getId()) {
				return false;
			} else {
				return true;
			}
		}
		if (world.getGlobalBlock(x + 1, y, z) != Block.Air.getId()
				&& world.getGlobalBlock(x + 1, y, z) != Block.Water.getId()
				&& world.getGlobalBlock(x + 1, y, z) != Block.Glass.getId()
				&& world.getGlobalBlock(x + 1, y, z) != Block.Torch.getId()
				&& world.getGlobalBlock(x + 1, y, z) != Block.Leaves.getId()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean cullFaceDown(int x, int y, int z, IWorld world) {
		if (y > (cy * sizeY) + 1 && y < (cy * sizeY) + 16) {
			if (getLocalBlock(x, y - 1, z) != Block.Air.getId() && getLocalBlock(x, y - 1, z) != Block.Water.getId()
					&& getLocalBlock(x, y - 1, z) != Block.Glass.getId()
					&& getLocalBlock(x, y - 1, z) != Block.Torch.getId()
					&& getLocalBlock(x, y - 1, z) != Block.Leaves.getId()) {
				return false;
			} else {
				return true;
			}
		}
		if (world.getGlobalBlock(x, y - 1, z) != Block.Air.getId()
				&& world.getGlobalBlock(x, y - 1, z) != Block.Water.getId()
				&& world.getGlobalBlock(x, y - 1, z) != Block.Glass.getId()
				&& world.getGlobalBlock(x, y - 1, z) != Block.Torch.getId()
				&& world.getGlobalBlock(x, y - 1, z) != Block.Leaves.getId()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean cullFaceUpSolidBlock(int x, int y, int z, IWorld world) {
		if (y > (cy * sizeY) && y < (cy * sizeY) + 15) {
			if (getLocalBlock(x, y + 1, z) != Block.Air.getId() && getLocalBlock(x, y + 1, z) != Block.Water.getId()
					&& getLocalBlock(x, y + 1, z) != Block.Glass.getId()
					&& getLocalBlock(x, y + 1, z) != Block.Torch.getId()
					&& getLocalBlock(x, y + 1, z) != Block.Leaves.getId()) {
				return false;
			} else {
				return true;
			}
		}
		if (world.getGlobalBlock(x, y + 1, z) != Block.Air.getId()
				&& world.getGlobalBlock(x, y + 1, z) != Block.Water.getId()
				&& world.getGlobalBlock(x, y + 1, z) != Block.Glass.getId()
				&& world.getGlobalBlock(x, y + 1, z) != Block.Torch.getId()
				&& world.getGlobalBlock(x, y + 1, z) != Block.Leaves.getId()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean cullFaceUpWater(int x, int y, int z, IWorld world) {
		if (y > (cy * sizeY) && y < (cy * sizeY) + 15) {
			if (getLocalBlock(x, y + 1, z) != Block.Air.getId() && getLocalBlock(x, y + 1, z) != Block.Glass.getId()
					&& getLocalBlock(x, y + 1, z) != Block.Leaves.getId()) {
				return false;
			} else {
				return true;
			}
		}
		if (world.getGlobalBlock(x, y + 1, z) != Block.Air.getId()
				&& world.getGlobalBlock(x, y + 1, z) != Block.Glass.getId()
				&& world.getGlobalBlock(x, y + 1, z) != Block.Leaves.getId()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean cullFaceNorth(int x, int y, int z, IWorld world) {
		if (z > (cz * sizeZ) + 1 && z < (cz * sizeZ) + 16) {
			if (getLocalBlock(x, y, z - 1) != Block.Air.getId() && getLocalBlock(x, y, z - 1) != Block.Water.getId()
					&& getLocalBlock(x, y, z - 1) != Block.Glass.getId()
					&& getLocalBlock(x, y, z - 1) != Block.Torch.getId()
					&& getLocalBlock(x, y, z - 1) != Block.Leaves.getId()) {
				return false;
			} else {
				return true;
			}
		}
		if (world.getGlobalBlock(x, y, z - 1) != Block.Air.getId()
				&& world.getGlobalBlock(x, y, z - 1) != Block.Water.getId()
				&& world.getGlobalBlock(x, y, z - 1) != Block.Glass.getId()
				&& world.getGlobalBlock(x, y, z - 1) != Block.Torch.getId()
				&& world.getGlobalBlock(x, y, z - 1) != Block.Leaves.getId()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean cullFaceSouth(int x, int y, int z, IWorld world) {
		if (z > (cz * sizeZ) && z < (cz * sizeZ) + 15) {
			if (getLocalBlock(x, y, z + 1) != Block.Air.getId() && getLocalBlock(x, y, z + 1) != Block.Water.getId()
					&& getLocalBlock(x, y, z + 1) != Block.Glass.getId()
					&& getLocalBlock(x, y, z + 1) != Block.Torch.getId()
					&& getLocalBlock(x, y, z + 1) != Block.Leaves.getId()) {
				return false;
			} else {
				return true;
			}
		}
		if (world.getGlobalBlock(x, y, z + 1) != Block.Air.getId()
				&& world.getGlobalBlock(x, y, z + 1) != Block.Water.getId()
				&& world.getGlobalBlock(x, y, z + 1) != Block.Glass.getId()
				&& world.getGlobalBlock(x, y, z + 1) != Block.Torch.getId()
				&& world.getGlobalBlock(x, y, z + 1) != Block.Leaves.getId()) {
			return false;
		} else {
			return true;
		}
	}

	public void render(GameResources gm) {
		if (readyToRender) {
			tess.draw(gm.getCamera());
			gm.getRenderer().processChunk(blocksMesh, gm);
		} else {
			gm.getRenderer().processChunk(blocksMeshtemp, gm);
		}
	}

	public void renderShadow(GameResources gm) {
		gm.getMasterShadowRenderer().renderChunk(blocksMesh, gm);
	}

	private void clear() {
		blocksMesh.clear();
		blocksMeshtemp.clear();
		particlePoints.clear();
		tess.cleanUp();
	}

	public void dispose() {
		clear();
	}

}