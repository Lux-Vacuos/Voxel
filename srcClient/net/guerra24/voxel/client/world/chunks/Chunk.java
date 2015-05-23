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
					} else if (y < 61 && y > 0) {
						blocks[x][y][z] = Block.Stone.getId();
					} else if (y < 65 && y > 60
							&& Kernel.gameResources.rand.nextBoolean()) {
						blocks[x][y][z] = Block.Grass.getId();
					}
					if (blocks[x][y][z] == Block.Indes.getId()) {
						cubes.add(Block.Indes.getEntity(new Vector3f(x, y, z)));
					} else if (blocks[x][y][z] == Block.Stone.getId()) {
						cubes.add(Block.Stone.getEntity(new Vector3f(x, y, z)));
					} else if (blocks[x][y][z] == Block.Grass.getId()) {
						cubes.add(Block.Grass.getEntity(new Vector3f(x, y, z)));
					}
				}
			}
		}
		Kernel.gameResources.allEntities.addAll(cubes);
	}

	@SuppressWarnings("unused")
	private void rebuild() {
		blocks = new byte[sizeX][sizeY][sizeZ];
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int y = (int) pos.getY(); y < sizeY; y++) {
				for (int z = (int) pos.getZ(); z < sizeZ; z++) {
					if (y == 0) {
						blocks[x][y][z] = Block.Indes.getId();
					} else if (y < 61 && y > 0) {
						blocks[x][y][z] = Block.Stone.getId();
					} else if (y < 65 && y > 60
							&& Kernel.gameResources.rand.nextBoolean()) {
						blocks[x][y][z] = Block.Grass.getId();
					}
					if (blocks[x][y][z] == Block.Indes.getId()) {
						cubes.add(Block.Indes.getEntity(new Vector3f(x, y, z)));
					} else if (blocks[x][y][z] == Block.Stone.getId()) {
						cubes.add(Block.Stone.getEntity(new Vector3f(x, y, z)));
					} else if (blocks[x][y][z] == Block.Grass.getId()) {
						cubes.add(Block.Grass.getEntity(new Vector3f(x, y, z)));
					}
				}
			}
		}
	}

	public void dispose() {
		Kernel.gameResources.allEntities.clear();
		cubes.clear();
	}
}
