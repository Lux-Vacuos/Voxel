package io.github.guerra24.voxel.client.world.chunks;

import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.kernel.util.ArrayList3;
import io.github.guerra24.voxel.client.world.block.Block;
import io.github.guerra24.voxel.client.world.entities.Entity;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

public class Chunk {

	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_HEIGHT = 128;

	public boolean isToRebuild = false;
	private ArrayList3<Entity> cubes = new ArrayList3<Entity>();
	private Vector3f pos;
	private int sizeX, sizeY, sizeZ, viewDistanceX = 32 - 8,
			viewDistanceZ = 16 - 8;
	public boolean isNotLoaded;
	public ChunkInfo blocksData;

	public Chunk(Vector3f pos) {
		this.pos = pos;
		blocksData = new ChunkInfo();
		init();
	}

	public void init() {
		sizeX = (int) (pos.getX() + CHUNK_SIZE);
		sizeY = (int) (pos.getY() + CHUNK_HEIGHT);
		sizeZ = (int) (pos.getZ() + CHUNK_SIZE);

		blocksData.blocks = new byte[sizeX][sizeY][sizeZ];

		createChunk();
		rebuild();
	}

	public void update() {
		if (Kernel.gameResources.camera.getPosition().z > pos.z + viewDistanceX
				&& !isNotLoaded) {
			Kernel.gameResources.allEntities.removeAll(cubes);
			isNotLoaded = true;
		} else if (Kernel.gameResources.camera.getPosition().x > pos.x
				+ viewDistanceX
				&& !isNotLoaded) {
			Kernel.gameResources.allEntities.removeAll(cubes);
			isNotLoaded = true;
		} else if (Kernel.gameResources.camera.getPosition().z < pos.z
				- viewDistanceZ
				&& !isNotLoaded) {
			Kernel.gameResources.allEntities.removeAll(cubes);
			isNotLoaded = true;
		} else if (Kernel.gameResources.camera.getPosition().x < pos.x
				- viewDistanceZ
				&& !isNotLoaded) {
			Kernel.gameResources.allEntities.removeAll(cubes);
			isNotLoaded = true;
		} else if (isNotLoaded) {
			if (Kernel.gameResources.camera.getPosition().x < pos.x
					+ viewDistanceX) {
				if (Kernel.gameResources.camera.getPosition().z < pos.z
						+ viewDistanceX) {
					if (Kernel.gameResources.camera.getPosition().x > pos.x
							- viewDistanceZ) {
						if (Kernel.gameResources.camera.getPosition().z > pos.z
								- viewDistanceZ) {
							Kernel.gameResources.allEntities.addAll(cubes);
							isNotLoaded = false;
						}
					}
				}
			}
		} else if (isToRebuild) {
			Kernel.gameResources.allEntities.removeAll(cubes);
			cubes.clear();
			rebuild();
			Kernel.gameResources.allEntities.addAll(cubes);
			isToRebuild = false;
		}
	}

	private void createChunk() {
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int z = (int) pos.getZ(); z < sizeZ; z++) {
				int rand = (int) (sizeY * clamp(Kernel.world.perlinNoiseArray[x][z]));
				for (int y = (int) pos.getY(); y < rand; y++) {
					if (y == rand - 1 && y > 65) {
						blocksData.blocks[x][y][z] = Block.Grass.getId();
					} else if (y == rand - 2 && y > 65) {
						blocksData.blocks[x][y][z] = Block.Dirt.getId();
					} else if (y == rand - 1 && y < 66) {
						blocksData.blocks[x][y][z] = Block.Sand.getId();
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

	private void rebuild() {
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int z = (int) pos.getZ(); z < sizeZ; z++) {
				for (int y = (int) pos.getY(); y < sizeY; y++) {
					if (blocksData.blocks[x][y][z] == Block.Indes.getId()
							&& !checkBlockNotInView(x, y, z)) {
						cubes.add(Block.Indes.getEntity(new Vector3f(x, y, z)));
					} else if (blocksData.blocks[x][y][z] == Block.Stone
							.getId() && !checkBlockNotInView(x, y, z)) {
						cubes.add(Block.Stone.getEntity(new Vector3f(x, y, z)));
					} else if (blocksData.blocks[x][y][z] == Block.Grass
							.getId() && !checkBlockNotInView(x, y, z)) {
						cubes.add(Block.Grass.getEntity(new Vector3f(x, y, z)));
					} else if (blocksData.blocks[x][y][z] == Block.Sand.getId()
							&& !checkBlockNotInView(x, y, z)) {
						cubes.add(Block.Sand.getEntity(new Vector3f(x, y, z)));
					} else if (blocksData.blocks[x][y][z] == Block.Dirt.getId()
							&& !checkBlockNotInView(x, y, z)) {
						cubes.add(Block.Dirt.getEntity(new Vector3f(x, y, z)));
					}
				}
			}
		}
	}

	private boolean checkBlockNotInView(int x, int y, int z) {
		boolean facesHidden[] = new boolean[6];
		if (x > pos.getX()) {
			if (blocksData.blocks[x - 1][y][z] != 0)
				facesHidden[0] = true;
			else
				facesHidden[0] = false;
		} else {
			facesHidden[0] = false;
		}
		if (x < sizeX - 1) {
			if (blocksData.blocks[x + 1][y][z] != 0)
				facesHidden[1] = true;
			else
				facesHidden[1] = false;
		} else {
			facesHidden[1] = false;
		}

		if (y > pos.getY()) {
			if (blocksData.blocks[x][y - 1][z] != 0)
				facesHidden[2] = true;
			else
				facesHidden[2] = false;
		} else {
			facesHidden[2] = false;
		}
		if (y < sizeY - 1) {
			if (blocksData.blocks[x][y + 1][z] != 0)
				facesHidden[3] = true;
			else
				facesHidden[3] = false;
		} else {
			facesHidden[3] = false;
		}

		if (z > pos.getZ()) {
			if (blocksData.blocks[x][y][z - 1] != 0)
				facesHidden[4] = true;
			else
				facesHidden[4] = false;
		} else {
			facesHidden[4] = false;
		}
		if (z < sizeZ - 1) {
			if (blocksData.blocks[x][y][z + 1] != 0)
				facesHidden[5] = true;
			else
				facesHidden[5] = false;
		} else {
			facesHidden[5] = false;
		}
		return facesHidden[0] && facesHidden[1] && facesHidden[2]
				&& facesHidden[3] && facesHidden[4] && facesHidden[5];
	}

	public static float clamp(float val) {
		return Math.max(0, Math.min(128, val));
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public void dispose() {
		Kernel.gameResources.allEntities.clear();
		cubes.clear();
	}
}
