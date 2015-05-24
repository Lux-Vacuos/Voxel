package net.guerra24.voxel.client.world.chunks;

import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.client.kernel.Kernel;
import net.guerra24.voxel.client.kernel.entities.Entity;
import net.guerra24.voxel.client.world.block.Block;

import org.lwjgl.util.vector.Vector3f;

public class Chunk {

	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_HEIGHT = 128;

	private List<Entity> cubes = new ArrayList<Entity>();
	private Vector3f pos;
	private byte[][][] blocks;
	private int sizeX, sizeY, sizeZ;
	private boolean isNotLoaded;

	public Chunk(Vector3f pos) {
		this.pos = pos;
		init();
	}

	public void init() {
		sizeX = (int) (pos.getX() + CHUNK_SIZE);
		sizeY = (int) (pos.getY() + CHUNK_HEIGHT);
		sizeZ = (int) (pos.getZ() + CHUNK_SIZE);

		blocks = new byte[sizeX][sizeY][sizeZ];

		createChunk();
		rebuild();
	}

	public void update() {
		float X = pos.x;
		float Z = pos.z;
		if (Kernel.gameResources.camera.getPosition().z > Z + 32
				&& !isNotLoaded) {
			Kernel.gameResources.allEntities.removeAll(cubes);
			isNotLoaded = true;
		} else if (Kernel.gameResources.camera.getPosition().x > X + 32
				&& !isNotLoaded) {
			Kernel.gameResources.allEntities.removeAll(cubes);
			isNotLoaded = true;
		} else if (isNotLoaded) {
			if (Kernel.gameResources.camera.getPosition().x < X + 32) {
				if (Kernel.gameResources.camera.getPosition().z < Z + 32) {
					Kernel.gameResources.allEntities.addAll(cubes);
					isNotLoaded = false;
				}
			}
		}
	}

	private void createChunk() {
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int y = (int) pos.getY(); y < sizeY; y++) {
				for (int z = (int) pos.getZ(); z < sizeZ; z++) {
					if (y == 0) {
						blocks[x][y][z] = Block.Indes.getId();
					} else if (y < 65 && y > 0) {
						blocks[x][y][z] = Block.Stone.getId();
					} else if (y < 71 && y > 64
							&& !checkBlockNotInView(x, y, z)) {
						blocks[x][y][z] = Block.Grass.getId();
					}
				}
			}
		}
	}

	private void rebuild() {
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int y = (int) pos.getY(); y < sizeY; y++) {
				for (int z = (int) pos.getZ(); z < sizeZ; z++) {
					if (blocks[x][y][z] == Block.Indes.getId()
							&& !checkBlockNotInView(x, y, z)) {
						cubes.add(Block.Indes.getEntity(new Vector3f(x, y, z)));
					} else if (blocks[x][y][z] == Block.Stone.getId()
							&& !checkBlockNotInView(x, y, z)) {
						cubes.add(Block.Stone.getEntity(new Vector3f(x, y, z)));
					} else if (blocks[x][y][z] == Block.Grass.getId()
							&& !checkBlockNotInView(x, y, z)) {
						cubes.add(Block.Grass.getEntity(new Vector3f(x, y, z)));
					}
				}
			}
		}
		Kernel.gameResources.allEntities.addAll(cubes);
	}

	private boolean checkBlockNotInView(int x, int y, int z) {
		boolean facesHidden[] = new boolean[6];
		if (x > pos.getX()) {
			if (blocks[x - 1][y][z] != -2)
				facesHidden[0] = true;
			else
				facesHidden[0] = false;
		} else {
			facesHidden[0] = false;
		}
		if (x < sizeX - 1) {
			if (blocks[x + 1][y][z] != -2)
				facesHidden[1] = true;
			else
				facesHidden[1] = false;
		} else {
			facesHidden[1] = false;
		}

		if (y > pos.getY()) {
			if (blocks[x][y - 1][z] != -2)
				facesHidden[2] = true;
			else
				facesHidden[2] = false;
		} else {
			facesHidden[2] = false;
		}
		if (y < sizeY - 58) {
			if (blocks[x][y + 1][z] != -2)
				facesHidden[3] = true;
			else
				facesHidden[3] = false;
		} else {
			facesHidden[3] = false;
		}

		if (z > pos.getZ()) {
			if (blocks[x][y][z - 1] != -2)
				facesHidden[4] = true;
			else
				facesHidden[4] = false;
		} else {
			facesHidden[4] = false;
		}
		if (z < sizeZ - 1) {
			if (blocks[x][y][z + 1] != -2)
				facesHidden[5] = true;
			else
				facesHidden[5] = false;
		} else {
			facesHidden[5] = false;
		}
		return facesHidden[0] && facesHidden[1] && facesHidden[2]
				&& facesHidden[3] && facesHidden[4] && facesHidden[5];
	}

	public void dispose() {
		Kernel.gameResources.allEntities.clear();
		cubes.clear();
	}
}
