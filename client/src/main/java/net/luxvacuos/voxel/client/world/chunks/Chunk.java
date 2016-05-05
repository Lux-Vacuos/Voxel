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

package net.luxvacuos.voxel.client.world.chunks;

import static org.lwjgl.opengl.GL15.GL_SAMPLES_PASSED;
import static org.lwjgl.opengl.GL15.glBeginQuery;
import static org.lwjgl.opengl.GL15.glEndQuery;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.models.ParticlePoint;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.Dimension;
import net.luxvacuos.voxel.client.world.DimensionService;
import net.luxvacuos.voxel.client.world.block.Block;
import net.luxvacuos.voxel.client.world.block.BlockBase;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.client.world.entities.Camera;

/**
 * Chunk
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category dimension
 */
public class Chunk {

	/**
	 * Chunk Data
	 */
	public int posX, posY, posZ, cx, cy, cz;
	public byte[][][] blocks;
	public byte[][][] lightMap;
	private transient Queue<ParticlePoint> particlePoints;
	transient int sizeX, sizeY, sizeZ;
	private transient Tessellator tess;
	private transient float distance;
	public transient boolean needsRebuild = true, updated = false, updating = false, empty = true, visible = false,
			creating = false, decorating = false, updatingBlocks = false, updatedBlocks = false;
	public boolean created = false, decorated = false, cavesGenerated = false;
	public int version = 1;

	public Chunk(int cx, int cy, int cz, Dimension dimension, GameResources gm) throws Exception {
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
		this.posX = cx * 16;
		this.posZ = cz * 16;
		this.posY = cy * 16;
		init(dimension, gm);
	}

	/**
	 * Empty Constructor only for load from file
	 */
	public Chunk() {
	}

	public void init(Dimension dimension, GameResources gm) throws Exception {
		load(gm);
		blocks = new byte[sizeX][sizeY][sizeZ];
		lightMap = new byte[sizeX][sizeY][sizeZ];
	}

	public void load(GameResources gm) throws Exception {
		particlePoints = new ConcurrentLinkedQueue<ParticlePoint>();
		sizeX = VoxelVariables.CHUNK_SIZE;
		sizeY = VoxelVariables.CHUNK_HEIGHT;
		sizeZ = VoxelVariables.CHUNK_SIZE;
		tess = new Tessellator(gm);
	}

	public void rebuild(DimensionService service, Dimension dimension) {
		if ((needsRebuild || !updated) && !updating) {
			updating = true;
			service.add_worker(new ChunkWorkerMesh(dimension, this));
		}
	}

	public void update(Dimension dimension, DimensionService service, Camera camera, float delta) {
		distance = (float) Vector3f.sub(camera.getPosition(), new Vector3f(posX, posY, posZ), null).lengthSquared();

		if (!created && !creating) {
			creating = true;
			service.add_worker(new ChunkWorkerGenerator(dimension, this));
		}
		if (!updatingBlocks && !updatedBlocks) {
			updatingBlocks = true;
			service.add_worker(new ChunkWorkerUpdate(dimension, this));
		}

		if (!decorated) {
			boolean can = true;
			T: for (int jx = cx - 1; jx < cx + 1; jx++)
				for (int jz = cz - 1; jz < cz + 1; jz++)
					for (int jy = cy - 1; jy < cy + 1; jy++)
						if (!dimension.hasChunk(jx, jy, jz)) {
							can = false;
							break T;
						}

			if (can)
				decorate(dimension);
		}
	}

	protected void update(Dimension dimension) {
		particlePoints.clear();
		rebuildChunkSection(dimension);
	}

