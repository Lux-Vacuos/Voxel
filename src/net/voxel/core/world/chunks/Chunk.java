package net.voxel.core.world.chunks;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import net.logger.Logger;
import net.voxel.core.geometry.Shape;
import net.voxel.core.world.blocks.Blocks;

import org.lwjgl.BufferUtils;

import com.nishu.utils.Vector3f;

public class Chunk {

	public static int CHUNKSIZE = 16;
	public static int CHUNKHEIGHT = 16;

	private Vector3f pos;
	private FloatBuffer vCoords, cCoords;
	private byte[][][] blocks;

	private int vID, cID, sizeX, sizeY, sizeZ;
	private boolean isActive;

	public Chunk(float x, float y, float z) {
		this(new Vector3f(x, y, z));
	}

	public Chunk(Vector3f pos) {
		this.pos = pos;
		
		initGL();
		init();
	}

	public void initGL() {
		Logger.log("Generating Chunks");
		sizeX = (int) pos.getX() + CHUNKSIZE;
		sizeY = (int) pos.getY() + CHUNKHEIGHT;
		sizeZ = (int) pos.getZ() + CHUNKSIZE;

		blocks = new byte[sizeX][sizeY][sizeZ];

		vCoords = BufferUtils.createFloatBuffer(CHUNKSIZE * CHUNKHEIGHT
				* CHUNKSIZE * (3 * 4 * 6));
		cCoords = BufferUtils.createFloatBuffer(CHUNKSIZE * CHUNKHEIGHT
				* CHUNKSIZE * (4 * 4 * 6));

		createChunk();

		vCoords.flip();
		cCoords.flip();

		vID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vID);
		glBufferData(GL_ARRAY_BUFFER, vCoords, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		cID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, cID);
		glBufferData(GL_ARRAY_BUFFER, cCoords, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void init() {

	}

	private void createChunk() {
		Logger.log("Generating Blocks");
		for (int x = (int) pos.getX(); x < sizeX; x++) {
			for (int y = (int) pos.getY(); y < sizeY; y++) {
				for (int z = (int) pos.getZ(); z < sizeZ; z++) {
					blocks[x][y][z] = Blocks.Grass.getId();
					vCoords.put(Shape.createCubeVertices(x, y, z, 1));
					cCoords.put(Shape.getCubeColors(blocks[x][y][z]));
				}
			}
		}
	}

	public void update() {

	}

	public void render() {
		glBindBuffer(GL_ARRAY_BUFFER, vID);
		glVertexPointer(3, GL_FLOAT, 0, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, cID);
		glColorPointer(4, GL_FLOAT, 0, 0L);
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		
		glDrawArrays(GL_QUADS, 0, CHUNKSIZE * CHUNKHEIGHT
				* CHUNKSIZE * (4 * 4 * 6));
		
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
	}

	public void reBuild() {

	}

	private void checkBlockInView() {

	}

	public void dispose() {
		glDeleteBuffers(vID);
		glDeleteBuffers(cID);
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}