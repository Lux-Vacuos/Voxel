package net.luxvacuos.voxel.server.world.chunks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.luxvacuos.voxel.server.core.VoxelVariables;
import net.luxvacuos.voxel.server.util.Maths;
import net.luxvacuos.voxel.server.world.Dimension;
import net.luxvacuos.voxel.server.world.block.Block;
import net.luxvacuos.voxel.server.world.block.BlockBase;
import net.luxvacuos.voxel.server.world.block.BlockEntity;

public class Chunk {

	public int posX, posY, posZ, cx, cy, cz;
	public BlockBase[][][] blocks;
	public byte[][][] lightMap;
	public boolean created = false, decorated = false, cavesGenerated = false;
	public transient boolean needsRebuild = true, updated = false, updating = false, empty = true, visible = false,
			creating = false, decorating = false, updatingBlocks = false, updatedBlocks = false;
	protected transient float distance;
	protected transient List<BlockEntity> blockEntities;
	protected transient int sizeX, sizeY, sizeZ;

	public Chunk(int cx, int cy, int cz) {
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
		this.posX = cx * 16;
		this.posZ = cz * 16;
		this.posY = cy * 16;
		init();
	}

	public Chunk() {
	}

	public void init() {
		onLoad();
		blocks = new BlockBase[sizeX][sizeY][sizeZ];
		lightMap = new byte[sizeX][sizeY][sizeZ];
	}

	public void onLoad() {
		blockEntities = new ArrayList<>();
		sizeX = VoxelVariables.CHUNK_SIZE;
		sizeY = VoxelVariables.CHUNK_HEIGHT;
		sizeZ = VoxelVariables.CHUNK_SIZE;
	}

	public void update(Dimension dimension, float delta) {
		empty = Arrays.asList(blocks).size() == 0;
		if (empty)
			return;

		if (!created && !creating) {
			creating = true;
			dimension.getDimensionService().add_worker(new ChunkWorkerGenerator(dimension, this));
		}
		if (!updatingBlocks && !updatedBlocks) {
			updatingBlocks = true;
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
			dimension.getDimensionService().add_worker(new ChunkWorkerUpdate(dimension, this));
		}
		for (BlockEntity blockEntity : blockEntities) {
			blockEntity.update(dimension, delta);
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
		created = true;
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

	public BlockBase getLocalBlock(int x, int y, int z) {
		BlockBase b = blocks[x & 0xF][y & 0xF][z & 0xF];
		if (b != null)
			return b;
		return Block.Air;
	}

	public void setLocalBlock(int x, int y, int z, BlockBase id) {
		updated = false;
		needsRebuild = true;
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

	public float getDistance() {
		return distance;
	}

}