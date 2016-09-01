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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.rendering.api.opengl.Tessellator;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.models.ParticlePoint;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.Dimension;
import net.luxvacuos.voxel.client.world.block.Block;
import net.luxvacuos.voxel.client.world.block.BlockBase;
import net.luxvacuos.voxel.client.world.block.BlockEntity;
import net.luxvacuos.voxel.client.world.block.BlocksResources;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.world.utils.ChunkNode;


@Deprecated
public class Chunk {

	public int posX, posY, posZ, cx, cy, cz;
	public BlockBase[][][] blocks;
	public byte[][][] lightMap;
	public boolean decorated = false, cavesGenerated = false;
	public transient boolean empty = true, updatedBlocks = false, rebuild = true, loaded = false, rebuilding = false,
			remove = false;
	public ChunkNode node;
	protected transient Tessellator tess;
	protected transient float distance;
	protected transient List<BlockEntity> blockEntities;
	protected transient int sizeX, sizeY, sizeZ;
	protected transient Queue<ParticlePoint> particlePoints;

	public Chunk(ChunkNode node, Dimension dim) {
		this.cx = node.getX();
		this.cy = node.getY();
		this.cz = node.getZ();
		this.posX = cx * 16;
		this.posZ = cz * 16;
		this.posY = cy * 16;
		this.node = node;
	}

	public Chunk() {
	}

	public void init(Dimension dim) {
		onLoad();
		blocks = new BlockBase[sizeX][sizeY][sizeZ];
		lightMap = new byte[sizeX][sizeY][sizeZ];
		createBasicTerrain(dim);
		loaded = true;
	}

	public void onLoad() {
		particlePoints = new ConcurrentLinkedQueue<ParticlePoint>();
		blockEntities = new ArrayList<>();
		sizeX = 16;
		sizeY = 16;
		sizeZ = 16;
		if (blocks != null) {
			for (BlockBase[][] blockBases : blocks) {
				for (BlockBase[] blockBases2 : blockBases) {
					for (BlockBase b : blockBases2) {
						if (b instanceof BlockEntity) {
							if (((BlockEntity) b).isObjModel())
								((BlockEntity) b).init();
						}
					}
				}
			}
		} else {
			blocks = new BlockBase[sizeX][sizeY][sizeZ];
		}
		if (lightMap == null) {
			lightMap = new byte[sizeX][sizeY][sizeZ];
		}
	}

	public void update(Dimension dimension, Camera camera, float delta) {
		distance = (float) Vector3f.sub(camera.getPosition(), new Vector3f(posX + 8f, posY + 8f, posZ + 8f), null)
				.lengthSquared();
		empty = Arrays.asList(blocks).size() == 0;
		if (empty)
			return;

		if (!updatedBlocks) {
			blockEntities.clear();
			for (BlockBase[][] blockBases : blocks) {
				for (BlockBase[] blockBases2 : blockBases) {
					for (BlockBase b : blockBases2) {
						if (b instanceof BlockEntity) {
							blockEntities.add((BlockEntity) b);
						}
					}
				}
			}
			updateBlocks(dimension);
			updatedBlocks = true;
		}
		for (BlockEntity blockEntity : blockEntities) {
			blockEntity.update(dimension, delta);
		}
	}

	public void updateGraphics() {
		if (!empty && tess == null) {
			generateGraphics();
		} else if (empty && tess != null) {
			disposeGraphics();
		}
	}