	protected void updateBlocks(Dimension dimension) {
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					if (Block.getBlock(dimension.getGlobalBlock(x + posX, y + posY, z + posZ)).isAffectedByGravity()
							|| Block.getBlock(dimension.getGlobalBlock(x + posX, y + posY, z + posZ)).isFluid()) {
						if (dimension.getGlobalBlock(x + posX, y + posY - 1, z + posZ) == 0) {
							if (dimension.setGlobalBlock(x + posX, y + posY - 1, z + posZ,
									dimension.getGlobalBlock(x + posX, y + posY, z + posZ)))
								dimension.setGlobalBlock(x + posX, y + posY, z + posZ, (byte) 0);
						}
					}
				}
			}
		}

	}

	protected void createBasicTerrain(Dimension dimension) {
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					if (y + posY <= 128)
						setLocalBlock(x, y, z, Block.Water.getId());
				}
			}
		}
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				double tempHeight = dimension.getNoise().getNoise((int) ((x + cx * 16)), (int) ((z + cz * 16)));
				tempHeight += 1;
				int height = (int) (128 * Maths.clamp(tempHeight));
				for (int y = 0; y < sizeY; y++) {
					if (y + posY == height - 1 && y + posY > 128)
						setLocalBlock(x, y, z, Block.Grass.getId());
					else if (y + posY == height - 2 && y + posY > 128)
						setLocalBlock(x, y, z, Block.Dirt.getId());
					else if (y + posY == height - 1 && y + posY < 129)
						setLocalBlock(x, y, z, Block.Sand.getId());
					else if (y + posY < height - 2)
						setLocalBlock(x, y, z, Block.Stone.getId());
					else if (getLocalBlock(x, y, z) != Block.Water.getId())
						setLocalBlock(x, y, z, Block.Air.getId());
				}
			}
		}
		dimension.getChunkGenerator().generateCaves(this, dimension);
		created = true;
	}

	protected void decorate(Dimension dimension) {
		for (int i = 0; i < 4; i++) {
			int xx = Maths.randInt(0, 15);
			int zz = Maths.randInt(0, 15);
			double tempHeight = dimension.getNoise().getNoise((int) ((xx + cx * 16)), (int) ((zz + cz * 16)));
			tempHeight += 1;
			int height = (int) (128 * Maths.clamp(tempHeight));
			int h = getLocalBlock(xx, height - 1, zz);
			if (h == Block.Grass.getId() || h == Block.Dirt.getId())
				dimension.getChunkGenerator().addTree(dimension, xx + cx * 16, height, zz + cz * 16,
						Maths.randInt(4, 10), new Random(dimension.getSeed()));
		}
		decorated = true;
	}

	private void rebuildChunkSection(Dimension dimension) {
		tess.begin(BlocksResources.getTessellatorTextureAtlas().getTexture(), BlocksResources.getNormalMap(),
				BlocksResources.getHeightMap(), BlocksResources.getSpecularMap());
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					BlockBase block = Block.getBlock(blocks[x][y][z]);
					if (block == Block.Torch) {
						block.generateCustomModel(tess, x + posX, y + posY, z + posZ, 1,
								cullFaceUpSolidBlock(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceDown(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceEast(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceWest(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceNorth(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceSouth(block.getId(), x + posX, y + posY, z + posZ, dimension),
								dimension.getLight(x + posX, y + posY + 1, z + posZ),
								dimension.getLight(x + posX + 1, y + posY + 1, z + posZ),
								dimension.getLight(x + posX, y + posY + 1, z + posZ + 1),
								dimension.getLight(x + posX + 1, y + posY + 1, z + posZ + 1),
								dimension.getLight(x + posX, y + posY, z + posZ),
								dimension.getLight(x + posX + 1, y + posY, z + posZ),
								dimension.getLight(x + posX, y + posY, z + posZ + 1),
								dimension.getLight(x + posX + 1, y + posY, z + posZ + 1));
						particlePoints.add(new ParticlePoint(
								new Vector3f((x + posX) + 0.5f, (y + posY) + 0.8f, (z + posZ) + 0.5f)));
					} else if (block != Block.Air && !block.isCustomModel()) {
						tess.generateCube(x + posX, y + posY, z + posZ, 1,
								cullFaceUpSolidBlock(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceDown(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceEast(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceWest(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceNorth(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceSouth(block.getId(), x + posX, y + posY, z + posZ, dimension), block,
								dimension.getLight(x + posX, y + posY + 1, z + posZ),
								dimension.getLight(x + posX + 1, y + posY + 1, z + posZ),
								dimension.getLight(x + posX, y + posY + 1, z + posZ + 1),
								dimension.getLight(x + posX + 1, y + posY + 1, z + posZ + 1),
								dimension.getLight(x + posX, y + posY, z + posZ),
								dimension.getLight(x + posX + 1, y + posY, z + posZ),
								dimension.getLight(x + posX, y + posY, z + posZ + 1),
								dimension.getLight(x + posX + 1, y + posY, z + posZ + 1));
					} else if (block != Block.Air && block.isCustomModel()) {
						block.generateCustomModel(tess, x + posX, y + posY, z + posZ, 1,
								cullFaceUpSolidBlock(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceDown(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceEast(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceWest(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceNorth(block.getId(), x + posX, y + posY, z + posZ, dimension),
								cullFaceSouth(block.getId(), x + posX, y + posY, z + posZ, dimension),
								dimension.getLight(x + posX, y + posY + 1, z + posZ),
								dimension.getLight(x + posX + 1, y + posY + 1, z + posZ),
								dimension.getLight(x + posX, y + posY + 1, z + posZ + 1),
								dimension.getLight(x + posX + 1, y + posY + 1, z + posZ + 1),
								dimension.getLight(x + posX, y + posY, z + posZ),
								dimension.getLight(x + posX + 1, y + posY, z + posZ),
								dimension.getLight(x + posX, y + posY, z + posZ + 1),
								dimension.getLight(x + posX + 1, y + posY, z + posZ + 1));
					}
				}
			}
		}
		tess.end();
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

	private boolean cullFaceWest(byte block, int x, int y, int z, Dimension dimension) {
		if (x > (posX) + 1 && x < (posX) + 16) {
			if (getLocalBlock(x - 1, y, z) == block)
				return false;
			if (Block.getBlock(getLocalBlock(x - 1, y, z)).isTransparent())
				return true;
		}
		if (dimension.getGlobalBlock(x - 1, y, z) == block)
			return false;
		if (Block.getBlock(dimension.getGlobalBlock(x - 1, y, z)).isTransparent())
			return true;
		return false;
	}

	private boolean cullFaceEast(byte block, int x, int y, int z, Dimension dimension) {
		if (x > (posX) && x < (posX) + 15) {
			if (getLocalBlock(x + 1, y, z) == block)
				return false;
			if (Block.getBlock(getLocalBlock(x + 1, y, z)).isTransparent())
				return true;
		}
		if (dimension.getGlobalBlock(x + 1, y, z) == block)
			return false;
		if (Block.getBlock(dimension.getGlobalBlock(x + 1, y, z)).isTransparent())
			return true;
		return false;
	}

	private boolean cullFaceDown(byte block, int x, int y, int z, Dimension dimension) {
		if (y > (posY) + 1 && y < (posY) + 16) {
			if (getLocalBlock(x, y - 1, z) == block)
				return false;
			if (Block.getBlock(getLocalBlock(x, y - 1, z)).isTransparent())
				return true;
		}
		if (dimension.getGlobalBlock(x, y - 1, z) == block)
			return false;
		if (Block.getBlock(dimension.getGlobalBlock(x, y - 1, z)).isTransparent())
			return true;
		return false;
	}

	private boolean cullFaceUpSolidBlock(byte block, int x, int y, int z, Dimension dimension) {
		if (y > (posY) && y < (posY) + 15) {
			if (getLocalBlock(x, y + 1, z) == block)
				return false;
			if (Block.getBlock(getLocalBlock(x, y + 1, z)).isTransparent())
				return true;
		}
		if (dimension.getGlobalBlock(x, y + 1, z) == block)
			return false;
		if (Block.getBlock(dimension.getGlobalBlock(x, y + 1, z)).isTransparent())
			return true;
		return false;
	}

	private boolean cullFaceNorth(byte block, int x, int y, int z, Dimension dimension) {
		if (z > (posZ) + 1 && z < (posZ) + 16) {
			if (getLocalBlock(x, y, z - 1) == block)
				return false;
			if (Block.getBlock(getLocalBlock(x, y, z - 1)).isTransparent())
				return true;
		}
		if (dimension.getGlobalBlock(x, y, z - 1) == block)
			return false;
		if (Block.getBlock(dimension.getGlobalBlock(x, y, z - 1)).isTransparent())
			return true;
		return false;
	}

	private boolean cullFaceSouth(byte block, int x, int y, int z, Dimension dimension) {
		if (z > (posZ) && z < (posZ) + 15) {
			if (getLocalBlock(x, y, z + 1) == block)
				return false;
			if (Block.getBlock(getLocalBlock(x, y, z + 1)).isTransparent())
				return true;
		}
		if (dimension.getGlobalBlock(x, y, z + 1) == block)
			return false;

		if (Block.getBlock(dimension.getGlobalBlock(x, y, z + 1)).isTransparent())
			return true;
		return false;
	}

	public void render(GameResources gm) {
		tess.draw(gm);
	}

	public void renderShadow(GameResources gm) {
		tess.drawShadow(gm.getSun_Camera());
	}

	public void renderOcclusion(GameResources gm) {
		glBeginQuery(GL_SAMPLES_PASSED, tess.getOcclusion());
		tess.drawOcclusion(gm.getCamera(), gm.getRenderer().getProjectionMatrix());
		glEndQuery(GL_SAMPLES_PASSED);
	}

	private void clear() {
		particlePoints.clear();
		tess.cleanUp();
	}

	public void dispose() {
		clear();
	}

	public Tessellator getTess() {
		return tess;
	}

}