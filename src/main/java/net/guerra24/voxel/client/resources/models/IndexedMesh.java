package net.guerra24.voxel.client.resources.models;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glUnmapBuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glMapBufferRange;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Set;

import org.lwjgl.BufferUtils;

public class IndexedMesh extends AbstractMesh {

	public int indices_counter;

	public ByteBuffer ibo_data;

	protected int ibo_capacity;
	protected int ibo;

	public IndexedMesh() {
		super();

		this.ibo_capacity = initial_capacity;
		this.ibo = glGenBuffers();
		this.init_ibo();

		this.init_vao();
	}

	public void update_gl_buffers() {
		super.update_gl_buffers();

		int ibo_data_size = ibo_data.capacity();
		boolean ibo_orphan = false;
		while (ibo_data_size > ibo_capacity) {
			ibo_capacity *= 2;
			ibo_orphan = true;
		}
		if (ibo_orphan) {
			init_ibo();
		}

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glMapBufferRange(GL_ELEMENT_ARRAY_BUFFER, 0, ibo_data_size, mapping_flags).put(ibo_data);
		glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
	}

	public void update_gl_data(float[] vertices, float[] tex_coords, float[] tex_coords_offsets, float[] colors) {
		super.update_gl_data(vertices, tex_coords, tex_coords_offsets, colors);
		this.update_ibo(vertices.length / 2);
	}

	public void update_gl_data(List<Float> vertices, List<Float> tex_coords, List<Float> tex_coords_offsets,
			List<Float> colors) {
		super.update_gl_data(vertices, tex_coords, tex_coords_offsets, colors);
		this.update_ibo(vertices.size() / 2);
	}

	public void update_gl_data(Set<Vertex> vertices, List<Integer> indices) {
		this.indices_counter = indices.size();
		this.ibo_data = BufferUtils.createByteBuffer(indices_counter * 4);
		this.vbo_data = BufferUtils.createByteBuffer(vertices.size() * 40);
		for (int index : indices) {
			ibo_data.putInt(index);
		}
		ibo_data.flip();

		for (Vertex vertex : vertices) {
			vbo_data.putFloat(vertex.pos_x).putFloat(vertex.pos_y).putFloat(vertex.pos_z);
			vbo_data.putFloat(vertex.tex_coord_x).putFloat(vertex.tex_coord_y);
			vbo_data.putFloat(vertex.tex_coord_offset_x).putFloat(vertex.tex_coord_offset_y);
			vbo_data.putFloat(vertex.color_r).putFloat(vertex.color_g).putFloat(vertex.color_b);
		}
		vbo_data.flip();
	}

	public void draw() {
		glBindVertexArray(vao);
		glDrawElements(GL_TRIANGLES, indices_counter, GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);
	}

	public void destroy() {
		super.destroy();
		glDeleteBuffers(ibo);
	}

	protected void init_ibo() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, ibo_capacity, null, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	protected void update_ibo(int count) {
		this.indices_counter = count;
		this.ibo_data = BufferUtils.createByteBuffer(count * 4);

		for (int i = 0, bytes = 0; bytes < count; i += 4, bytes += 6) {
			ibo_data.putInt(i);
			ibo_data.putInt(i + 1);
			ibo_data.putInt(i + 2);

			ibo_data.putInt(i + 1);
			ibo_data.putInt(i + 3);
			ibo_data.putInt(i + 2);
		}
		ibo_data.flip();
	}

	protected void init_vao() {
		glBindVertexArray(vao);
		super.init_vao();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBindVertexArray(0);
	}

}
