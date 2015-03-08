package net.voxel.core.world.chunks;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;

import java.util.Random;

import net.logger.Logger;
import net.voxel.core.geometry.Shape;
import net.voxel.core.world.World;
import net.voxel.core.world.blocks.Blocks;

import com.nishu.utils.ShaderProgram;
import com.nishu.utils.Vector3f;

public class Chunk {

	public static int CHUNKSIZE = 16;
	public static int CHUNKHEIGHT = 256;

	private Vector3f pos;
	private byte[][][] blocks;
	private ShaderProgram shader;

	private int vcID, sizeX, sizeY, sizeZ, type;
	private boolean isActive;

	private Random rand;

	public Chunk(ShaderProgram shader, int type, float x, float y, float z) {
		this(shader, type, new Vector3f(x, y, z));
	}

	public Chunk(ShaderProgram shader, int type, Vector3f pos) {
		this.pos = pos;
		this.shader = shader;
		this.type = type;

		initGL();
		init();
	}

	public void initGL() {
		Logger.log("Generating Chunks");
		rand = new Random();
		sizeX = (int) pos.getX() + CHUNKSIZE;
		sizeY = (int) pos.getY() + CHUNKHEIGHT;
		sizeZ = (int) pos.getZ() + CHUNKSIZE;

		vcID = glGenLists(1);

		blocks = new byte[sizeX][sizeY][sizeZ];

		createChunk();
		reBuild();
	}

	public void init() {
	}

	private void createChunk() {
		Logger.log("Generating Blocks");
		if (type == World.AIRCHUNK) {
			for (int x = (int) pos.getX(); x < sizeX; x++) {
				for (int y = (int) pos.getY(); y < sizeY; y++) {
					for (int z = (int) pos.getZ(); z < sizeZ; z++) {
						blocks[x][y][z] = Blocks.Air.getId();
					}
				}
			}
		}
		if (type == World.MIXEDCHUNK) {

			for (int x = (int) pos.getX(); x < sizeX; x++) {
				for (int y = (int) pos.getY(); y < sizeY; y++) {
					for (int z = (int) pos.getZ(); z < sizeZ; z++) {
						blocks[x][y][z] = Blocks.Grass.getId();
						if (rand.nextInt(10) == 0)
							blocks[x][y][z] = Blocks.Void.getId();
						if (rand.nextInt(10) == 0)
							blocks[x][y][z] = Blocks.Air.getId();
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

	public void reBuild() {
		if (type != World.AIRCHUNK) {
			glNewList(vcID, GL_COMPILE);
			glBegin(GL_QUADS);
			for (int x = (int) pos.getX(); x < sizeX; x++) {
				for (int y = (int) pos.getY(); y < sizeY; y++) {
					for (int z = (int) pos.getZ(); z < sizeZ; z++) {
						if (blocks[x][y][z] != 0 && !checkBlockNotView(x, y, z)) {
							Shape.createCube(x, y, z,
									Blocks.getBlocks(blocks[x][y][z])
											.getColor(),
									Blocks.getBlocks(blocks[x][y][z])
											.getTexCoords(), 1);
						}
					}
				}
			}
			glEnd();
			glEndList();
		}
	}

	private boolean checkBlockNotView(int x, int y, int z) {
		boolean facesHidden[] = new boolean[6];
		if (x > pos.getX()) {
			if(blocks[x - 1][y][z] != 0)  facesHidden[0] = true;
			else facesHidden[0] = false;
		}else {
			facesHidden[0] = false;
		}
		if (x < sizeX - 1) {
			if(blocks[x + 1][y][z] != 0) facesHidden[1] =  true;
			else facesHidden[1] = false;
		}else {
			facesHidden[1] = false;
		}
		if (y > pos.getY()) {
			if(blocks[x][y - 1][z] != 0)  facesHidden[2] = true;
			else facesHidden[2] = false;
		}else {
			facesHidden[2] = false;
		}
		if (y < sizeY - 1) {
			if(blocks[x][y + 1][z] != 0) facesHidden[3] =  true;
			else facesHidden[3] = false;
		}else {
			facesHidden[3] = false;
		}
		if (z > pos.getZ()) {
			if(blocks[x ][y][z - 1] != 0)  facesHidden[4] = true;
			else facesHidden[4] = false;
		}else {
			facesHidden[4] = false;
		}
		if (z < sizeX - 1) {
			if(blocks[x][y][z + 1] != 0) facesHidden[5] =  true;
			else facesHidden[5] = false;
		}else {
			facesHidden[5] = false;
		}
		return facesHidden[0] && facesHidden[1] && facesHidden[2] && facesHidden[3] && facesHidden[4] && facesHidden[5];
	}

	public void dispose() {
		shader.dispose();
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}