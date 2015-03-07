package net.voxel.core.world.chunks;

import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import com.nishu.utils.Vector3f;

public class Chunk {

	public static final int CHUNKSIZE = 16;
	
	private Vector3f pos;
	private FloatBuffer vCoords, cCoords;
	private byte[][][] blocks;
	
	private int vID, cID;
	private boolean isActive;
	
	public Chunk(float x, float y, float z) {
		this(new Vector3f(x, y, z));
	}
	public Chunk(Vector3f pos) {
		this.pos = pos;
	}
	public void initGL() {
		vCoords = BufferUtils.createFloatBuffer(CHUNKSIZE * CHUNKSIZE * 128 * (4 * 6));
		cCoords = BufferUtils.createFloatBuffer(CHUNKSIZE * CHUNKSIZE * 128 * (4 * 6));
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
	public void update() {
		
	}
	public void render() {
		
	}
	public void reBuild() {
		
	}
	private void checkBlockInView() {
		
	}
	public void dispose() {
		
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}