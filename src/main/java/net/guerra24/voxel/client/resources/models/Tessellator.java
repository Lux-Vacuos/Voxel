package net.guerra24.voxel.client.resources.models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import net.guerra24.voxel.client.graphics.MasterRenderer;
import net.guerra24.voxel.client.graphics.shaders.TessellatorShader;
import net.guerra24.voxel.client.world.entities.Camera;
import net.guerra24.voxel.universal.util.vector.Vector2f;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public class Tessellator {

	private int vaoID, vboID0, vboID1, vboID2, iboID, vboCap0 = 512, vboCap1 = 512, vboCap2 = 512, indicesCounter,
			iboCapacity = 512;

	private ByteBuffer buffer0, buffer1, buffer2, ibo;

	private List<Vector3f> pos, normals;
	private List<Vector2f> texcoords;
	private int texture;

	private TessellatorShader shader;

	private boolean update = false;

	public Tessellator(MasterRenderer renderer) {
		init(renderer);
	}

	private void init(MasterRenderer renderer) {
		pos = new ArrayList<Vector3f>();
		texcoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		shader = TessellatorShader.getInstance();
		shader.start();
		shader.loadProjectionMatrix(renderer.getProjectionMatrix());
		shader.stop();

		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		iboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboCapacity, null, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		vboID0 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID0);
		glBufferData(GL_ARRAY_BUFFER, vboCap0, null, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		vboID1 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID1);
		glBufferData(GL_ARRAY_BUFFER, vboCap1, null, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		vboID2 = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID2);
		glBufferData(GL_ARRAY_BUFFER, vboCap2, null, GL_DYNAMIC_DRAW);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBindVertexArray(0);
	}

	public void begin(int texture) {
		this.texture = texture;
		pos.clear();
		texcoords.clear();
		normals.clear();
	}

	public void vertex3f(Vector3f pos) {
		this.pos.add(pos);
	}

	public void texture2f(Vector2f texcoords) {
		this.texcoords.add(texcoords);
	}

	public void normal3f(Vector3f normals) {
		this.normals.add(normals);
	}

	public void end() {
		loadData(pos, texcoords, normals);
		update = true;
	}

	public void draw(Camera camera) {
		if (update) {
			updateGlBuffers(vboID0, vboCap0, buffer0);
			updateGlBuffers(vboID1, vboCap1, buffer1);
			updateGlBuffers(vboID2, vboCap2, buffer2);
			updateGLIBOBuffer();
			update = false;
		}
		shader.start();
		shader.loadviewMatrix(camera);
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
		glDrawElements(GL_TRIANGLES, indicesCounter, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
		shader.stop();
	}

	public void loadData(List<Vector3f> pos, List<Vector2f> texcoords, List<Vector3f> normals) {
		buffer0 = BufferUtils.createByteBuffer((pos.size() * 3) * 4);
		buffer1 = BufferUtils.createByteBuffer((texcoords.size() * 2) * 4);
		buffer2 = BufferUtils.createByteBuffer((normals.size() * 3) * 4);
		for (int i = 0; i < pos.size(); i++) {
			buffer0.putFloat(pos.get(i).x);
			buffer0.putFloat(pos.get(i).y);
			buffer0.putFloat(pos.get(i).z);
		}
		for (int i = 0; i < texcoords.size(); i++) {
			buffer1.putFloat(texcoords.get(i).x);
			buffer1.putFloat(texcoords.get(i).y);
		}
		for (int i = 0; i < normals.size(); i++) {
			buffer2.putFloat(normals.get(i).x);
			buffer2.putFloat(normals.get(i).y);
			buffer2.putFloat(normals.get(i).z);
		}
		buffer0.flip();
		buffer1.flip();
		buffer2.flip();
		updateIBO((pos.size() * 3) / 2);
	}

	public void updateIBO(int count) {
		indicesCounter = count;
		ibo = BufferUtils.createByteBuffer(count * 4);
		for (int i = 0, bytes = 0; bytes < count; i += 4, bytes += 6) {
			ibo.putInt(i);
			ibo.putInt(i + 1);
			ibo.putInt(i + 2);
			ibo.putInt(i + 2);
			ibo.putInt(i + 3);
			ibo.putInt(i + 0);
		}
		ibo.flip();
	}

	public void updateGlBuffers(int vbo, int vboCapacity, ByteBuffer data) {
		int vbo_data_size = data.capacity();
		boolean vbo_orphan = false;
		while (vbo_data_size > vboCapacity) {
			vboCapacity *= 2;
			vbo_orphan = true;
		}
		if (vbo_orphan) {
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, vboCapacity, null, GL_DYNAMIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glMapBufferRange(GL_ARRAY_BUFFER, 0, vbo_data_size, GL_MAP_WRITE_BIT).put(data);
		glUnmapBuffer(GL_ARRAY_BUFFER);
	}

	public void updateGLIBOBuffer() {
		int ibo_data_size = ibo.capacity();
		boolean ibo_orphan = false;
		while (ibo_data_size > iboCapacity) {
			iboCapacity *= 2;
			ibo_orphan = true;
		}
		if (ibo_orphan) {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboCapacity, null, GL_DYNAMIC_DRAW);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		}

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glMapBufferRange(GL_ELEMENT_ARRAY_BUFFER, 0, ibo_data_size, GL_MAP_WRITE_BIT).put(ibo);
		glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
	}

	public void cleanUp() {
		glDeleteVertexArrays(vaoID);
		glDeleteBuffers(vboID0);
		glDeleteBuffers(vboID1);
		glDeleteBuffers(vboID2);
		glDeleteBuffers(iboID);
	}

}
