/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.server.world.chunks;

import net.luxvacuos.voxel.server.util.Logger;
import net.luxvacuos.voxel.server.util.Maths;
import net.luxvacuos.voxel.server.world.IWorld;
import net.luxvacuos.voxel.server.world.WorldService;
import net.luxvacuos.voxel.server.world.block.Block;

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
	private transient int sizeX, sizeY, sizeZ;
	public boolean created = false, decorated = false, cavesGenerated = false;
	public transient boolean creating = false, decorating = false;
	public int version = 1;

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
	public Chunk(int dim, int cx, int cy, int cz, IWorld world) {
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
	public void init(IWorld world) {
		blocks = new byte[sizeX][sizeY][sizeZ];
		lightMap = new byte[sizeX][sizeY][sizeZ];
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

	public void update(IWorld world, WorldService service) {
		if (!created && !creating) {
			creating = true;
			service.add_worker(new ChunkWorkerGenerator(world, this));
		}

		if (!decorated) {
			boolean can = true;
			T: for (int jx = cx - 1; jx < cx + 1; jx++)
				for (int jz = cz - 1; jz < cz + 1; jz++)
					for (int jy = cy - 1; jy < cy + 1; jy++)
						if (!world.hasChunk(dim, jx, jy, jz)) {
							can = false;
							break T;
						}

			if (can)
				decorate(world);
		}
	}

	protected void createBasicTerrain(IWorld world) {
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < 128; y++) {
					if (y >= cy * 16 && y < 16 + cy * 16) {
						setLocalBlock(x, y, z, Block.Water.getId());
					}
				}
			}
		}
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				double tempHeight = world.getNoise().getNoise((int) ((x + cx * 16)), (int) ((z + cz * 16)));
				tempHeight += 1;
				int height = (int) (128 * Maths.clamp(tempHeight));
				for (int y = 0; y < height; y++) {
					if (y >= cy * 16 && y < 16 + cy * 16) {
						if (y == height - 1 && y > 128)
							setLocalBlock(x, y, z, Block.Grass.getId());
						else if (y == height - 2 && y > 128)
							setLocalBlock(x, y, z, Block.Dirt.getId());
						else if (y == height - 1 && y < 129)
							setLocalBlock(x, y, z, Block.Sand.getId());
						else
							setLocalBlock(x, y, z, Block.Stone.getId());
					}
				}
			}
		}
		world.getChunkGenerator().generateCaves(this, world.getNoise());
		created = true;
	}

	protected void decorate(IWorld world) {
		for (int i = 0; i < 4; i++) {
			int xx = Maths.randInt(0, 15);
			int zz = Maths.randInt(0, 15);
			double tempHeight = world.getNoise().getNoise((int) ((xx + cx * 16)), (int) ((zz + cz * 16)));
			tempHeight += 1;
			int height = (int) (128 * Maths.clamp(tempHeight));
			int h = getLocalBlock(xx, height - 1, zz);
			if (h == Block.Grass.getId() || h == Block.Dirt.getId())
				world.getChunkGenerator().addTree(world, xx + cx * 16, height, zz + cz * 16, Maths.randInt(4, 10),
						world.getSeed());
		}
		decorated = true;
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

}