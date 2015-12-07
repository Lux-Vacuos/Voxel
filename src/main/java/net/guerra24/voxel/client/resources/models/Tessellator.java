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

	private int vaoID, vboID0, vboID1, vboID2, iboID, vboCap0 = 64, vboCap1 = 64, vboCap2 = 64, indicesCounter,
			iboCapacity = 64;

	private ByteBuffer buffer0, buffer1, buffer2, ibo;

	private List<Vector3f> pos, normals;
	private List<Vector2f> texcoords;
	private List<Integer> indices;
	private int texture;

	private TessellatorShader shader;

	public Tessellator(MasterRenderer renderer) {
		init(renderer);
	}

	private void init(MasterRenderer renderer) {
		pos = new ArrayList<Vector3f>();
		texcoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		indices = new ArrayList<Integer>();
		shader = TessellatorShader.getInstance();
		shader.start();
		shader.loadProjectionMatrix(renderer.getProjectionMatrix());
		shader.stop();

		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		iboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboCapacity, null, GL_DYNAMIC_DRAW);
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

	public void indice(int ind) {
		this.indices.add(ind);
	}

	public void end() {
		loadData(pos, texcoords, normals);
		updateGlBuffers(vboID0, vboCap0, buffer0);
		updateGlBuffers(vboID1, vboCap1, buffer1);
		updateGlBuffers(vboID2, vboCap2, buffer2);
		updateGLIBOBuffer();
	}

	public void draw(Camera camera) {
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
		vboCapacity = data.capacity();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vboCapacity, data, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void updateGLIBOBuffer() {
		iboCapacity = ibo.capacity();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, iboCapacity, ibo, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

	}

	public void generateCube(int x, int y, int z, int size, boolean top, boolean bottom, boolean left, boolean right,
			boolean front, boolean back) {
		// TODO: FIX THIS
		if (false) {
			// top face
			vertex3f(new Vector3f(x, y + size, z + size));
			texture2f(new Vector2f(0, 1));
			normal3f(new Vector3f(0, 1, 0));

			vertex3f(new Vector3f(x + size, y + size, z + size));
			texture2f(new Vector2f(1, 1));
			normal3f(new Vector3f(0, 1, 0));

			vertex3f(new Vector3f(x + size, y + size, z));
			texture2f(new Vector2f(1, 0));
			normal3f(new Vector3f(0, 1, 0));

			vertex3f(new Vector3f(x, y + size, 0));
			texture2f(new Vector2f(0, 0));
			normal3f(new Vector3f(0, 1, 0));

		}
		// TODO: FIX THIS
		if (false) {
			// bottom face
			vertex3f(new Vector3f(x, y, z));
			texture2f(new Vector2f(0, 1));
			normal3f(new Vector3f(0, -1, 0));

			vertex3f(new Vector3f(x + size, y, z));
			texture2f(new Vector2f(1, 1));
			normal3f(new Vector3f(0, -1, 0));

			vertex3f(new Vector3f(x + size, y, z + size));
			texture2f(new Vector2f(1, 0));
			normal3f(new Vector3f(0, -1, 0));

			vertex3f(new Vector3f(x, y, z + size));
			texture2f(new Vector2f(0, 0));
			normal3f(new Vector3f(0, -1, 0));
		}

		if (back) {
			// back face
			vertex3f(new Vector3f(x, y, z + size));
			texture2f(new Vector2f(0, 1));
			normal3f(new Vector3f(0, 0, 1));

			vertex3f(new Vector3f(x + size, y, z + size));
			texture2f(new Vector2f(1, 1));
			normal3f(new Vector3f(0, 0, 1));

			vertex3f(new Vector3f(x + size, y + size, z + size));
			texture2f(new Vector2f(1, 0));
			normal3f(new Vector3f(0, 0, 1));

			vertex3f(new Vector3f(x, y + size, z + size));
			texture2f(new Vector2f(0, 0));
			normal3f(new Vector3f(0, 0, 1));
		}
		if (front) {
			// front face
			vertex3f(new Vector3f(x, y + size, z));
			texture2f(new Vector2f(1, 0));
			normal3f(new Vector3f(0, 0, -1));

			vertex3f(new Vector3f(x + size, y + size, z));
			texture2f(new Vector2f(0, 0));
			normal3f(new Vector3f(0, 0, -1));

			vertex3f(new Vector3f(x + size, y, z));
			texture2f(new Vector2f(0, 1));
			normal3f(new Vector3f(0, 0, -1));

			vertex3f(new Vector3f(x, y, z));
			texture2f(new Vector2f(1, 1));
			normal3f(new Vector3f(0, 0, -1));
		}
		if (right) {
			// right face
			vertex3f(new Vector3f(x, y, z));
			texture2f(new Vector2f(0, 1));
			normal3f(new Vector3f(-1, 0, 0));

			vertex3f(new Vector3f(x, y, z + size));
			texture2f(new Vector2f(1, 1));
			normal3f(new Vector3f(-1, 0, 0));

			vertex3f(new Vector3f(x, y + size, z + size));
			texture2f(new Vector2f(1, 0));
			normal3f(new Vector3f(-1, 0, 0));

			vertex3f(new Vector3f(x, y + size, z));
			texture2f(new Vector2f(0, 0));
			normal3f(new Vector3f(-1, 0, 0));
		}
		if (left) {

			// left face
			vertex3f(new Vector3f(x + size, y, z + size));
			texture2f(new Vector2f(0, 1));
			normal3f(new Vector3f(1, 0, 0));

			vertex3f(new Vector3f(x + size, y, z));
			texture2f(new Vector2f(1, 1));
			normal3f(new Vector3f(1, 0, 0));

			vertex3f(new Vector3f(x + size, y + size, z));
			texture2f(new Vector2f(1, 0));
			normal3f(new Vector3f(1, 0, 0));

			vertex3f(new Vector3f(x + size, y + size, z + size));
			texture2f(new Vector2f(0, 0));
			normal3f(new Vector3f(1, 0, 0));
		}
	}

	public void cleanUp() {
		glDeleteVertexArrays(vaoID);
		glDeleteBuffers(vboID0);
		glDeleteBuffers(vboID1);
		glDeleteBuffers(vboID2);
		glDeleteBuffers(iboID);
	}

}
