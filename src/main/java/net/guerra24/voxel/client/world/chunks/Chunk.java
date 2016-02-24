/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
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

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_SAMPLES_PASSED;
import static org.lwjgl.opengl.GL15.glBeginQuery;
import static org.lwjgl.opengl.GL15.glEndQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.particle.ParticlePoint;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.models.Tessellator;
import net.guerra24.voxel.client.resources.models.WaterTile;
import net.guerra24.voxel.client.util.Logger;
import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.client.world.IWorld;
import net.guerra24.voxel.client.world.WorldService;
import net.guerra24.voxel.client.world.block.Block;
import net.guerra24.voxel.client.world.block.BlockBase;
import net.guerra24.voxel.client.world.block.BlockEntity;
import net.guerra24.voxel.client.world.block.BlocksResources;
import net.guerra24.voxel.client.world.entities.Camera;
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
	private transient List<Object> blocksMesh;
	private transient List<Object> blocksMeshtemp;
	private transient Queue<ParticlePoint> particlePoints;
	private transient Queue<WaterTile> waterTiles;
	private transient int sizeX, sizeY, sizeZ;
	transient boolean readyToRender = true;
	private transient Tessellator tess;
	private transient float distance;
	public transient boolean needsRebuild = true, updated = false, updating = false, empty = true, visible = false,
			creating = false, decorating = false;
	public boolean created = false, decorated = false, cavesGenerated = false;
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
		blocksMesh = new ArrayList<Object>();
		blocksMeshtemp = new ArrayList<Object>();
		particlePoints = new ConcurrentLinkedQueue<ParticlePoint>();
		waterTiles = new ConcurrentLinkedQueue<WaterTile>();
		sizeX = VoxelVariables.CHUNK_SIZE;
		sizeY = VoxelVariables.CHUNK_HEIGHT;
		sizeZ = VoxelVariables.CHUNK_SIZE;
		tess = new Tessellator(gm);
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

	public void rebuild(WorldService service, IWorld world) {
		if ((needsRebuild || !updated) && !updating) {
			updating = true;
			service.add_worker(new ChunkWorkerMesh(world, this));
		}
	}

	public void update(IWorld world, WorldService service, Camera camera) {
		distance = Vector3f.sub(camera.getPosition(), new Vector3f(posX, posY, posZ), null).lengthSquared();

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

	protected void update(IWorld world) {
		blocksMeshtemp.addAll(blocksMesh);
		blocksMesh.clear();
		waterTiles.clear();
		particlePoints.clear();
		calculateLight(blocksMesh, world);
		rebuildChunkSection(blocksMesh, world);
		rebuildChunkSection(world);
		blocksMeshtemp.clear();
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

	@Deprecated
	private void rebuildChunkSection(List<Object> cubes, IWorld world) {
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					if (Block.getBlock(blocks[x][y][z]) == Block.Torch) {
						cubes.add(Block.getBlock(blocks[x][y][z])
								.getSingleModel(new Vector3f(x + posX, y + posY, z + posZ)));
						particlePoints.add(new ParticlePoint(
								new Vector3f((x + posX) + 0.5f, (y + posY) + 0.8f, (z + posZ) - 0.5f)));
					} else if (Block.getBlock(blocks[x][y][z]).usesSingleModel()) {
						cubes.add(Block.getBlock(blocks[x][y][z])
								.getSingleModel(new Vector3f(x + posX, y + posY, z + posZ)));
					} else if (Block.getBlock(blocks[x][y][z]) == Block.Water) {
						if (cullFaceUpWater(x + posX, y + posY, z + posZ, world)) {
							waterTiles.add(Block.Water.getWaterTitle(new Vector3f(x + posX, y + posY, z + posZ)));
						}
					}
				}
			}
		}
	}

	private void rebuildChunkSection(IWorld world) {
		tess.begin(BlocksResources.getTessellatorTextureAtlas().getTexture(), BlocksResources.getNormalMap(),
				BlocksResources.getHeightMap(), BlocksResources.getSpecularMap());
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					BlockBase block = Block.getBlock(blocks[x][y][z]);
					if (block == Block.Torch) {
					} else if (block != Block.Air && block != Block.Water && !block.usesSingleModel()
							&& !block.isCustomModel()) {
						tess.generateCube(x + posX, y + posY, (z + posZ) - 1, 1,
								cullFaceUpSolidBlock(block.getId(), x + posX, y + posY, z + posZ, world),
								cullFaceDown(block.getId(), x + posX, y + posY, z + posZ, world),
								cullFaceEast(block.getId(), x + posX, y + posY, z + posZ, world),
								cullFaceWest(block.getId(), x + posX, y + posY, z + posZ, world),
								cullFaceNorth(block.getId(), x + posX, y + posY, z + posZ, world),
								cullFaceSouth(block.getId(), x + posX, y + posY, z + posZ, world), block,
								getTorchLight(x, y, z));
					} else if (block.isCustomModel()) {
						block.generateCustomModel(tess, x + posX, y + posY, (z + posZ) - 1,
								cullFaceUpSolidBlock(block.getId(), x + posX, y + posY, z + posZ, world),
								cullFaceDown(block.getId(), x + posX, y + posY, z + posZ, world),
								cullFaceEast(block.getId(), x + posX, y + posY, z + posZ, world),
								cullFaceWest(block.getId(), x + posX, y + posY, z + posZ, world),
								cullFaceNorth(block.getId(), x + posX, y + posY, z + posZ, world),
								cullFaceSouth(block.getId(), x + posX, y + posY, z + posZ, world),
								getTorchLight(x, y, z));
					} else if (block.usesSingleModel() && !block.isCustomModel()) {
					} else if (block == Block.Water) {
					}
				}
			}
		}
		tess.end();
	}

	@Deprecated
	private void calculateLight(List<Object> cubes, IWorld world) {
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

	public float getDistance() {
		return distance;
	}

	private boolean cullFaceWest(byte block, int x, int y, int z, IWorld world) {
		if (x > (posX) + 1 && x < (posX) + 16) {
			if (getLocalBlock(x - 1, y, z) == block)
				return false;
			if (Block.getBlock(getLocalBlock(x - 1, y, z)).isTransparent())
				return true;
		}
		if (world.getGlobalBlock(x - 1, y, z) == block)
			return false;
		if (Block.getBlock(world.getGlobalBlock(x - 1, y, z)).isTransparent())
			return true;
		return false;
	}

	private boolean cullFaceEast(byte block, int x, int y, int z, IWorld world) {
		if (x > (posX) && x < (posX) + 15) {
			if (getLocalBlock(x + 1, y, z) == block)
				return false;
			if (Block.getBlock(getLocalBlock(x + 1, y, z)).isTransparent())
				return true;
		}
		if (world.getGlobalBlock(x + 1, y, z) == block)
			return false;
		if (Block.getBlock(world.getGlobalBlock(x + 1, y, z)).isTransparent())
			return true;
		return false;
	}

	private boolean cullFaceDown(byte block, int x, int y, int z, IWorld world) {
		if (y > (posY) + 1 && y < (posY) + 16) {
			if (getLocalBlock(x, y - 1, z) == block)
				return false;
			if (Block.getBlock(getLocalBlock(x, y - 1, z)).isTransparent())
				return true;
		}
		if (world.getGlobalBlock(x, y - 1, z) == block)
			return false;
		if (Block.getBlock(world.getGlobalBlock(x, y - 1, z)).isTransparent())
			return true;
		return false;
	}

	private boolean cullFaceUpSolidBlock(byte block, int x, int y, int z, IWorld world) {
		if (y > (posY) && y < (posY) + 15) {
			if (getLocalBlock(x, y + 1, z) == block)
				return false;
			if (Block.getBlock(getLocalBlock(x, y + 1, z)).isTransparent())
				return true;
		}
		if (world.getGlobalBlock(x, y + 1, z) == block)
			return false;
		if (Block.getBlock(world.getGlobalBlock(x, y + 1, z)).isTransparent())
			return true;
		return false;
	}

	private boolean cullFaceUpWater(int x, int y, int z, IWorld world) {
		if (y > (posY) && y < (posY) + 15) {
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

	private boolean cullFaceNorth(byte block, int x, int y, int z, IWorld world) {
		if (z > (posZ) + 1 && z < (posZ) + 16) {
			if (getLocalBlock(x, y, z - 1) == block)
				return false;
			if (Block.getBlock(getLocalBlock(x, y, z - 1)).isTransparent())
				return true;
		}
		if (world.getGlobalBlock(x, y, z - 1) == block)
			return false;
		if (Block.getBlock(world.getGlobalBlock(x, y, z - 1)).isTransparent())
			return true;
		return false;
	}

	private boolean cullFaceSouth(byte block, int x, int y, int z, IWorld world) {
		if (z > (posZ) && z < (posZ) + 15) {
			if (getLocalBlock(x, y, z + 1) == block)
				return false;
			if (Block.getBlock(getLocalBlock(x, y, z + 1)).isTransparent())
				return true;
		}
		if (world.getGlobalBlock(x, y, z + 1) == block)
			return false;

		if (Block.getBlock(world.getGlobalBlock(x, y, z + 1)).isTransparent())
			return true;
		return false;
	}

	public void render(GameResources gm) {
		if (readyToRender) {
			tess.draw(gm);
			gm.getRenderer().processChunk(blocksMesh, gm);
			glDisable(GL_CULL_FACE);
			gm.getRenderer().getWaterRenderer().render(waterTiles, gm);
			glEnable(GL_CULL_FACE);
		} else {
			gm.getRenderer().processChunk(blocksMeshtemp, gm);
		}
	}

	public void renderShadow(GameResources gm) {
		if (readyToRender) {
			gm.getMasterShadowRenderer().renderChunk(blocksMesh, gm);
			tess.drawShadow(gm.getSun_Camera());
		}
	}

	public void renderOcclusion(GameResources gm) {
		if (readyToRender) {
			glBeginQuery(GL_SAMPLES_PASSED, tess.getOcclusion());
			tess.drawOcclusion(gm.getCamera(), gm.getRenderer().getProjectionMatrix());
			glDisable(GL_CULL_FACE);
			gm.getRenderer().getWaterRenderer().renderOcclusion(waterTiles, gm);
			glEnable(GL_CULL_FACE);
			glEndQuery(GL_SAMPLES_PASSED);
		}
	}

	private void clear() {
		blocksMesh.clear();
		blocksMeshtemp.clear();
		particlePoints.clear();
		waterTiles.clear();
		tess.cleanUp();
	}

	public void dispose() {
		clear();
	}

	public Tessellator getTess() {
		return tess;
	}

}