	void createBasicTerrain(Dimension dimension) {
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					if (y + posY <= 128)
						setLocalBlock(x, y, z, Block.Water);
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
						setLocalBlock(x, y, z, Block.Grass);
					else if (y + posY == height - 2 && y + posY > 128)
						setLocalBlock(x, y, z, Block.Dirt);
					else if (y + posY == height - 1 && y + posY < 129)
						setLocalBlock(x, y, z, Block.Sand);
					else if (y + posY == height - 2 && y + posY < 129)
						setLocalBlock(x, y, z, Block.Sand);
					else if (y + posY < height - 2)
						setLocalBlock(x, y, z, Block.Stone);
					else if (getLocalBlock(x, y, z) != Block.Water)
						setLocalBlock(x, y, z, Block.Air);
				}
			}
		}
		dimension.getChunkGenerator().generateCaves(this, dimension);
	}

	void updateBlocks(Dimension dimension) {
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					BlockBase b = dimension.getGlobalBlock(x + posX, y + posY, z + posZ);
					if (b.isAffectedByGravity() || b.isFluid()) {
						if (dimension.getGlobalBlock(x + posX, y + posY - 1, z + posZ) == Block.Air) {
							if (dimension.setGlobalBlock(x + posX, y + posY - 1, z + posZ, b))
								dimension.setGlobalBlock(x + posX, y + posY, z + posZ, Block.Air);
						}
					}
				}
			}
		}

	}

	public boolean checkForRebuild() {
		return rebuild && !rebuilding && !empty;
	}

	public void rebuildMesh(Dimension dimension) {
		if (checkForRebuild()) {
			particlePoints.clear();
			if (rebuildChunkSection(dimension)) {
				rebuild = false;
			}
		}
	}

	private boolean rebuildChunkSection(Dimension dimension) {
		if (tess == null)
			return false;
		rebuilding = true;
		tess.begin(BlocksResources.getTessellatorTextureAtlas().getTexture(), BlocksResources.getNormalMap(),
				BlocksResources.getHeightMap(), BlocksResources.getSpecularMap());
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = 0; y < sizeY; y++) {
					BlockBase block = blocks[x][y][z];
					if (block == null)
						continue;
					if (block == Block.Torch) {
						block.generateCustomModel(tess, x + posX, y + posY, z + posZ, 1,
								cullFaceUpSolidBlock(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceDown(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceEast(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceWest(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceNorth(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceSouth(block, x + posX, y + posY, z + posZ, dimension),
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
					} else if (block != Block.Air && !block.isCustomModel() && !block.isObjModel()) {
						tess.generateCube(x + posX, y + posY, z + posZ, 1,
								cullFaceUpSolidBlock(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceDown(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceEast(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceWest(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceNorth(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceSouth(block, x + posX, y + posY, z + posZ, dimension), block,
								dimension.getLight(x + posX, y + posY + 1, z + posZ),
								dimension.getLight(x + posX + 1, y + posY + 1, z + posZ),
								dimension.getLight(x + posX, y + posY + 1, z + posZ + 1),
								dimension.getLight(x + posX + 1, y + posY + 1, z + posZ + 1),
								dimension.getLight(x + posX, y + posY, z + posZ),
								dimension.getLight(x + posX + 1, y + posY, z + posZ),
								dimension.getLight(x + posX, y + posY, z + posZ + 1),
								dimension.getLight(x + posX + 1, y + posY, z + posZ + 1));
					} else if (block != Block.Air && block.isCustomModel() && !block.isObjModel()) {
						block.generateCustomModel(tess, x + posX, y + posY, z + posZ, 1,
								cullFaceUpSolidBlock(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceDown(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceEast(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceWest(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceNorth(block, x + posX, y + posY, z + posZ, dimension),
								cullFaceSouth(block, x + posX, y + posY, z + posZ, dimension),
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
		rebuilding = false;
		return true;
	}

	protected boolean cullFaceWest(BlockBase block, int x, int y, int z, Dimension dimension) {
		if (x > (posX) + 1 && x < (posX) + 16) {
			if (getLocalBlock(x - 1, y, z).getId() == block.getId())
				return false;
			if (getLocalBlock(x - 1, y, z).isTransparent())
				return true;
		}
		if (dimension.getGlobalBlock(x - 1, y, z).getId() == block.getId())
			return false;
		if (dimension.getGlobalBlock(x - 1, y, z).isTransparent())
			return true;
		return false;
	}

	protected boolean cullFaceEast(BlockBase block, int x, int y, int z, Dimension dimension) {
		if (x > (posX) && x < (posX) + 15) {
			if (getLocalBlock(x + 1, y, z).getId() == block.getId())
				return false;
			if (getLocalBlock(x + 1, y, z).isTransparent())
				return true;
		}
		if (dimension.getGlobalBlock(x + 1, y, z).getId() == block.getId())
			return false;
		if (dimension.getGlobalBlock(x + 1, y, z).isTransparent())
			return true;
		return false;
	}

	protected boolean cullFaceDown(BlockBase block, int x, int y, int z, Dimension dimension) {
		if (y > (posY) + 1 && y < (posY) + 16) {
			if (getLocalBlock(x, y - 1, z).getId() == block.getId())
				return false;
			if (getLocalBlock(x, y - 1, z).isTransparent())
				return true;
		}
		if (dimension.getGlobalBlock(x, y - 1, z).getId() == block.getId())
			return false;
		if (dimension.getGlobalBlock(x, y - 1, z).isTransparent())
			return true;
		return false;
	}

	protected boolean cullFaceUpSolidBlock(BlockBase block, int x, int y, int z, Dimension dimension) {
		if (y > (posY) && y < (posY) + 15) {
			if (getLocalBlock(x, y + 1, z).getId() == block.getId())
				return false;
			if (getLocalBlock(x, y + 1, z).isTransparent())
				return true;
		}
		if (dimension.getGlobalBlock(x, y + 1, z).getId() == block.getId())
			return false;
		if (dimension.getGlobalBlock(x, y + 1, z).isTransparent())
			return true;
		return false;
	}

	protected boolean cullFaceNorth(BlockBase block, int x, int y, int z, Dimension dimension) {
		if (z > (posZ) + 1 && z < (posZ) + 16) {
			if (getLocalBlock(x, y, z - 1).getId() == block.getId())
				return false;
			if (getLocalBlock(x, y, z - 1).isTransparent())
				return true;
		}
		if (dimension.getGlobalBlock(x, y, z - 1).getId() == block.getId())
			return false;
		if (dimension.getGlobalBlock(x, y, z - 1).isTransparent())
			return true;
		return false;
	}

	protected boolean cullFaceSouth(BlockBase block, int x, int y, int z, Dimension dimension) {
		if (z > (posZ) && z < (posZ) + 15) {
			if (getLocalBlock(x, y, z + 1).getId() == block.getId())
				return false;
			if (getLocalBlock(x, y, z + 1).isTransparent())
				return true;
		}
		if (dimension.getGlobalBlock(x, y, z + 1).getId() == block.getId())
			return false;

		if (dimension.getGlobalBlock(x, y, z + 1).isTransparent())
			return true;
		return false;
	}

	public void render(GameResources gm, boolean transparent) {
		if (tess == null)
			return;
		if (!empty) {
			tess.draw(gm, transparent);
			if (!transparent)
				for (BlockEntity blockEntity : blockEntities) {
					if (blockEntity.isObjModel())
						blockEntity.render();
				}
		}
	}

	public void renderShadow(GameResources gm) {
		if (tess == null)
			return;
		if (!empty)
			tess.drawShadow(gm.getSun_Camera());
	}

	public void renderOcclusion(GameResources gm) {
		if (tess == null)
			return;
		if (!empty) {
			glBeginQuery(GL_SAMPLES_PASSED, tess.getOcclusion());
			tess.drawOcclusion(gm.getCamera(), gm.getRenderer().getProjectionMatrix());
			glEndQuery(GL_SAMPLES_PASSED);
		}
	}

	public void dispose() {
		if (!loaded)
			return;
		if (particlePoints != null)
			particlePoints.clear();
		if (tess != null)
			tess.cleanUp();
	}

	public void generateGraphics() {
		tess = new Tessellator();
	}

	public void disposeGraphics() {
		tess.cleanUp();
		tess = null;
	}

	public Tessellator getTess() {
		return tess;
	}

	public BlockBase getLocalBlock(int x, int y, int z) {
		BlockBase b = blocks[x & 0xF][y & 0xF][z & 0xF];
		if (b != null)
			return b;
		return Block.Air;
	}

	public void setLocalBlock(int x, int y, int z, BlockBase id) {
		rebuild = true;
		updatedBlocks = false;
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

}