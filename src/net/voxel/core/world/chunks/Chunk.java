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
import net.voxel.core.world.blocks.Blocks;

import com.nishu.utils.ShaderProgram;
import com.nishu.utils.Vector3f;

public class Chunk {

	public static int CHUNKSIZE = 16;
	public static int CHUNKHEIGHT = 16;

	private Vector3f pos;
	private byte[][][] blocks;
	private ShaderProgram shader;

	private int vcID, sizeX, sizeY, sizeZ;
	private boolean isActive;
	
	private Random rand;

	public Chunk(ShaderProgram shader, float x, float y, float z) {
		this(shader, new Vector3f(x, y, z));
	}

	public Chunk(ShaderProgram shader, Vector3f pos) {
		this.pos = pos;
		this.shader = shader;
		
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
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int y = (int) pos.getY(); y < sizeY; y++) {
				for (int z = (int) pos.getZ(); z < sizeZ; z++) {
					blocks[x][y][z] = Blocks.Grass.getId();
					if(rand.nextInt(10) == 0) blocks[x][y][z] = Blocks.Void.getId();
				}
			}
		}
	}

	public void update() {
	}

	public void render() {
		glCallList(vcID);
	}

	public void reBuild() {
		glNewList(vcID, GL_COMPILE);
		glBegin(GL_QUADS);
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int y = (int) pos.getY(); y < sizeY; y++) {
				for (int z = (int) pos.getZ(); z < sizeZ; z++) {
					Shape.createCube(x, y, z, Blocks.getBlocks(blocks[x][y][z]).getColor(), Blocks.getBlocks(blocks[x][y][z]).getTexCoords(), 1);
				}
			}
		}
		glEnd();
		glEndList();
	}

	@SuppressWarnings("unused")
	private void checkBlockInView() {
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