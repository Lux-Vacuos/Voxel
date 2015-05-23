package net.guerra24.voxel.client.world.chunks;

import java.util.ArrayList;
import java.util.List;

import net.guerra24.voxel.client.kernel.Engine;
import net.guerra24.voxel.client.kernel.entities.Entity;
import net.guerra24.voxel.client.world.World;
import net.guerra24.voxel.client.world.block.Block;

import org.lwjgl.util.vector.Vector3f;

public class Chunk {

	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_HEIGHT = 128;

	public List<Entity> cubes = new ArrayList<Entity>();
	private Vector3f pos;
	private byte[][][] blocks;

	private int sizeX, sizeY, sizeZ;

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
		float Z = pos.y;
		if (Engine.gameResources.camera.getPosition().x > X + 16
				& Engine.gameResources.camera.getPosition().z > Z + 16) {
			Engine.gameResources.allEntities.removeAll(cubes);
			cubes.clear();
		} else {
			Engine.gameResources.allEntities.removeAll(cubes);
			rebuild();
			Engine.gameResources.allEntities.addAll(cubes);
		}
	}

	private void createChunk() {
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int y = (int) pos.getY(); y < sizeY; y++) {
				for (int z = (int) pos.getZ(); z < sizeZ; z++) {
					if (y < 61 && y > 0) {
						blocks[x][y][z] = Block.Stone.getId();
					} else if (y < 65 && y > 60
							&& Engine.gameResources.rand.nextBoolean()) {
						blocks[x][y][z] = Block.Grass.getId();
					}
					if (blocks[x][y][z] == Block.Stone.getId()) {
						cubes.add(Block.Stone.getEntity(new Vector3f(x, y, z)));
					} else if (blocks[x][y][z] == Block.Grass.getId()) {
						cubes.add(Block.Grass.getEntity(new Vector3f(x, y, z)));
					}
				}
			}
		}
	}

	private void rebuild() {
		cubes.clear();
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int y = (int) pos.getY(); y < sizeY; y++) {
				for (int z = (int) pos.getZ(); z < sizeZ; z++) {
					if (y < 61 && y > 0) {
						blocks[x][y][z] = Block.Stone.getId();
					} else if (y < 65 && y > 60) {
						blocks[x][y][z] = Block.Grass.getId();
					}
					if (blocks[x][y][z] == Block.Stone.getId()) {
						cubes.add(Block.Stone.getEntity(new Vector3f(x, y, z)));
					} else if (blocks[x][y][z] == Block.Grass.getId()) {
						cubes.add(Block.Grass.getEntity(new Vector3f(x, y, z)));
					}
				}
			}
		}
	}

	public void dispose() {
		Engine.gameResources.allEntities.removeAll(World.c.cubes);
		cubes.clear();
	}
}
