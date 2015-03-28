package voxel.server.core.world.chunk;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glDeleteLists;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;

import java.util.Random;

import voxel.client.core.engine.shaders.ShaderProgram;
import voxel.client.core.engine.vectors.Vector3f;
import voxel.client.core.util.ConstantsClient;
import voxel.client.core.world.block.geometry.Shape;
import voxel.server.core.world.World;
import voxel.server.core.world.block.Tile;

public class Chunk {

	private Vector3f pos;
	private byte[][][] tiles;
	private ShaderProgram shader;

	private int vcID, sizeX, sizeY, sizeZ, type;
	private boolean isActive;

	private Random rand;

	public Chunk(ShaderProgram shader, int type, float x, float y, float z) {
		this(type, new Vector3f(x, y, z));
	}

	public Chunk(int type, Vector3f pos) {
		this.pos = pos;
		this.type = type;

		initGL();
		init();
	}

	public void initGL() {
		rand = new Random();

		sizeX = (int) pos.getX() + ConstantsClient.CHUNKSIZE;
		sizeY = (int) pos.getY() + ConstantsClient.CHUNKHEIGTH;
		sizeZ = (int) pos.getZ() + ConstantsClient.CHUNKSIZE;

		vcID = glGenLists(1);

		tiles = new byte[sizeX][sizeY][sizeZ];

		createChunk();
		rebuild();
	}

	public void init() {
	}

	private void createChunk() {
		if (type == World.AIRCHUNK) {
			for (int x = (int) pos.getX(); x < sizeX; x++) {
				for (int y = (int) pos.getY(); y < sizeY; y++) {
					for (int z = (int) pos.getZ(); z < sizeZ; z++) {
						tiles[x][y][z] = Tile.Air.getId();
					}
				}
			}
		}
		if (type == World.MIXEDCHUNK) {
			for (int x = (int) pos.getX(); x < sizeX; x++) {
				for (int y = (int) pos.getY(); y < sizeY; y++) {
					for (int z = (int) pos.getZ(); z < sizeZ; z++) {
						tiles[x][y][z] = Tile.Grass.getId();
						if (rand.nextInt(2) == 0)
							if (rand.nextBoolean())
								tiles[x][y][z] = Tile.Air.getId();
							else
								tiles[x][y][z] = Tile.CrackedStone.getId();
					}
				}
			}
		}
	}

	public void update() {
	}

	public void render() {
		if (type != World.AIRCHUNK) {
			glCallList(vcID);
		}
	}

	public void rebuild() {
		if (type != World.AIRCHUNK) {
			glNewList(vcID, GL_COMPILE);
			for (int x = (int) pos.getX(); x < sizeX; x++) {
				for (int y = (int) pos.getY(); y < sizeY; y++) {
					for (int z = (int) pos.getZ(); z < sizeZ; z++) {
						if (tiles[x][y][z] != -1
								&& !checkTileNotInView(x, y, z)) {
							Shape.createCube(x, y, z,
									Tile.getTile(tiles[x][y][z]).getColor(),
									Tile.getTile(tiles[x][y][z]).getTextID(), 1);
						}
					}
				}
			}
			glEndList();
		}
	}

	private boolean checkTileNotInView(int x, int y, int z) {
		boolean facesHidden[] = new boolean[6];
		if (x > pos.getX()) {
			if (tiles[x - 1][y][z] != -1)
				facesHidden[0] = true;
			else
				facesHidden[0] = false;
		} else {
			facesHidden[0] = false;
		}
		if (x < sizeX - 1) {
			if (tiles[x + 1][y][z] != -1)
				facesHidden[1] = true;
			else
				facesHidden[1] = false;
		} else {
			facesHidden[1] = false;
		}

		if (y > pos.getY()) {
			if (tiles[x][y - 1][z] != -1)
				facesHidden[2] = true;
			else
				facesHidden[2] = false;
		} else {
			facesHidden[2] = false;
		}
		if (y < sizeY - 1) {
			if (tiles[x][y + 1][z] != -1)
				facesHidden[3] = true;
			else
				facesHidden[3] = false;
		} else {
			facesHidden[3] = false;
		}

		if (z > pos.getZ()) {
			if (tiles[x][y][z - 1] != -1)
				facesHidden[4] = true;
			else
				facesHidden[4] = false;
		} else {
			facesHidden[4] = false;
		}
		if (z < sizeZ - 1) {
			if (tiles[x][y][z + 1] != -1)
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
		shader.dispose();
		glDeleteLists(vcID, 1);
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Vector3f getCenter() {
		return new Vector3f(pos.getX() - (ConstantsClient.CHUNKSIZE / 2), pos.getY()
				- (ConstantsClient.CHUNKSIZE / 2), pos.getZ()
				- (ConstantsClient.CHUNKSIZE / 2));
	}

	public Vector3f getPos() {
		return pos;
	}

	public int getType() {
		return type;
	}
}
